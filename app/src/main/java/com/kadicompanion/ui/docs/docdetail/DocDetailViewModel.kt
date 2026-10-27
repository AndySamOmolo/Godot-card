package com.kadicompanion.ui.docs.docdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kadicompanion.domain.model.GodotDocEntry
import com.kadicompanion.domain.repository.DocsRepository
import com.kadicompanion.domain.repository.ProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DocDetailUiState(
    val entry: GodotDocEntry? = null,
    val isBookmarked: Boolean = false,
    val isLoading: Boolean = true
)

@HiltViewModel
class DocDetailViewModel @Inject constructor(
    private val docsRepository: DocsRepository,
    private val progressRepository: ProgressRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val className: String = checkNotNull(savedStateHandle["className"])
    private val _entry = MutableStateFlow<GodotDocEntry?>(null)
    private val _loading = MutableStateFlow(true)

    val uiState: StateFlow<DocDetailUiState> = combine(
        _entry,
        _loading,
        progressRepository.observeBookmarkForPhase(className)
    ) { entry, loading, bookmark ->
        DocDetailUiState(
            entry = entry,
            isBookmarked = bookmark != null,
            isLoading = loading
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DocDetailUiState())

    init {
        viewModelScope.launch {
            _entry.value = docsRepository.get(className)
            _loading.value = false
        }
    }

    fun toggleBookmark() {
        val current = uiState.value
        val doc = current.entry ?: return
        viewModelScope.launch {
            if (current.isBookmarked) {
                progressRepository.removeBookmarkForPhase(doc.className)
            } else {
                progressRepository.addBookmark(
                    phaseId = doc.className,
                    note = doc.shortDescription
                )
            }
        }
    }
}
