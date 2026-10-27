package com.kadicompanion.ui.tutorial.practice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kadicompanion.domain.model.Lesson
import com.kadicompanion.domain.repository.TutorialRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PracticeUiState(
    val lesson: Lesson? = null,
    val userCode: String = "",
    val hintShown: Boolean = false,
    val solutionShown: Boolean = false,
    val checkPassed: Boolean? = null,
    val checkFeedback: String? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class PracticeViewModel @Inject constructor(
    private val tutorialRepository: TutorialRepository,
    savedStateHandle: androidx.lifecycle.SavedStateHandle
) : ViewModel() {

    private val lessonId: String = checkNotNull(savedStateHandle["lessonId"])
    private val _ui = MutableStateFlow(PracticeUiState())
    val uiState: StateFlow<PracticeUiState> = _ui.asStateFlow()

    init {
        viewModelScope.launch {
            val lesson = tutorialRepository.getLesson(lessonId)
            val starter = lesson?.practicePrompt?.starterCode
                ?: "# Write your GDScript solution below:\n"
            _ui.value = PracticeUiState(
                lesson = lesson,
                userCode = starter,
                isLoading = false
            )
        }
    }

    fun updateUserCode(newCode: String) {
        _ui.value = _ui.value.copy(
            userCode = newCode,
            checkPassed = null,
            checkFeedback = null
        )
    }

    fun insertToken(token: String) {
        val current = _ui.value.userCode
        val updated = if (current.endsWith("\n") || current.endsWith(" ") || current.isEmpty()) {
            current + token
        } else {
            "$current $token"
        }
        updateUserCode(updated)
    }

    fun resetCode() {
        val starter = _ui.value.lesson?.practicePrompt?.starterCode
            ?: "# Write your GDScript solution below:\n"
        _ui.value = _ui.value.copy(
            userCode = starter,
            checkPassed = null,
            checkFeedback = null
        )
    }

    fun checkCode() {
        val s = _ui.value
        val prompt = s.lesson?.practicePrompt ?: return
        val codeTrimmed = s.userCode
            .lines()
            .filterNot { it.trim().startsWith("#") }
            .joinToString("\n")
            .trim()

        if (codeTrimmed.isBlank()) {
            _ui.value = s.copy(
                checkPassed = false,
                checkFeedback = "Your editor only has comments right now. Type your GDScript attempt below the comment line and tap Check My Code!"
            )
            return
        }

        // Determine required tokens: use explicit requiredTokens if present, otherwise extract key identifiers from solutionCode
        val tokensToCheck = if (prompt.requiredTokens.isNotEmpty()) {
            prompt.requiredTokens
        } else {
            extractKeyTokensFromSolution(prompt.solutionCode.orEmpty())
        }

        val missing = tokensToCheck.filter { token ->
            !codeTrimmed.contains(token)
        }

        if (missing.isEmpty()) {
            val outMsg = prompt.expectedOutput?.let { "\nExpected behavior in Godot: $it" } ?: ""
            _ui.value = s.copy(
                checkPassed = true,
                checkFeedback = "Spot on! Your GDScript includes all required constructs (${tokensToCheck.joinToString(", ")}).$outMsg"
            )
        } else {
            _ui.value = s.copy(
                checkPassed = false,
                checkFeedback = "Almost there! Check your syntax — your code is still missing: ${missing.take(4).joinToString(", ") { "`$it`" }}. Tap Show Hint if you need a nudge."
            )
        }
    }

    private fun extractKeyTokensFromSolution(solution: String): List<String> {
        val nonComment = solution.lines()
            .filterNot { it.trim().startsWith("#") }
            .joinToString(" ")
        val regex = Regex("[A-Za-z_][A-Za-z0-9_]*|==|!=|>=|<=|\\+=|\\*=|->")
        return regex.findAll(nonComment)
            .map { it.value }
            .filter { it.length >= 2 }
            .distinct()
            .take(5)
            .toList()
    }

    fun toggleHint() { _ui.value = _ui.value.copy(hintShown = !_ui.value.hintShown) }
    fun revealSolution() { _ui.value = _ui.value.copy(solutionShown = true) }
}
