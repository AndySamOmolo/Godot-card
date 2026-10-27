package com.kadicompanion.ui.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.WrapText
import androidx.compose.material.icons.outlined.Animation
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.BorderStyle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.ColorLens
import androidx.compose.material.icons.outlined.Contrast
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.DashboardCustomize
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.FormatLineSpacing
import androidx.compose.material.icons.outlined.FormatSize
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material.icons.outlined.RoundedCorner
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SpaceDashboard
import androidx.compose.material.icons.outlined.TableChart
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.Update
import androidx.compose.material.icons.outlined.ViewCompact
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kadicompanion.R
import com.kadicompanion.data.local.datastore.AccentPreset
import com.kadicompanion.data.local.datastore.BottomBarStyle
import com.kadicompanion.data.local.datastore.CardStyle
import com.kadicompanion.data.local.datastore.CodeThemePreset
import com.kadicompanion.data.local.datastore.FontFamilyPreset
import com.kadicompanion.data.local.datastore.HeadingWeightPreset
import com.kadicompanion.data.local.datastore.HeroStyle
import com.kadicompanion.data.local.datastore.HomeHeroLayout
import com.kadicompanion.data.local.datastore.HomeHeroPalette
import com.kadicompanion.data.local.datastore.InlineCodeStyle
import com.kadicompanion.data.local.datastore.LessonLayoutMode
import com.kadicompanion.data.local.datastore.LineSpacingPreset
import com.kadicompanion.data.local.datastore.TableDisplayMode
import com.kadicompanion.data.local.datastore.ThemePref
import com.kadicompanion.data.local.datastore.TopBarStyle
import com.kadicompanion.data.local.datastore.UiDensity
import com.kadicompanion.domain.model.CodeSnippet
import com.kadicompanion.domain.model.LearnerTrack
import com.kadicompanion.ui.common.CodeBlock
import com.kadicompanion.ui.common.IconBubble
import com.kadicompanion.ui.common.KadiCard
import com.kadicompanion.ui.common.KadiTopBar
import com.kadicompanion.ui.common.Pill
import com.kadicompanion.ui.common.RichInlineText
import com.kadicompanion.ui.common.SectionHeader
import com.kadicompanion.ui.common.studioCardSpacing
import com.kadicompanion.ui.common.studioScreenPadding
import com.kadicompanion.ui.home.heroPaletteSwatch
import com.kadicompanion.ui.theme.CodeFontFamily
import com.kadicompanion.ui.theme.LocalUiPreferences
import com.kadicompanion.ui.theme.successColor
import kotlin.math.roundToInt

