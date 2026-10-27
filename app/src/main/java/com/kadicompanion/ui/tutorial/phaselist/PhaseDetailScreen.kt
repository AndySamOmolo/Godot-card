package com.kadicompanion.ui.tutorial.phaselist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kadicompanion.domain.model.Lesson
import com.kadicompanion.ui.common.EmptyState
import com.kadicompanion.ui.common.HeroPanel
import com.kadicompanion.ui.common.IconBubble
import com.kadicompanion.ui.common.KadiCard
import com.kadicompanion.ui.common.KadiTopBar
import com.kadicompanion.ui.common.LoadingSkeleton
import com.kadicompanion.ui.common.Pill
import com.kadicompanion.ui.common.SectionHeader
import com.kadicompanion.ui.theme.CodeFontFamily
import com.kadicompanion.ui.theme.SyntaxColors
import com.kadicompanion.ui.theme.heroTextPrimary
import com.kadicompanion.ui.theme.heroTextSecondary
import com.kadicompanion.ui.theme.heroTextTertiary
import com.kadicompanion.ui.theme.isDarkScheme
import com.kadicompanion.ui.theme.successColor

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
                    val completedInPhase = lessons.count { it.id in state.completedIds }
                    val nextUnfinishedLesson = lessons.firstOrNull { it.id !in state.completedIds } ?: lessons.firstOrNull()

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
                                completedCount = completedInPhase,
                                nextLessonTitle = nextUnfinishedLesson?.title,
                                onStartOrContinue = { nextUnfinishedLesson?.let { onLessonClick(it.id) } }
                            )
                        }
                        if (phase.learningObjectives.isNotEmpty() || phase.projectFiles.isNotEmpty()) {
                            item {
                                PhaseBlueprintSummaryCard(
                                    objectives = phase.learningObjectives,
                                    projectFiles = phase.projectFiles,
                                    sceneTree = phase.sceneTreeBlueprint
                                )
                            }
                        }
                        item {
                            SectionHeader(
                                title = "Step-by-Step Lessons",
                                eyebrow = "build in order",
                                trailing = {
                                    Pill(
                                        text = "$completedInPhase/${lessons.size}",
                                        color = successColor()
                                    )
                                }
                            )
                        }
                        items(lessons, key = { it.id }) { lesson ->
                            val index = lessons.indexOf(lesson) + 1
                            val done = lesson.id in state.completedIds
                            val isNextUp = lesson.id == nextUnfinishedLesson?.id && !done
                            LessonRow(
                                index = index,
                                lesson = lesson,
                                done = done,
                                isNextUp = isNextUp,
                                onClick = { onLessonClick(lesson.id) }
                            )
                        }
                        if (phase.debugChallenge != null) {
                            item {
                                SectionHeader(
                                    title = "Phase Boss: Bug Hunt",
                                    eyebrow = "test your debugging eyes"
                                )
                            }
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
private fun PhaseBlueprintSummaryCard(
    objectives: List<String>,
    projectFiles: List<String>,
    sceneTree: String?
) {
    val isDark = isDarkScheme()
    val codeBg = if (isDark) SyntaxColors.darkBackground else SyntaxColors.lightBackground
    KadiCard(accent = MaterialTheme.colorScheme.secondary) {
        if (objectives.isNotEmpty()) {
            Text(
                text = "WHAT YOU'LL MASTER IN THIS PHASE",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                objectives.forEach { obj ->
                    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("\u2022", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                        Text(obj, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }
        if (projectFiles.isNotEmpty()) {
            Text(
                text = "GODOT PROJECT FILES IN THIS PHASE",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = if (objectives.isNotEmpty()) 6.dp else 0.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                projectFiles.forEach { file ->
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(
                            Icons.Outlined.Description,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = file,
                            style = MaterialTheme.typography.bodySmall.copy(fontFamily = CodeFontFamily),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
        if (!sceneTree.isNullOrBlank()) {
            Text(
                text = "TARGET SCENE TREE STRUCTURE",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(top = 6.dp)
            )
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = codeBg,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(10.dp)
                ) {
                    Text(
                        text = sceneTree,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = CodeFontFamily,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun PhaseHero(
    summary: String,
    minutes: Int,
    lessonCount: Int,
    completedCount: Int,
    nextLessonTitle: String?,
    onStartOrContinue: () -> Unit
) {
    val heroText = heroTextPrimary()
    val heroTextSub = heroTextSecondary()
    val heroTextMuted = heroTextTertiary()
    val fraction = if (lessonCount == 0) 0f else completedCount.toFloat() / lessonCount
    val ctaLabel = when {
        completedCount == 0 -> "Start Phase: ${nextLessonTitle ?: "Lesson 1"}"
        completedCount < lessonCount -> "Continue: ${nextLessonTitle ?: "Next Lesson"}"
        else -> "Review Phase from Lesson 1"
    }
    HeroPanel(onClick = onStartOrContinue) {
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
        Spacer(Modifier.height(4.dp))
        Surface(
            shape = CircleShape,
            color = heroText.copy(alpha = 0.22f),
            modifier = Modifier.clickable(onClick = onStartOrContinue)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(Icons.Outlined.PlayArrow, contentDescription = null, tint = heroText, modifier = Modifier.size(18.dp))
                Text(
                    text = ctaLabel,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = heroText
                )
            }
        }
    }
}

@Composable
private fun LessonRow(
    index: Int,
    lesson: Lesson,
    done: Boolean,
    isNextUp: Boolean,
    onClick: () -> Unit
) {
    val success = successColor()
    val accent = when {
        done -> success
        isNextUp -> MaterialTheme.colorScheme.primary
        else -> null
    }
    KadiCard(
        onClick = onClick,
        accent = accent
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            StepBadge(index = index, done = done)
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = lesson.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                lesson.targetFile?.let { file ->
                    Text(
                        text = file,
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (done) {
                        Pill(text = "Completed", color = success, icon = Icons.Filled.Check)
                    } else if (isNextUp) {
                        Pill(text = "Up Next", color = MaterialTheme.colorScheme.primary, icon = Icons.Outlined.PlayArrow)
                    }
                    if (lesson.editorSteps.isNotEmpty()) {
                        Pill(
                            text = "${lesson.editorSteps.size} steps",
                            color = MaterialTheme.colorScheme.secondary,
                            icon = Icons.Outlined.Checklist
                        )
                    }
                    if (lesson.quizQuestions.isNotEmpty()) {
                        Pill(
                            text = "${lesson.quizQuestions.size} Qs",
                            color = MaterialTheme.colorScheme.primary,
                            icon = Icons.Outlined.Quiz
                        )
                    }
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
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "Interactive Debug Challenge",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Tap the buggy line in the broken GDScript snippet and squash the bug!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Pill(text = "Bug Hunt", color = MaterialTheme.colorScheme.tertiary, icon = Icons.Outlined.BugReport)
        }
    }
}
