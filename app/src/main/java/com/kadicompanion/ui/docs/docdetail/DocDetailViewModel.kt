package com.kadicompanion.ui.docs.docdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kadicompanion.domain.model.GodotDocEntry
import com.kadicompanion.domain.repository.DocsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DocDetailUiState(val entry: GodotDocEntry? = null, val isLoading: Boolean = true)

@HiltViewModel
class DocDetailViewModel @Inject constructor(
    private val docsRepository: DocsRepository,
    savedStateHandle: androidx.lifecycle.SavedStateHandle
) : ViewModel() {
    private val className: String = checkNotNull(savedStateHandle["className"])
    private val _ui = MutableStateFlow(DocDetailUiState())
    val uiState: StateFlow<DocDetailUiState> = _ui.asStateFlow()
    init { viewModelScope.launch { _ui.value = DocDetailUiState(entry = docsRepository.get(className), isLoading = false) } }
}
