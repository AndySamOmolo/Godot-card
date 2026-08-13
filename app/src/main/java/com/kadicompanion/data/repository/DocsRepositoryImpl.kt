package com.kadicompanion.data.repository

import com.kadicompanion.data.content.ContentLoader
import com.kadicompanion.data.content.GodotDocParser
import com.kadicompanion.data.local.datastore.PreferencesRepository
import com.kadicompanion.data.local.db.dao.GodotDocDao
import com.kadicompanion.data.local.db.entity.GodotDocEntity
import com.kadicompanion.data.local.db.entity.GodotDocFtsEntity
import com.kadicompanion.domain.model.GodotDocEntry
import com.kadicompanion.domain.repository.DocsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DocsRepositoryImpl @Inject constructor(
    private val contentLoader: ContentLoader,
    private val godotDocDao: GodotDocDao,
    private val prefs: PreferencesRepository,
    private val json: Json
) : DocsRepository {

    private val reindexMutex = Mutex()
    private var reindexed = false

    override fun observeAll(): Flow<List<GodotDocEntry>> =
        godotDocDao.observeAll().map { rows ->
            rows.mapNotNull { row ->
                runCatching { json.decodeFromString<GodotDocEntry>(row.json) }.getOrNull()
            }.sortedBy { it.className }
        }

    override suspend fun get(className: String): GodotDocEntry? = withContext(Dispatchers.IO) {
        val row = godotDocDao.get(className) ?: return@withContext null
        runCatching { json.decodeFromString<GodotDocEntry>(row.json) }.getOrNull()
    }

    override suspend fun search(query: String): List<String> = withContext(Dispatchers.IO) {
        if (query.isBlank()) return@withContext emptyList()
        val sanitized = query.trim().replace("\"", "").replace("'", "")
        if (sanitized.isEmpty()) return@withContext emptyList()
        val ftsQuery = "\"$sanitized\"*"
        runCatching { godotDocDao.search(ftsQuery) }.getOrDefault(emptyList())
    }

    override suspend fun reindexIfNeeded() {
        if (reindexed) return
        reindexMutex.withLock {
            if (reindexed) return
            val bundled = contentLoader.loadContentVersion()
            val storedVersion = prefs.observeContentVersion().first()
            if (storedVersion == bundled) {
                reindexed = true
                return
            }
            val sources = contentLoader.loadGodotDocSources()
            val defaultVerified = bundled.substringBefore('-').ifBlank { "4.3" }
            val entries = sources.map { src -> GodotDocParser.parse(src, defaultVerified) }
            val entities = entries.map { entry ->
                GodotDocEntity(
                    className = entry.className,
                    json = json.encodeToString<GodotDocEntry>(entry)
                )
            }
            val ftsEntities = entries.map { entry ->
                val searchable = buildString {
                    append(entry.className).append(' ')
                    append(entry.shortDescription).append(' ')
                    entry.properties.forEach { append(it.name).append(' ').append(it.type ?: "").append(' ') }
                    entry.methods.forEach { append(it.name).append(' ').append(it.type ?: "").append(' ') }
                    entry.signals.forEach { append(it.name).append(' ') }
                }
                GodotDocFtsEntity(entry.className, searchable)
            }
            godotDocDao.clearFts()
            godotDocDao.clearAll()
            godotDocDao.insertAll(entities)
            godotDocDao.insertFts(ftsEntities)
            prefs.setContentVersion(bundled)
            reindexed = true
        }
    }
}
