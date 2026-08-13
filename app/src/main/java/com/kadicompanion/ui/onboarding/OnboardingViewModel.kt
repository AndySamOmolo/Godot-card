package com.kadicompanion.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kadicompanion.domain.model.LearnerTrack
import com.kadicompanion.domain.repository.LearnerProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OnboardingUiState(
    val programmedBefore: Boolean? = null,
    val usedGodotBefore: Boolean? = null,
    val resolvedTrack: LearnerTrack? = null,
    val isSaving: Boolean = false
) {
    val canFinish: Boolean get() = programmedBefore != null && usedGodotBefore != null
}

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val learnerProfile: LearnerProfileRepository
) : ViewModel() {
    private val _ui = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _ui.asStateFlow()

    fun setProgrammedBefore(value: Boolean) { _ui.value = _ui.value.copy(programmedBefore = value); resolveTrack() }
    fun setUsedGodotBefore(value: Boolean) { _ui.value = _ui.value.copy(usedGodotBefore = value); resolveTrack() }

    private fun resolveTrack() {
        val s = _ui.value
        val prog = s.programmedBefore
        val godot = s.usedGodotBefore
        val track = when {
            prog == null || godot == null -> null
            godot -> LearnerTrack.GODOT_REFERENCE
            prog -> LearnerTrack.EXPERIENCED_DEV
            else -> LearnerTrack.FOUNDATIONS
        }
        _ui.value = s.copy(resolvedTrack = track)
    }

    fun finish() {
        val track = _ui.value.resolvedTrack ?: return
        _ui.value = _ui.value.copy(isSaving = true)
        viewModelScope.launch {
            learnerProfile.setTrack(track)
            learnerProfile.markOnboarded()
            _ui.value = _ui.value.copy(isSaving = false)
        }
    }
}
