package com.kadicompanion.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Collections
import androidx.compose.material.icons.outlined.DashboardCustomize
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kadicompanion.R
import com.kadicompanion.data.local.datastore.HomeHeroLayout
import com.kadicompanion.data.local.datastore.HomeHeroPalette
import com.kadicompanion.data.local.datastore.TopBarStyle
import com.kadicompanion.data.local.datastore.UiDensity
import com.kadicompanion.domain.model.ReviewReason
import com.kadicompanion.ui.common.IconBubble
import com.kadicompanion.ui.common.KadiCard
import com.kadicompanion.ui.common.Pill
import com.kadicompanion.ui.common.ProgressRing
import com.kadicompanion.ui.common.RichInlineText
import com.kadicompanion.ui.common.SectionHeader
import com.kadicompanion.ui.common.StatTile
import com.kadicompanion.ui.common.cleanPlainText
import com.kadicompanion.ui.common.studioCardSpacing
import com.kadicompanion.ui.common.studioScreenPadding
import com.kadicompanion.ui.theme.CodeFontFamily
import com.kadicompanion.ui.theme.LocalOpenQuickCustomizer
import com.kadicompanion.ui.theme.LocalUiPreferences
import com.kadicompanion.ui.theme.brandGradient
import com.kadicompanion.ui.theme.successColor

private val DAILY_PRO_TIPS = listOf(
    "Using **downloaded card PNGs**? Put them in `res://assets/cards/` and use `TextureRect` (`Expand Mode = Ignore Size`, `Stretch Mode = Keep Aspect Centered`) so any downloaded deck scales cleanly!",
    "In Godot 4.3, press **F5** to run the Main Scene (`table.tscn`) and **F6** to test only the currently open scene (`card_scene.tscn`) in isolation.",
    "Always call `add_child(card_node)` BEFORE calling `card_node.setup(data)` so `@onready` child nodes (`CardArt`, `TopRankLabel`) are initialized first.",
    "In Kenyan Kadi, **2, 3, 8, J, Q, K, Ace, and Joker** cannot start the discard pile — only **4, 5, 6, 7, 9, and 10** are valid openers.",
    "When importing pixel-art or crisp 2D card packs in Godot 4.3, set **Project Settings -> Rendering -> Textures -> Default Texture Filter** to **Nearest** (for pixel art) or **Linear** (for HD vector/PNG cards).",
    "In Kadi, playing a Question Card (**Q** or **8**) requires accompanying it in the same move with a matching-suit Answer Card (or drawing from the pile)."
)

