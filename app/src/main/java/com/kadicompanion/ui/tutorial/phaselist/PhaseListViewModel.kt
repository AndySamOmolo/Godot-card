package com.kadicompanion.ui.tutorial.phaselist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kadicompanion.domain.model.LearnerTrack
import com.kadicompanion.domain.repository.LearnerProfileRepository
import com.kadicompanion.domain.usecase.GetPhasesUseCase
import com.kadicompanion.domain.usecase.PhaseWithProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class PhaseListUiState(
    val isLoading: Boolean = true,
    val phases: List<PhaseWithProgress> = emptyList(),
    val track: LearnerTrack? = null
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PhaseListViewModel @Inject constructor(
    private val getPhases: GetPhasesUseCase,
    private val learnerProfile: LearnerProfileRepository
) : ViewModel() {

    val uiState: StateFlow<PhaseListUiState> = learnerProfile.observeTrack()
        .flatMapLatest { track -> getPhases(track).map { phases -> PhaseListUiState(false, phases, track) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PhaseListUiState())
}
