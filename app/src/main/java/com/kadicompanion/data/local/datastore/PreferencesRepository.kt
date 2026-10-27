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

enum class FontFamilyPreset(val label: String, val description: String) {
    SANS_SERIF("Modern Studio Sans", "Clean geometric sans-serif (Default)"),
    SANS_MEDIUM("Crisp Medium Sans", "Slightly bolder body weight for high-DPI phone screens"),
    SANS_COMPACT("Condensed Phone Sans", "Tighter character tracking so 15% more words fit per line"),
    SANS_WIDE("Airy Wide Sans", "Generous letter spacing for relaxed scanning"),
    SERIF("Editorial Book Serif", "Classic book serif for long-form tutorial reading"),
    SERIF_CRISP("Academic Medium Serif", "High-contrast medium-weight editorial serif"),
    MONOSPACE("Developer Terminal Mono", "Full monospace typography across UI and prose"),
    MONO_COMPACT("IDE Compact Mono", "Tight technical monospace for dense code-heavy reading"),
    CURSIVE("Notebook Script", "Playful handwritten script style"),
    ROUNDED("Clean System Default", "Balanced platform-native default typeface")
}

enum class HeadingWeightPreset(val label: String) {
    MEDIUM("Medium"),
    SEMI_BOLD("Semi-Bold"),
    BOLD("Bold"),
    EXTRA_BOLD("Extra-Bold"),
    BLACK("Heavy Black")
}

enum class CodeThemePreset(val label: String, val description: String) {
    GODOT_EDITOR("Godot 4.3 Editor", "Authentic Godot Script Editor dark navy, coral & mint"),
    GITHUB_STUDIO("GitHub Studio", "Crisp GitHub Dark & Light syntax palette"),
    DRACULA_NEON("Dracula Neon", "Deep purple-slate with electric cyan, pink & lime"),
    MONOKAI_PRO("Monokai Pro", "Warm charcoal with vibrant magenta, yellow & orange"),
    NORD_FROST("Nord Arctic", "Cool glacier blue, frost cyan & snow white"),
    HIGH_CONTRAST("High Contrast", "Pure black/white canvas with maximum token contrast")
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
    FLAT_BORDER("Studio Border", "Crisp hairline border with flat surface"),
    ELEVATED_SHADOW("Elevated Shadow", "Soft Material elevation depth"),
    SUBTLE_GLASS("Frosted Glass", "Translucent surface with luminous border"),
    OUTLINED_MINIMAL("Minimal Outline", "Transparent background with clean outline")
}

enum class UiDensity(val label: String, val description: String) {
    COMPACT("Compact Phone", "Tighter padding to fit more on screen"),
    COMFORTABLE("Balanced", "Default ergonomic mobile spacing"),
    SPACIOUS("Spacious", "Generous padding & card breathing room")
}

enum class TopBarStyle(val label: String, val description: String) {
    ULTRA_SLIM("Ultra-Slim (36dp)", "Single-line minimal header for maximum screen height"),
    COMPACT("Compact (42dp)", "Sleek phone header with subtle eyebrow"),
    STANDARD("Standard (50dp)", "Roomier two-line studio header")
}

enum class HeroStyle(val label: String, val description: String) {
    RICH_GRADIENT("Studio Gradient", "Deep multi-stop accent banner"),
    COMPACT_CARD("Compact Header", "Slim banner that saves vertical space"),
    SURFACE_FLAT("Minimal Surface", "Matches card surface with accent border")
}

enum class HomeHeroLayout(val label: String, val description: String) {
    FULL_WITH_RING("Circular Ring", "Title, subtitle, circular progress gauge & buttons"),
    COMPACT_BAR_PROGRESS("Slim Progress Bar", "Wider title area with a sleek horizontal progress bar"),
    MINIMAL_CLEAN("Minimal Compact", "Ultra-clean title and action button only"),
    STATS_COMBINED("Unified Dashboard", "Embeds Lessons, Mastery & Review counts inside the top card")
}