@Composable
fun HomeScreen(
    onContinueLesson: (String) -> Unit,
    onReview: (String) -> Unit,
    onOpenSettings: () -> Unit,
    onOpenInsights: () -> Unit,
    onOpenPhases: () -> Unit,
    onOpenBlueprint: () -> Unit = {},
    onOpenPhase: (String) -> Unit = {},
    vm: HomeViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    val uiPrefs = LocalUiPreferences.current
    val hPad = studioScreenPadding()
    val vGap = studioCardSpacing()
    var showHeroCustomizer by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        StudioHomeTopBar(
            customGreeting = uiPrefs.customGreeting,
            customSubtitle = uiPrefs.customSubtitle,
            topBarStyle = uiPrefs.topBarStyle,
            onOpenHeroCustomizer = { showHeroCustomizer = true },
            onOpenInsights = onOpenInsights,
            onOpenSettings = onOpenSettings
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = hPad, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(vGap)
        ) {
            if (uiPrefs.showHomeHeroCard) {
                ActiveMissionHero(
                    lessonTitle = state.continueLessonTitle,
                    phaseTitle = state.continuePhaseTitle,
                    lessonOrder = state.continueLessonOrder,
                    overallFraction = state.overallFraction,
                    overallCompleted = state.overallCompleted,
                    overallTotal = state.overallTotal,
                    reviewsDueCount = state.reviewQueue.size,
                    onContinue = { state.continueLessonId?.let(onContinueLesson) ?: onOpenPhases() },
                    onOpenRoadmap = onOpenPhases,
                    onCustomizeCard = { showHeroCustomizer = true }
                )
            } else {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showHeroCustomizer = true }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Outlined.Visibility,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Top Mission Card is hidden",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Pill(
                            text = "Show / Customize",
                            color = MaterialTheme.colorScheme.primary,
                            onClick = { vm.setShowHomeHeroCard(true) }
                        )
                    }
                }
            }

            if (uiPrefs.showHomeStats && uiPrefs.homeHeroLayout != HomeHeroLayout.STATS_COMBINED) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatTile(
                        modifier = Modifier.weight(1f),
                        value = "${state.overallCompleted}/${state.overallTotal}",
                        label = "Lessons",
                        accent = successColor(),
                        icon = Icons.Outlined.CheckCircle
                    )
                    StatTile(
                        modifier = Modifier.weight(1f),
                        value = "${(state.overallFraction * 100).toInt()}%",
                        label = "Mastery",
                        accent = MaterialTheme.colorScheme.primary,
                        icon = Icons.AutoMirrored.Outlined.TrendingUp
                    )
                    StatTile(
                        modifier = Modifier.weight(1f),
                        value = "${state.reviewQueue.size}",
                        label = "Reviews",
                        accent = MaterialTheme.colorScheme.tertiary,
                        icon = Icons.Outlined.Refresh
                    )
                }
            }

            if (uiPrefs.showHomeQuickLaunch) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BentoActionTile(
                        modifier = Modifier.weight(1f),
                        eyebrow = "ASSETS & MAP",
                        title = "Blueprint & Assets",
                        subtitle = "Downloaded card packs, res:// map & rule simulator",
                        icon = Icons.Outlined.Collections,
                        accent = MaterialTheme.colorScheme.secondary,
                        onClick = onOpenBlueprint
                    )
                    BentoActionTile(
                        modifier = Modifier.weight(1f),
                        eyebrow = "CURRICULUM",
                        title = "12-Phase Roadmap",
                        subtitle = "${state.overallCompleted} of ${state.overallTotal} lessons done",
                        icon = Icons.AutoMirrored.Outlined.MenuBook,
                        accent = MaterialTheme.colorScheme.primary,
                        onClick = onOpenPhases
                    )
                }
            }

            if (uiPrefs.showHomePhaseStrip && state.phasesMini.isNotEmpty()) {
                SectionHeader(
                    title = "Course Modules",
                    eyebrow = "PHASES 00 – 11",
                    trailing = {
                        Pill(
                            text = "All Phases",
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable(onClick = onOpenPhases)
                        )
                    }
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    state.phasesMini.forEach { mini ->
                        PhaseMiniTile(
                            mini = mini,
                            onClick = { onOpenPhase(mini.id) }
                        )
                    }
                }
            }

            if (uiPrefs.showDailyTip) {
                DailyProTipCard()
            }

            if (uiPrefs.showReviewQueue) {
                SectionHeader(
                    title = stringResource(R.string.home_review_card_title),
                    eyebrow = "SPACED REPETITION"
                )
                if (state.reviewQueue.isEmpty()) {
                    KadiCard {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            IconBubble(icon = Icons.Outlined.CheckCircle, tint = successColor(), size = 34.dp)
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Review queue clear",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = stringResource(R.string.home_review_card_empty),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                } else {
                    state.reviewQueue.forEach { item ->
                        ReviewCard(
                            concept = item.concept,
                            reason = item.reasonSurfaced,
                            onClick = { onReview(item.sourceLessonId) }
                        )
                    }
                }
            }
            Spacer(Modifier.height(14.dp))
        }
    }

    if (showHeroCustomizer) {
        HomeHeroCustomizerSheet(
            vm = vm,
            onDismiss = { showHeroCustomizer = false },
            onOpenAllSettings = {
                showHeroCustomizer = false
                onOpenSettings()
            }
        )
    }
}

