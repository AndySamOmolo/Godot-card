package com.kadicompanion.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kadicompanion.domain.model.LearnerTrack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "kadi_prefs")

enum class ThemePref { DARK, AMOLED, LIGHT, SYSTEM }

enum class AccentPreset(val label: String, val description: String) {
    GODOT_SAPPHIRE("Godot Sapphire", "Sky blue #38BDF8 & emerald mint"),
    NAIROBI_GOLD("Nairobi Kadi Gold", "Warm Kenyan gold #F59E0B & crimson"),
    EMERALD_FELT("Emerald Card Table", "Casino felt green #10B981 & gold"),
    CYBER_VIOLET("Cyber Violet", "Electric purple #A855F7 & cyan"),
    CRIMSON_ACE("Crimson Ace", "Ruby red #F43F5E & warm amber"),
    SUNSET_ORANGE("Savannah Sunset", "Tangerine #FB923C & ocean teal"),
    MONOCHROME_SLATE("Monochrome Ink", "Minimalist high-contrast silver & slate")
}

enum class FontFamilyPreset(val label: String) {
    SANS_SERIF("Modern Sans"),
    SERIF("Editorial Serif"),
    MONOSPACE("Developer Mono"),
    ROUNDED("Clean Geometric")
}

enum class LineSpacingPreset(val label: String, val multiplier: Float) {
    COMPACT("Compact", 0.88f),
    COMFORTABLE("Comfortable", 1.0f),
    RELAXED("Relaxed", 1.16f)
}

enum class InlineCodeStyle(val label: String, val description: String) {
    PILL_HIGHLIGHT("Pill Badge", "Soft tinted chip behind inline code & terms"),
    COLORED_TEXT("Accent Text", "Colored monospace text with zero background box"),
    PLAIN_BOLD("Clean Prose", "Seamless bold text with zero boxes or underlines")
}

enum class CardStyle(val label: String, val description: String) {
    FLAT_BORDER("Studio Border", "Crisp 1dp hairline border with flat surface"),
    ELEVATED_SHADOW("Elevated Shadow", "Soft Material elevation depth"),
    SUBTLE_GLASS("Frosted Glass", "Translucent surface with luminous border"),
    OUTLINED_MINIMAL("Minimal Outline", "Transparent background with clean outline")
}

enum class UiDensity(val label: String, val description: String) {
    COMPACT("Compact Phone", "Tighter padding to fit more on screen"),
    COMFORTABLE("Balanced", "Default ergonomic mobile spacing"),
    SPACIOUS("Spacious", "Generous padding & card breathing room")
}

enum class HeroStyle(val label: String, val description: String) {
    RICH_GRADIENT("Studio Gradient", "Deep multi-stop accent banner"),
    COMPACT_CARD("Compact Header", "Slim banner that saves vertical space"),
    SURFACE_FLAT("Minimal Surface", "Matches card surface with accent border")
}

enum class BottomBarStyle(val label: String, val description: String) {
    PILL_TABS("Studio Pill", "Icon + label with active pill indicator"),
    COMPACT_ICONS("Compact Icons", "Slim icon-only bar for taller reading area"),
    FLOATING_DOCK("Floating Dock", "Rounded floating bar inset from edges")
}

enum class LessonLayoutMode(val label: String, val description: String) {
    TABBED_STUDIO("3-Tab Studio", "Organized into Guide, Godot Steps & Lab tabs"),
    SINGLE_SCROLL("Single Scroll", "All lesson sections on one continuous page")
}

enum class TableDisplayMode(val label: String, val description: String) {
    AUTO_RESPONSIVE("Smart Phone Fit", "2-col fits screen width; 3+ col aligned scroll"),
    STACKED_CARDS("Mobile Cards", "Each table row stacked vertically for narrow phones"),
    HORIZONTAL_SCROLL("Classic Table", "Traditional horizontally scrollable grid")
}

