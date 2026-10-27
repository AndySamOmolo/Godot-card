package com.kadicompanion.ui.tutorial.phaselist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.AccountTree
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Lock
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kadicompanion.R
import com.kadicompanion.domain.usecase.PhaseWithProgress
import com.kadicompanion.ui.common.EmptyState
import com.kadicompanion.ui.common.KadiCard
import com.kadicompanion.ui.common.KadiTopBar
import com.kadicompanion.ui.common.LoadingSkeleton
import com.kadicompanion.ui.common.Pill
import com.kadicompanion.ui.common.RichInlineText
import com.kadicompanion.ui.common.cleanPlainText
import com.kadicompanion.ui.common.studioScreenPadding
import com.kadicompanion.ui.theme.CodeFontFamily
import com.kadicompanion.ui.theme.successColor

private val PHASE_FILTERS = listOf("All Phases", "In Progress", "To Do", "Completed")

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
    val hPad = studioScreenPadding()

    val totalLessons = state.phases.sumOf { it.totalLessons }
    val completedLessons = state.phases.sumOf { it.completedLessons }
    val overallFraction = if (totalLessons > 0) completedLessons.toFloat() / totalLessons else 0f

    val filteredPhases = state.phases.filter { item ->
        val matchesSearch = searchQuery.isBlank() ||
            item.phase.title.contains(searchQuery, ignoreCase = true) ||
            item.phase.summary.contains(searchQuery, ignoreCase = true) ||
            item.phase.lessons.any {
                it.title.contains(searchQuery, ignoreCase = true) ||
                    (it.targetFile?.contains(searchQuery, ignoreCase = true) == true)
            }
        val isDone = item.totalLessons > 0 && item.completedLessons >= item.totalLessons
        val isInProgress = item.completedLessons in 1 until item.totalLessons
        val isTodo = item.completedLessons == 0
        val matchesFilter = when (selectedFilter) {
            1 -> isInProgress
            2 -> isTodo
            3 -> isDone
            else -> true
        }
        matchesSearch && matchesFilter
    }

    Scaffold(
        containerColor = Color.Transparent,
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0),
        topBar = {
            KadiTopBar(
                title = "Course Curriculum",
                subtitle = "12 PHASES · $totalLessons LESSONS",
                actions = {
                    IconButton(onClick = onOpenBlueprint) {
                        Icon(
                            Icons.Outlined.AccountTree,
                            contentDescription = "Project Blueprint",
                            tint = MaterialTheme.colorScheme.primary
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
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> LoadingSkeleton()
                state.phases.isEmpty() -> EmptyState(
                    title = "No tutorial content",
                    message = "Check that tutorial JSON assets are bundled."
                )
                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = hPad,
                        end = hPad,
                        top = 12.dp,
                        bottom = 28.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    item(key = "course_summary_header") {
                        Column(
                            modifier = Modifier.padding(bottom = 14.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                shape = MaterialTheme.shapes.medium,
                                color = MaterialTheme.colorScheme.surfaceContainer,
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "OVERALL CURRICULUM PROGRESS",
                                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = "$completedLessons/$totalLessons lessons (${(overallFraction * 100).toInt()}%)",
                                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                    LinearProgressIndicator(
                                        progress = { overallFraction },
                                        modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(50)),
                                        color = if (overallFraction >= 1f) successColor() else MaterialTheme.colorScheme.primary,
                                        trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.10f),
                                        strokeCap = StrokeCap.Round
                                    )
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(MaterialTheme.shapes.small)
                                            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.10f))
                                            .clickable(onClick = onOpenBlueprint)
                                            .padding(horizontal = 10.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            Icons.Outlined.AccountTree,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.secondary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = "Open Godot 4.3 res:// Blueprint & Kadi Rule Simulator",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.secondary,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Icon(
                                            Icons.AutoMirrored.Outlined.ArrowForward,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.secondary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }

                            Surface(
                                shape = MaterialTheme.shapes.medium,
                                color = MaterialTheme.colorScheme.surfaceContainer,
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                            ) {
                                TextField(
                                    value = searchQuery,
                                    onValueChange = { searchQuery = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = {
                                        Text(
                                            "Filter phases, lessons, or res:// files…",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Outlined.Search,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    },
                                    trailingIcon = {
                                        if (searchQuery.isNotEmpty()) {
                                            IconButton(onClick = { searchQuery = "" }) {
                                                Icon(Icons.Outlined.Close, contentDescription = "Clear")
                                            }
                                        }
                                    },
                                    singleLine = true,
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        disabledContainerColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    )
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                PHASE_FILTERS.forEachIndexed { idx, label ->
                                    val selected = selectedFilter == idx
                                    Surface(
                                        shape = MaterialTheme.shapes.small,
                                        color = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.16f)
                                        else MaterialTheme.colorScheme.surfaceContainer,
                                        border = BorderStroke(
                                            width = 1.dp,
                                            color = if (selected) MaterialTheme.colorScheme.primary
                                            else MaterialTheme.colorScheme.outlineVariant
                                        ),
                                        modifier = Modifier.clickable { selectedFilter = idx }
                                    ) {
                                        Text(
                                            text = label,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (selected) MaterialTheme.colorScheme.primary
                                            else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (filteredPhases.isEmpty()) {
                        item(key = "no_matches") {
                            EmptyState(
                                title = "No matching phases",
                                message = "Try clearing your search query or switching the status filter."
                            )
                        }
                    } else {
                        itemsIndexed(filteredPhases, key = { _, item -> item.phase.id }) { index, item ->
                            TimelinePhaseRow(
                                item = item,
                                isLast = index == filteredPhases.lastIndex,
                                onClick = { if (!item.isLocked) onPhaseClick(item.phase.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TimelinePhaseRow(
    item: PhaseWithProgress,
    isLast: Boolean,
    onClick: () -> Unit
) {
    val fraction = if (item.totalLessons == 0) 0f else item.completedLessons.toFloat() / item.totalLessons
    val isDone = item.totalLessons > 0 && item.completedLessons >= item.totalLessons
    val isInProgress = item.completedLessons in 1 until item.totalLessons
    val nodeColor = when {
        item.isLocked -> MaterialTheme.colorScheme.outline
        isDone -> successColor()
        isInProgress -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outlineVariant
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .alpha(if (item.isLocked) 0.6f else 1f),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(34.dp).fillMaxHeight()
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(
                        when {
                            isDone -> successColor()
                            isInProgress -> MaterialTheme.colorScheme.primary
                            else -> MaterialTheme.colorScheme.surfaceContainerHigh
                        }
                    )
                    .border(
                        width = 1.dp,
                        color = nodeColor,
                        shape = MaterialTheme.shapes.small
                    ),
                contentAlignment = Alignment.Center
            ) {
                when {
                    item.isLocked -> Icon(
                        Icons.Outlined.Lock,
                        contentDescription = "Locked",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(15.dp)
                    )
                    isDone -> Icon(
                        Icons.Filled.Check,
                        contentDescription = "Completed",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    else -> Text(
                        text = item.phase.order.toString().padStart(2, '0'),
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                        fontWeight = FontWeight.Bold,
                        color = if (isInProgress) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .weight(1f)
                        .background(
                            if (isDone) successColor().copy(alpha = 0.5f)
                            else MaterialTheme.colorScheme.outlineVariant
                        )
                )
            }
        }

        Box(modifier = Modifier.weight(1f).padding(bottom = 12.dp)) {
            val targetFilesCount = item.phase.lessons.mapNotNull { lesson -> lesson.targetFile }.distinct().size
            KadiCard(
                onClick = if (item.isLocked) null else onClick,
                accent = when {
                    isDone -> successColor()
                    isInProgress -> MaterialTheme.colorScheme.primary
                    else -> null
                },
                enabled = !item.isLocked
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "MODULE ${item.phase.order.toString().padStart(2, '0')}",
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                        fontWeight = FontWeight.Bold,
                        color = if (isDone) successColor() else MaterialTheme.colorScheme.primary
                    )
                    Pill(
                        text = when {
                            item.isLocked -> "Locked"
                            isDone -> "Completed"
                            isInProgress -> "${item.completedLessons}/${item.totalLessons} Done"
                            else -> "${item.totalLessons} Lessons"
                        },
                        color = when {
                            item.isLocked -> MaterialTheme.colorScheme.outline
                            isDone -> successColor()
                            isInProgress -> MaterialTheme.colorScheme.primary
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }

                Text(
                    text = cleanPlainText(item.phase.title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                RichInlineText(
                    text = item.phase.summary,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Pill(
                        text = "${item.totalLessons} lessons",
                        color = MaterialTheme.colorScheme.primary,
                        icon = Icons.AutoMirrored.Outlined.MenuBook
                    )
                    if (targetFilesCount > 0) {
                        Pill(
                            text = "$targetFilesCount files",
                            color = MaterialTheme.colorScheme.secondary,
                            icon = Icons.Outlined.Description
                        )
                    }
                    if (item.phase.debugChallenge != null) {
                        Pill(
                            text = "Bug Lab",
                            color = MaterialTheme.colorScheme.tertiary,
                            icon = Icons.Outlined.BugReport
                        )
                    }
                }

                LinearProgressIndicator(
                    progress = { fraction },
                    modifier = Modifier.fillMaxWidth().height(5.dp).clip(CircleShape),
                    color = if (isDone) successColor() else MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                    strokeCap = StrokeCap.Round
                )
            }
        }
    }
}
