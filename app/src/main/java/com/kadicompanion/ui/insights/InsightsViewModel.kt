package com.kadicompanion.ui.insights

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kadicompanion.domain.repository.AssessmentRepository
import com.kadicompanion.domain.repository.DebugAttemptRecord
import com.kadicompanion.domain.repository.QuizAttemptRecord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class InsightsUiState(
    val isLoading: Boolean = true,
    val quizAttempts: List<QuizAttemptRecord> = emptyList(),
    val debugAttempts: List<DebugAttemptRecord> = emptyList(),
    val accuracyFraction: Float = 0f,
    val totalQuestions: Int = 0,
    val weakestConcepts: List<Pair<String, Float>> = emptyList(),
    val debugTotal: Int = 0,
    val debugRevealedWithoutAttempt: Int = 0
)

@HiltViewModel
class InsightsViewModel @Inject constructor(
    private val assessmentRepository: AssessmentRepository
) : ViewModel() {
    val uiState: StateFlow<InsightsUiState> = combine(assessmentRepository.observeQuizAttempts(), assessmentRepository.observeDebugAttempts()) { quiz, debug ->
        val correct = quiz.count { it.wasCorrect }
        val total = quiz.size
        val accuracy = if (total == 0) 0f else correct.toFloat() / total
        val byConcept = quiz.groupBy { it.concept }
            .map { (concept, rows) -> concept to (1f - (rows.count { it.wasCorrect }.toFloat() / rows.size)) }
            .sortedByDescending { it.second }
            .take(5)
        InsightsUiState(false, quiz, debug, accuracy, total, byConcept, debug.size, debug.count { it.viewedFix && it.completedAt == null })
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), InsightsUiState())
}
