package com.kadicompanion.ui.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.icons.outlined.AccountTree
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
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
import com.kadicompanion.ui.common.SectionHeader
import com.kadicompanion.ui.common.StatTile
import com.kadicompanion.ui.theme.LocalReduceMotion
import com.kadicompanion.ui.theme.heroTextPrimary
import com.kadicompanion.ui.theme.heroTextSecondary
import com.kadicompanion.ui.theme.heroTextTertiary
import com.kadicompanion.ui.theme.successColor

private val DAILY_PRO_TIPS = listOf(
    "In Godot 4.3, press F5 to run the Main Scene (`table.tscn`) and F6 to test only the currently open scene (`card_scene.tscn`) in isolation!",
    "Always call `add_child(card_node)` BEFORE calling `card_node.setup(data)` so `@onready` child labels are initialized first.",
    "In Kenyan Kadi, 2, 3, 8, J, Q, K, Ace, and Joker cannot start the discard pile — only 4, 5, 6, 7, 9, and 10 are valid openers!",
    "When registering `GameState` as an Autoload in Project Settings, omit `class_name GameState` inside `game_state.gd` to avoid a name collision.",
    "Full-screen flash overlays (`FlashRect: ColorRect`) must set `Mouse -> Filter = Ignore` so they don't block clicks on cards underneath!",
    "In Kadi, playing a Question Card (Q or 8) requires accompanying it in the same move with a matching-suit Answer Card (or drawing from the pile)."
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
    val reduceMotion = LocalReduceMotion.current

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }
    val slideOffset by animateDpAsState(
        targetValue = if (visible) 0.dp else 24.dp,
        animationSpec = if (reduceMotion) tween(0) else tween(400),
        label = "homeSlide"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .offset(y = slideOffset)
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        GreetingHeader(onOpenInsights = onOpenInsights, onOpenSettings = onOpenSettings)
        ContinueHero(
            lessonTitle = state.continueLessonTitle,
            phaseTitle = state.continuePhaseTitle,
            lessonOrder = state.continueLessonOrder,
            onClick = { state.continueLessonId?.let(onContinueLesson) ?: onOpenPhases() }
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatTile(
                modifier = Modifier.weight(1f),
                value = "${state.overallCompleted}/${state.overallTotal}",
                label = "Lessons done",
                accent = successColor(),
                icon = Icons.Outlined.CheckCircle
            )
            StatTile(
                modifier = Modifier.weight(1f),
                value = "${(state.overallFraction * 100).toInt()}%",
                label = "Course complete",
                accent = MaterialTheme.colorScheme.primary,
                icon = Icons.AutoMirrored.Outlined.TrendingUp
            )
            StatTile(
                modifier = Modifier.weight(1f),
                value = "${state.reviewQueue.size}",
                label = "To review",
                accent = MaterialTheme.colorScheme.secondary,
                icon = Icons.Outlined.Refresh
            )
        }

        // Interactive Phase Jump Strip
        if (state.phasesMini.isNotEmpty()) {
            SectionHeader(
                title = "Jump to a Phase",
                eyebrow = "12-phase roadmap",
                trailing = {
                    Pill(
                        text = "View all",
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

        // Interactive Blueprint & Kadi Rule Simulator Card
        KadiCard(onClick = onOpenBlueprint, accent = MaterialTheme.colorScheme.secondary) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                IconBubble(icon = Icons.Outlined.AccountTree, tint = MaterialTheme.colorScheme.secondary, size = 42.dp)
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "GODOT ARCHITECTURE & RULE SANDBOX",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = "Project Blueprint & Kadi Rule Simulator",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Inspect the complete `res://` file map, scene trees, and test Kadi plays live.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(
                    Icons.AutoMirrored.Outlined.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        KadiCard(onClick = onOpenPhases) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(R.string.home_overall_progress),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${state.overallCompleted} of ${state.overallTotal} guided lessons completed",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    LinearProgressIndicator(
                        progress = { state.overallFraction },
                        modifier = Modifier.fillMaxWidth().height(7.dp),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.10f),
                        strokeCap = StrokeCap.Round
                    )
                    Pill(text = "Open Full Course Map", color = MaterialTheme.colorScheme.primary, icon = Icons.AutoMirrored.Outlined.MenuBook)
                }
                ProgressRing(fraction = state.overallFraction, size = 92.dp, label = "done")
            }
        }

        DailyProTipCard()

        SectionHeader(
            title = stringResource(R.string.home_review_card_title),
            eyebrow = "spaced repetition"
        )
        if (state.reviewQueue.isEmpty()) {
            KadiCard {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    IconBubble(icon = Icons.Outlined.CheckCircle, tint = successColor(), size = 36.dp)
                    Text(
                        text = stringResource(R.string.home_review_card_empty),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            state.reviewQueue.forEach { item ->
                ReviewCard(concept = item.concept, reason = item.reasonSurfaced, onClick = { onReview(item.sourceLessonId) })
            }
        }
        Spacer(Modifier.height(28.dp))
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
            .width(156.dp)
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        border = BorderStroke(1.dp, accent.copy(alpha = if (isDone || isStarted) 0.5f else 0.35f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "PHASE ${mini.order}",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isDone) successColor() else MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${mini.completed}/${mini.total}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = mini.title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            LinearProgressIndicator(
                progress = { mini.fraction },
                modifier = Modifier.fillMaxWidth().height(5.dp).clip(RoundedCornerShape(50)),
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
        accent = MaterialTheme.colorScheme.primary
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IconBubble(icon = Icons.Outlined.Bolt, tint = MaterialTheme.colorScheme.primary, size = 34.dp)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "GODOT 4.3 & KADI PRO TIP",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Pill(text = "Tap for next (${tipIndex + 1}/${DAILY_PRO_TIPS.size})", color = MaterialTheme.colorScheme.primary)
        }
        Text(
            text = tip,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun GreetingHeader(onOpenInsights: () -> Unit, onOpenSettings: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "KADI COMPANION",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.home_greeting),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        IconButton(onClick = onOpenInsights) {
            Icon(
                Icons.Outlined.Insights,
                contentDescription = stringResource(R.string.action_insights),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        IconButton(onClick = onOpenSettings) {
            Icon(
                Icons.Outlined.Settings,
                contentDescription = stringResource(R.string.action_settings),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ContinueHero(
    lessonTitle: String?,
    phaseTitle: String?,
    lessonOrder: Int,
    onClick: () -> Unit
) {
    val heroText = heroTextPrimary()
    val heroTextSub = heroTextSecondary()
    val heroTextMuted = heroTextTertiary()
    HeroPanel(onClick = onClick, contentPadding = androidx.compose.foundation.layout.PaddingValues(22.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.home_continue_card_title).uppercase(),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = heroTextMuted
            )
            if (lessonTitle != null) {
                Surface(shape = CircleShape, color = heroText.copy(alpha = 0.20f)) {
                    Text(
                        text = "LESSON $lessonOrder",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = heroText,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }
        if (lessonTitle != null) {
            Text(
                text = lessonTitle,
                style = MaterialTheme.typography.headlineMedium,
                color = heroText
            )
            phaseTitle?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelLarge,
                    color = heroTextSub
                )
            }
        } else {
            Text(
                text = "Start building Kadi in Godot 4.3",
                style = MaterialTheme.typography.headlineSmall,
                color = heroText
            )
        }
        Spacer(Modifier.height(6.dp))
        Surface(shape = CircleShape, color = heroText.copy(alpha = 0.22f)) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    Icons.Outlined.PlayArrow,
                    contentDescription = null,
                    tint = heroText,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = stringResource(R.string.action_continue),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = heroText
                )
            }
        }
    }
}

@Composable
private fun ReviewCard(concept: String, reason: ReviewReason, onClick: () -> Unit) {
    val reasonText = when (reason) {
        ReviewReason.MISSED_QUIZ -> "Missed on a recent quiz"
        ReviewReason.TIME_ELAPSED -> "Not reviewed in a while"
    }
    val accent = when (reason) {
        ReviewReason.MISSED_QUIZ -> MaterialTheme.colorScheme.tertiary
        ReviewReason.TIME_ELAPSED -> MaterialTheme.colorScheme.secondary
    }
    KadiCard(onClick = onClick, accent = accent) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            IconBubble(icon = Icons.Outlined.Refresh, tint = accent)
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(concept, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                Text(reasonText, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Pill(text = "Review", color = accent)
        }
    }
}