@Composable
private fun StudioHomeTopBar(
    customGreeting: String,
    customSubtitle: String,
    topBarStyle: TopBarStyle,
    onOpenHeroCustomizer: () -> Unit,
    onOpenInsights: () -> Unit,
    onOpenSettings: () -> Unit
) {
    val openCustomizer = LocalOpenQuickCustomizer.current
    val hPad = studioScreenPadding()
    val barHeight = when (topBarStyle) {
        TopBarStyle.ULTRA_SLIM -> 36.dp
        TopBarStyle.COMPACT -> 42.dp
        TopBarStyle.STANDARD -> 50.dp
    }
    val btnSize = when (topBarStyle) {
        TopBarStyle.ULTRA_SLIM -> 28.dp
        TopBarStyle.COMPACT -> 32.dp
        TopBarStyle.STANDARD -> 36.dp
    }
    val iconSize = when (topBarStyle) {
        TopBarStyle.ULTRA_SLIM -> 15.dp
        TopBarStyle.COMPACT -> 17.dp
        TopBarStyle.STANDARD -> 19.dp
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(barHeight)
                .padding(horizontal = hPad),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                if (topBarStyle != TopBarStyle.ULTRA_SLIM) {
                    Text(
                        text = customSubtitle.ifBlank { "GODOT 4.3 · KADI STUDIO" }.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = CodeFontFamily,
                            fontSize = 9.5.sp,
                            lineHeight = 11.sp
                        ),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text = customGreeting.ifBlank { stringResource(R.string.home_greeting) },
                    style = when (topBarStyle) {
                        TopBarStyle.ULTRA_SLIM -> MaterialTheme.typography.titleSmall
                        TopBarStyle.COMPACT -> MaterialTheme.typography.titleSmall.copy(fontSize = 15.sp)
                        TopBarStyle.STANDARD -> MaterialTheme.typography.titleMedium
                    },
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Surface(
                shape = MaterialTheme.shapes.extraSmall,
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.14f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.35f))
            ) {
                IconButton(onClick = onOpenHeroCustomizer, modifier = Modifier.size(btnSize)) {
                    Icon(
                        Icons.Outlined.DashboardCustomize,
                        contentDescription = "Customize Top Card",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(iconSize)
                    )
                }
            }
            if (openCustomizer != null) {
                Surface(
                    shape = MaterialTheme.shapes.extraSmall,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.14f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f))
                ) {
                    IconButton(onClick = openCustomizer, modifier = Modifier.size(btnSize)) {
                        Icon(
                            Icons.Outlined.Tune,
                            contentDescription = "Customize UI",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(iconSize)
                        )
                    }
                }
            }
            Surface(
                shape = MaterialTheme.shapes.extraSmall,
                color = MaterialTheme.colorScheme.surfaceContainer,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                IconButton(onClick = onOpenInsights, modifier = Modifier.size(btnSize)) {
                    Icon(
                        Icons.Outlined.Insights,
                        contentDescription = stringResource(R.string.action_insights),
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(iconSize)
                    )
                }
            }
            Surface(
                shape = MaterialTheme.shapes.extraSmall,
                color = MaterialTheme.colorScheme.surfaceContainer,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                IconButton(onClick = onOpenSettings, modifier = Modifier.size(btnSize)) {
                    Icon(
                        Icons.Outlined.Settings,
                        contentDescription = stringResource(R.string.action_settings),
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(iconSize)
                    )
                }
            }
        }
        HorizontalDivider(
            thickness = 0.75.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f)
        )
    }
}