data class UiPreferences(
    val theme: ThemePref = ThemePref.DARK,
    val accentPreset: AccentPreset = AccentPreset.GODOT_SAPPHIRE,
    val dynamicColor: Boolean = false,
    val reduceMotion: Boolean = false,
    val dyslexiaFont: Boolean = false,
    val skipGating: Boolean = false,
    val fontScale: Float = 1.0f,
    val codeFontScale: Float = 1.0f,
    val fontFamilyPreset: FontFamilyPreset = FontFamilyPreset.SANS_SERIF,
    val lineSpacingPreset: LineSpacingPreset = LineSpacingPreset.COMFORTABLE,
    val inlineCodeStyle: InlineCodeStyle = InlineCodeStyle.PILL_HIGHLIGHT,
    val cornerRadiusDp: Int = 14,
    val cardStyle: CardStyle = CardStyle.FLAT_BORDER,
    val showCardAccentBar: Boolean = true,
    val uiDensity: UiDensity = UiDensity.COMFORTABLE,
    val heroStyle: HeroStyle = HeroStyle.RICH_GRADIENT,
    val bottomBarStyle: BottomBarStyle = BottomBarStyle.PILL_TABS,
    val lessonLayoutMode: LessonLayoutMode = LessonLayoutMode.TABBED_STUDIO,
    val tableDisplayMode: TableDisplayMode = TableDisplayMode.AUTO_RESPONSIVE,
    val codeWordWrap: Boolean = true,
    val showCodeLineNumbers: Boolean = true,
    val showCodeWindowDots: Boolean = true,
    val showHomeStats: Boolean = true,
    val showHomeQuickLaunch: Boolean = true,
    val showHomePhaseStrip: Boolean = true,
    val showDailyTip: Boolean = true,
    val showReviewQueue: Boolean = true,
    val customGreeting: String = "",
    val customSubtitle: String = ""
)

