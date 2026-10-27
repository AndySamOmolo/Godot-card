package com.kadicompanion.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kadicompanion.domain.model.LearnerTrack
import com.kadicompanion.domain.model.ReviewItem
import com.kadicompanion.domain.repository.LearnerProfileRepository
import com.kadicompanion.domain.repository.ProgressRepository
import com.kadicompanion.domain.repository.TutorialRepository
import com.kadicompanion.domain.usecase.GetOverallProgressUseCase
import com.kadicompanion.domain.usecase.GetReviewQueueUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class HomePhaseMini(
    val id: String,
    val order: Int,
    val title: String,
    val completed: Int,
    val total: Int
) {
    val fraction: Float get() = if (total == 0) 0f else completed.toFloat() / total
}

data class HomeUiState(
    val isLoading: Boolean = true,
    val continueLessonId: String? = null,
    val continueLessonTitle: String? = null,
    val continuePhaseTitle: String? = null,
    val continueLessonOrder: Int = 1,
    val overallCompleted: Int = 0,
    val overallTotal: Int = 0,
    val overallFraction: Float = 0f,
    val phasesMini: List<HomePhaseMini> = emptyList(),
    val reviewQueue: List<ReviewItem> = emptyList(),
    val track: LearnerTrack? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val progress: ProgressRepository,
    private val tutorial: TutorialRepository,
    private val learnerProfile: LearnerProfileRepository,
    private val getOverallProgress: GetOverallProgressUseCase,
    private val getReviewQueue: GetReviewQueueUseCase
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = combine(
        tutorial.observePhases(),
        progress.observeAllLessonProgress(),
        getOverallProgress(),
        getReviewQueue(),
        learnerProfile.observeTrack()
    ) { phases, allProgress, overall, review, track ->
        val sortedPhases = phases.sortedBy { it.order }
        val completedIds = allProgress.filter { it.isCompleted }.map { it.lessonId }.toHashSet()

        // 1. Prefer the most recently opened incomplete lesson;
        // 2. Otherwise pick the first uncompleted lesson across the course;
        // 3. Otherwise fall back to the very first lesson.
        val lastOpenedIncomplete = allProgress
            .filter { !it.isCompleted && (it.lastOpenedAt ?: 0L) > 0L }
            .maxByOrNull { it.lastOpenedAt ?: 0L }
            ?.lessonId

        val allLessonsWithPhase = sortedPhases.flatMap { phase ->
            phase.lessons.sortedBy { it.order }.map { lesson -> lesson to phase }
        }

        val targetPair = allLessonsWithPhase.firstOrNull { it.first.id == lastOpenedIncomplete }
            ?: allLessonsWithPhase.firstOrNull { (lesson, phase) ->
                val skipPhase0 = (track == LearnerTrack.EXPERIENCED_DEV && phase.order == 0)
                !skipPhase0 && lesson.id !in completedIds
            }
            ?: allLessonsWithPhase.firstOrNull()

        val minis = sortedPhases.map { p ->
            HomePhaseMini(
                id = p.id,
                order = p.order,
                title = p.title,
                completed = p.lessons.count { it.id in completedIds },
                total = p.lessons.size
            )
        }

        HomeUiState(
            isLoading = false,
            continueLessonId = targetPair?.first?.id,
            continueLessonTitle = targetPair?.first?.title,
            continuePhaseTitle = targetPair?.second?.let { "Phase ${it.order}: ${it.title}" },
            continueLessonOrder = targetPair?.first?.order ?: 1,
            overallCompleted = overall.completed,
            overallTotal = overall.total,
            overallFraction = overall.fraction,
            phasesMini = minis,
            reviewQueue = review,
            track = track
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())
}