enum class HomeHeroPalette(val label: String, val description: String) {
    FOLLOW_ACCENT("Follow Theme Accent", "Matches your active Accent Color Palette"),
    GODOT_SAPPHIRE("Godot Deep Sapphire", "Navy #0F172A to Sky Blue #0284C7"),
    EMERALD_TABLE("Kadi Felt Emerald", "Casino Green #042F2E to Emerald #059669"),
    NAIROBI_GOLD("Nairobi Sunset Gold", "Warm Amber #78350F to Gold #D97706"),
    CYBER_PURPLE("Cyber Neon Violet", "Deep Indigo #1E1B4B to Purple #7C3AED"),
    CRIMSON_ROYAL("Royal Crimson", "Deep Ruby #4C0519 to Rose #E11D48"),
    OBSIDIAN_DARK("Obsidian Carbon", "Sleek dark carbon #090D16 with accent border"),
    SURFACE_CARD("Match Card Surface", "Uses your standard card surface color with no dark gradient")
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
    val headingWeightPreset: HeadingWeightPreset = HeadingWeightPreset.BOLD,
    val codeThemePreset: CodeThemePreset = CodeThemePreset.GODOT_EDITOR,
    val lineSpacingPreset: LineSpacingPreset = LineSpacingPreset.COMFORTABLE,
    val inlineCodeStyle: InlineCodeStyle = InlineCodeStyle.PILL_HIGHLIGHT,
    val cornerRadiusDp: Int = 14,
    val cardBorderWidthDp: Float = 1.0f,
    val cardStyle: CardStyle = CardStyle.FLAT_BORDER,
    val showCardAccentBar: Boolean = true,
    val uiDensity: UiDensity = UiDensity.COMFORTABLE,
    val topBarStyle: TopBarStyle = TopBarStyle.COMPACT,
    val heroStyle: HeroStyle = HeroStyle.RICH_GRADIENT,
    val bottomBarStyle: BottomBarStyle = BottomBarStyle.PILL_TABS,
    val lessonLayoutMode: LessonLayoutMode = LessonLayoutMode.TABBED_STUDIO,
    val tableDisplayMode: TableDisplayMode = TableDisplayMode.AUTO_RESPONSIVE,
    val codeWordWrap: Boolean = true,
    val showCodeLineNumbers: Boolean = true,
    val showCodeWindowDots: Boolean = true,
    // Home Screen & Top Hero Card Customization
    val showHomeHeroCard: Boolean = true,
    val homeHeroLayout: HomeHeroLayout = HomeHeroLayout.FULL_WITH_RING,
    val homeHeroPalette: HomeHeroPalette = HomeHeroPalette.FOLLOW_ACCENT,
    val showHeroBadge: Boolean = true,
    val showHeroProgressRing: Boolean = true,
    val showHeroSecondaryBtn: Boolean = true,
    val customHeroBadge: String = "",
    val customHeroTitle: String = "",
    val customHeroSubtitle: String = "",
    val customHeroPrimaryBtn: String = "",
    val customHeroSecondaryBtn: String = "",
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
            headingWeightPreset = prefs[KEY_HEADING_WEIGHT]?.let { runCatching { HeadingWeightPreset.valueOf(it) }.getOrNull() } ?: HeadingWeightPreset.BOLD,
            codeThemePreset = prefs[KEY_CODE_THEME]?.let { runCatching { CodeThemePreset.valueOf(it) }.getOrNull() } ?: CodeThemePreset.GODOT_EDITOR,
            lineSpacingPreset = prefs[KEY_LINE_SPACING]?.let { runCatching { LineSpacingPreset.valueOf(it) }.getOrNull() } ?: LineSpacingPreset.COMFORTABLE,
            inlineCodeStyle = prefs[KEY_INLINE_CODE_STYLE]?.let { runCatching { InlineCodeStyle.valueOf(it) }.getOrNull() } ?: InlineCodeStyle.PILL_HIGHLIGHT,
            cornerRadiusDp = (prefs[KEY_CORNER_RADIUS] ?: 14).coerceIn(0, 32),
            cardBorderWidthDp = (prefs[KEY_CARD_BORDER_WIDTH] ?: 1.0f).coerceIn(0f, 3f),
            cardStyle = prefs[KEY_CARD_STYLE]?.let { runCatching { CardStyle.valueOf(it) }.getOrNull() } ?: CardStyle.FLAT_BORDER,
            showCardAccentBar = prefs[KEY_SHOW_CARD_ACCENT] ?: true,
            uiDensity = prefs[KEY_UI_DENSITY]?.let { runCatching { UiDensity.valueOf(it) }.getOrNull() } ?: UiDensity.COMFORTABLE,
            topBarStyle = prefs[KEY_TOP_BAR_STYLE]?.let { runCatching { TopBarStyle.valueOf(it) }.getOrNull() } ?: TopBarStyle.COMPACT,
            heroStyle = prefs[KEY_HERO_STYLE]?.let { runCatching { HeroStyle.valueOf(it) }.getOrNull() } ?: HeroStyle.RICH_GRADIENT,
            bottomBarStyle = prefs[KEY_BOTTOM_BAR_STYLE]?.let { runCatching { BottomBarStyle.valueOf(it) }.getOrNull() } ?: BottomBarStyle.PILL_TABS,
            lessonLayoutMode = prefs[KEY_LESSON_LAYOUT]?.let { runCatching { LessonLayoutMode.valueOf(it) }.getOrNull() } ?: LessonLayoutMode.TABBED_STUDIO,
            tableDisplayMode = prefs[KEY_TABLE_DISPLAY]?.let { runCatching { TableDisplayMode.valueOf(it) }.getOrNull() } ?: TableDisplayMode.AUTO_RESPONSIVE,
            codeWordWrap = prefs[KEY_CODE_WORD_WRAP] ?: true,
            showCodeLineNumbers = prefs[KEY_CODE_LINE_NUMBERS] ?: true,
            showCodeWindowDots = prefs[KEY_CODE_WINDOW_DOTS] ?: true,
            showHomeHeroCard = prefs[KEY_SHOW_HOME_HERO] ?: true,
            homeHeroLayout = prefs[KEY_HOME_HERO_LAYOUT]?.let { runCatching { HomeHeroLayout.valueOf(it) }.getOrNull() } ?: HomeHeroLayout.FULL_WITH_RING,
            homeHeroPalette = prefs[KEY_HOME_HERO_PALETTE]?.let { runCatching { HomeHeroPalette.valueOf(it) }.getOrNull() } ?: HomeHeroPalette.FOLLOW_ACCENT,
            showHeroBadge = prefs[KEY_SHOW_HERO_BADGE] ?: true,
            showHeroProgressRing = prefs[KEY_SHOW_HERO_RING] ?: true,
            showHeroSecondaryBtn = prefs[KEY_SHOW_HERO_SEC_BTN] ?: true,
            customHeroBadge = prefs[KEY_CUSTOM_HERO_BADGE] ?: "",
            customHeroTitle = prefs[KEY_CUSTOM_HERO_TITLE] ?: "",
            customHeroSubtitle = prefs[KEY_CUSTOM_HERO_SUBTITLE] ?: "",
            customHeroPrimaryBtn = prefs[KEY_CUSTOM_HERO_PRI_BTN] ?: "",
            customHeroSecondaryBtn = prefs[KEY_CUSTOM_HERO_SEC_BTN] ?: "",
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
    suspend fun setTheme(theme: ThemePref) { context.dataStore.edit { it[KEY_THEME] = theme.name } }
    suspend fun setAccentPreset(preset: AccentPreset) { context.dataStore.edit { it[KEY_ACCENT] = preset.name } }
    fun observeDynamicColor(): Flow<Boolean> = observeUiPreferences().map { it.dynamicColor }
    suspend fun setDynamicColor(enabled: Boolean) { context.dataStore.edit { it[KEY_DYNAMIC_COLOR] = enabled } }
    fun observeReduceMotion(): Flow<Boolean> = observeUiPreferences().map { it.reduceMotion }
    suspend fun setReduceMotion(enabled: Boolean) { context.dataStore.edit { it[KEY_REDUCE_MOTION] = enabled } }
    fun observeDyslexiaFont(): Flow<Boolean> = observeUiPreferences().map { it.dyslexiaFont }
    suspend fun setDyslexiaFont(enabled: Boolean) { context.dataStore.edit { it[KEY_DYSLEXIA_FONT] = enabled } }
    fun observeSkipGating(): Flow<Boolean> = observeUiPreferences().map { it.skipGating }
    suspend fun setSkipGating(enabled: Boolean) { context.dataStore.edit { it[KEY_SKIP_GATING] = enabled } }
    suspend fun setFontScale(scale: Float) { context.dataStore.edit { it[KEY_FONT_SCALE] = scale.coerceIn(0.80f, 1.35f) } }
    suspend fun setCodeFontScale(scale: Float) { context.dataStore.edit { it[KEY_CODE_FONT_SCALE] = scale.coerceIn(0.80f, 1.35f) } }
    suspend fun setFontFamilyPreset(preset: FontFamilyPreset) { context.dataStore.edit { it[KEY_FONT_FAMILY] = preset.name } }
    suspend fun setHeadingWeightPreset(preset: HeadingWeightPreset) { context.dataStore.edit { it[KEY_HEADING_WEIGHT] = preset.name } }
    suspend fun setCodeThemePreset(preset: CodeThemePreset) { context.dataStore.edit { it[KEY_CODE_THEME] = preset.name } }
    suspend fun setLineSpacingPreset(preset: LineSpacingPreset) { context.dataStore.edit { it[KEY_LINE_SPACING] = preset.name } }
    suspend fun setInlineCodeStyle(style: InlineCodeStyle) { context.dataStore.edit { it[KEY_INLINE_CODE_STYLE] = style.name } }
    suspend fun setCornerRadiusDp(radiusDp: Int) { context.dataStore.edit { it[KEY_CORNER_RADIUS] = radiusDp.coerceIn(0, 32) } }
    suspend fun setCardBorderWidthDp(widthDp: Float) { context.dataStore.edit { it[KEY_CARD_BORDER_WIDTH] = widthDp.coerceIn(0f, 3f) } }
    suspend fun setCardStyle(style: CardStyle) { context.dataStore.edit { it[KEY_CARD_STYLE] = style.name } }
    suspend fun setShowCardAccentBar(show: Boolean) { context.dataStore.edit { it[KEY_SHOW_CARD_ACCENT] = show } }
    suspend fun setUiDensity(density: UiDensity) { context.dataStore.edit { it[KEY_UI_DENSITY] = density.name } }
    suspend fun setTopBarStyle(style: TopBarStyle) { context.dataStore.edit { it[KEY_TOP_BAR_STYLE] = style.name } }
    suspend fun setHeroStyle(style: HeroStyle) { context.dataStore.edit { it[KEY_HERO_STYLE] = style.name } }
    suspend fun setBottomBarStyle(style: BottomBarStyle) { context.dataStore.edit { it[KEY_BOTTOM_BAR_STYLE] = style.name } }
    suspend fun setLessonLayoutMode(mode: LessonLayoutMode) { context.dataStore.edit { it[KEY_LESSON_LAYOUT] = mode.name } }
    suspend fun setTableDisplayMode(mode: TableDisplayMode) { context.dataStore.edit { it[KEY_TABLE_DISPLAY] = mode.name } }
    suspend fun setCodeWordWrap(wrap: Boolean) { context.dataStore.edit { it[KEY_CODE_WORD_WRAP] = wrap } }
    suspend fun setShowCodeLineNumbers(show: Boolean) { context.dataStore.edit { it[KEY_CODE_LINE_NUMBERS] = show } }
    suspend fun setShowCodeWindowDots(show: Boolean) { context.dataStore.edit { it[KEY_CODE_WINDOW_DOTS] = show } }

