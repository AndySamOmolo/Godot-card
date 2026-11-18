package com.kadicompanion.data.repository

import com.kadicompanion.data.content.ContentLoader
import com.kadicompanion.domain.model.GlossaryEntry
import com.kadicompanion.domain.repository.GlossaryRepository
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlossaryRepositoryImpl @Inject constructor(
    private val contentLoader: ContentLoader
) : GlossaryRepository {

    private val mutex = Mutex()
    private val _cache = MutableStateFlow<List<GlossaryEntry>>(emptyList())
    private val cache = _cache.asStateFlow()
    @Volatile private var isLoaded = false

    override fun observeAll(): Flow<List<GlossaryEntry>> = cache.onStart {
        if (!isLoaded) ensureLoaded()
    }

    override suspend fun get(term: String): GlossaryEntry? {
        ensureLoaded()
        val normalized = term.substringBefore('|').trim()
        return _cache.value.firstOrNull { it.term.equals(normalized, ignoreCase = true) }
            ?: _cache.value.firstOrNull {
                it.term.equals("${normalized}s", ignoreCase = true) ||
                    it.term.equals(normalized.removeSuffix("s"), ignoreCase = true)
            }
    }

    override suspend fun search(query: String): List<GlossaryEntry> {
        ensureLoaded()
        val q = query.trim().lowercase(Locale.ROOT)
        if (q.isEmpty()) return _cache.value
        return _cache.value.filter { entry ->
            entry.term.lowercase(Locale.ROOT).contains(q) || entry.shortDefinition.lowercase(Locale.ROOT).contains(q)
        }
    }

    private suspend fun ensureLoaded() {
        if (isLoaded && _cache.value.isNotEmpty()) return
        mutex.withLock {
            if (isLoaded && _cache.value.isNotEmpty()) return
            val loaded = withContext(NonCancellable) {
                contentLoader.loadGlossary()
            }
            _cache.value = loaded
            if (loaded.isNotEmpty()) {
                isLoaded = true
            }
        }
    }
}
