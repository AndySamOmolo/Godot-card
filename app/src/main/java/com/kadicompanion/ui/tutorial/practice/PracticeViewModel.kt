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

data class PracticeUiState(val lesson: Lesson? = null, val hintShown: Boolean = false, val solutionShown: Boolean = false, val isLoading: Boolean = true)

@HiltViewModel
class PracticeViewModel @Inject constructor(
    private val tutorialRepository: TutorialRepository,
    savedStateHandle: androidx.lifecycle.SavedStateHandle
) : ViewModel() {

    private val lessonId: String = checkNotNull(savedStateHandle["lessonId"])
    private val _ui = MutableStateFlow(PracticeUiState())
    val uiState: StateFlow<PracticeUiState> = _ui.asStateFlow()

    init { viewModelScope.launch { _ui.value = PracticeUiState(lesson = tutorialRepository.getLesson(lessonId), isLoading = false) } }

    fun toggleHint() { _ui.value = _ui.value.copy(hintShown = !_ui.value.hintShown) }
    fun revealSolution() { _ui.value = _ui.value.copy(solutionShown = true) }
}
