package com.kadicompanion.ui.glossary

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kadicompanion.domain.model.GlossaryEntry
import com.kadicompanion.domain.repository.GlossaryRepository
import com.kadicompanion.domain.repository.ProgressRepository
import com.kadicompanion.domain.repository.TutorialRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GlossaryDetailUiState(
    val entry: GlossaryEntry? = null,
    val lessonTitles: Map<String, String> = emptyMap(),
    val isBookmarked: Boolean = false,
    val isLoading: Boolean = true
)

@HiltViewModel
class GlossaryDetailViewModel @Inject constructor(
    private val glossary: GlossaryRepository,
    private val tutorialRepository: TutorialRepository,
    private val progressRepository: ProgressRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val rawTerm: String = checkNotNull(savedStateHandle["term"])
    private val cleanTerm: String = rawTerm.substringBefore('|').trim()

    private val _entry = MutableStateFlow<GlossaryEntry?>(null)
    private val _titles = MutableStateFlow<Map<String, String>>(emptyMap())
    private val _loading = MutableStateFlow(true)

    val uiState: StateFlow<GlossaryDetailUiState> = combine(
        _entry,
        _titles,
        _loading,
        progressRepository.observeBookmarkForGlossary(cleanTerm)
    ) { entry, titles, loading, bookmark ->
        GlossaryDetailUiState(
            entry = entry,
            lessonTitles = titles,
            isBookmarked = bookmark != null,
            isLoading = loading
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), GlossaryDetailUiState())

    init {
        viewModelScope.launch {
            val entry = glossary.get(rawTerm)
            val titles = mutableMapOf<String, String>()
            entry?.relatedLessonIds?.forEach { lid ->
                tutorialRepository.getLesson(lid)?.let { lesson ->
                    titles[lid] = lesson.title
                }
            }
            _entry.value = entry
            _titles.value = titles
            _loading.value = false
        }
    }

    fun toggleBookmark() {
        val current = uiState.value
        val entry = current.entry ?: return
        viewModelScope.launch {
            if (current.isBookmarked) {
                progressRepository.removeBookmarkForGlossary(entry.term)
            } else {
                progressRepository.addBookmark(
                    phaseId = "glossary",
                    glossaryTerm = entry.term,
                    note = entry.shortDefinition.lineSequence().firstOrNull()
                )
            }
        }
    }
}
