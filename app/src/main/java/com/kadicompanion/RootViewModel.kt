package com.kadicompanion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kadicompanion.data.local.datastore.PreferencesRepository
import com.kadicompanion.data.local.datastore.ThemePref
import com.kadicompanion.data.local.datastore.UiPreferences
import com.kadicompanion.domain.repository.LearnerProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RootUiState(
    val isOnboarded: Boolean = false,
    val uiPrefs: UiPreferences = UiPreferences()
) {
    val darkTheme: Boolean
        get() = when (uiPrefs.theme) {
            ThemePref.DARK, ThemePref.AMOLED -> true
            ThemePref.LIGHT -> false
            ThemePref.SYSTEM -> false
        }
    val dynamicColor: Boolean get() = uiPrefs.dynamicColor
    val reduceMotion: Boolean get() = uiPrefs.reduceMotion
    val dyslexiaFont: Boolean get() = uiPrefs.dyslexiaFont
}

@HiltViewModel
class RootViewModel @Inject constructor(
    private val prefs: PreferencesRepository,
    private val learnerProfile: LearnerProfileRepository
) : ViewModel() {

    val uiState: StateFlow<RootUiState> = combine(
        prefs.observeUiPreferences(),
        learnerProfile.observeTrack()
    ) { uiPrefs, track ->
        RootUiState(
            isOnboarded = track != null,
            uiPrefs = uiPrefs
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), RootUiState())

    fun onOnboarded() {
        viewModelScope.launch { learnerProfile.markOnboarded() }
    }
}
