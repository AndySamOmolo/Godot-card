package com.kadicompanion.ui.tutorial.phaselist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.AccountTree
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.kadicompanion.ui.common.IconBubble
import com.kadicompanion.ui.common.KadiCard
import com.kadicompanion.ui.common.KadiTopBar
import com.kadicompanion.ui.common.LoadingSkeleton
import com.kadicompanion.ui.common.Pill
import com.kadicompanion.ui.theme.heroTextPrimary
import com.kadicompanion.ui.theme.heroTextSecondary
import com.kadicompanion.ui.theme.heroTextTertiary
import com.kadicompanion.ui.theme.successColor

/**
 * The course map with live search & status filter chips (`All`, `In Progress`, `To Do`, `Completed`).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhaseListScreen(
    onPhaseClick: (String) -> Unit,
    onOpenSettings: () -> Unit,
    onOpenBlueprint: () -> Unit = {},
    vm: PhaseListViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedFilter by rememberSaveable { mutableIntStateOf(0) }
    val filters = listOf("All Phases", "In Progress", "To Do", "Completed")

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            KadiTopBar(
                title = stringResource(R.string.nav_tutorials),
                subtitle = "build kadi step by step",
                actions = {
                    IconButton(onClick = onOpenBlueprint) {
                        Icon(Icons.Outlined.AccountTree, contentDescription = "Project Blueprint")
                    }
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

                    val filteredPhases = state.phases.filter { p ->
                        val matchesFilter = when (selectedFilter) {
                            1 -> p.completedLessons in 1 until p.totalLessons
                            2 -> p.completedLessons == 0
                            3 -> p.totalLessons > 0 && p.completedLessons >= p.totalLessons
                            else -> true
                        }
                        val q = searchQuery.trim()
                        val matchesQuery = q.isEmpty() ||
                            p.phase.title.contains(q, ignoreCase = true) ||
                            p.phase.summary.contains(q, ignoreCase = true) ||
                            p.phase.concepts.any { it.contains(q, ignoreCase = true) } ||
                            p.phase.projectFiles.any { it.contains(q, ignoreCase = true) } ||
                            p.phase.lessons.any { it.title.contains(q, ignoreCase = true) }
                        matchesFilter && matchesQuery
                    }

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
                        item {
                            BlueprintBannerCard(onClick = onOpenBlueprint)
                        }
                        item {
                            PhaseSearchAndFilterBar(
                                query = searchQuery,
                                onQueryChange = { searchQuery = it },
                                filters = filters,
                                selectedFilter = selectedFilter,
                                onSelectFilter = { selectedFilter = it }
                            )
                        }
                        if (filteredPhases.isEmpty()) {
                            item {
                                EmptyState(
                                    title = "No matching phases",
                                    message = "Try clearing your search or switching the filter chip above."
                                )
                            }
                        } else {
                            items(filteredPhases, key = { it.phase.id }) { phase ->
                                PhaseCard(phase = phase, onClick = { if (!phase.isLocked) onPhaseClick(phase.phase.id) })
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PhaseSearchAndFilterBar(
    query: String,
    onQueryChange: (String) -> Unit,
    filters: List<String>,
    selectedFilter: Int,
    onSelectFilter: (Int) -> Unit
) {
    val container = MaterialTheme.colorScheme.surfaceContainerHigh
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = "Search phases, lessons, or res:// files…",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            },
            leadingIcon = {
                Icon(Icons.Outlined.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            },
            trailingIcon = {
                AnimatedVisibility(visible = query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(
                            Icons.Outlined.Close,
                            contentDescription = "Clear search",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(50),
            textStyle = MaterialTheme.typography.bodyMedium,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = container,
                unfocusedContainerColor = container,
                disabledContainerColor = container,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filters.forEachIndexed { idx, label ->
                val active = selectedFilter == idx
                Surface(
                    shape = RoundedCornerShape(50),
                    color = if (active) MaterialTheme.colorScheme.primary.copy(alpha = 0.20f)
                    else MaterialTheme.colorScheme.surfaceContainerHigh,
                    border = BorderStroke(
                        width = if (active) 1.5.dp else 0.5.dp,
                        color = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                    ),
                    modifier = Modifier.clickable { onSelectFilter(idx) }
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (active) FontWeight.Bold else FontWeight.Medium,
                        color = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun BlueprintBannerCard(onClick: () -> Unit) {
    val accent = MaterialTheme.colorScheme.secondary
    KadiCard(onClick = onClick, accent = accent) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            IconBubble(icon = Icons.Outlined.AccountTree, tint = accent, size = 44.dp)
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    text = "BIG PICTURE & INTERACTIVE SANDBOX",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = accent
                )
                Text(
                    text = "Godot Project Map & Kadi Rule Simulator",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "See how every script, scene, and signal connects — and test Kadi card rules interactively.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                Icons.AutoMirrored.Outlined.ArrowForward,
                contentDescription = null,
                tint = accent,
                modifier = Modifier.size(18.dp)
            )
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
            text = "THE COMPLETE KADI CURRICULUM",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = heroTextMuted
        )
        Text(
            text = "$phaseCount phases · $totalLessons guided lessons",
            style = MaterialTheme.typography.headlineMedium,
            color = heroText
        )
        Text(
            text = "Every lesson shows the exact Godot 4.3 file path, node tree, editor steps, line-by-line code walkthrough, and verification steps (~$totalMinutes min total).",
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
                    else if (inProgress) Pill(text = "In progress", color = MaterialTheme.colorScheme.primary)
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
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MetaChip(icon = Icons.Outlined.Schedule, text = "~${phase.phase.estimatedMinutes} min")
            MetaChip(icon = Icons.AutoMirrored.Outlined.MenuBook, text = "${phase.totalLessons} lessons")
            if (phase.phase.projectFiles.isNotEmpty()) {
                MetaChip(icon = Icons.Outlined.Description, text = "${phase.phase.projectFiles.size} files")
            }
            if (phase.phase.debugChallenge != null) {
                MetaChip(icon = Icons.Outlined.BugReport, text = "Bug hunt")
            }
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
