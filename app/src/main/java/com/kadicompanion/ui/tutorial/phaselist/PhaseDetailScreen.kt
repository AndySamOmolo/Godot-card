package com.kadicompanion.ui.tutorial.phaselist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.kadicompanion.ui.common.SectionHeader
import com.kadicompanion.ui.theme.heroTextPrimary
import com.kadicompanion.ui.theme.heroTextSecondary
import com.kadicompanion.ui.theme.heroTextTertiary
import com.kadicompanion.ui.theme.successColor

/**
 * Inside a phase: a hero recapping the outcome and estimated time, then every
 * lesson as a tappable row with a numbered step indicator.
 */
@Composable
fun PhaseDetailScreen(
    phaseId: String,
    onLessonClick: (String) -> Unit,
    onDebugChallenge: () -> Unit,
    onBack: () -> Unit,
    vm: PhaseDetailViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            KadiTopBar(
                title = state.phase?.title ?: phaseId,
                subtitle = state.phase?.let { "Phase ${it.order}" },
                onBack = onBack
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> LoadingSkeleton()
                state.phase == null -> EmptyState(title = "Phase not found", message = "Go back and pick another phase.")
                else -> {
                    val phase = state.phase!!
                    val lessons = state.lessons
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 28.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            PhaseHero(
                                summary = phase.summary,
                                minutes = phase.estimatedMinutes,
                                lessonCount = lessons.size,
                                completedCount = state.completedIds.size
                            )
                        }
                        item {
                            SectionHeader(
                                title = "Lessons",
                                eyebrow = "step by step",
                                trailing = {
                                    Pill(
                                        text = "${state.completedIds.size}/${lessons.size}",
                                        color = successColor()
                                    )
                                }
                            )
                        }
                        items(lessons, key = { it.id }) { lesson ->
                            val index = lessons.indexOf(lesson) + 1
                            val done = lesson.id in state.completedIds
                            LessonRow(
                                index = index,
                                title = lesson.title,
                                done = done,
                                onClick = { onLessonClick(lesson.id) }
                            )
                        }
                        if (phase.debugChallenge != null) {
                            item {
                                DebugChallengeRow(onClick = onDebugChallenge)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PhaseHero(summary: String, minutes: Int, lessonCount: Int, completedCount: Int) {
    val heroText = heroTextPrimary()
    val heroTextSub = heroTextSecondary()
    val heroTextMuted = heroTextTertiary()
    val fraction = if (lessonCount == 0) 0f else completedCount.toFloat() / lessonCount
    HeroPanel {
        Text(
            text = "WHAT YOU'LL BUILD",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = heroTextMuted
        )
        Text(
            text = summary,
            style = MaterialTheme.typography.bodyLarge,
            color = heroTextSub
        )
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(Icons.Outlined.Schedule, contentDescription = null, tint = heroText, modifier = Modifier.size(14.dp))
                Text("~$minutes min", style = MaterialTheme.typography.labelMedium, color = heroText)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(Icons.AutoMirrored.Outlined.MenuBook, contentDescription = null, tint = heroText, modifier = Modifier.size(14.dp))
                Text("$lessonCount lessons", style = MaterialTheme.typography.labelMedium, color = heroText)
            }
        }
        LinearProgressIndicator(
            progress = { fraction },
            modifier = Modifier.fillMaxWidth().height(7.dp),
            color = heroText,
            trackColor = heroText.copy(alpha = 0.25f),
            strokeCap = StrokeCap.Round
        )
    }
}

@Composable
private fun LessonRow(index: Int, title: String, done: Boolean, onClick: () -> Unit) {
    val success = successColor()
    KadiCard(
        onClick = onClick,
        accent = if (done) success else null
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            StepBadge(index = index, done = done)
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (done) {
                    Pill(text = "Completed", color = success, icon = Icons.Filled.Check)
                }
            }
            Icon(
                Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun StepBadge(index: Int, done: Boolean) {
    val success = successColor()
    val bg = if (done) success else MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)
    val fg = if (done) Color.White else MaterialTheme.colorScheme.primary
    Box(
        modifier = Modifier.size(38.dp).clip(CircleShape).background(bg),
        contentAlignment = Alignment.Center
    ) {
        if (done) {
            Icon(Icons.Filled.Check, contentDescription = "Done", tint = fg, modifier = Modifier.size(20.dp))
        } else {
            Text("$index", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = fg)
        }
    }
}

@Composable
private fun DebugChallengeRow(onClick: () -> Unit) {
    KadiCard(onClick = onClick, accent = MaterialTheme.colorScheme.tertiary) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            IconBubble(icon = Icons.Outlined.BugReport, tint = MaterialTheme.colorScheme.tertiary)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Debug challenge",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Find and fix the bug",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Pill(text = "Try it", color = MaterialTheme.colorScheme.tertiary, icon = Icons.Outlined.BugReport)
        }
    }
}
