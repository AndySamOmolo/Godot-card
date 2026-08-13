package com.kadicompanion.data.repository

import com.kadicompanion.data.content.ContentLoader
import com.kadicompanion.domain.model.GlossaryEntry
import com.kadicompanion.domain.repository.GlossaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlossaryRepositoryImpl @Inject constructor(
    private val contentLoader: ContentLoader
) : GlossaryRepository {

    private val mutex = Mutex()
    private val _cache = MutableStateFlow<List<GlossaryEntry>>(emptyList())
    private val cache = _cache.asStateFlow()
    private var loadStarted = false

    // *** THE FIX *** — same as TutorialRepositoryImpl
    override fun observeAll(): Flow<List<GlossaryEntry>> = cache.onEach {
        if (!loadStarted) ensureLoaded()
    }

    override suspend fun get(term: String): GlossaryEntry? {
        ensureLoaded()
        return _cache.value.firstOrNull { it.term.equals(term, ignoreCase = true) }
    }

    override suspend fun search(query: String): List<GlossaryEntry> {
        ensureLoaded()
        val q = query.trim().lowercase()
        if (q.isEmpty()) return _cache.value
        return _cache.value.filter { entry ->
            entry.term.lowercase().contains(q) || entry.shortDefinition.lowercase().contains(q)
        }
    }

    private suspend fun ensureLoaded() {
        if (_cache.value.isNotEmpty()) return
        loadStarted = true
        mutex.withLock {
            if (_cache.value.isNotEmpty()) return
            _cache.value = contentLoader.loadGlossary()
        }
    }
}