@Singleton
class PreferencesRepository @Inject constructor(
    @dagger.hilt.android.qualifiers.ApplicationContext private val context: Context
) {
    fun observeUiPreferences(): Flow<UiPreferences> = context.dataStore.data.map { prefs ->
        UiPreferences(
            theme = prefs[KEY_THEME]?.let { runCatching { ThemePref.valueOf(it) }.getOrNull() } ?: ThemePref.DARK,
            accentPreset = prefs[KEY_ACCENT]?.let { runCatching { AccentPreset.valueOf(it) }.getOrNull() } ?: AccentPreset.GODOT_SAPPHIRE,
            dynamicColor = prefs[KEY_DYNAMIC_COLOR] ?: false,
            reduceMotion = prefs[KEY_REDUCE_MOTION] ?: false,
            dyslexiaFont = prefs[KEY_DYSLEXIA_FONT] ?: false,
            skipGating = prefs[KEY_SKIP_GATING] ?: false,
            fontScale = (prefs[KEY_FONT_SCALE] ?: 1.0f).coerceIn(0.80f, 1.35f),
            codeFontScale = (prefs[KEY_CODE_FONT_SCALE] ?: 1.0f).coerceIn(0.80f, 1.35f),
            fontFamilyPreset = prefs[KEY_FONT_FAMILY]?.let { runCatching { FontFamilyPreset.valueOf(it) }.getOrNull() } ?: FontFamilyPreset.SANS_SERIF,
            lineSpacingPreset = prefs[KEY_LINE_SPACING]?.let { runCatching { LineSpacingPreset.valueOf(it) }.getOrNull() } ?: LineSpacingPreset.COMFORTABLE,
            inlineCodeStyle = prefs[KEY_INLINE_CODE_STYLE]?.let { runCatching { InlineCodeStyle.valueOf(it) }.getOrNull() } ?: InlineCodeStyle.PILL_HIGHLIGHT,
            cornerRadiusDp = (prefs[KEY_CORNER_RADIUS] ?: 14).coerceIn(0, 32),
            cardStyle = prefs[KEY_CARD_STYLE]?.let { runCatching { CardStyle.valueOf(it) }.getOrNull() } ?: CardStyle.FLAT_BORDER,
            showCardAccentBar = prefs[KEY_SHOW_CARD_ACCENT] ?: true,
            uiDensity = prefs[KEY_UI_DENSITY]?.let { runCatching { UiDensity.valueOf(it) }.getOrNull() } ?: UiDensity.COMFORTABLE,
            heroStyle = prefs[KEY_HERO_STYLE]?.let { runCatching { HeroStyle.valueOf(it) }.getOrNull() } ?: HeroStyle.RICH_GRADIENT,
            bottomBarStyle = prefs[KEY_BOTTOM_BAR_STYLE]?.let { runCatching { BottomBarStyle.valueOf(it) }.getOrNull() } ?: BottomBarStyle.PILL_TABS,
            lessonLayoutMode = prefs[KEY_LESSON_LAYOUT]?.let { runCatching { LessonLayoutMode.valueOf(it) }.getOrNull() } ?: LessonLayoutMode.TABBED_STUDIO,
            tableDisplayMode = prefs[KEY_TABLE_DISPLAY]?.let { runCatching { TableDisplayMode.valueOf(it) }.getOrNull() } ?: TableDisplayMode.AUTO_RESPONSIVE,
            codeWordWrap = prefs[KEY_CODE_WORD_WRAP] ?: true,
            showCodeLineNumbers = prefs[KEY_CODE_LINE_NUMBERS] ?: true,
            showCodeWindowDots = prefs[KEY_CODE_WINDOW_DOTS] ?: true,
            showHomeStats = prefs[KEY_SHOW_HOME_STATS] ?: true,
            showHomeQuickLaunch = prefs[KEY_SHOW_HOME_QUICK_LAUNCH] ?: true,
            showHomePhaseStrip = prefs[KEY_SHOW_HOME_PHASE_STRIP] ?: true,
            showDailyTip = prefs[KEY_SHOW_DAILY_TIP] ?: true,
            showReviewQueue = prefs[KEY_SHOW_REVIEW_QUEUE] ?: true,
            customGreeting = prefs[KEY_CUSTOM_GREETING] ?: "",
            customSubtitle = prefs[KEY_CUSTOM_SUBTITLE] ?: ""
        )
    }

    fun observeTheme(): Flow<ThemePref> = observeUiPreferences().map { it.theme }
    suspend fun setTheme(theme: ThemePref) {
        context.dataStore.edit { it[KEY_THEME] = theme.name }
    }

    suspend fun setAccentPreset(preset: AccentPreset) {
        context.dataStore.edit { it[KEY_ACCENT] = preset.name }
    }

    fun observeDynamicColor(): Flow<Boolean> = observeUiPreferences().map { it.dynamicColor }
    suspend fun setDynamicColor(enabled: Boolean) {
        context.dataStore.edit { it[KEY_DYNAMIC_COLOR] = enabled }
    }

    fun observeReduceMotion(): Flow<Boolean> = observeUiPreferences().map { it.reduceMotion }
    suspend fun setReduceMotion(enabled: Boolean) {
        context.dataStore.edit { it[KEY_REDUCE_MOTION] = enabled }
    }

    fun observeDyslexiaFont(): Flow<Boolean> = observeUiPreferences().map { it.dyslexiaFont }
    suspend fun setDyslexiaFont(enabled: Boolean) {
        context.dataStore.edit { it[KEY_DYSLEXIA_FONT] = enabled }
    }

    fun observeSkipGating(): Flow<Boolean> = observeUiPreferences().map { it.skipGating }
    suspend fun setSkipGating(enabled: Boolean) {
        context.dataStore.edit { it[KEY_SKIP_GATING] = enabled }
    }

    suspend fun setFontScale(scale: Float) {
        context.dataStore.edit { it[KEY_FONT_SCALE] = scale.coerceIn(0.80f, 1.35f) }
    }

    suspend fun setCodeFontScale(scale: Float) {
        context.dataStore.edit { it[KEY_CODE_FONT_SCALE] = scale.coerceIn(0.80f, 1.35f) }
    }

    suspend fun setFontFamilyPreset(preset: FontFamilyPreset) {
        context.dataStore.edit { it[KEY_FONT_FAMILY] = preset.name }
    }

    suspend fun setLineSpacingPreset(preset: LineSpacingPreset) {
        context.dataStore.edit { it[KEY_LINE_SPACING] = preset.name }
    }

    suspend fun setInlineCodeStyle(style: InlineCodeStyle) {
        context.dataStore.edit { it[KEY_INLINE_CODE_STYLE] = style.name }
    }

    suspend fun setCornerRadiusDp(radiusDp: Int) {
        context.dataStore.edit { it[KEY_CORNER_RADIUS] = radiusDp.coerceIn(0, 32) }
    }

    suspend fun setCardStyle(style: CardStyle) {
        context.dataStore.edit { it[KEY_CARD_STYLE] = style.name }
    }

    suspend fun setShowCardAccentBar(show: Boolean) {
        context.dataStore.edit { it[KEY_SHOW_CARD_ACCENT] = show }
    }

    suspend fun setUiDensity(density: UiDensity) {
        context.dataStore.edit { it[KEY_UI_DENSITY] = density.name }
    }

    suspend fun setHeroStyle(style: HeroStyle) {
        context.dataStore.edit { it[KEY_HERO_STYLE] = style.name }
    }

    suspend fun setBottomBarStyle(style: BottomBarStyle) {
        context.dataStore.edit { it[KEY_BOTTOM_BAR_STYLE] = style.name }
    }

    suspend fun setLessonLayoutMode(mode: LessonLayoutMode) {
        context.dataStore.edit { it[KEY_LESSON_LAYOUT] = mode.name }
    }

    suspend fun setTableDisplayMode(mode: TableDisplayMode) {
        context.dataStore.edit { it[KEY_TABLE_DISPLAY] = mode.name }
    }

    suspend fun setCodeWordWrap(wrap: Boolean) {
        context.dataStore.edit { it[KEY_CODE_WORD_WRAP] = wrap }
    }

    suspend fun setShowCodeLineNumbers(show: Boolean) {
        context.dataStore.edit { it[KEY_CODE_LINE_NUMBERS] = show }
    }

    suspend fun setShowCodeWindowDots(show: Boolean) {
        context.dataStore.edit { it[KEY_CODE_WINDOW_DOTS] = show }
    }

    suspend fun setShowHomeStats(show: Boolean) {
        context.dataStore.edit { it[KEY_SHOW_HOME_STATS] = show }
    }

    suspend fun setShowHomeQuickLaunch(show: Boolean) {
        context.dataStore.edit { it[KEY_SHOW_HOME_QUICK_LAUNCH] = show }
    }

    suspend fun setShowHomePhaseStrip(show: Boolean) {
        context.dataStore.edit { it[KEY_SHOW_HOME_PHASE_STRIP] = show }
    }

    suspend fun setShowDailyTip(show: Boolean) {
        context.dataStore.edit { it[KEY_SHOW_DAILY_TIP] = show }
    }

    suspend fun setShowReviewQueue(show: Boolean) {
        context.dataStore.edit { it[KEY_SHOW_REVIEW_QUEUE] = show }
    }

    suspend fun setCustomGreeting(greeting: String) {
        context.dataStore.edit { it[KEY_CUSTOM_GREETING] = greeting }
    }

    suspend fun setCustomSubtitle(subtitle: String) {
        context.dataStore.edit { it[KEY_CUSTOM_SUBTITLE] = subtitle }
    }

    suspend fun resetUiPreferencesToDefault() {
        context.dataStore.edit { prefs ->
            prefs.remove(KEY_THEME)
            prefs.remove(KEY_ACCENT)
            prefs.remove(KEY_DYNAMIC_COLOR)
            prefs.remove(KEY_REDUCE_MOTION)
            prefs.remove(KEY_DYSLEXIA_FONT)
            prefs.remove(KEY_FONT_SCALE)
            prefs.remove(KEY_CODE_FONT_SCALE)
            prefs.remove(KEY_FONT_FAMILY)
            prefs.remove(KEY_LINE_SPACING)
            prefs.remove(KEY_INLINE_CODE_STYLE)
            prefs.remove(KEY_CORNER_RADIUS)
            prefs.remove(KEY_CARD_STYLE)
            prefs.remove(KEY_SHOW_CARD_ACCENT)
            prefs.remove(KEY_UI_DENSITY)
            prefs.remove(KEY_HERO_STYLE)
            prefs.remove(KEY_BOTTOM_BAR_STYLE)
            prefs.remove(KEY_LESSON_LAYOUT)
            prefs.remove(KEY_TABLE_DISPLAY)
            prefs.remove(KEY_CODE_WORD_WRAP)
            prefs.remove(KEY_CODE_LINE_NUMBERS)
            prefs.remove(KEY_CODE_WINDOW_DOTS)
            prefs.remove(KEY_SHOW_HOME_STATS)
            prefs.remove(KEY_SHOW_HOME_QUICK_LAUNCH)
            prefs.remove(KEY_SHOW_HOME_PHASE_STRIP)
            prefs.remove(KEY_SHOW_DAILY_TIP)
            prefs.remove(KEY_SHOW_REVIEW_QUEUE)
            prefs.remove(KEY_CUSTOM_GREETING)
            prefs.remove(KEY_CUSTOM_SUBTITLE)
        }
    }

    fun observeContentVersion(): Flow<String?> = context.dataStore.data.map {
        it[KEY_CONTENT_VERSION]
    }
    suspend fun setContentVersion(version: String) {
        context.dataStore.edit { it[KEY_CONTENT_VERSION] = version }
    }

    fun observeLearnerTrack(): Flow<LearnerTrack?> = context.dataStore.data.map {
        it[KEY_TRACK]?.let { name -> runCatching { LearnerTrack.valueOf(name) }.getOrNull() }
    }
    suspend fun setLearnerTrack(track: LearnerTrack) {
        context.dataStore.edit { it[KEY_TRACK] = track.name }
    }

    companion object {
        private val KEY_THEME = stringPreferencesKey("theme")
        private val KEY_ACCENT = stringPreferencesKey("accent_preset")
        private val KEY_DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color")
        private val KEY_REDUCE_MOTION = booleanPreferencesKey("reduce_motion")
        private val KEY_DYSLEXIA_FONT = booleanPreferencesKey("dyslexia_font")
        private val KEY_SKIP_GATING = booleanPreferencesKey("skip_gating")
        private val KEY_FONT_SCALE = floatPreferencesKey("font_scale")
        private val KEY_CODE_FONT_SCALE = floatPreferencesKey("code_font_scale")
        private val KEY_FONT_FAMILY = stringPreferencesKey("font_family")
        private val KEY_LINE_SPACING = stringPreferencesKey("line_spacing")
        private val KEY_INLINE_CODE_STYLE = stringPreferencesKey("inline_code_style")
        private val KEY_CORNER_RADIUS = intPreferencesKey("corner_radius_dp")
        private val KEY_CARD_STYLE = stringPreferencesKey("card_style")
        private val KEY_SHOW_CARD_ACCENT = booleanPreferencesKey("show_card_accent")
        private val KEY_UI_DENSITY = stringPreferencesKey("ui_density")
        private val KEY_HERO_STYLE = stringPreferencesKey("hero_style")
        private val KEY_BOTTOM_BAR_STYLE = stringPreferencesKey("bottom_bar_style")
        private val KEY_LESSON_LAYOUT = stringPreferencesKey("lesson_layout")
        private val KEY_TABLE_DISPLAY = stringPreferencesKey("table_display")
        private val KEY_CODE_WORD_WRAP = booleanPreferencesKey("code_word_wrap")
        private val KEY_CODE_LINE_NUMBERS = booleanPreferencesKey("code_line_numbers")
        private val KEY_CODE_WINDOW_DOTS = booleanPreferencesKey("code_window_dots")
        private val KEY_SHOW_HOME_STATS = booleanPreferencesKey("show_home_stats")
        private val KEY_SHOW_HOME_QUICK_LAUNCH = booleanPreferencesKey("show_home_quick_launch")
        private val KEY_SHOW_HOME_PHASE_STRIP = booleanPreferencesKey("show_home_phase_strip")
        private val KEY_SHOW_DAILY_TIP = booleanPreferencesKey("show_daily_tip")
        private val KEY_SHOW_REVIEW_QUEUE = booleanPreferencesKey("show_review_queue")
        private val KEY_CUSTOM_GREETING = stringPreferencesKey("custom_greeting")
        private val KEY_CUSTOM_SUBTITLE = stringPreferencesKey("custom_subtitle")
        private val KEY_CONTENT_VERSION = stringPreferencesKey("content_version")
        private val KEY_TRACK = stringPreferencesKey("track")
    }
}
