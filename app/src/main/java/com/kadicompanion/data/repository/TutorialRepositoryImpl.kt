package com.kadicompanion.data.repository

import com.kadicompanion.data.content.ContentLoader
import com.kadicompanion.domain.model.Lesson
import com.kadicompanion.domain.model.TutorialPhase
import com.kadicompanion.domain.repository.TutorialRepository
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TutorialRepositoryImpl @Inject constructor(
    private val contentLoader: ContentLoader
) : TutorialRepository {

    private val cacheMutex = Mutex()
    private val _phases = MutableStateFlow<List<TutorialPhase>>(emptyList())
    private val phases = _phases.asStateFlow()
    @Volatile private var isLoaded = false
    @Volatile private var lessonById: Map<String, Lesson> = emptyMap()
    @Volatile private var phaseById: Map<String, TutorialPhase> = emptyMap()

    override fun observePhases(): Flow<List<TutorialPhase>> = phases.onStart {
        if (!isLoaded) ensureLoaded()
    }

    override suspend fun getPhase(phaseId: String): TutorialPhase? {
        ensureLoaded()
        return phaseById[phaseId]
    }

    override suspend fun getLesson(lessonId: String): Lesson? {
        ensureLoaded()
        return lessonById[lessonId]
    }

    override suspend fun getPreviousLesson(lessonId: String): Lesson? {
        ensureLoaded()
        for (phase in _phases.value) {
            val lessons = phase.lessons
            val idx = lessons.indexOfFirst { it.id == lessonId }
            if (idx != -1) {
                if (idx - 1 >= 0) return lessons[idx - 1]
                return null
            }
        }
        return null
    }

    override suspend fun getNextLesson(lessonId: String): Lesson? {
        ensureLoaded()
        for (phase in _phases.value) {
            val lessons = phase.lessons
            val idx = lessons.indexOfFirst { it.id == lessonId }
            if (idx != -1) {
                if (idx + 1 < lessons.size) return lessons[idx + 1]
                return null
            }
        }
        return null
    }

    override suspend fun getLastLessonOfPreviousPhase(phaseId: String): Lesson? {
        ensureLoaded()
        val sorted = _phases.value
        val idx = sorted.indexOfFirst { it.id == phaseId }
        if (idx <= 0) return null
        return sorted[idx - 1].lessons.lastOrNull()
    }

    override suspend fun getFirstLessonOfNextPhase(phaseId: String): Lesson? {
        ensureLoaded()
        val sorted = _phases.value
        val idx = sorted.indexOfFirst { it.id == phaseId }
        if (idx == -1 || idx + 1 >= sorted.size) return null
        return sorted[idx + 1].lessons.firstOrNull()
    }

    private suspend fun ensureLoaded() {
        if (isLoaded && _phases.value.isNotEmpty()) return
        cacheMutex.withLock {
            if (isLoaded && _phases.value.isNotEmpty()) return
            val loaded = withContext(NonCancellable) {
                contentLoader.loadPhases().sortedBy { it.order }
            }
            phaseById = loaded.associateBy { it.id }
            lessonById = loaded.flatMap { it.lessons }.associateBy { it.id }
            _phases.value = loaded
            if (loaded.isNotEmpty()) {
                isLoaded = true
            }
        }
    }
}
