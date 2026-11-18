package com.kadicompanion.ui.docs.doclist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kadicompanion.domain.model.GodotDocEntry
import com.kadicompanion.domain.repository.DocsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
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
        val q = query.trim()
        val filtered = if (q.isBlank()) {
            all
        } else {
            val ftsClassNames = docsRepository.search(q).toSet()
            all.filter { entry ->
                entry.className in ftsClassNames ||
                    entry.className.contains(q, ignoreCase = true) ||
                    entry.shortDescription.contains(q, ignoreCase = true) ||
                    entry.properties.any { it.name.contains(q, ignoreCase = true) || it.description.contains(q, ignoreCase = true) } ||
                    entry.methods.any { it.name.contains(q, ignoreCase = true) || it.description.contains(q, ignoreCase = true) } ||
                    entry.signals.any { it.name.contains(q, ignoreCase = true) || it.description.contains(q, ignoreCase = true) } ||
                    entry.bodyMarkdown.contains(q, ignoreCase = true)
            }
        }
        DocsListUiState(false, all, query, filtered)
    }
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DocsListUiState())

    init {
        viewModelScope.launch {
            runCatching { docsRepository.reindexIfNeeded() }
                .onFailure { android.util.Log.w("DocsListViewModel", "Failed to reindex docs", it) }
        }
    }

    fun setQuery(q: String) { _query.value = q }
}
