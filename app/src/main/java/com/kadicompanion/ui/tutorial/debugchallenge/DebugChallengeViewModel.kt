package com.kadicompanion.ui.tutorial.debugchallenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kadicompanion.domain.model.DebugChallenge
import com.kadicompanion.domain.repository.AssessmentRepository
import com.kadicompanion.domain.repository.TutorialRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DebugChallengeUiState(val challenge: DebugChallenge? = null, val hintAcknowledged: Boolean = false, val fixShown: Boolean = false, val isLoading: Boolean = true)

@HiltViewModel
class DebugChallengeViewModel @Inject constructor(
    private val tutorialRepository: TutorialRepository,
    private val assessmentRepository: AssessmentRepository,
    savedStateHandle: androidx.lifecycle.SavedStateHandle
) : ViewModel() {

    private val phaseId: String = checkNotNull(savedStateHandle["phaseId"])
    private val _ui = MutableStateFlow(DebugChallengeUiState())
    val uiState: StateFlow<DebugChallengeUiState> = _ui.asStateFlow()

    init {
        viewModelScope.launch {
            val ch = assessmentRepository.getDebugChallenge(phaseId)
            _ui.value = DebugChallengeUiState(challenge = ch, isLoading = false)
        }
    }

    fun revealFix() {
        val s = _ui.value
        val ch = s.challenge ?: return
        if (s.fixShown) return
        _ui.value = s.copy(fixShown = true)
        viewModelScope.launch {
            assessmentRepository.markDebugFixViewed(ch.id)
            assessmentRepository.markDebugChallengeCompleted(ch.id)
        }
    }
}
