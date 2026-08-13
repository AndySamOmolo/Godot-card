package com.kadicompanion.ui.tutorial.phaselist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kadicompanion.domain.model.Lesson
import com.kadicompanion.domain.model.TutorialPhase
import com.kadicompanion.domain.repository.ProgressRepository
import com.kadicompanion.domain.repository.TutorialRepository
import com.kadicompanion.ui.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class PhaseDetailUiState(
    val isLoading: Boolean = true,
    val phase: TutorialPhase? = null,
    val lessons: List<Lesson> = emptyList(),
    val completedIds: Set<String> = emptySet()
)

@HiltViewModel
class PhaseDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val tutorialRepo: TutorialRepository,
    private val progressRepo: ProgressRepository
) : ViewModel() {

    private val phaseId: String = checkNotNull(savedStateHandle[Routes.ARG_PHASE_ID])

    val uiState: StateFlow<PhaseDetailUiState> = combine(
        flow {
            val phase = tutorialRepo.getPhase(phaseId)
            emit(phase)
        },
        progressRepo.observeAllLessonProgress()
    ) { phase, allProgress ->
        val completed = allProgress.filter { it.isCompleted }.map { it.lessonId }.toSet()
        PhaseDetailUiState(
            isLoading = false,
            phase = phase,
            lessons = phase?.lessons.orEmpty(),
            completedIds = completed
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PhaseDetailUiState())
}
