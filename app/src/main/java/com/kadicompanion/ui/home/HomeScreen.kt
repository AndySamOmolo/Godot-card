package com.kadicompanion.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.outlined.AccountTree
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kadicompanion.R
import com.kadicompanion.domain.model.ReviewReason
import com.kadicompanion.ui.common.HeroPanel
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
import com.kadicompanion.ui.theme.heroTextPrimary
import com.kadicompanion.ui.theme.heroTextSecondary
import com.kadicompanion.ui.theme.heroTextTertiary
import com.kadicompanion.ui.theme.successColor

private val DAILY_PRO_TIPS = listOf(
    "In Godot 4.3, press **F5** to run the Main Scene (`table.tscn`) and **F6** to test only the currently open scene (`card_scene.tscn`) in isolation.",
    "Always call `add_child(card_node)` BEFORE calling `card_node.setup(data)` so `@onready` child labels are initialized first.",
    "In Kenyan Kadi, **2, 3, 8, J, Q, K, Ace, and Joker** cannot start the discard pile — only **4, 5, 6, 7, 9, and 10** are valid openers.",
    "When registering `GameState` as an Autoload in Project Settings, omit `class_name GameState` inside `game_state.gd` to avoid a name collision.",
    "Full-screen flash overlays (`FlashRect: ColorRect`) must set `Mouse -> Filter = Ignore` so they don't block clicks on cards underneath.",
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        StudioHomeTopBar(
            customGreeting = uiPrefs.customGreeting,
            customSubtitle = uiPrefs.customSubtitle,
            onOpenInsights = onOpenInsights,
            onOpenSettings = onOpenSettings
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = hPad, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(vGap)
        ) {
            // Active Mission Studio Hero
            ActiveMissionHero(
                lessonTitle = state.continueLessonTitle,
                phaseTitle = state.continuePhaseTitle,
                lessonOrder = state.continueLessonOrder,
                overallFraction = state.overallFraction,
                overallCompleted = state.overallCompleted,
                overallTotal = state.overallTotal,
                onContinue = { state.continueLessonId?.let(onContinueLesson) ?: onOpenPhases() },
                onOpenRoadmap = onOpenPhases
            )

            // 3-Column Bento Metrics Strip (customizable visibility)
            if (uiPrefs.showHomeStats) {
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

            // 2-Column Studio Quick Launch Grid (customizable visibility)
            if (uiPrefs.showHomeQuickLaunch) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    BentoActionTile(
                        modifier = Modifier.weight(1f),
                        eyebrow = "ARCHITECTURE",
                        title = "Project Blueprint",
                        subtitle = "res:// file map & live Kadi rule tester",
                        icon = Icons.Outlined.AccountTree,
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

            // Horizontal Phase Stepper (customizable visibility)
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

            // Godot 4.3 & Kadi Tip Switcher (customizable visibility)
            if (uiPrefs.showDailyTip) {
                DailyProTipCard()
            }

            // Spaced Repetition Queue (customizable visibility)
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
                            IconBubble(icon = Icons.Outlined.CheckCircle, tint = successColor(), size = 36.dp)
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
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun StudioHomeTopBar(
    customGreeting: String,
    customSubtitle: String,
    onOpenInsights: () -> Unit,
    onOpenSettings: () -> Unit
) {
    val openCustomizer = LocalOpenQuickCustomizer.current
    val hPad = studioScreenPadding()

    Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = hPad, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = customSubtitle.ifBlank { "GODOT 4.3 · KADI STUDIO" }.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = customGreeting.ifBlank { stringResource(R.string.home_greeting) },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (openCustomizer != null) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.14f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f))
                ) {
                    IconButton(onClick = openCustomizer, modifier = Modifier.size(38.dp)) {
                        Icon(
                            Icons.Outlined.Tune,
                            contentDescription = "Customize UI",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }
            }
            Surface(
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.surfaceContainer,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                IconButton(onClick = onOpenInsights, modifier = Modifier.size(38.dp)) {
                    Icon(
                        Icons.Outlined.Insights,
                        contentDescription = stringResource(R.string.action_insights),
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }
            Surface(
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.surfaceContainer,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                IconButton(onClick = onOpenSettings, modifier = Modifier.size(38.dp)) {
                    Icon(
                        Icons.Outlined.Settings,
                        contentDescription = stringResource(R.string.action_settings),
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
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
    onContinue: () -> Unit,
    onOpenRoadmap: () -> Unit
) {
    val heroWhite = heroTextPrimary()
    val heroSub = heroTextSecondary()
    val heroMuted = heroTextTertiary()
    val cleanLessonTitle = lessonTitle?.let(::cleanPlainText) ?: "Build Kadi in Godot 4.3"
    val cleanPhaseTitle = phaseTitle?.let(::cleanPlainText) ?: "$overallCompleted of $overallTotal guided lessons completed"

    HeroPanel {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF38BDF8).copy(alpha = 0.22f),
                    border = BorderStroke(1.dp, Color(0xFF38BDF8).copy(alpha = 0.45f))
                ) {
                    Text(
                        text = if (lessonTitle != null) "UP NEXT · LESSON $lessonOrder" else "START COURSE",
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF7DD3FC),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
                Text(
                    text = cleanLessonTitle,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = heroWhite,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = cleanPhaseTitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = heroSub,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            ProgressRing(
                fraction = overallFraction,
                size = 72.dp,
                strokeWidth = 7.dp,
                label = "done",
                textColor = heroWhite,
                trackColor = heroWhite.copy(alpha = 0.16f),
                activeColor = Color(0xFF38BDF8)
            )
        }

        Spacer(Modifier.height(2.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onContinue,
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF0F172A)
                ),
                modifier = Modifier.weight(1.3f).height(44.dp)
            ) {
                Icon(Icons.Outlined.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text(
                    text = if (overallCompleted == 0) "Start Lesson 1" else "Continue Lesson",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }

            Surface(
                shape = MaterialTheme.shapes.small,
                color = heroWhite.copy(alpha = 0.10f),
                border = BorderStroke(1.dp, heroWhite.copy(alpha = 0.24f)),
                modifier = Modifier
                    .weight(0.9f)
                    .height(44.dp)
                    .clip(MaterialTheme.shapes.small)
                    .clickable(onClick = onOpenRoadmap)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "Curriculum",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = heroMuted,
                        maxLines = 1
                    )
                }
            }
        }
    }
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
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(accent.copy(alpha = 0.14f))
                        .border(1.dp, accent.copy(alpha = 0.28f), MaterialTheme.shapes.small),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(18.dp))
                }
                Icon(
                    Icons.AutoMirrored.Outlined.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = eyebrow,
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
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
            .width(158.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer,
        border = BorderStroke(1.dp, if (isDone || isStarted) accent.copy(alpha = 0.55f) else MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
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
            IconBubble(icon = Icons.Outlined.Bolt, tint = MaterialTheme.colorScheme.tertiary, size = 34.dp)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "GODOT 4.3 & KADI TIP",
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
            IconBubble(icon = Icons.Outlined.Refresh, tint = accent, size = 36.dp)
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
