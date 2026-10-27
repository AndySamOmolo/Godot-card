package com.kadicompanion.ui.tutorial.lessondetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kadicompanion.domain.model.Lesson
import com.kadicompanion.domain.repository.ProgressRepository
import com.kadicompanion.domain.repository.TutorialRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LessonDetailUiState(
    val lesson: Lesson? = null,
    val phaseTitle: String? = null,
    val phaseId: String? = null,
    val totalLessonsInPhase: Int = 0,
    val previousLessonId: String? = null,
    val nextLessonId: String? = null,
    val hasDebugChallenge: Boolean = false,
    val isCompleted: Boolean = false,
    val isBookmarked: Boolean = false,
    val isLoading: Boolean = true,
    val error: String? = null
)

private data class LessonBundle(
    val lesson: Lesson?,
    val phaseTitle: String?,
    val phaseId: String?,
    val totalLessonsInPhase: Int,
    val previousLessonId: String?,
    val nextLessonId: String?,
    val hasDebugChallenge: Boolean
)

@HiltViewModel
class LessonDetailViewModel @Inject constructor(
    private val tutorialRepository: TutorialRepository,
    private val progressRepository: ProgressRepository,
    savedStateHandle: androidx.lifecycle.SavedStateHandle
) : ViewModel() {

    private val lessonId: String = checkNotNull(savedStateHandle["lessonId"])

    val uiState: StateFlow<LessonDetailUiState> = combine(
        flow {
            val lesson = tutorialRepository.getLesson(lessonId)
            val phase = lesson?.let { tutorialRepository.getPhase(it.phaseId) }
            val prev = tutorialRepository.getPreviousLesson(lessonId)
                ?: lesson?.let { tutorialRepository.getLastLessonOfPreviousPhase(it.phaseId) }
            val next = tutorialRepository.getNextLesson(lessonId)
                ?: lesson?.let { tutorialRepository.getFirstLessonOfNextPhase(it.phaseId) }
            emit(
                LessonBundle(
                    lesson = lesson,
                    phaseTitle = phase?.title,
                    phaseId = lesson?.phaseId,
                    totalLessonsInPhase = phase?.lessons?.size ?: 0,
                    previousLessonId = prev?.id,
                    nextLessonId = next?.id,
                    hasDebugChallenge = phase?.debugChallenge != null
                )
            )
        },
        progressRepository.observeLessonProgress(lessonId),
        progressRepository.observeBookmarkForLesson(lessonId)
    ) { bundle, progress, bookmark ->
        LessonDetailUiState(
            lesson = bundle.lesson,
            phaseTitle = bundle.phaseTitle,
            phaseId = bundle.phaseId,
            totalLessonsInPhase = bundle.totalLessonsInPhase,
            previousLessonId = bundle.previousLessonId,
            nextLessonId = bundle.nextLessonId,
            hasDebugChallenge = bundle.hasDebugChallenge,
            isCompleted = progress?.isCompleted == true,
            isBookmarked = bookmark != null,
            isLoading = false
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LessonDetailUiState())

    init { viewModelScope.launch { progressRepository.markLessonOpened(lessonId) } }

    fun markComplete() { viewModelScope.launch { progressRepository.markLessonComplete(lessonId) } }

    fun toggleBookmark() {
        viewModelScope.launch {
            val s = uiState.value
            val phaseId = s.lesson?.phaseId ?: return@launch
            if (s.isBookmarked) progressRepository.removeBookmarkForLesson(lessonId)
            else progressRepository.addBookmark(phaseId = phaseId, lessonId = lessonId)
        }
    }
}
