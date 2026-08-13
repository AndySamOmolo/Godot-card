package com.kadicompanion.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kadicompanion.data.local.datastore.PreferencesRepository
import com.kadicompanion.data.local.datastore.ThemePref
import com.kadicompanion.domain.model.LearnerTrack
import com.kadicompanion.domain.repository.LearnerProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val theme: ThemePref = ThemePref.DARK,
    val dynamicColor: Boolean = true,
    val reduceMotion: Boolean = false,
    val dyslexiaFont: Boolean = false,
    val skipGating: Boolean = false,
    val track: LearnerTrack? = null,
    val contentVersion: String = "unknown"
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefs: PreferencesRepository,
    private val learnerProfile: LearnerProfileRepository
) : ViewModel() {
    private val visuals = combine(prefs.observeTheme(), prefs.observeDynamicColor(), prefs.observeReduceMotion(), prefs.observeDyslexiaFont()) { theme, dynamic, motion, dyslexia -> Visuals(theme, dynamic, motion, dyslexia) }
    private val gateAndVersion = combine(prefs.observeSkipGating(), prefs.observeContentVersion()) { skip, version -> skip to (version ?: "unknown") }

    val uiState: StateFlow<SettingsUiState> = combine(visuals, gateAndVersion, learnerProfile.observeTrack()) { v, gv, track ->
        SettingsUiState(v.theme, v.dynamicColor, v.reduceMotion, v.dyslexiaFont, gv.first, track, gv.second)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SettingsUiState())

    private data class Visuals(val theme: ThemePref, val dynamicColor: Boolean, val reduceMotion: Boolean, val dyslexiaFont: Boolean)

    fun setTheme(theme: ThemePref) = viewModelScope.launch { prefs.setTheme(theme) }
    fun setDynamicColor(v: Boolean) = viewModelScope.launch { prefs.setDynamicColor(v) }
    fun setReduceMotion(v: Boolean) = viewModelScope.launch { prefs.setReduceMotion(v) }
    fun setDyslexiaFont(v: Boolean) = viewModelScope.launch { prefs.setDyslexiaFont(v) }
    fun setSkipGating(v: Boolean) = viewModelScope.launch { prefs.setSkipGating(v) }
    fun setTrack(track: LearnerTrack) = viewModelScope.launch { learnerProfile.setTrack(track) }
}
