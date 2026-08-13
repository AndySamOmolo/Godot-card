package com.kadicompanion.ui.glossary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kadicompanion.domain.model.GlossaryEntry
import com.kadicompanion.domain.repository.GlossaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GlossaryDetailUiState(val entry: GlossaryEntry? = null, val isLoading: Boolean = true)

@HiltViewModel
class GlossaryDetailViewModel @Inject constructor(
    private val glossary: GlossaryRepository,
    savedStateHandle: androidx.lifecycle.SavedStateHandle
) : ViewModel() {
    private val term: String = checkNotNull(savedStateHandle["term"])
    private val _ui = MutableStateFlow(GlossaryDetailUiState())
    val uiState: StateFlow<GlossaryDetailUiState> = _ui.asStateFlow()
    init { viewModelScope.launch { _ui.value = GlossaryDetailUiState(entry = glossary.get(term), isLoading = false) } }
}
