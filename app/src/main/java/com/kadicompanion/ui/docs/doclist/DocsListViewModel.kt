package com.kadicompanion.ui.docs.doclist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kadicompanion.domain.model.GodotDocEntry
import com.kadicompanion.domain.repository.DocsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DocsListUiState(
    val isLoading: Boolean = true,
    val all: List<GodotDocEntry> = emptyList(),
    val query: String = "",
    val filtered: List<GodotDocEntry> = emptyList()
)

@HiltViewModel
class DocsListViewModel @Inject constructor(
    private val docsRepository: DocsRepository
) : ViewModel() {
    private val _query = MutableStateFlow("")

    val uiState: StateFlow<DocsListUiState> = combine(docsRepository.observeAll(), _query) { all, query ->
        val filtered = if (query.isBlank()) all
        else all.filter { it.className.contains(query, ignoreCase = true) || it.shortDescription.contains(query, ignoreCase = true) }
        DocsListUiState(false, all, query, filtered)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DocsListUiState())

    init { viewModelScope.launch { runCatching { docsRepository.reindexIfNeeded() } } }

    fun setQuery(q: String) { _query.value = q }
}
