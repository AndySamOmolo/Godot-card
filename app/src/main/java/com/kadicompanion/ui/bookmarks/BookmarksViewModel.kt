package com.kadicompanion.ui.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kadicompanion.domain.model.Bookmark
import com.kadicompanion.domain.repository.ProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BookmarksUiState(val isLoading: Boolean = true, val bookmarks: List<Bookmark> = emptyList())

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    val uiState: StateFlow<BookmarksUiState> = progressRepository.observeAllBookmarks()
        .map { BookmarksUiState(false, it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BookmarksUiState())

    fun remove(id: Long) { viewModelScope.launch { progressRepository.removeBookmark(id) } }
}
