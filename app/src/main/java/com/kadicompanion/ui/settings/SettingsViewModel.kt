package com.kadicompanion.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kadicompanion.data.content.ContentLoader
import com.kadicompanion.data.local.datastore.AccentPreset
import com.kadicompanion.data.local.datastore.AnimationSpeedPreset
import com.kadicompanion.data.local.datastore.BottomBarStyle
import com.kadicompanion.data.local.datastore.CardStyle
import com.kadicompanion.data.local.datastore.ChipStylePreset
import com.kadicompanion.data.local.datastore.CodeThemePreset
import com.kadicompanion.data.local.datastore.ContrastLevel
import com.kadicompanion.data.local.datastore.FontFamilyPreset
import com.kadicompanion.data.local.datastore.HeadingWeightPreset
import com.kadicompanion.data.local.datastore.HeroStyle
import com.kadicompanion.data.local.datastore.HomeHeroLayout
import com.kadicompanion.data.local.datastore.HomeHeroPalette
import com.kadicompanion.data.local.datastore.InlineCodeStyle
import com.kadicompanion.data.local.datastore.KadiFeltPreset
import com.kadicompanion.data.local.datastore.LessonLayoutMode
import com.kadicompanion.data.local.datastore.LineSpacingPreset
import com.kadicompanion.data.local.datastore.PreferencesRepository
import com.kadicompanion.data.local.datastore.SearchBarSizePreset
import com.kadicompanion.data.local.datastore.StudioThemeBundle
import com.kadicompanion.data.local.datastore.TableDisplayMode
import com.kadicompanion.data.local.datastore.ThemePref
import com.kadicompanion.data.local.datastore.TopBarStyle
import com.kadicompanion.data.local.datastore.UiDensity
import com.kadicompanion.data.local.datastore.UiPreferences
import com.kadicompanion.domain.model.LearnerTrack
import com.kadicompanion.domain.repository.AssessmentRepository
import com.kadicompanion.domain.repository.LearnerProfileRepository
import com.kadicompanion.domain.repository.ProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val uiPrefs: UiPreferences = UiPreferences(),
    val track: LearnerTrack? = null,
    val contentVersion: String = "4.0.0"
) {
    val theme: ThemePref get() = uiPrefs.theme
    val dynamicColor: Boolean get() = uiPrefs.dynamicColor
    val reduceMotion: Boolean get() = uiPrefs.reduceMotion
    val dyslexiaFont: Boolean get() = uiPrefs.dyslexiaFont
    val skipGating: Boolean get() = uiPrefs.skipGating
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefs: PreferencesRepository,
    private val learnerProfile: LearnerProfileRepository,
    private val progressRepository: ProgressRepository,
    private val assessmentRepository: AssessmentRepository,
    private val contentLoader: ContentLoader
) : ViewModel() {

    private val _bundledVersion = MutableStateFlow("4.0.0")

    init {
        viewModelScope.launch {
            _bundledVersion.value = contentLoader.loadContentVersion()
        }
    }

    val uiState: StateFlow<SettingsUiState> = combine(
        prefs.observeUiPreferences(),
        prefs.observeContentVersion(),
        learnerProfile.observeTrack(),
        _bundledVersion
    ) { uiPrefs, version, track, bundled ->
        SettingsUiState(
            uiPrefs = uiPrefs,
            track = track,
            contentVersion = version ?: bundled
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SettingsUiState())

    fun setTheme(theme: ThemePref) = viewModelScope.launch { prefs.setTheme(theme) }
    fun setAccentPreset(preset: AccentPreset) = viewModelScope.launch { prefs.setAccentPreset(preset) }
    fun setContrastLevel(level: ContrastLevel) = viewModelScope.launch { prefs.setContrastLevel(level) }
    fun setDynamicColor(v: Boolean) = viewModelScope.launch { prefs.setDynamicColor(v) }
    fun setReduceMotion(v: Boolean) = viewModelScope.launch { prefs.setReduceMotion(v) }
    fun setDyslexiaFont(v: Boolean) = viewModelScope.launch { prefs.setDyslexiaFont(v) }
    fun setSkipGating(v: Boolean) = viewModelScope.launch { prefs.setSkipGating(v) }
    fun setFontScale(scale: Float) = viewModelScope.launch { prefs.setFontScale(scale) }
    fun setCodeFontScale(scale: Float) = viewModelScope.launch { prefs.setCodeFontScale(scale) }
    fun setFontFamilyPreset(preset: FontFamilyPreset) = viewModelScope.launch { prefs.setFontFamilyPreset(preset) }
    fun setHeadingWeightPreset(preset: HeadingWeightPreset) = viewModelScope.launch { prefs.setHeadingWeightPreset(preset) }
    fun setLineSpacingPreset(preset: LineSpacingPreset) = viewModelScope.launch { prefs.setLineSpacingPreset(preset) }
    fun setInlineCodeStyle(style: InlineCodeStyle) = viewModelScope.launch { prefs.setInlineCodeStyle(style) }
    fun setCodeThemePreset(preset: CodeThemePreset) = viewModelScope.launch { prefs.setCodeThemePreset(preset) }
    fun setCornerRadiusDp(radiusDp: Int) = viewModelScope.launch { prefs.setCornerRadiusDp(radiusDp) }
    fun setCardStyle(style: CardStyle) = viewModelScope.launch { prefs.setCardStyle(style) }
    fun setCardBorderWidthDp(widthDp: Float) = viewModelScope.launch { prefs.setCardBorderWidthDp(widthDp) }
    fun setShowCardAccentBar(show: Boolean) = viewModelScope.launch { prefs.setShowCardAccentBar(show) }
    fun setUiDensity(density: UiDensity) = viewModelScope.launch { prefs.setUiDensity(density) }
    fun setTopBarStyle(style: TopBarStyle) = viewModelScope.launch { prefs.setTopBarStyle(style) }
    fun setHeroStyle(style: HeroStyle) = viewModelScope.launch { prefs.setHeroStyle(style) }
    fun setBottomBarStyle(style: BottomBarStyle) = viewModelScope.launch { prefs.setBottomBarStyle(style) }
    fun setLessonLayoutMode(mode: LessonLayoutMode) = viewModelScope.launch { prefs.setLessonLayoutMode(mode) }
    fun setTableDisplayMode(mode: TableDisplayMode) = viewModelScope.launch { prefs.setTableDisplayMode(mode) }
    fun setCodeWordWrap(wrap: Boolean) = viewModelScope.launch { prefs.setCodeWordWrap(wrap) }
    fun setShowCodeLineNumbers(show: Boolean) = viewModelScope.launch { prefs.setShowCodeLineNumbers(show) }
    fun setShowCodeWindowDots(show: Boolean) = viewModelScope.launch { prefs.setShowCodeWindowDots(show) }
    fun setShowHomeHeroCard(show: Boolean) = viewModelScope.launch { prefs.setShowHomeHeroCard(show) }
    fun setHomeHeroLayout(layout: HomeHeroLayout) = viewModelScope.launch { prefs.setHomeHeroLayout(layout) }
    fun setHomeHeroPalette(palette: HomeHeroPalette) = viewModelScope.launch { prefs.setHomeHeroPalette(palette) }
    fun setShowHeroBadge(show: Boolean) = viewModelScope.launch { prefs.setShowHeroBadge(show) }
    fun setShowHeroProgressRing(show: Boolean) = viewModelScope.launch { prefs.setShowHeroProgressRing(show) }
    fun setShowHeroSecondaryBtn(show: Boolean) = viewModelScope.launch { prefs.setShowHeroSecondaryBtn(show) }
    fun setCustomHeroBadge(badge: String) = viewModelScope.launch { prefs.setCustomHeroBadge(badge) }
    fun setCustomHeroTitle(title: String) = viewModelScope.launch { prefs.setCustomHeroTitle(title) }
    fun setCustomHeroSubtitle(subtitle: String) = viewModelScope.launch { prefs.setCustomHeroSubtitle(subtitle) }
    fun setCustomHeroPrimaryBtn(label: String) = viewModelScope.launch { prefs.setCustomHeroPrimaryBtn(label) }
    fun setCustomHeroSecondaryBtn(label: String) = viewModelScope.launch { prefs.setCustomHeroSecondaryBtn(label) }
    fun setShowHomeStats(show: Boolean) = viewModelScope.launch { prefs.setShowHomeStats(show) }
    fun setShowHomeQuickLaunch(show: Boolean) = viewModelScope.launch { prefs.setShowHomeQuickLaunch(show) }
    fun setShowHomePhaseStrip(show: Boolean) = viewModelScope.launch { prefs.setShowHomePhaseStrip(show) }
    fun setShowDailyTip(show: Boolean) = viewModelScope.launch { prefs.setShowDailyTip(show) }
    fun setShowReviewQueue(show: Boolean) = viewModelScope.launch { prefs.setShowReviewQueue(show) }
    fun setCustomGreeting(greeting: String) = viewModelScope.launch { prefs.setCustomGreeting(greeting) }
    fun setCustomSubtitle(subtitle: String) = viewModelScope.launch { prefs.setCustomSubtitle(subtitle) }
    fun setSearchBarSize(preset: SearchBarSizePreset) = viewModelScope.launch { prefs.setSearchBarSize(preset) }
    fun setAnimationSpeed(preset: AnimationSpeedPreset) = viewModelScope.launch { prefs.setAnimationSpeed(preset) }
    fun setChipStyle(preset: ChipStylePreset) = viewModelScope.launch { prefs.setChipStyle(preset) }
    fun setKadiFeltPreset(preset: KadiFeltPreset) = viewModelScope.launch { prefs.setKadiFeltPreset(preset) }
    fun setShowBottomBarLabels(show: Boolean) = viewModelScope.launch { prefs.setShowBottomBarLabels(show) }
    fun setShowProgressBadges(show: Boolean) = viewModelScope.launch { prefs.setShowProgressBadges(show) }
    fun applyStudioBundle(bundle: StudioThemeBundle) = viewModelScope.launch { prefs.applyStudioBundle(bundle) }
    fun resetAllUiToDefault() = viewModelScope.launch { prefs.resetUiPreferencesToDefault() }
    fun resetAllLearningProgress() = viewModelScope.launch {
        progressRepository.clearAllProgress()
        progressRepository.clearAllBookmarks()
        assessmentRepository.clearAllAttempts()
    }
    fun setTrack(track: LearnerTrack) = viewModelScope.launch { learnerProfile.setTrack(track) }
}
