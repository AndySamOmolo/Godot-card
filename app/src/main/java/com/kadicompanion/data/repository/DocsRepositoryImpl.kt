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
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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
    @Volatile private var reindexed = false
    private val _cachedEntries = MutableStateFlow<List<GodotDocEntry>?>(null)

    override fun observeAll(): Flow<List<GodotDocEntry>> =
        godotDocDao.observeAll()
            .onStart { reindexIfNeeded() }
            .map { rows ->
                val cached = _cachedEntries.value
                if (cached != null && cached.size == rows.size && rows.isNotEmpty()) {
                    cached
                } else {
                    rows.mapNotNull { row ->
                        runCatching { json.decodeFromString<GodotDocEntry>(row.json) }.getOrNull()
                    }.sortedBy { it.className }.also { decoded ->
                        if (decoded.isNotEmpty()) _cachedEntries.value = decoded
                    }
                }
            }
            .flowOn(Dispatchers.Default)

    override suspend fun get(className: String): GodotDocEntry? = withContext(Dispatchers.IO) {
        reindexIfNeeded()
        _cachedEntries.value?.firstOrNull { it.className.equals(className, ignoreCase = true) }?.let { return@withContext it }
        val row = godotDocDao.get(className) ?: return@withContext null
        runCatching { json.decodeFromString<GodotDocEntry>(row.json) }.getOrNull()
    }

    override suspend fun search(query: String): List<String> = withContext(Dispatchers.IO) {
        reindexIfNeeded()
        if (query.isBlank()) return@withContext emptyList()
        val tokens = query.trim()
            .split(Regex("\\s+"))
            .map { it.replace(Regex("[^A-Za-z0-9_@]"), "") }
            .filter { it.isNotEmpty() }
        if (tokens.isEmpty()) return@withContext emptyList()
        val ftsQuery = tokens.joinToString(" ") { "\"$it\"*" }
        runCatching { godotDocDao.search(ftsQuery) }.getOrDefault(emptyList())
    }

    override suspend fun reindexIfNeeded() {
        if (reindexed) return
        reindexMutex.withLock {
            if (reindexed) return
            val bundled = contentLoader.loadContentVersion()
            val storedVersion = prefs.observeContentVersion().first()
            val existingCount = _cachedEntries.value?.size ?: 0
            if (storedVersion == bundled && existingCount > 0) {
                reindexed = true
                return
            }
            val sources = contentLoader.loadGodotDocSources()
            if (sources.isEmpty()) return
            val defaultVerified = bundled.substringBefore('-').ifBlank { "4.3" }
            val entries = sources.map { src -> GodotDocParser.parse(src, defaultVerified) }.sortedBy { it.className }
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
            withContext(NonCancellable) {
                godotDocDao.replaceAllDocs(entities, ftsEntities)
                _cachedEntries.value = entries
                prefs.setContentVersion(bundled)
                reindexed = true
            }
        }
    }
}
