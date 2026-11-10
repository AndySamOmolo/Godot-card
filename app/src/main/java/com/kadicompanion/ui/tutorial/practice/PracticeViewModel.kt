package com.kadicompanion.ui.tutorial.practice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kadicompanion.domain.model.Lesson
import com.kadicompanion.domain.repository.ProgressRepository
import com.kadicompanion.domain.repository.TutorialRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PracticeUiState(
    val lesson: Lesson? = null,
    val nextLessonId: String? = null,
    val userCode: String = "",
    val soloMode: Boolean = false,
    val hintShown: Boolean = false,
    val solutionShown: Boolean = false,
    val checkPassed: Boolean? = null,
    val checkFeedback: String? = null,
    val syntaxWarnings: List<String> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class PracticeViewModel @Inject constructor(
    private val tutorialRepository: TutorialRepository,
    private val progressRepository: ProgressRepository,
    savedStateHandle: androidx.lifecycle.SavedStateHandle
) : ViewModel() {

    private val lessonId: String = checkNotNull(savedStateHandle["lessonId"])
    private val _ui = MutableStateFlow(PracticeUiState())
    val uiState: StateFlow<PracticeUiState> = _ui.asStateFlow()

    init {
        viewModelScope.launch {
            val lesson = tutorialRepository.getLesson(lessonId)
            val next = tutorialRepository.getNextLesson(lessonId)
                ?: lesson?.let { tutorialRepository.getFirstLessonOfNextPhase(it.phaseId) }
            val starter = lesson?.practicePrompt?.starterCode
                ?: "# Write your GDScript solution below:\n"
            _ui.value = PracticeUiState(
                lesson = lesson,
                nextLessonId = next?.id,
                userCode = starter,
                isLoading = false
            )
        }
    }

    fun updateUserCode(newCode: String) {
        _ui.value = _ui.value.copy(
            userCode = newCode,
            checkPassed = null,
            checkFeedback = null,
            syntaxWarnings = emptyList()
        )
    }

    fun toggleSoloMode() {
        val nextSolo = !_ui.value.soloMode
        val starter = if (nextSolo) {
            "# SOLO MODE: Write your GDScript solution from scratch!\n# (Try solving without starter scaffolding to build solo muscle memory)\n\n"
        } else {
            _ui.value.lesson?.practicePrompt?.starterCode ?: "# Write your GDScript solution below:\n"
        }
        _ui.value = _ui.value.copy(
            soloMode = nextSolo,
            userCode = starter,
            checkPassed = null,
            checkFeedback = null,
            syntaxWarnings = emptyList()
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
        val starter = if (_ui.value.soloMode) {
            "# SOLO MODE: Write your GDScript solution from scratch!\n\n"
        } else {
            _ui.value.lesson?.practicePrompt?.starterCode ?: "# Write your GDScript solution below:\n"
        }
        _ui.value = _ui.value.copy(
            userCode = starter,
            checkPassed = null,
            checkFeedback = null,
            syntaxWarnings = emptyList()
        )
    }

    fun checkCode() {
        val s = _ui.value
        val prompt = s.lesson?.practicePrompt ?: return
        val nonCommentLines = s.userCode
            .lines()
            .filterNot { it.trim().startsWith("#") }
        val codeTrimmed = nonCommentLines.joinToString("\n").trim()

        if (codeTrimmed.isBlank()) {
            _ui.value = s.copy(
                checkPassed = false,
                checkFeedback = "Your editor only has comments right now. Type your GDScript attempt below the comment line and tap Check My Code!"
            )
            return
        }

        val warnings = runGDScriptSyntaxDiagnostics(nonCommentLines)

        val tokensToCheck = if (prompt.requiredTokens.isNotEmpty()) {
            prompt.requiredTokens
        } else {
            extractKeyTokensFromSolution(prompt.solutionCode.orEmpty())
        }

        val missing = tokensToCheck.filter { token ->
            !codeTrimmed.contains(token)
        }

        if (missing.isEmpty() && warnings.isEmpty()) {
            val outMsg = prompt.expectedOutput?.let { "\n\n**Simulated Godot 4.3 Output:**\n`$it`" } ?: ""
            val modeBadge = if (s.soloMode) " in **Solo Mode (From Scratch)**" else ""
            _ui.value = s.copy(
                checkPassed = true,
                syntaxWarnings = emptyList(),
                checkFeedback = "Spot on! You passed$modeBadge — all required GDScript constructs (${tokensToCheck.joinToString(", ") { "`$it`" }}) and block syntax are valid.$outMsg"
            )
            viewModelScope.launch { progressRepository.markLessonComplete(lessonId) }
        } else if (missing.isEmpty() && warnings.isNotEmpty()) {
            _ui.value = s.copy(
                checkPassed = false,
                syntaxWarnings = warnings,
                checkFeedback = "You included all required keywords, but Godot's GDScript parser flagged ${warnings.size} syntax issue(s) below. Fix the highlighted line(s) and check again!"
            )
        } else {
            _ui.value = s.copy(
                checkPassed = false,
                syntaxWarnings = warnings,
                checkFeedback = "Almost there! Your code is still missing: ${missing.take(5).joinToString(", ") { "`$it`" }}. Tap **Show Hint** if you need a nudge."
            )
        }
    }

    /**
     * Lightweight GDScript 4.3 structural linter so learners get authentic compiler-style feedback
     * (missing colons on block headers, unbalanced parentheses/brackets, `=` vs `==` inside `if`).
     */
    private fun runGDScriptSyntaxDiagnostics(lines: List<String>): List<String> {
        val issues = mutableListOf<String>()
        var openParens = 0
        var openBrackets = 0
        var openBraces = 0

        lines.forEachIndexed { idx, rawLine ->
            val stripped = rawLine.substringBefore("#").trim()
            if (stripped.isEmpty()) return@forEachIndexed

            openParens += stripped.count { it == '(' } - stripped.count { it == ')' }
            openBrackets += stripped.count { it == '[' } - stripped.count { it == ']' }
            openBraces += stripped.count { it == '{' } - stripped.count { it == '}' }

            val isBlockHeader = stripped.startsWith("func ") ||
                stripped.startsWith("static func ") ||
                stripped.startsWith("if ") ||
                stripped.startsWith("elif ") ||
                stripped == "else" ||
                stripped.startsWith("for ") ||
                stripped.startsWith("while ") ||
                stripped.startsWith("match ")

            if (isBlockHeader && !stripped.endsWith(":") && openParens <= 0) {
                val keyword = stripped.substringBefore(" ")
                issues += "Line ${idx + 1}: GDScript `$keyword` header must end with a colon (`:`) — e.g. `$stripped:`."
            }

            if ((stripped.startsWith("if ") || stripped.startsWith("elif ")) &&
                Regex("(?<![=!<>])=(?!=)").containsMatchIn(stripped)
            ) {
                issues += "Line ${idx + 1}: Inside `if`/`elif`, use `==` for comparison instead of single `=` assignment."
            }
        }

        if (openParens != 0) {
            issues += "Unbalanced parentheses `(` `)` across your script — check that every `(` has a matching `)`."
        }
        if (openBrackets != 0) {
            issues += "Unbalanced square brackets `[` `]` — check your Array indexing or literals."
        }
        if (openBraces != 0) {
            issues += "Unbalanced curly braces `{` `}` — check your Dictionary or enum literals."
        }
        return issues
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
