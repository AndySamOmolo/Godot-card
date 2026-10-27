package com.kadicompanion.ui.tutorial.debugchallenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kadicompanion.domain.model.DebugChallenge
import com.kadicompanion.domain.repository.AssessmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DebugChallengeUiState(
    val challenge: DebugChallenge? = null,
    val selectedLineNumber: Int? = null,
    val targetBuggyLine: Int = 1,
    val solvedByAttempt: Boolean = false,
    val attemptFeedback: String? = null,
    val hintShown: Boolean = false,
    val fixShown: Boolean = false,
    val isLoading: Boolean = true
)

@HiltViewModel
class DebugChallengeViewModel @Inject constructor(
    private val assessmentRepository: AssessmentRepository,
    savedStateHandle: androidx.lifecycle.SavedStateHandle
) : ViewModel() {

    private val phaseId: String = checkNotNull(savedStateHandle["phaseId"])
    private val _ui = MutableStateFlow(DebugChallengeUiState())
    val uiState: StateFlow<DebugChallengeUiState> = _ui.asStateFlow()

    init {
        viewModelScope.launch {
            val ch = assessmentRepository.getDebugChallenge(phaseId)
            val targetLine = ch?.let { resolveBuggyLine(it) } ?: 1
            _ui.value = DebugChallengeUiState(
                challenge = ch,
                targetBuggyLine = targetLine,
                isLoading = false
            )
        }
    }

    /**
     * Resolves the 1-based line number of the bug in [ch.brokenCode]:
     * uses [ch.buggyLineNumber] if explicitly provided in JSON, otherwise finds
     * the first line where [ch.brokenCode] and [ch.fixedCode] differ.
     */
    private fun resolveBuggyLine(ch: DebugChallenge): Int {
        ch.buggyLineNumber?.let { if (it > 0) return it }
        val brokenLines = ch.brokenCode.lines()
        val fixedLines = ch.fixedCode.lines()
        for (i in brokenLines.indices) {
            val b = brokenLines[i].trim()
            val f = fixedLines.getOrNull(i)?.trim()
            if (b != f) return i + 1
        }
        return 1
    }

    fun selectLine(lineNumber: Int) {
        val s = _ui.value
        val ch = s.challenge ?: return
        if (s.fixShown) return

        if (lineNumber == s.targetBuggyLine) {
            _ui.value = s.copy(
                selectedLineNumber = lineNumber,
                solvedByAttempt = true,
                fixShown = true,
                attemptFeedback = "Bullseye! Line $lineNumber is where the bug lives. Here is the fixed code and why it works:"
            )
            viewModelScope.launch {
                // Completed by genuine attempt (without viewing fix first!)
                assessmentRepository.markDebugChallengeCompleted(ch.id)
            }
        } else {
            _ui.value = s.copy(
                selectedLineNumber = lineNumber,
                attemptFeedback = "Line $lineNumber looks okay! Re-read the symptom above and check how conditions or variables change on each line."
            )
        }
    }

    fun toggleHint() {
        _ui.value = _ui.value.copy(hintShown = !_ui.value.hintShown)
    }

    fun revealFix() {
        val s = _ui.value
        val ch = s.challenge ?: return
        if (s.fixShown) return
        _ui.value = s.copy(
            fixShown = true,
            selectedLineNumber = s.targetBuggyLine
        )
        viewModelScope.launch {
            assessmentRepository.markDebugFixViewed(ch.id)
            assessmentRepository.markDebugChallengeCompleted(ch.id)
        }
    }
}
