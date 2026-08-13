package com.kadicompanion.domain.usecase

import com.kadicompanion.domain.model.LearnerTrack
import com.kadicompanion.domain.model.TutorialPhase
import com.kadicompanion.domain.repository.ProgressRepository
import com.kadicompanion.domain.repository.TutorialRepository
import com.kadicompanion.data.local.datastore.PreferencesRepository
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

data class PhaseWithProgress(
    val phase: TutorialPhase,
    val completedLessons: Int,
    val totalLessons: Int,
    val isLocked: Boolean,
    val isOptionalForTrack: Boolean
)

class GetPhasesUseCase @Inject constructor(
    private val tutorialRepository: TutorialRepository,
    private val progressRepository: ProgressRepository,
    private val skipGating: ObserveSkipGatingUseCase
) {
    operator fun invoke(track: LearnerTrack?) = combine(
        tutorialRepository.observePhases(),
        progressRepository.observeAllLessonProgress(),
        skipGating()
    ) { phases, progress, skip ->
        val completedIds = progress.filter { it.isCompleted }.map { it.lessonId }.toHashSet()
        val sorted = phases.sortedBy { it.order }
        val result = mutableListOf<PhaseWithProgress>()
        var previousCompleted = true
        for (phase in sorted) {
            val total = phase.lessons.size
            val completed = phase.lessons.count { it.id in completedIds }
            val phaseDone = total > 0 && completed == total
            val optionalForTrack = track != null && phase.isSkippableForTrack.contains(track)
            val locked = when {
                skip -> false
                optionalForTrack -> false
                track == LearnerTrack.GODOT_REFERENCE -> false
                !previousCompleted -> true
                else -> false
            }
            result += PhaseWithProgress(phase, completed, total, locked, optionalForTrack)
            if (!phaseDone && !optionalForTrack) previousCompleted = false
        }
        result
    }
}

class ObserveSkipGatingUseCase @Inject constructor(
    private val prefs: PreferencesRepository
) {
    operator fun invoke() = prefs.observeSkipGating()
}
