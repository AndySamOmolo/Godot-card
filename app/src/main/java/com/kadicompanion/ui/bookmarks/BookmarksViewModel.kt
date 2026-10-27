package com.kadicompanion.ui.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kadicompanion.domain.model.Bookmark
import com.kadicompanion.domain.repository.ProgressRepository
import com.kadicompanion.domain.repository.TutorialRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BookmarksUiState(
    val isLoading: Boolean = true,
    val bookmarks: List<Bookmark> = emptyList(),
    val lessonTitles: Map<String, String> = emptyMap(),
    val phaseTitles: Map<String, String> = emptyMap()
)

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val progressRepository: ProgressRepository,
    private val tutorialRepository: TutorialRepository
) : ViewModel() {

    val uiState: StateFlow<BookmarksUiState> = combine(
        progressRepository.observeAllBookmarks(),
        tutorialRepository.observePhases()
    ) { bookmarks, phases ->
        val lMap = phases.flatMap { it.lessons }.associate { it.id to it.title }
        val pMap = phases.associate { it.id to "Phase ${it.order}: ${it.title}" }
        BookmarksUiState(
            isLoading = false,
            bookmarks = bookmarks,
            lessonTitles = lMap,
            phaseTitles = pMap
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BookmarksUiState())

    fun remove(id: Long) { viewModelScope.launch { progressRepository.removeBookmark(id) } }
}
