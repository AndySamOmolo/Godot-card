package com.kadicompanion.ui.glossary

import androidx.lifecycle.ViewModel
import com.kadicompanion.domain.model.GlossaryEntry
import com.kadicompanion.domain.repository.GlossaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class GlossaryListUiState(val isLoading: Boolean = true, val all: List<GlossaryEntry> = emptyList(), val query: String = "", val filtered: List<GlossaryEntry> = emptyList())

@HiltViewModel
class GlossaryListViewModel @Inject constructor(
    private val glossary: GlossaryRepository
) : ViewModel() {
    private val _query = MutableStateFlow("")

    val uiState: StateFlow<GlossaryListUiState> = combine(glossary.observeAll(), _query) { all, query ->
        val filtered = if (query.isBlank()) all.sortedBy { it.term.lowercase() }
        else all.filter { it.term.contains(query, ignoreCase = true) || it.shortDefinition.contains(query, ignoreCase = true) }
        GlossaryListUiState(false, all, query, filtered)
    }.stateIn(kotlinx.coroutines.MainScope(), SharingStarted.WhileSubscribed(5000), GlossaryListUiState())

    fun setQuery(q: String) { _query.value = q }
}
