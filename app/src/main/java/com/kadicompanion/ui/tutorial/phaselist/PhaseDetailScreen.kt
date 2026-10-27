package com.kadicompanion.ui.tutorial.phaselist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
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
import com.kadicompanion.ui.common.RichInlineText
import com.kadicompanion.ui.common.SectionHeader
import com.kadicompanion.ui.common.cleanPlainText
import com.kadicompanion.ui.common.studioCardSpacing
import com.kadicompanion.ui.common.studioScreenPadding
import com.kadicompanion.ui.theme.CodeFontFamily
import com.kadicompanion.ui.theme.heroTextPrimary
import com.kadicompanion.ui.theme.heroTextSecondary
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
    val phase = state.phase
    val hPad = studioScreenPadding()
    val vGap = studioCardSpacing()

    Scaffold(
        containerColor = Color.Transparent,
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0),
        topBar = {
            KadiTopBar(
                title = phase?.title ?: "Module",
                subtitle = phase?.let { "MODULE ${it.order.toString().padStart(2, '0')}" },
                onBack = onBack
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> LoadingSkeleton()
                phase == null -> EmptyState(title = "Module not found", message = "")
                else -> {
                    val completedCount = phase.lessons.count { it.id in state.completedIds }
                    val nextUncompletedLesson = phase.lessons.firstOrNull { it.id !in state.completedIds }
                        ?: phase.lessons.firstOrNull()
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = hPad,
                            end = hPad,
                            top = 12.dp,
                            bottom = 28.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(vGap)
                    ) {
                        item(key = "hero") {
                            ModuleHeaderCard(
                                order = phase.order,
                                title = phase.title,
                                summary = phase.summary,
                                completed = completedCount,
                                total = phase.lessons.size,
                                nextLessonTitle = nextUncompletedLesson?.title,
                                onStartOrContinue = { nextUncompletedLesson?.let { onLessonClick(it.id) } }
                            )
                        }
                        item(key = "lessons_header") {
                            SectionHeader(
                                title = "Module Syllabus",
                                modifier = Modifier.padding(top = 6.dp),
                                eyebrow = "${phase.lessons.size} GUIDED LESSONS",
                                trailing = {
                                    Pill(
                                        text = "$completedCount/${phase.lessons.size} Done",
                                        color = if (completedCount == phase.lessons.size && phase.lessons.isNotEmpty()) {
                                            successColor()
                                        } else {
                                            MaterialTheme.colorScheme.primary
                                        }
                                    )
                                }
                            )
                        }
                        items(phase.lessons, key = { it.id }) { lesson ->
                            val isCompleted = lesson.id in state.completedIds
                            val isNext = !isCompleted && lesson.id == nextUncompletedLesson?.id
                            SyllabusLessonRow(
                                lesson = lesson,
                                completed = isCompleted,
                                isNext = isNext,
                                onClick = { onLessonClick(lesson.id) }
                            )
                        }
                        if (phase.debugChallenge != null) {
                            item(key = "debug_header") {
                                SectionHeader(
                                    title = "Hands-On Bug Hunt",
                                    modifier = Modifier.padding(top = 8.dp),
                                    eyebrow = "PHASE LAB"
                                )
                            }
                            item(key = "debug_card") {
                                DebugLabCard(
                                    bugSummary = phase.debugChallenge.bugDescription,
                                    onClick = onDebugChallenge
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
private fun ModuleHeaderCard(
    order: Int,
    title: String,
    summary: String,
    completed: Int,
    total: Int,
    nextLessonTitle: String?,
    onStartOrContinue: () -> Unit
) {
    val fraction = if (total == 0) 0f else completed.toFloat() / total
    val heroWhite = heroTextPrimary()
    val heroSub = heroTextSecondary()

    HeroPanel {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(0xFF38BDF8).copy(alpha = 0.22f),
                border = BorderStroke(1.dp, Color(0xFF38BDF8).copy(alpha = 0.45f))
            ) {
                Text(
                    text = "MODULE ${order.toString().padStart(2, '0')}",
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF7DD3FC),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
            Text(
                text = "$completed of $total lessons (${(fraction * 100).toInt()}%)",
                style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                fontWeight = FontWeight.Bold,
                color = heroSub
            )
        }
        Text(
            text = cleanPlainText(title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = heroWhite
        )
        RichInlineText(
            text = summary,
            style = MaterialTheme.typography.bodyMedium,
            color = heroSub
        )
        LinearProgressIndicator(
            progress = { fraction },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .height(6.dp)
                .clip(RoundedCornerShape(50)),
            color = Color(0xFF38BDF8),
            trackColor = heroWhite.copy(alpha = 0.18f),
            strokeCap = StrokeCap.Round
        )
        if (nextLessonTitle != null) {
            Button(
                onClick = onStartOrContinue,
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF0F172A)
                ),
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp).height(44.dp)
            ) {
                Icon(Icons.Outlined.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text(
                    text = when {
                        completed == 0 -> "Start Lesson 1: ${cleanPlainText(nextLessonTitle)}"
                        completed >= total -> "Review Module: ${cleanPlainText(nextLessonTitle)}"
                        else -> "Continue: ${cleanPlainText(nextLessonTitle)}"
                    },
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun SyllabusLessonRow(
    lesson: Lesson,
    completed: Boolean,
    isNext: Boolean,
    onClick: () -> Unit
) {
    val doneColor = successColor()
    val stepColor = when {
        completed -> doneColor
        isNext -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outlineVariant
    }
    val readMins = ((lesson.bodyMarkdown.length + lesson.codeSnippets.sumOf { it.code.length }) / 950).coerceAtLeast(4)

    KadiCard(
        onClick = onClick,
        accent = when {
            completed -> doneColor
            isNext -> MaterialTheme.colorScheme.primary
            else -> null
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(
                        when {
                            completed -> doneColor
                            isNext -> MaterialTheme.colorScheme.primary
                            else -> MaterialTheme.colorScheme.surfaceContainerHigh
                        }
                    )
                    .border(1.dp, stepColor, MaterialTheme.shapes.small),
                contentAlignment = Alignment.Center
            ) {
                if (completed) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                } else {
                    Text(
                        text = lesson.order.toString().padStart(2, '0'),
                        style = MaterialTheme.typography.labelMedium.copy(fontFamily = CodeFontFamily),
                        color = if (isNext) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = cleanPlainText(lesson.title),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f, fill = false),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (isNext) {
                        Pill(text = "Up Next", color = MaterialTheme.colorScheme.primary)
                    }
                }

                lesson.targetFile?.let { path ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Description,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = path,
                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Pill(
                        text = "~$readMins min",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        icon = Icons.Outlined.Schedule,
                        filled = false
                    )
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
                            color = MaterialTheme.colorScheme.tertiary,
                            icon = Icons.Outlined.Quiz
                        )
                    }
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DebugLabCard(bugSummary: String, onClick: () -> Unit) {
    KadiCard(
        onClick = onClick,
        accent = MaterialTheme.colorScheme.tertiary
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconBubble(
                icon = Icons.Outlined.BugReport,
                tint = MaterialTheme.colorScheme.tertiary,
                size = 40.dp
            )
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "INTERACTIVE DEBUG CHALLENGE",
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Text(
                    text = "Spot & Fix the Broken GDScript Line",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                RichInlineText(
                    text = bugSummary,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Pill(text = "Launch", color = MaterialTheme.colorScheme.tertiary)
        }
    }
}