    // Home Top Hero Card Setters
    suspend fun setShowHomeHeroCard(show: Boolean) { context.dataStore.edit { it[KEY_SHOW_HOME_HERO] = show } }
    suspend fun setHomeHeroLayout(layout: HomeHeroLayout) { context.dataStore.edit { it[KEY_HOME_HERO_LAYOUT] = layout.name } }
    suspend fun setHomeHeroPalette(palette: HomeHeroPalette) { context.dataStore.edit { it[KEY_HOME_HERO_PALETTE] = palette.name } }
    suspend fun setShowHeroBadge(show: Boolean) { context.dataStore.edit { it[KEY_SHOW_HERO_BADGE] = show } }
    suspend fun setShowHeroProgressRing(show: Boolean) { context.dataStore.edit { it[KEY_SHOW_HERO_RING] = show } }
    suspend fun setShowHeroSecondaryBtn(show: Boolean) { context.dataStore.edit { it[KEY_SHOW_HERO_SEC_BTN] = show } }
    suspend fun setCustomHeroBadge(badge: String) { context.dataStore.edit { it[KEY_CUSTOM_HERO_BADGE] = badge } }
    suspend fun setCustomHeroTitle(title: String) { context.dataStore.edit { it[KEY_CUSTOM_HERO_TITLE] = title } }
    suspend fun setCustomHeroSubtitle(subtitle: String) { context.dataStore.edit { it[KEY_CUSTOM_HERO_SUBTITLE] = subtitle } }
    suspend fun setCustomHeroPrimaryBtn(label: String) { context.dataStore.edit { it[KEY_CUSTOM_HERO_PRI_BTN] = label } }
    suspend fun setCustomHeroSecondaryBtn(label: String) { context.dataStore.edit { it[KEY_CUSTOM_HERO_SEC_BTN] = label } }

