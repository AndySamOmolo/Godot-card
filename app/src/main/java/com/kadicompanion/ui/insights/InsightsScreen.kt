package com.kadicompanion.ui.insights

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kadicompanion.R
import com.kadicompanion.ui.common.EmptyState
import com.kadicompanion.ui.common.HeroPanel
import com.kadicompanion.ui.common.IconBubble
import com.kadicompanion.ui.common.KadiCard
import com.kadicompanion.ui.common.KadiTopBar
import com.kadicompanion.ui.common.LoadingSkeleton
import com.kadicompanion.ui.common.Pill
import com.kadicompanion.ui.common.ProgressRing
import com.kadicompanion.ui.common.SectionHeader
import com.kadicompanion.ui.common.StatTile
import com.kadicompanion.ui.theme.LocalReduceMotion
import com.kadicompanion.ui.theme.heroTextPrimary
import com.kadicompanion.ui.theme.heroTextSecondary
import com.kadicompanion.ui.theme.heroTextTertiary
import com.kadicompanion.ui.theme.successColor

/*
 * A small analytics dashboard: a gradient hero carrying the headline accuracy
 * ring, a row of metric tiles, then per-concept mastery as labelled bars that
 * are tinted by how well the concept is going.
 */

@Composable
fun InsightsScreen(onBack: () -> Unit, vm: InsightsViewModel = hiltViewModel()) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            KadiTopBar(
                title = stringResource(R.string.action_insights),
                subtitle = "Your progress",
                onBack = onBack
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> LoadingSkeleton()
                state.totalQuestions == 0 && state.debugTotal == 0 -> EmptyState(
                    title = "Nothing to show yet",
                    message = stringResource(R.string.empty_insights),
                    icon = Icons.Outlined.Insights
                )
                else -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 28.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    AccuracyHero(
                        fraction = state.accuracyFraction,
                        totalQuestions = state.totalQuestions
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        StatTile(
                            value = "${state.totalQuestions}",
                            label = stringResource(R.string.insights_attempted),
                            accent = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.weight(1f),
                            icon = Icons.Outlined.Quiz
                        )
                        StatTile(
                            value = "${(state.accuracyFraction * 100).toInt()}%",
                            label = stringResource(R.string.insights_accuracy),
                            accent = successColor(),
                            modifier = Modifier.weight(1f),
                            icon = Icons.AutoMirrored.Outlined.TrendingUp
                        )
                        StatTile(
                            value = "${state.debugTotal}",
                            label = stringResource(R.string.insights_debug),
                            accent = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.weight(1f),
                            icon = Icons.Outlined.BugReport
                        )
                    }

                    if (state.weakestConcepts.isNotEmpty()) {
                        SectionHeader(
                            title = stringResource(R.string.insights_weakest),
                            modifier = Modifier.padding(top = 6.dp),
                            eyebrow = "Mastery"
                        )
                        KadiCard {
                            state.weakestConcepts.forEachIndexed { index, entry ->
                                val (concept, missRate) = entry
                                ConceptMasteryRow(
                                    concept = concept,
                                    missRate = missRate,
                                    modifier = Modifier.padding(top = if (index == 0) 0.dp else 4.dp)
                                )
                            }
                        }
                    }

                    SectionHeader(
                        title = stringResource(R.string.insights_debug),
                        modifier = Modifier.padding(top = 6.dp),
                        eyebrow = "Practice"
                    )
                    KadiCard {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            IconBubble(icon = Icons.Outlined.BugReport, tint = MaterialTheme.colorScheme.tertiary)
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = "Challenges attempted",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${state.debugTotal} in total",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            IconBubble(icon = Icons.Outlined.Visibility, tint = MaterialTheme.colorScheme.error)
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = stringResource(R.string.insights_debug_reveal_rate),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${state.debugRevealedWithoutAttempt} revealed the fix without attempting",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AccuracyHero(fraction: Float, totalQuestions: Int) {
    val heroText = heroTextPrimary()
    val heroTextSub = heroTextSecondary()
    val heroTextMuted = heroTextTertiary()
    HeroPanel {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "This is how you are doing".uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = heroTextMuted,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.insights_accuracy),
                    style = MaterialTheme.typography.headlineMedium,
                    color = heroText
                )
                Text(
                    text = "$totalQuestions " + stringResource(R.string.insights_attempted).lowercase(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = heroTextSub
                )
            }
            ProgressRing(fraction = fraction, size = 96.dp, strokeWidth = 9.dp)
        }
    }
}

/** One concept, shown as mastery (the inverse of its miss rate) with a tinted bar. */
@Composable
private fun ConceptMasteryRow(concept: String, missRate: Float, modifier: Modifier = Modifier) {
    val mastery = (1f - missRate).coerceIn(0f, 1f)
    val tint = masteryColor(mastery)
    val reduceMotion = LocalReduceMotion.current
    val animated by animateFloatAsState(
        targetValue = mastery,
        animationSpec = if (reduceMotion) tween(durationMillis = 0) else tween(durationMillis = 650),
        label = "mastery"
    )
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = concept,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Pill(text = "${(mastery * 100).toInt()}%", color = tint)
        }
        LinearProgressIndicator(
            progress = { animated },
            modifier = Modifier.fillMaxWidth().height(9.dp),
            color = tint,
            trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.10f),
            strokeCap = StrokeCap.Round
        )
        Text(
            text = "${(missRate * 100).toInt()}% miss rate",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun masteryColor(mastery: Float): Color = when {
    mastery >= 0.75f -> successColor()
    mastery >= 0.45f -> MaterialTheme.colorScheme.primary
    else -> MaterialTheme.colorScheme.error
}