@Composable
private fun ActiveMissionHero(
    lessonTitle: String?,
    phaseTitle: String?,
    lessonOrder: Int,
    overallFraction: Float,
    overallCompleted: Int,
    overallTotal: Int,
    reviewsDueCount: Int,
    onContinue: () -> Unit,
    onOpenRoadmap: () -> Unit,
    onCustomizeCard: () -> Unit
) {
    val uiPrefs = LocalUiPreferences.current
    val layout = uiPrefs.homeHeroLayout
    val palette = uiPrefs.homeHeroPalette
    val isSurfaceMode = palette == HomeHeroPalette.SURFACE_CARD

    val defaultBadge = if (lessonTitle != null) "UP NEXT · LESSON $lessonOrder" else "START COURSE"
    val defaultTitle = lessonTitle?.let(::cleanPlainText) ?: "Build Kadi in Godot 4.3"
    val defaultSubtitle = phaseTitle?.let(::cleanPlainText) ?: "$overallCompleted of $overallTotal guided lessons completed"
    val defaultPrimaryBtn = if (overallCompleted == 0) "Start Lesson 1" else "Continue Lesson"
    val defaultSecondaryBtn = "Curriculum"

    val displayBadge = uiPrefs.customHeroBadge.trim().ifEmpty { defaultBadge }
    val displayTitle = uiPrefs.customHeroTitle.trim().ifEmpty { defaultTitle }
    val displaySubtitle = uiPrefs.customHeroSubtitle.trim().ifEmpty { defaultSubtitle }
    val displayPrimaryBtn = uiPrefs.customHeroPrimaryBtn.trim().ifEmpty { defaultPrimaryBtn }
    val displaySecondaryBtn = uiPrefs.customHeroSecondaryBtn.trim().ifEmpty { defaultSecondaryBtn }

    val heroAccent = when (palette) {
        HomeHeroPalette.FOLLOW_ACCENT -> MaterialTheme.colorScheme.primary
        HomeHeroPalette.GODOT_SAPPHIRE -> Color(0xFF38BDF8)
        HomeHeroPalette.EMERALD_TABLE -> Color(0xFF34D399)
        HomeHeroPalette.NAIROBI_GOLD -> Color(0xFFFBBF24)
        HomeHeroPalette.CYBER_PURPLE -> Color(0xFFC084FC)
        HomeHeroPalette.CRIMSON_ROYAL -> Color(0xFFFB7185)
        HomeHeroPalette.OBSIDIAN_DARK -> Color(0xFF94A3B8)
        HomeHeroPalette.SURFACE_CARD -> MaterialTheme.colorScheme.primary
    }

    val heroBrush = when (palette) {
        HomeHeroPalette.FOLLOW_ACCENT -> brandGradient()
        HomeHeroPalette.GODOT_SAPPHIRE -> Brush.linearGradient(
            listOf(Color(0xFF0A1628), Color(0xFF0F2D59), Color(0xFF0369A1))
        )
        HomeHeroPalette.EMERALD_TABLE -> Brush.linearGradient(
            listOf(Color(0xFF052018), Color(0xFF064E3B), Color(0xFF047857))
        )
        HomeHeroPalette.NAIROBI_GOLD -> Brush.linearGradient(
            listOf(Color(0xFF231404), Color(0xFF78350F), Color(0xFFB45309))
        )
        HomeHeroPalette.CYBER_PURPLE -> Brush.linearGradient(
            listOf(Color(0xFF180C2E), Color(0xFF4C1D95), Color(0xFF6D28D9))
        )
        HomeHeroPalette.CRIMSON_ROYAL -> Brush.linearGradient(
            listOf(Color(0xFF280811), Color(0xFF881337), Color(0xFFBE123C))
        )
        HomeHeroPalette.OBSIDIAN_DARK -> Brush.linearGradient(
            listOf(Color(0xFF090D16), Color(0xFF111827), Color(0xFF1E293B))
        )
        HomeHeroPalette.SURFACE_CARD -> Brush.linearGradient(
            listOf(
                MaterialTheme.colorScheme.surfaceContainerHigh,
                MaterialTheme.colorScheme.surfaceContainer
            )
        )
    }

    val titleColor = if (isSurfaceMode) MaterialTheme.colorScheme.onSurface else Color(0xFFF8FAFC)
    val subColor = if (isSurfaceMode) MaterialTheme.colorScheme.onSurfaceVariant else Color(0xFFE2E8F0)
    val mutedColor = if (isSurfaceMode) MaterialTheme.colorScheme.onSurfaceVariant else Color(0xFFCBD5E1)
    val borderColor = if (isSurfaceMode) {
        MaterialTheme.colorScheme.outlineVariant
    } else {
        heroAccent.copy(alpha = 0.35f)
    }
    val borderStrokeWidth = if (uiPrefs.cardBorderWidthDp <= 0f) 0.dp else uiPrefs.cardBorderWidthDp.dp
    val padValues = when {
        layout == HomeHeroLayout.MINIMAL_CLEAN || uiPrefs.uiDensity == UiDensity.COMPACT -> PaddingValues(13.dp)
        uiPrefs.uiDensity == UiDensity.SPACIOUS -> PaddingValues(18.dp)
        else -> PaddingValues(15.dp)
    }

    val cardModifier = Modifier
        .fillMaxWidth()
        .clip(MaterialTheme.shapes.large)
        .background(heroBrush)
        .let { mod ->
            if (borderStrokeWidth > 0.dp) mod.border(borderStrokeWidth, borderColor, MaterialTheme.shapes.large)
            else mod
        }
        .padding(padValues)

    Column(
        modifier = cardModifier,
        verticalArrangement = Arrangement.spacedBy(11.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (uiPrefs.showHeroBadge) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = heroAccent.copy(alpha = 0.20f),
                            border = BorderStroke(1.dp, heroAccent.copy(alpha = 0.45f))
                        ) {
                            Text(
                                text = displayBadge.uppercase(),
                                style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                                fontWeight = FontWeight.Bold,
                                color = if (isSurfaceMode) heroAccent else Color(0xFFF8FAFC),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    } else {
                        Spacer(Modifier.width(1.dp))
                    }

                    // Inline Customize Top Card button right on the Hero Card
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = titleColor.copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, titleColor.copy(alpha = 0.22f)),
                        modifier = Modifier.clickable(onClick = onCustomizeCard)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                Icons.Outlined.Edit,
                                contentDescription = "Customize Top Card",
                                tint = titleColor,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "Edit Card",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                fontWeight = FontWeight.SemiBold,
                                color = titleColor
                            )
                        }
                    }
                }

                Text(
                    text = displayTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = titleColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = displaySubtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = subColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (layout == HomeHeroLayout.FULL_WITH_RING && uiPrefs.showHeroProgressRing) {
                ProgressRing(
                    fraction = overallFraction,
                    size = 64.dp,
                    strokeWidth = 6.dp,
                    label = "done",
                    textColor = titleColor,
                    trackColor = titleColor.copy(alpha = 0.16f),
                    activeColor = heroAccent
                )
            }
        }

        if (layout == HomeHeroLayout.COMPACT_BAR_PROGRESS) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$overallCompleted / $overallTotal lessons complete",
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                        color = subColor
                    )
                    Text(
                        text = "${(overallFraction * 100).toInt()}%",
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                        fontWeight = FontWeight.Bold,
                        color = heroAccent
                    )
                }
                LinearProgressIndicator(
                    progress = { overallFraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(50)),
                    color = heroAccent,
                    trackColor = titleColor.copy(alpha = 0.16f),
                    strokeCap = StrokeCap.Round
                )
            }
        }

        if (layout == HomeHeroLayout.STATS_COMBINED) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                HeroMiniStatPill(
                    modifier = Modifier.weight(1f),
                    label = "Lessons",
                    value = "$overallCompleted/$overallTotal",
                    textColor = titleColor,
                    subColor = mutedColor
                )
                HeroMiniStatPill(
                    modifier = Modifier.weight(1f),
                    label = "Mastery",
                    value = "${(overallFraction * 100).toInt()}%",
                    textColor = titleColor,
                    subColor = mutedColor
                )
                HeroMiniStatPill(
                    modifier = Modifier.weight(1f),
                    label = "Reviews",
                    value = "$reviewsDueCount",
                    textColor = titleColor,
                    subColor = mutedColor
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onContinue,
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSurfaceMode) MaterialTheme.colorScheme.primary else Color.White,
                    contentColor = if (isSurfaceMode) MaterialTheme.colorScheme.onPrimary else Color(0xFF0F172A)
                ),
                modifier = Modifier.weight(1.3f).height(40.dp)
            ) {
                Icon(Icons.Outlined.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(5.dp))
                Text(
                    text = displayPrimaryBtn,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (uiPrefs.showHeroSecondaryBtn) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = titleColor.copy(alpha = 0.10f),
                    border = BorderStroke(1.dp, titleColor.copy(alpha = 0.24f)),
                    modifier = Modifier
                        .weight(0.9f)
                        .height(40.dp)
                        .clip(MaterialTheme.shapes.small)
                        .clickable(onClick = onOpenRoadmap)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = displaySecondaryBtn,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = mutedColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HeroMiniStatPill(
    label: String,
    value: String,
    textColor: Color,
    subColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = textColor.copy(alpha = 0.10f),
        border = BorderStroke(1.dp, textColor.copy(alpha = 0.18f))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall.copy(fontFamily = CodeFontFamily),
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = subColor
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeHeroCustomizerSheet(
    vm: HomeViewModel,
    onDismiss: () -> Unit,
    onOpenAllSettings: () -> Unit
) {
    val p = LocalUiPreferences.current
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
                .padding(horizontal = 16.dp, vertical = 8.dp),
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
                    IconBubble(icon = Icons.Outlined.DashboardCustomize, size = 34.dp)
                    Column {
                        Text(
                            text = "TOP HERO CARD STUDIO",
                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Customize Home Top Card",
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Show Top Hero Card on Home", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                Switch(checked = p.showHomeHeroCard, onCheckedChange = vm::setShowHomeHeroCard)
            }

            Text(
                text = "HERO CARD LAYOUT",
                style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HomeHeroLayout.entries.forEach { layout ->
                    val selected = p.homeHeroLayout == layout
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.18f) else MaterialTheme.colorScheme.surfaceContainer,
                        border = BorderStroke(if (selected) 1.5.dp else 1.dp, if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier.clickable { vm.setHomeHeroLayout(layout) }
                    ) {
                        Text(
                            text = layout.label,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 11.dp, vertical = 7.dp)
                        )
                    }
                }
            }

            Text(
                text = "HERO CARD COLOR PALETTE",
                style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HomeHeroPalette.entries.forEach { palette ->
                    val selected = p.homeHeroPalette == palette
                    val swatch = heroPaletteSwatch(palette)
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = if (selected) swatch.copy(alpha = 0.18f) else MaterialTheme.colorScheme.surfaceContainer,
                        border = BorderStroke(if (selected) 1.5.dp else 1.dp, if (selected) swatch else MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier.clickable { vm.setHomeHeroPalette(palette) }
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
                                text = palette.label,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                color = if (selected) swatch else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            Text(
                text = "CARD ELEMENTS VISIBILITY",
                style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Show Top Status Badge", style = MaterialTheme.typography.bodySmall)
                Switch(checked = p.showHeroBadge, onCheckedChange = vm::setShowHeroBadge)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Show Progress Ring (in Ring Layout)", style = MaterialTheme.typography.bodySmall)
                Switch(checked = p.showHeroProgressRing, onCheckedChange = vm::setShowHeroProgressRing)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Show Secondary Button (Curriculum)", style = MaterialTheme.typography.bodySmall)
                Switch(checked = p.showHeroSecondaryBtn, onCheckedChange = vm::setShowHeroSecondaryBtn)
            }

            Text(
                text = "CUSTOM TEXT OVERRIDES (LEAVE BLANK FOR DYNAMIC LESSON INFO)",
                style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = p.customHeroBadge,
                onValueChange = vm::setCustomHeroBadge,
                label = { Text("Custom Badge (e.g. KADI PRO STUDIO)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = p.customHeroTitle,
                onValueChange = vm::setCustomHeroTitle,
                label = { Text("Custom Title (overrides current lesson title)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = p.customHeroSubtitle,
                onValueChange = vm::setCustomHeroSubtitle,
                label = { Text("Custom Subtitle (overrides phase name)") },
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
                    label = { Text("Primary Button") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = p.customHeroSecondaryBtn,
                    onValueChange = vm::setCustomHeroSecondaryBtn,
                    label = { Text("Secondary Button") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FilledTonalButton(
                    onClick = {
                        vm.setShowHomeHeroCard(true)
                        vm.setHomeHeroLayout(HomeHeroLayout.FULL_WITH_RING)
                        vm.setHomeHeroPalette(HomeHeroPalette.FOLLOW_ACCENT)
                        vm.setShowHeroBadge(true)
                        vm.setShowHeroProgressRing(true)
                        vm.setShowHeroSecondaryBtn(true)
                        vm.setCustomHeroBadge("")
                        vm.setCustomHeroTitle("")
                        vm.setCustomHeroSubtitle("")
                        vm.setCustomHeroPrimaryBtn("")
                        vm.setCustomHeroSecondaryBtn("")
                    },
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.weight(1f).height(42.dp)
                ) {
                    Text("Reset Card", fontWeight = FontWeight.SemiBold)
                }
                Button(
                    onClick = onOpenAllSettings,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.weight(1.1f).height(42.dp)
                ) {
                    Text("All UI Settings", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

internal fun heroPaletteSwatch(palette: HomeHeroPalette): Color = when (palette) {
    HomeHeroPalette.FOLLOW_ACCENT -> Color(0xFF38BDF8)
    HomeHeroPalette.GODOT_SAPPHIRE -> Color(0xFF0284C7)
    HomeHeroPalette.EMERALD_TABLE -> Color(0xFF10B981)
    HomeHeroPalette.NAIROBI_GOLD -> Color(0xFFF59E0B)
    HomeHeroPalette.CYBER_PURPLE -> Color(0xFF8B5CF6)
    HomeHeroPalette.CRIMSON_ROYAL -> Color(0xFFF43F5E)
    HomeHeroPalette.OBSIDIAN_DARK -> Color(0xFF64748B)
    HomeHeroPalette.SURFACE_CARD -> Color(0xFF94A3B8)
}

@Composable
private fun BentoActionTile(
    eyebrow: String,
    title: String,
    subtitle: String,
    icon: ImageVector,
    accent: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(accent.copy(alpha = 0.14f))
                        .border(1.dp, accent.copy(alpha = 0.28f), MaterialTheme.shapes.small),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(16.dp))
                }
                Icon(
                    Icons.AutoMirrored.Outlined.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(15.dp)
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = eyebrow,
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily, fontSize = 10.sp),
                    color = accent,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun PhaseMiniTile(mini: HomePhaseMini, onClick: () -> Unit) {
    val isDone = mini.total > 0 && mini.completed >= mini.total
    val isStarted = mini.completed > 0 && !isDone
    val accent = when {
        isDone -> successColor()
        isStarted -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outlineVariant
    }
    Surface(
        modifier = Modifier
            .width(152.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer,
        border = BorderStroke(1.dp, if (isDone || isStarted) accent.copy(alpha = 0.55f) else MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(11.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = (if (isDone) successColor() else MaterialTheme.colorScheme.primary).copy(alpha = 0.14f)
                ) {
                    Text(
                        text = "P${mini.order.toString().padStart(2, '0')}",
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                        fontWeight = FontWeight.Bold,
                        color = if (isDone) successColor() else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Text(
                    text = "${mini.completed}/${mini.total}",
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = cleanPlainText(mini.title),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            LinearProgressIndicator(
                progress = { mini.fraction },
                modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(50)),
                color = if (isDone) successColor() else MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.10f),
                strokeCap = StrokeCap.Round
            )
        }
    }
}

@Composable
private fun DailyProTipCard() {
    var tipIndex by remember { mutableIntStateOf(0) }
    val tip = DAILY_PRO_TIPS[tipIndex % DAILY_PRO_TIPS.size]
    KadiCard(
        onClick = { tipIndex = (tipIndex + 1) % DAILY_PRO_TIPS.size },
        accent = MaterialTheme.colorScheme.tertiary
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IconBubble(icon = Icons.Outlined.Bolt, tint = MaterialTheme.colorScheme.tertiary, size = 32.dp)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "GODOT 4.3 & ASSETS TIP",
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            Pill(
                text = "${tipIndex + 1}/${DAILY_PRO_TIPS.size} · Next",
                color = MaterialTheme.colorScheme.tertiary
            )
        }
        RichInlineText(
            text = tip,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun ReviewCard(concept: String, reason: ReviewReason, onClick: () -> Unit) {
    val reasonText = when (reason) {
        ReviewReason.MISSED_QUIZ -> "Missed on a recent quiz"
        ReviewReason.TIME_ELAPSED -> "Scheduled for spaced review"
    }
    val accent = when (reason) {
        ReviewReason.MISSED_QUIZ -> MaterialTheme.colorScheme.tertiary
        ReviewReason.TIME_ELAPSED -> MaterialTheme.colorScheme.primary
    }
    KadiCard(onClick = onClick) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconBubble(icon = Icons.Outlined.Refresh, tint = accent, size = 34.dp)
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = cleanPlainText(concept),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = reasonText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Pill(text = "Review", color = accent)
        }
    }
}