    suspend fun setShowHomeStats(show: Boolean) { context.dataStore.edit { it[KEY_SHOW_HOME_STATS] = show } }
    suspend fun setShowHomeQuickLaunch(show: Boolean) { context.dataStore.edit { it[KEY_SHOW_HOME_QUICK_LAUNCH] = show } }
    suspend fun setShowHomePhaseStrip(show: Boolean) { context.dataStore.edit { it[KEY_SHOW_HOME_PHASE_STRIP] = show } }
    suspend fun setShowDailyTip(show: Boolean) { context.dataStore.edit { it[KEY_SHOW_DAILY_TIP] = show } }
    suspend fun setShowReviewQueue(show: Boolean) { context.dataStore.edit { it[KEY_SHOW_REVIEW_QUEUE] = show } }
    suspend fun setCustomGreeting(greeting: String) { context.dataStore.edit { it[KEY_CUSTOM_GREETING] = greeting } }
    suspend fun setCustomSubtitle(subtitle: String) { context.dataStore.edit { it[KEY_CUSTOM_SUBTITLE] = subtitle } }

    suspend fun resetUiPreferencesToDefault() {
        context.dataStore.edit { prefs ->
            val version = prefs[KEY_CONTENT_VERSION]
            val track = prefs[KEY_TRACK]
            prefs.clear()
            if (version != null) prefs[KEY_CONTENT_VERSION] = version
            if (track != null) prefs[KEY_TRACK] = track
        }
    }

