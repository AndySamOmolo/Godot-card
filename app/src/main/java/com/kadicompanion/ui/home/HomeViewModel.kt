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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = true,
    val continueLessonId: String? = null,
    val continueLessonTitle: String? = null,
    val continuePhaseTitle: String? = null,
    val overallCompleted: Int = 0,
    val overallTotal: Int = 0,
    val overallFraction: Float = 0f,
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

    private val continueFlow = flow {
        val last = progress.getLastOpenedLesson()
        if (last != null) {
            val lesson = tutorial.getLesson(last.lessonId)
            if (lesson != null) {
                val phase = tutorial.getPhase(lesson.phaseId)
                emit(Triple(lesson.id, lesson.title, phase?.title))
            } else emit(Triple<String?, String?, String?>(null, null, null))
        } else emit(Triple<String?, String?, String?>(null, null, null))
    }

    val uiState: StateFlow<HomeUiState> = combine(continueFlow, getOverallProgress(), getReviewQueue(), learnerProfile.observeTrack()) { cont, overall, review, track ->
        HomeUiState(
            isLoading = false,
            continueLessonId = cont.first,
            continueLessonTitle = cont.second,
            continuePhaseTitle = cont.third,
            overallCompleted = overall.completed,
            overallTotal = overall.total,
            overallFraction = overall.fraction,
            reviewQueue = review,
            track = track
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())
}
