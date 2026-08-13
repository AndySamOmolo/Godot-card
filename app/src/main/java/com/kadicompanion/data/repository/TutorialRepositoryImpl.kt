package com.kadicompanion.data.repository

import com.kadicompanion.data.content.ContentLoader
import com.kadicompanion.domain.model.Lesson
import com.kadicompanion.domain.model.TutorialPhase
import com.kadicompanion.domain.repository.TutorialRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TutorialRepositoryImpl @Inject constructor(
    private val contentLoader: ContentLoader
) : TutorialRepository {

    private val cacheMutex = Mutex()
    private val _phases = MutableStateFlow<List<TutorialPhase>>(emptyList())
    private val phases = _phases.asStateFlow()
    private var loadStarted = false

    // *** THE FIX *** — observePhases() now triggers ensureLoaded() on subscription.
    // Without this the UI would see an empty list forever because no one was
    // calling getPhase/getLesson (those do trigger ensureLoaded). The PhaseList
    // screen only observes the flow, so we have to start the load from there.
    override fun observePhases(): Flow<List<TutorialPhase>> = phases.onEach {
        if (!loadStarted) ensureLoaded()
    }

    override suspend fun getPhase(phaseId: String): TutorialPhase? {
        ensureLoaded()
        return _phases.value.firstOrNull { it.id == phaseId }
    }

    override suspend fun getLesson(lessonId: String): Lesson? {
        ensureLoaded()
        return _phases.value.flatMap { it.lessons }.firstOrNull { it.id == lessonId }
    }

    override suspend fun getNextLesson(lessonId: String): Lesson? {
        ensureLoaded()
        for (phase in _phases.value.sortedBy { it.order }) {
            val lessons = phase.lessons.sortedBy { it.order }
            val idx = lessons.indexOfFirst { it.id == lessonId }
            if (idx != -1) {
                if (idx + 1 < lessons.size) return lessons[idx + 1]
                return null
            }
        }
        return null
    }

    override suspend fun getFirstLessonOfNextPhase(phaseId: String): Lesson? {
        ensureLoaded()
        val sorted = _phases.value.sortedBy { it.order }
        val idx = sorted.indexOfFirst { it.id == phaseId }
        if (idx == -1 || idx + 1 >= sorted.size) return null
        return sorted[idx + 1].lessons.minByOrNull { it.order }
    }

    private suspend fun ensureLoaded() {
        if (_phases.value.isNotEmpty()) return
        loadStarted = true
        cacheMutex.withLock {
            if (_phases.value.isNotEmpty()) return
            val loaded = contentLoader.loadPhases()
            _phases.value = loaded
        }
    }
}
