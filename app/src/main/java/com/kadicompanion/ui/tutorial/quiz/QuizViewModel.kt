package com.kadicompanion.ui.tutorial.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kadicompanion.domain.model.QuizQuestion
import com.kadicompanion.domain.repository.AssessmentRepository
import com.kadicompanion.domain.repository.TutorialRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuizUiState(
    val questions: List<QuizQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val selectedChoice: Int? = null,
    val wasCorrect: Boolean? = null,
    val canRetry: Boolean = true,
    val firstTryCorrectCount: Int = 0,
    val attemptedOnCurrentQuestion: Boolean = false,
    val isLoading: Boolean = true
) {
    val current: QuizQuestion? get() = questions.getOrNull(currentIndex)
    val isLast: Boolean get() = currentIndex >= questions.size - 1
    val finished: Boolean get() = questions.isNotEmpty() && currentIndex >= questions.size
    val scoreFraction: Float get() = if (questions.isEmpty()) 1f else firstTryCorrectCount.toFloat() / questions.size
}

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val tutorialRepository: TutorialRepository,
    private val assessmentRepository: AssessmentRepository,
    savedStateHandle: androidx.lifecycle.SavedStateHandle
) : ViewModel() {

    private val lessonId: String = checkNotNull(savedStateHandle["lessonId"])

    private val _ui = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _ui.asStateFlow()

    init {
        viewModelScope.launch {
            val lesson = tutorialRepository.getLesson(lessonId)
            _ui.value = QuizUiState(questions = lesson?.quizQuestions ?: emptyList(), isLoading = false)
        }
    }

    fun selectAnswer(choiceIndex: Int) {
        val s = _ui.value
        val q = s.current ?: return
        if (s.wasCorrect != null) return
        val correct = choiceIndex == q.correctChoiceIndex
        val newCorrectCount = if (correct && !s.attemptedOnCurrentQuestion) {
            s.firstTryCorrectCount + 1
        } else {
            s.firstTryCorrectCount
        }
        _ui.value = s.copy(
            selectedChoice = choiceIndex,
            wasCorrect = correct,
            firstTryCorrectCount = newCorrectCount,
            attemptedOnCurrentQuestion = true
        )
        viewModelScope.launch { assessmentRepository.recordQuizAttempt(q.id, q.concept, correct) }
    }

    fun retry() {
        _ui.value = _ui.value.copy(selectedChoice = null, wasCorrect = null, canRetry = false)
    }

    fun next() {
        val s = _ui.value
        _ui.value = s.copy(
            currentIndex = s.currentIndex + 1,
            selectedChoice = null,
            wasCorrect = null,
            canRetry = true,
            attemptedOnCurrentQuestion = false
        )
    }

    fun restartQuiz() {
        val s = _ui.value
        _ui.value = QuizUiState(
            questions = s.questions,
            isLoading = false
        )
    }
}
