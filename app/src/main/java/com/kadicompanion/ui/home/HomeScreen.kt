package com.kadicompanion.ui.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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

/**
 * Home is a dashboard, not a list: one obvious next action in a gradient hero,
 * three glanceable metrics, an overall progress ring, and the review queue.
 */
@Composable
fun HomeScreen(
    onContinueLesson: (String) -> Unit,
    onReview: (String) -> Unit,
    onOpenSettings: () -> Unit,
    onOpenInsights: () -> Unit,
    onOpenPhases: () -> Unit,
    vm: HomeViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    val reduceMotion = LocalReduceMotion.current

    // Staggered entrance animation
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
            onClick = { state.continueLessonId?.let(onContinueLesson) ?: onOpenPhases() }
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatTile(
                modifier = Modifier.weight(1f),
                value = "${state.overallCompleted}",
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
        KadiCard {
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
                        text = "${state.overallCompleted} / ${state.overallTotal} lessons",
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
                    Pill(text = "Keep going", color = MaterialTheme.colorScheme.primary, icon = Icons.AutoMirrored.Outlined.MenuBook)
                }
                ProgressRing(fraction = state.overallFraction, size = 92.dp, label = "done")
            }
        }
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
private fun ContinueHero(lessonTitle: String?, phaseTitle: String?, onClick: () -> Unit) {
    val heroText = heroTextPrimary()
    val heroTextSub = heroTextSecondary()
    val heroTextMuted = heroTextTertiary()
    HeroPanel(onClick = onClick, contentPadding = androidx.compose.foundation.layout.PaddingValues(22.dp)) {
        Text(
            text = stringResource(R.string.home_continue_card_title).uppercase(),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = heroTextMuted
        )
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
                text = "Pick a phase to begin.",
                style = MaterialTheme.typography.headlineSmall,
                color = heroText
            )
        }
        Spacer(Modifier.height(6.dp))
        Surface(shape = CircleShape, color = heroText.copy(alpha = 0.20f)) {
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