    fun observeContentVersion(): Flow<String?> = context.dataStore.data.map { it[KEY_CONTENT_VERSION] }
    suspend fun setContentVersion(version: String) { context.dataStore.edit { it[KEY_CONTENT_VERSION] = version } }

    fun observeLearnerTrack(): Flow<LearnerTrack?> = context.dataStore.data.map {
        it[KEY_TRACK]?.let { name -> runCatching { LearnerTrack.valueOf(name) }.getOrNull() }
    }
    suspend fun setLearnerTrack(track: LearnerTrack) { context.dataStore.edit { it[KEY_TRACK] = track.name } }

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
        private val KEY_HEADING_WEIGHT = stringPreferencesKey("heading_weight")
        private val KEY_CODE_THEME = stringPreferencesKey("code_theme")
        private val KEY_LINE_SPACING = stringPreferencesKey("line_spacing")
        private val KEY_INLINE_CODE_STYLE = stringPreferencesKey("inline_code_style")
        private val KEY_CORNER_RADIUS = intPreferencesKey("corner_radius_dp")
        private val KEY_CARD_BORDER_WIDTH = floatPreferencesKey("card_border_width_dp")
        private val KEY_CARD_STYLE = stringPreferencesKey("card_style")
        private val KEY_SHOW_CARD_ACCENT = booleanPreferencesKey("show_card_accent")
        private val KEY_UI_DENSITY = stringPreferencesKey("ui_density")
        private val KEY_TOP_BAR_STYLE = stringPreferencesKey("top_bar_style")
        private val KEY_HERO_STYLE = stringPreferencesKey("hero_style")
        private val KEY_BOTTOM_BAR_STYLE = stringPreferencesKey("bottom_bar_style")
        private val KEY_LESSON_LAYOUT = stringPreferencesKey("lesson_layout")
        private val KEY_TABLE_DISPLAY = stringPreferencesKey("table_display")
        private val KEY_CODE_WORD_WRAP = booleanPreferencesKey("code_word_wrap")
        private val KEY_CODE_LINE_NUMBERS = booleanPreferencesKey("code_line_numbers")
        private val KEY_CODE_WINDOW_DOTS = booleanPreferencesKey("code_window_dots")
        private val KEY_SHOW_HOME_HERO = booleanPreferencesKey("show_home_hero")
        private val KEY_HOME_HERO_LAYOUT = stringPreferencesKey("home_hero_layout")
        private val KEY_HOME_HERO_PALETTE = stringPreferencesKey("home_hero_palette")
        private val KEY_SHOW_HERO_BADGE = booleanPreferencesKey("show_hero_badge")
        private val KEY_SHOW_HERO_RING = booleanPreferencesKey("show_hero_ring")
        private val KEY_SHOW_HERO_SEC_BTN = booleanPreferencesKey("show_hero_sec_btn")
        private val KEY_CUSTOM_HERO_BADGE = stringPreferencesKey("custom_hero_badge")
        private val KEY_CUSTOM_HERO_TITLE = stringPreferencesKey("custom_hero_title")
        private val KEY_CUSTOM_HERO_SUBTITLE = stringPreferencesKey("custom_hero_subtitle")
        private val KEY_CUSTOM_HERO_PRI_BTN = stringPreferencesKey("custom_hero_pri_btn")
        private val KEY_CUSTOM_HERO_SEC_BTN = stringPreferencesKey("custom_hero_sec_btn")
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