@Composable
fun SettingsScreen(onBack: () -> Unit, vm: SettingsViewModel = hiltViewModel()) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    var activeSection by rememberSaveable { mutableIntStateOf(0) }
    val sections = listOf("Theme & Color", "Fonts & Code", "Home Top Card", "Layout & Bars", "Course & About")

    Scaffold(
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            KadiTopBar(
                title = "UI Studio & Settings",
                subtitle = "CUSTOMIZE EVERYTHING",
                onBack = onBack,
                showCustomizerButton = false,
                actions = {
                    FilledTonalButton(
                        onClick = vm::resetAllUiToDefault,
                        shape = MaterialTheme.shapes.extraSmall,
                        modifier = Modifier.padding(end = 8.dp).height(30.dp)
                    ) {
                        Icon(Icons.Outlined.RestartAlt, contentDescription = "Reset UI", modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Reset", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    ) { padding ->
        val hPad = studioScreenPadding()
        val vGap = studioCardSpacing()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = hPad, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                sections.forEachIndexed { idx, label ->
                    val selected = activeSection == idx
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainer,
                        border = BorderStroke(
                            1.dp,
                            if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                        ),
                        modifier = Modifier.clickable { activeSection = idx }
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                            color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(start = hPad, end = hPad, top = 10.dp, bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(vGap)
            ) {
                LiveUiPreviewCard()

                when (activeSection) {
                    0 -> ThemeAndColorSection(state = state, vm = vm)
                    1 -> TextAndCodeSection(state = state, vm = vm)
                    2 -> HomeTopCardSection(state = state, vm = vm)
                    3 -> LayoutAndCardsSection(state = state, vm = vm)
                    else -> CourseAndAboutSection(state = state, vm = vm)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun QuickUiCustomizerSheet(
    onDismiss: () -> Unit,
    onOpenFullSettings: () -> Unit,
    vm: SettingsViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    val p = state.uiPrefs
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    IconBubble(icon = Icons.Outlined.Tune, size = 34.dp)
                    Column {
                        Text(
                            text = "LIVE UI STUDIO",
                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Customize Anything Live",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Outlined.Close, contentDescription = "Close")
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // 1. Top Navbar Size
            QuickGroupTitle("Top Navbar Size")
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TopBarStyle.entries.forEach { tStyle ->
                    QuickOptionChip(
                        label = tStyle.label,
                        selected = p.topBarStyle == tStyle,
                        onClick = { vm.setTopBarStyle(tStyle) }
                    )
                }
            }

            // 2. Home Top Hero Card Layout & Palette
            QuickGroupTitle("Home Top Hero Card Layout & Palette")
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HomeHeroLayout.entries.forEach { hLayout ->
                    QuickOptionChip(
                        label = hLayout.label,
                        selected = p.homeHeroLayout == hLayout,
                        onClick = { vm.setHomeHeroLayout(hLayout) }
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HomeHeroPalette.entries.forEach { hPal ->
                    val swatch = heroPaletteSwatch(hPal)
                    val selected = p.homeHeroPalette == hPal
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = if (selected) swatch.copy(alpha = 0.18f) else MaterialTheme.colorScheme.surfaceContainer,
                        border = BorderStroke(if (selected) 1.5.dp else 1.dp, if (selected) swatch else MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier.clickable { vm.setHomeHeroPalette(hPal) }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(modifier = Modifier.size(11.dp).clip(CircleShape).background(swatch))
                            Text(
                                text = hPal.label,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                color = if (selected) swatch else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // 3. Theme Mode & Accent Palette
            QuickGroupTitle("Theme Mode & Accent Palette")
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    ThemePref.DARK to "Dark",
                    ThemePref.AMOLED to "AMOLED Black",
                    ThemePref.LIGHT to "Light",
                    ThemePref.SYSTEM to "System"
                ).forEach { (mode, label) ->
                    QuickOptionChip(
                        label = label,
                        selected = p.theme == mode,
                        onClick = { vm.setTheme(mode) }
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AccentPreset.entries.forEach { preset ->
                    val swatch = presetSwatchColor(preset)
                    val selected = p.accentPreset == preset
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = if (selected) swatch.copy(alpha = 0.18f) else MaterialTheme.colorScheme.surfaceContainer,
                        border = BorderStroke(if (selected) 1.5.dp else 1.dp, if (selected) swatch else MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier.clickable { vm.setAccentPreset(preset) }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(swatch)
                            )
                            Text(
                                text = preset.label,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                color = if (selected) swatch else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // 4. 10 Font Families & 5 Heading Weights
            QuickGroupTitle("Font Family (10 Typefaces) & Heading Weight")
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FontFamilyPreset.entries.forEach { ff ->
                    QuickOptionChip(
                        label = ff.label,
                        selected = p.fontFamilyPreset == ff,
                        onClick = { vm.setFontFamilyPreset(ff) }
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HeadingWeightPreset.entries.forEach { hw ->
                    QuickOptionChip(
                        label = hw.label,
                        selected = p.headingWeightPreset == hw,
                        onClick = { vm.setHeadingWeightPreset(hw) }
                    )
                }
            }

            // 5. Text & Code Scale Sliders
            QuickGroupTitle("Text Size (${(p.fontScale * 100).roundToInt()}%) & Code Size (${(p.codeFontScale * 100).roundToInt()}%)")
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("UI Text", style = MaterialTheme.typography.labelMedium, modifier = Modifier.width(64.dp))
                    Slider(
                        value = p.fontScale,
                        onValueChange = { vm.setFontScale((it * 20).roundToInt() / 20f) },
                        valueRange = 0.85f..1.25f,
                        steps = 7,
                        modifier = Modifier.weight(1f)
                    )
                    Pill(text = "${(p.fontScale * 100).roundToInt()}%")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("GDScript", style = MaterialTheme.typography.labelMedium, modifier = Modifier.width(64.dp))
                    Slider(
                        value = p.codeFontScale,
                        onValueChange = { vm.setCodeFontScale((it * 20).roundToInt() / 20f) },
                        valueRange = 0.85f..1.25f,
                        steps = 7,
                        modifier = Modifier.weight(1f)
                    )
                    Pill(text = "${(p.codeFontScale * 100).roundToInt()}%")
                }
            }

            // 6. Code Syntax Theme & Inline Code Style
            QuickGroupTitle("GDScript Syntax Theme & Inline Code Style")
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CodeThemePreset.entries.forEach { cTheme ->
                    QuickOptionChip(
                        label = cTheme.label,
                        selected = p.codeThemePreset == cTheme,
                        onClick = { vm.setCodeThemePreset(cTheme) }
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InlineCodeStyle.entries.forEach { style ->
                    QuickOptionChip(
                        label = style.label,
                        selected = p.inlineCodeStyle == style,
                        onClick = { vm.setInlineCodeStyle(style) }
                    )
                }
            }

            // 7. Phone Density, Corner Radius, Card Style & Border Width
            QuickGroupTitle("Phone Layout Density & Card Geometry")
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                UiDensity.entries.forEach { density ->
                    QuickOptionChip(
                        label = density.label,
                        selected = p.uiDensity == density,
                        onClick = { vm.setUiDensity(density) }
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CardStyle.entries.forEach { cs ->
                    QuickOptionChip(
                        label = cs.label,
                        selected = p.cardStyle == cs,
                        onClick = { vm.setCardStyle(cs) }
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(0 to "Sharp 0dp", 8 to "Crisp 8dp", 14 to "Studio 14dp", 20 to "Round 20dp", 28 to "Soft 28dp").forEach { (dpVal, lbl) ->
                    QuickOptionChip(
                        label = lbl,
                        selected = p.cornerRadiusDp == dpVal,
                        onClick = { vm.setCornerRadiusDp(dpVal) }
                    )
                }
            }

            // 8. Lesson Layout, Table Display & Bottom Bar
            QuickGroupTitle("Lesson Reader & Table Mode on Phone")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LessonLayoutMode.entries.forEach { mode ->
                    QuickOptionChip(
                        label = mode.label,
                        selected = p.lessonLayoutMode == mode,
                        onClick = { vm.setLessonLayoutMode(mode) }
                    )
                }
                TableDisplayMode.entries.forEach { tMode ->
                    QuickOptionChip(
                        label = tMode.label,
                        selected = p.tableDisplayMode == tMode,
                        onClick = { vm.setTableDisplayMode(tMode) }
                    )
                }
            }

            QuickGroupTitle("Bottom Navigation Bar")
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BottomBarStyle.entries.forEach { bStyle ->
                    QuickOptionChip(
                        label = bStyle.label,
                        selected = p.bottomBarStyle == bStyle,
                        onClick = { vm.setBottomBarStyle(bStyle) }
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FilledTonalButton(
                    onClick = vm::resetAllUiToDefault,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.weight(1f).height(44.dp)
                ) {
                    Icon(Icons.Outlined.RestartAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Reset Defaults", fontWeight = FontWeight.SemiBold)
                }
                Button(
                    onClick = {
                        onDismiss()
                        onOpenFullSettings()
                    },
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.weight(1.15f).height(44.dp)
                ) {
                    Icon(Icons.Outlined.Settings, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("All UI Options", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
internal fun QuickGroupTitle(text: String) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold
    )
}

@Composable
internal fun QuickOptionChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val primary = MaterialTheme.colorScheme.primary
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = MaterialTheme.shapes.small,
        color = if (selected) primary.copy(alpha = 0.18f) else MaterialTheme.colorScheme.surfaceContainer,
        border = BorderStroke(
            width = if (selected) 1.5.dp else 1.dp,
            color = if (selected) primary else MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = if (selected) primary else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 11.dp, vertical = 7.dp)
        )
    }
}

internal fun presetSwatchColor(preset: AccentPreset): Color = when (preset) {
    AccentPreset.GODOT_SAPPHIRE -> Color(0xFF38BDF8)
    AccentPreset.NAIROBI_GOLD -> Color(0xFFFBBF24)
    AccentPreset.EMERALD_FELT -> Color(0xFF34D399)
    AccentPreset.CYBER_VIOLET -> Color(0xFFC084FC)
    AccentPreset.CRIMSON_ACE -> Color(0xFFFB7185)
    AccentPreset.SUNSET_ORANGE -> Color(0xFFFB923C)
    AccentPreset.MONOCHROME_SLATE -> Color(0xFF94A3B8)
}

@Composable
private fun LiveUiPreviewCard() {
    val p = LocalUiPreferences.current
    KadiCard(accent = MaterialTheme.colorScheme.primary) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IconBubble(icon = Icons.Outlined.AutoAwesome, tint = MaterialTheme.colorScheme.primary, size = 32.dp)
                Column {
                    Text(
                        text = "LIVE UI PREVIEW",
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${p.fontFamilyPreset.label} · ${p.codeThemePreset.label}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Pill(text = "${(p.fontScale * 100).roundToInt()}% · ${p.cornerRadiusDp}dp", color = MaterialTheme.colorScheme.secondary)
        }
        RichInlineText(
            text = "Sample lesson text with **bold emphasis**, inline GDScript `var penalty_stack: int = 0`, and a clean [[Penalty Cards]] reference with zero raw symbols.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ThemeAndColorSection(state: SettingsUiState, vm: SettingsViewModel) {
    val p = state.uiPrefs

    SectionHeader(title = stringResource(R.string.settings_theme), eyebrow = "THEME MODE")
    KadiCard {
        ChoiceRow(
            icon = Icons.Outlined.DarkMode,
            title = "Studio Dark",
            description = "Deep carbon-slate (#0B0F17) for comfortable coding.",
            selected = p.theme == ThemePref.DARK,
            onClick = { vm.setTheme(ThemePref.DARK) }
        )
        ChoiceRow(
            icon = Icons.Outlined.Contrast,
            title = "Pitch Black (AMOLED)",
            description = "True #000000 background for OLED phone battery & contrast.",
            selected = p.theme == ThemePref.AMOLED,
            onClick = { vm.setTheme(ThemePref.AMOLED) }
        )
        ChoiceRow(
            icon = Icons.Outlined.LightMode,
            title = "Studio Light",
            description = "Crisp alabaster & white surfaces for bright daylight.",
            selected = p.theme == ThemePref.LIGHT,
            onClick = { vm.setTheme(ThemePref.LIGHT) }
        )
        ChoiceRow(
            icon = Icons.Outlined.PhoneAndroid,
            title = stringResource(R.string.settings_theme_system),
            description = "Automatically switch with your phone's system setting.",
            selected = p.theme == ThemePref.SYSTEM,
            onClick = { vm.setTheme(ThemePref.SYSTEM) }
        )
    }

    SectionHeader(title = "Accent Color Palettes", eyebrow = "7 STUDIO PRESETS")
    KadiCard {
        AccentPreset.entries.forEach { preset ->
            val swatch = presetSwatchColor(preset)
            ChoiceRow(
                icon = Icons.Outlined.Palette,
                iconTint = swatch,
                title = preset.label,
                description = preset.description,
                selected = p.accentPreset == preset,
                onClick = { vm.setAccentPreset(preset) }
            )
        }
    }

    SectionHeader(title = "Wallpaper & Motion", eyebrow = "SYSTEM EFFECTS")
    KadiCard {
        ToggleRow(
            icon = Icons.Outlined.ColorLens,
            title = stringResource(R.string.settings_dynamic_color),
            description = "Override preset with Android 12+ Material You wallpaper colors.",
            checked = p.dynamicColor,
            onCheckedChange = vm::setDynamicColor
        )
        ToggleRow(
            icon = Icons.Outlined.Animation,
            title = stringResource(R.string.settings_reduce_motion),
            description = "Turn off screen transitions and progress ring animations.",
            checked = p.reduceMotion,
            onCheckedChange = vm::setReduceMotion
        )
    }
}

@Composable
private fun TextAndCodeSection(state: SettingsUiState, vm: SettingsViewModel) {
    val p = state.uiPrefs

    SectionHeader(title = "10 Font Families & Heading Weight", eyebrow = "TYPOGRAPHY STUDIO")
    KadiCard {
        Text(
            text = "Choose from 10 distinct system & editorial font families",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        FontFamilyPreset.entries.forEach { preset ->
            ChoiceRow(
                icon = Icons.Outlined.TextFields,
                title = preset.label,
                description = preset.description,
                selected = p.fontFamilyPreset == preset,
                onClick = { vm.setFontFamilyPreset(preset) }
            )
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

        Text("Heading Font Weight", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            HeadingWeightPreset.entries.forEach { hw ->
                QuickOptionChip(
                    label = hw.label,
                    selected = p.headingWeightPreset == hw,
                    onClick = { vm.setHeadingWeightPreset(hw) }
                )
            }
        }

        Text("Paragraph Line Spacing", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LineSpacingPreset.entries.forEach { preset ->
                QuickOptionChip(
                    label = preset.label,
                    selected = p.lineSpacingPreset == preset,
                    onClick = { vm.setLineSpacingPreset(preset) }
                )
            }
        }

        ToggleRow(
            icon = Icons.Outlined.TextFields,
            title = stringResource(R.string.settings_dyslexia_font),
            description = "Wider letter tracking across all lesson paragraphs.",
            checked = p.dyslexiaFont,
            onCheckedChange = vm::setDyslexiaFont
        )
    }

    SectionHeader(title = "Text & Code Sizing", eyebrow = "LIVE TYPOGRAPHY SCALE")
    KadiCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                IconBubble(icon = Icons.Outlined.FormatSize, size = 34.dp)
                Column {
                    Text("UI & Lesson Text Size", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Text("Scale headings, paragraphs, and cards", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Pill(text = "${(p.fontScale * 100).roundToInt()}%")
        }
        Slider(
            value = p.fontScale,
            onValueChange = { vm.setFontScale((it * 20).roundToInt() / 20f) },
            valueRange = 0.85f..1.25f,
            steps = 7
        )
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf(0.85f to "85%", 0.95f to "95%", 1.0f to "100% (Default)", 1.10f to "110%", 1.20f to "120%").forEach { (sc, lbl) ->
                QuickOptionChip(
                    label = lbl,
                    selected = (p.fontScale - sc).let { it > -0.02f && it < 0.02f },
                    onClick = { vm.setFontScale(sc) }
                )
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                IconBubble(icon = Icons.Outlined.Code, tint = MaterialTheme.colorScheme.secondary, size = 34.dp)
                Column {
                    Text("GDScript Code Font Size", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Text("Scale code blocks, scene trees, and inline code", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Pill(text = "${(p.codeFontScale * 100).roundToInt()}%", color = MaterialTheme.colorScheme.secondary)
        }
        Slider(
            value = p.codeFontScale,
            onValueChange = { vm.setCodeFontScale((it * 20).roundToInt() / 20f) },
            valueRange = 0.85f..1.25f,
            steps = 7
        )
    }

    SectionHeader(title = "GDScript Code Syntax Theme", eyebrow = "6 IDE PALETTES")
    KadiCard {
        CodeBlock(
            snippet = CodeSnippet(
                id = "theme-preview",
                code = "@onready var card_art: TextureRect = \$CardArt\nfunc apply_downloaded_png(path: String) -> void:\n    if ResourceLoader.exists(path):\n        card_art.texture = load(path) # Crisp PNG deck",
                caption = "Live Syntax Preview (${p.codeThemePreset.label})"
            )
        )
        CodeThemePreset.entries.forEach { cTheme ->
            ChoiceRow(
                icon = Icons.Outlined.Code,
                title = cTheme.label,
                description = cTheme.description,
                selected = p.codeThemePreset == cTheme,
                onClick = { vm.setCodeThemePreset(cTheme) }
            )
        }
    }

    SectionHeader(title = "Inline Code & Term Appearance", eyebrow = "TEXT FORMATTING STYLE")
    KadiCard {
        InlineCodeStyle.entries.forEach { style ->
            ChoiceRow(
                icon = Icons.Outlined.Code,
                title = style.label,
                description = style.description,
                selected = p.inlineCodeStyle == style,
                onClick = { vm.setInlineCodeStyle(style) }
            )
        }
    }

    SectionHeader(title = "Code Block Preferences", eyebrow = "MOBILE IDE OPTIONS")
    KadiCard {
        ToggleRow(
            icon = Icons.AutoMirrored.Outlined.WrapText,
            title = "Wrap Long GDScript Lines",
            description = "Wrap code lines to fit your phone screen without horizontal scrolling.",
            checked = p.codeWordWrap,
            onCheckedChange = vm::setCodeWordWrap
        )
        ToggleRow(
            icon = Icons.Outlined.FormatLineSpacing,
            title = "Show Code Line Numbers",
            description = "Display line number gutter on the left of GDScript snippets.",
            checked = p.showCodeLineNumbers,
            onCheckedChange = vm::setShowCodeLineNumbers
        )
        ToggleRow(
            icon = Icons.Outlined.DashboardCustomize,
            title = "Show IDE Header Window Dots",
            description = "Display red/amber/green studio dots on code block headers.",
            checked = p.showCodeWindowDots,
            onCheckedChange = vm::setShowCodeWindowDots
        )
    }
}

@Composable
private fun HomeTopCardSection(state: SettingsUiState, vm: SettingsViewModel) {
    val p = state.uiPrefs

    SectionHeader(title = "Top Hero Card Layout & Visibility", eyebrow = "HOMEPAGE TOP CARD")
    KadiCard {
        ToggleRow(
            icon = Icons.Outlined.Home,
            title = "Show Top Hero Card on Homepage",
            description = "Display the main active mission & progress card at the top of Home.",
            checked = p.showHomeHeroCard,
            onCheckedChange = vm::setShowHomeHeroCard
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

        Text("Top Card Layout Mode", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        HomeHeroLayout.entries.forEach { layout ->
            ChoiceRow(
                icon = Icons.Outlined.SpaceDashboard,
                title = layout.label,
                description = layout.description,
                selected = p.homeHeroLayout == layout,
                onClick = { vm.setHomeHeroLayout(layout) }
            )
        }
    }

    SectionHeader(title = "Top Hero Card Color Palette", eyebrow = "8 CARD PALETTES")
    KadiCard {
        HomeHeroPalette.entries.forEach { palette ->
            val swatch = heroPaletteSwatch(palette)
            ChoiceRow(
                icon = Icons.Outlined.Palette,
                iconTint = swatch,
                title = palette.label,
                description = palette.description,
                selected = p.homeHeroPalette == palette,
                onClick = { vm.setHomeHeroPalette(palette) }
            )
        }
    }

    SectionHeader(title = "Top Hero Card Elements & Custom Text", eyebrow = "CUSTOMIZE EVERY ELEMENT")
    KadiCard {
        ToggleRow(
            icon = Icons.Outlined.AutoAwesome,
            title = "Show Top Status Badge",
            description = "Display the 'UP NEXT · LESSON X' pill badge inside the top card.",
            checked = p.showHeroBadge,
            onCheckedChange = vm::setShowHeroBadge
        )
        ToggleRow(
            icon = Icons.Outlined.AutoAwesome,
            title = "Show Circular Progress Ring",
            description = "Display the animated percentage ring on the right side of the top card.",
            checked = p.showHeroProgressRing,
            onCheckedChange = vm::setShowHeroProgressRing
        )
        ToggleRow(
            icon = Icons.Outlined.SpaceDashboard,
            title = "Show Secondary Action Button",
            description = "Display the secondary 'Curriculum' button next to the primary button.",
            checked = p.showHeroSecondaryBtn,
            onCheckedChange = vm::setShowHeroSecondaryBtn
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

        Text(
            text = "Custom Top Card Text Overrides (leave blank for automatic lesson progress text)",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        OutlinedTextField(
            value = p.customHeroBadge,
            onValueChange = vm::setCustomHeroBadge,
            label = { Text("Custom Top Card Badge (default: UP NEXT · LESSON X)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = p.customHeroTitle,
            onValueChange = vm::setCustomHeroTitle,
            label = { Text("Custom Top Card Title (default: current lesson title)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = p.customHeroSubtitle,
            onValueChange = vm::setCustomHeroSubtitle,
            label = { Text("Custom Top Card Subtitle (default: current phase title)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = p.customHeroPrimaryBtn,
                onValueChange = vm::setCustomHeroPrimaryBtn,
                label = { Text("Primary Button Label") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = p.customHeroSecondaryBtn,
                onValueChange = vm::setCustomHeroSecondaryBtn,
                label = { Text("Secondary Button Label") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
        }
    }

    SectionHeader(title = "Home Top Bar & Widgets", eyebrow = "HOME SCREEN SECTIONS")
    KadiCard {
        OutlinedTextField(
            value = p.customGreeting,
            onValueChange = vm::setCustomGreeting,
            label = { Text("Home Greeting (default: Karibu, learner)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = p.customSubtitle,
            onValueChange = vm::setCustomSubtitle,
            label = { Text("Top Studio Eyebrow (default: GODOT 4.3 · KADI STUDIO)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

        ToggleRow(
            icon = Icons.Outlined.Home,
            title = "Show 3-Tile Progress Metrics Strip",
            description = "Display Lessons, Mastery %, and Review Queue stat tiles on Home.",
            checked = p.showHomeStats,
            onCheckedChange = vm::setShowHomeStats
        )
        ToggleRow(
            icon = Icons.Outlined.SpaceDashboard,
            title = "Show Blueprint & Curriculum Quick Launch",
            description = "Display the 2-column quick action grid on Home.",
            checked = p.showHomeQuickLaunch,
            onCheckedChange = vm::setShowHomeQuickLaunch
        )
        ToggleRow(
            icon = Icons.Outlined.ViewCompact,
            title = "Show Horizontal Course Modules Strip",
            description = "Display the P00–P11 horizontal phase stepper on Home.",
            checked = p.showHomePhaseStrip,
            onCheckedChange = vm::setShowHomePhaseStrip
        )
        ToggleRow(
            icon = Icons.Outlined.AutoAwesome,
            title = "Show Daily Godot 4.3 & Kadi Pro Tip",
            description = "Display the interactive tip switcher card on Home.",
            checked = p.showDailyTip,
            onCheckedChange = vm::setShowDailyTip
        )
        ToggleRow(
            icon = Icons.Outlined.Update,
            title = "Show Spaced Repetition Review Queue",
            description = "Display the concept review queue at the bottom of Home.",
            checked = p.showReviewQueue,
            onCheckedChange = vm::setShowReviewQueue
        )
    }
}

@Composable
private fun LayoutAndCardsSection(state: SettingsUiState, vm: SettingsViewModel) {
    val p = state.uiPrefs

    SectionHeader(title = "Top Navbar & Bottom Bar", eyebrow = "NAVIGATION CHROME")
    KadiCard {
        Text("Top Navbar Size", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        TopBarStyle.entries.forEach { tStyle ->
            ChoiceRow(
                icon = Icons.Outlined.SpaceDashboard,
                title = tStyle.label,
                description = tStyle.description,
                selected = p.topBarStyle == tStyle,
                onClick = { vm.setTopBarStyle(tStyle) }
            )
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

        Text("Bottom Navigation Bar Style", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        BottomBarStyle.entries.forEach { bStyle ->
            ChoiceRow(
                icon = Icons.Outlined.SpaceDashboard,
                title = bStyle.label,
                description = bStyle.description,
                selected = p.bottomBarStyle == bStyle,
                onClick = { vm.setBottomBarStyle(bStyle) }
            )
        }
    }

    SectionHeader(title = "Phone Screen Density", eyebrow = "ERGONOMICS & PADDING")
    KadiCard {
        UiDensity.entries.forEach { density ->
            ChoiceRow(
                icon = Icons.Outlined.ViewCompact,
                title = density.label,
                description = density.description,
                selected = p.uiDensity == density,
                onClick = { vm.setUiDensity(density) }
            )
        }
    }

    SectionHeader(title = "Card Surface, Borders & Corners", eyebrow = "CONTAINERS & GEOMETRY")
    KadiCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                IconBubble(icon = Icons.Outlined.RoundedCorner, size = 34.dp)
                Column {
                    Text("Corner Roundness", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Text("Adjust card, button, and banner corner radius", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Pill(text = "${p.cornerRadiusDp}dp")
        }
        Slider(
            value = p.cornerRadiusDp.toFloat(),
            onValueChange = { vm.setCornerRadiusDp(it.roundToInt()) },
            valueRange = 0f..28f,
            steps = 6
        )
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(0 to "Sharp (0dp)", 8 to "Compact (8dp)", 14 to "Studio (14dp)", 20 to "Rounded (20dp)", 28 to "Pill (28dp)").forEach { (r, label) ->
                QuickOptionChip(
                    label = label,
                    selected = p.cornerRadiusDp == r,
                    onClick = { vm.setCornerRadiusDp(r) }
                )
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                IconBubble(icon = Icons.Outlined.BorderStyle, tint = MaterialTheme.colorScheme.secondary, size = 34.dp)
                Column {
                    Text("Card Border Thickness", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Text("0dp borderless up to 2.5dp bold studio outlines", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Pill(text = "${p.cardBorderWidthDp}dp", color = MaterialTheme.colorScheme.secondary)
        }
        Slider(
            value = p.cardBorderWidthDp,
            onValueChange = { vm.setCardBorderWidthDp((it * 2).roundToInt() / 2f) },
            valueRange = 0f..2.5f,
            steps = 4
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

        CardStyle.entries.forEach { cs ->
            ChoiceRow(
                icon = Icons.Outlined.BorderStyle,
                title = cs.label,
                description = cs.description,
                selected = p.cardStyle == cs,
                onClick = { vm.setCardStyle(cs) }
            )
        }

        ToggleRow(
            icon = Icons.Outlined.BorderStyle,
            title = "Show Colored Card Top Accent Bar",
            description = "Display the 3dp colored top highlight bar on accented cards.",
            checked = p.showCardAccentBar,
            onCheckedChange = vm::setShowCardAccentBar
        )
    }

    SectionHeader(title = "Lesson Reader, Tables & Hero", eyebrow = "MOBILE READING FLOW")
    KadiCard {
        LessonLayoutMode.entries.forEach { mode ->
            ChoiceRow(
                icon = Icons.Outlined.SpaceDashboard,
                title = mode.label,
                description = mode.description,
                selected = p.lessonLayoutMode == mode,
                onClick = { vm.setLessonLayoutMode(mode) }
            )
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

        TableDisplayMode.entries.forEach { tMode ->
            ChoiceRow(
                icon = Icons.Outlined.TableChart,
                title = tMode.label,
                description = tMode.description,
                selected = p.tableDisplayMode == tMode,
                onClick = { vm.setTableDisplayMode(tMode) }
            )
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

        Text("Other Screen Hero Banner Style", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        HeroStyle.entries.forEach { hStyle ->
            ChoiceRow(
                icon = Icons.Outlined.AutoAwesome,
                title = hStyle.label,
                description = hStyle.description,
                selected = p.heroStyle == hStyle,
                onClick = { vm.setHeroStyle(hStyle) }
            )
        }
    }
}

@Composable
private fun CourseAndAboutSection(state: SettingsUiState, vm: SettingsViewModel) {
    val p = state.uiPrefs

    SectionHeader(title = stringResource(R.string.settings_track), eyebrow = "LEARNING PATH")
    KadiCard {
        LearnerTrack.entries.forEach { track ->
            ChoiceRow(
                icon = Icons.Outlined.Explore,
                title = trackLabel(track),
                description = trackDescription(track),
                selected = state.track == track,
                onClick = { vm.setTrack(track) }
            )
        }
        ToggleRow(
            icon = Icons.Outlined.LockOpen,
            title = stringResource(R.string.settings_skip_gating),
            description = "Unlock all 12 phases immediately without sequential gating.",
            checked = p.skipGating,
            onCheckedChange = vm::setSkipGating
        )
    }

    SectionHeader(title = stringResource(R.string.settings_about), eyebrow = "ABOUT")
    KadiCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconBubble(icon = Icons.Outlined.Update, tint = successColor())
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = stringResource(R.string.settings_content_version),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Bundled offline curriculum, downloaded asset workflows & Godot 4.3 docs",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Pill(text = state.contentVersion, color = successColor())
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconBubble(icon = Icons.Outlined.Info, tint = MaterialTheme.colorScheme.secondary)
            Text(
                text = stringResource(R.string.settings_about_text),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ToggleRow(
    icon: ImageVector,
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val accent = if (checked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        IconBubble(icon = icon, tint = accent, size = 34.dp)
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun ChoiceRow(
    icon: ImageVector,
    title: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit,
    iconTint: Color? = null
) {
    val accent = iconTint ?: if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.small,
        color = if (selected) accent.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceContainerLow,
        border = BorderStroke(
            width = if (selected) 1.5.dp else 1.dp,
            color = if (selected) accent else MaterialTheme.colorScheme.outlineVariant
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(11.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconBubble(icon = icon, tint = accent, size = 32.dp)
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            RadioButton(selected = selected, onClick = onClick)
        }
    }
}

@Composable
private fun trackLabel(track: LearnerTrack): String = when (track) {
    LearnerTrack.FOUNDATIONS -> stringResource(R.string.track_foundations)
    LearnerTrack.EXPERIENCED_DEV -> stringResource(R.string.track_experienced)
    LearnerTrack.GODOT_REFERENCE -> stringResource(R.string.track_reference)
}

@Composable
private fun trackDescription(track: LearnerTrack): String = when (track) {
    LearnerTrack.FOUNDATIONS -> stringResource(R.string.track_foundations_desc)
    LearnerTrack.EXPERIENCED_DEV -> stringResource(R.string.track_experienced_desc)
    LearnerTrack.GODOT_REFERENCE -> stringResource(R.string.track_reference_desc)
}
