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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kadicompanion.R
import com.kadicompanion.domain.usecase.PhaseWithProgress
import com.kadicompanion.ui.common.EmptyState
import com.kadicompanion.ui.common.HeroPanel
import com.kadicompanion.ui.common.KadiCard
import com.kadicompanion.ui.common.KadiTopBar
import com.kadicompanion.ui.common.LoadingSkeleton
import com.kadicompanion.ui.common.Pill
import com.kadicompanion.ui.theme.heroTextPrimary
import com.kadicompanion.ui.theme.heroTextSecondary
import com.kadicompanion.ui.theme.heroTextTertiary
import com.kadicompanion.ui.theme.successColor

/**
 * The course map. A hero summarises the whole journey, then each phase is a
 * card whose badge tells you at a glance whether it is locked, running or done.
 * A vertical timeline connector links phases visually.
 */
@Composable
fun PhaseListScreen(onPhaseClick: (String) -> Unit, onOpenSettings: () -> Unit, vm: PhaseListViewModel = hiltViewModel()) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            KadiTopBar(
                title = stringResource(R.string.nav_tutorials),
                subtitle = "build kadi step by step",
                actions = {
                    IconButton(onClick = onOpenSettings) {
                        Icon(Icons.Outlined.Settings, contentDescription = stringResource(R.string.action_settings))
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> LoadingSkeleton(lines = 8)
                state.phases.isEmpty() -> EmptyState(
                    title = "No tutorial phases yet",
                    message = "Content will appear here once it's bundled."
                )
                else -> {
                    val totalLessons = state.phases.sumOf { it.totalLessons }
                    val doneLessons = state.phases.sumOf { it.completedLessons }
                    val totalMinutes = state.phases.sumOf { it.phase.estimatedMinutes }
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 28.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            CourseHero(
                                phaseCount = state.phases.size,
                                doneLessons = doneLessons,
                                totalLessons = totalLessons,
                                totalMinutes = totalMinutes
                            )
                        }
                        items(state.phases, key = { it.phase.id }) { phase ->
                            PhaseCard(phase = phase, onClick = { if (!phase.isLocked) onPhaseClick(phase.phase.id) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CourseHero(phaseCount: Int, doneLessons: Int, totalLessons: Int, totalMinutes: Int) {
    val fraction = if (totalLessons == 0) 0f else doneLessons.toFloat() / totalLessons
    val heroText = heroTextPrimary()
    val heroTextSub = heroTextSecondary()
    val heroTextMuted = heroTextTertiary()
    HeroPanel {
        Text(
            text = "THE FULL COURSE",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = heroTextMuted
        )
        Text(
            text = "$phaseCount phases · $totalLessons lessons",
            style = MaterialTheme.typography.headlineMedium,
            color = heroText
        )
        Text(
            text = "From installing Godot to exporting your finished Kadi game — about $totalMinutes minutes of guided building.",
            style = MaterialTheme.typography.bodyMedium,
            color = heroTextSub
        )
        LinearProgressIndicator(
            progress = { fraction },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .padding(top = 4.dp),
            color = heroText,
            trackColor = heroText.copy(alpha = 0.25f),
            strokeCap = StrokeCap.Round
        )
        Text(
            text = "$doneLessons of $totalLessons lessons complete",
            style = MaterialTheme.typography.labelSmall,
            color = heroTextSub
        )
    }
}

@Composable
private fun PhaseCard(phase: PhaseWithProgress, onClick: () -> Unit) {
    val isComplete = phase.totalLessons > 0 && phase.completedLessons >= phase.totalLessons
    val inProgress = !phase.isLocked && !isComplete && phase.completedLessons > 0
    val accent = when {
        phase.isLocked -> null
        isComplete -> successColor()
        inProgress -> MaterialTheme.colorScheme.primary
        else -> null
    }
    KadiCard(
        modifier = if (phase.isLocked) Modifier.alpha(0.6f) else Modifier,
        onClick = onClick,
        enabled = !phase.isLocked,
        accent = accent
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            PhaseBadge(order = phase.phase.order, isLocked = phase.isLocked, isComplete = isComplete)
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "PHASE ${phase.phase.order}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    if (phase.isOptionalForTrack) {
                        Pill(
                            text = stringResource(R.string.phase_optional),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    if (isComplete) Pill(text = "Done", color = successColor(), icon = Icons.Filled.Check)
                }
                Text(
                    text = phase.phase.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            if (phase.isLocked) {
                Icon(Icons.Outlined.Lock, contentDescription = "Locked", tint = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                Icon(
                    Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Text(
            text = phase.phase.summary,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MetaChip(icon = Icons.Outlined.Schedule, text = "~${phase.phase.estimatedMinutes} min")
            MetaChip(icon = Icons.AutoMirrored.Outlined.MenuBook, text = "${phase.totalLessons} lessons")
        }
        if (phase.totalLessons > 0) {
            LinearProgressIndicator(
                progress = { phase.completedLessons.toFloat() / phase.totalLessons },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(7.dp)
                    .clip(RoundedCornerShape(50)),
                color = if (isComplete) successColor() else MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.10f),
                strokeCap = StrokeCap.Round
            )
            Text(
                text = "${phase.completedLessons}/${phase.totalLessons} lessons complete",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (phase.isLocked) {
            Text(
                text = stringResource(R.string.phase_locked),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun MetaChip(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(14.dp))
        Text(text, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun PhaseBadge(order: Int, isLocked: Boolean, isComplete: Boolean) {
    val success = successColor()
    val bg = when {
        isLocked -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
        isComplete -> success
        else -> MaterialTheme.colorScheme.primary.copy(alpha = 0.16f)
    }
    val fg = when {
        isLocked -> MaterialTheme.colorScheme.onSurfaceVariant
        isComplete -> Color.White
        else -> MaterialTheme.colorScheme.primary
    }
    Box(
        modifier = Modifier.size(46.dp).clip(CircleShape).background(bg),
        contentAlignment = Alignment.Center
    ) {
        if (isComplete) {
            Icon(Icons.Filled.Check, contentDescription = "Complete", tint = fg, modifier = Modifier.size(24.dp))
        } else {
            Text("$order", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = fg)
        }
    }
}
