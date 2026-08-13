package com.kadicompanion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kadicompanion.data.local.datastore.PreferencesRepository
import com.kadicompanion.data.local.datastore.ThemePref
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
    val darkTheme: Boolean = true,
    val dynamicColor: Boolean = false,
    val reduceMotion: Boolean = false,
    val dyslexiaFont: Boolean = false
)

@HiltViewModel
class RootViewModel @Inject constructor(
    private val prefs: PreferencesRepository,
    private val learnerProfile: LearnerProfileRepository
) : ViewModel() {

    val uiState: StateFlow<RootUiState> = combine(
        prefs.observeTheme(),
        prefs.observeDynamicColor(),
        prefs.observeReduceMotion(),
        prefs.observeDyslexiaFont(),
        learnerProfile.observeTrack()
    ) { theme, dynamic, motion, dyslexia, track ->
        RootUiState(
            isOnboarded = track != null,
            darkTheme = when (theme) { ThemePref.DARK -> true; ThemePref.LIGHT -> false; ThemePref.SYSTEM -> false },
            dynamicColor = dynamic,
            reduceMotion = motion,
            dyslexiaFont = dyslexia
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), RootUiState())

    fun onOnboarded() {
        viewModelScope.launch { learnerProfile.markOnboarded() }
    }
}
