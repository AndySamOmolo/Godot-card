package com.kadicompanion.domain.usecase

import com.kadicompanion.domain.repository.ProgressRepository
import com.kadicompanion.domain.repository.TutorialRepository
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

data class OverallProgress(val completed: Int, val total: Int) {
    val fraction: Float get() = if (total == 0) 0f else completed.toFloat() / total
}

class GetOverallProgressUseCase @Inject constructor(
    private val tutorialRepository: TutorialRepository,
    private val progressRepository: ProgressRepository
) {
    operator fun invoke() = combine(
        tutorialRepository.observePhases(),
        progressRepository.observeAllLessonProgress()
    ) { phases, progress ->
        val total = phases.sumOf { it.lessons.size }
        val completedIds = progress.filter { it.isCompleted }.map { it.lessonId }.toHashSet()
        val completed = phases.flatMap { it.lessons }.count { it.id in completedIds }
        OverallProgress(completed, total)
    }
}
