package com.kadicompanion.ui.tutorial.lessondetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kadicompanion.R
import com.kadicompanion.ui.common.CodeBlock
import com.kadicompanion.ui.common.EmptyState
import com.kadicompanion.ui.common.IconBubble
import com.kadicompanion.ui.common.KadiCard
import com.kadicompanion.ui.common.KadiTopBar
import com.kadicompanion.ui.common.LoadingSkeleton
import com.kadicompanion.ui.common.MarkdownText
import com.kadicompanion.ui.common.Pill
import com.kadicompanion.ui.common.SectionHeader
import com.kadicompanion.ui.theme.glassBorderColor
import com.kadicompanion.ui.theme.successColor

/**
 * The reading surface. Everything above the footer is prose rhythm — eyebrow,
 * big title, body, snippets — and every action lives in the pinned footer so the
 * text is never interrupted by a wall of buttons.
 */
@Composable
fun LessonDetailScreen(
    onBack: () -> Unit,
    onQuiz: () -> Unit,
    onPractice: () -> Unit,
    onNextLesson: (String) -> Unit,
    onDebugChallenge: (String) -> Unit,
    onTermClick: (String) -> Unit,
    vm: LessonDetailViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    val lesson = state.lesson
    val scrollState = rememberScrollState()
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            KadiTopBar(
                title = lesson?.title ?: "Lesson",
                subtitle = state.phaseTitle,
                onBack = onBack,
                actions = {
                    IconButton(onClick = vm::toggleBookmark) {
                        Icon(
                            imageVector = if (state.isBookmarked) Icons.Outlined.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = stringResource(R.string.action_bookmark),
                            tint = if (state.isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        },
        bottomBar = {
            if (lesson != null) {
                LessonFooter(
                    hasPractice = lesson.practicePrompt != null,
                    hasQuiz = lesson.quizQuestions.isNotEmpty(),
                    hasDebugChallenge = state.hasDebugChallenge && state.phaseId != null,
                    nextLessonId = state.nextLessonId,
                    onPractice = onPractice,
                    onQuiz = onQuiz,
                    onDebugChallenge = { state.phaseId?.let(onDebugChallenge) },
                    onNextLesson = onNextLesson,
                    onMarkComplete = {
                        vm.markComplete()
                        if (lesson.quizQuestions.isNotEmpty()) onQuiz()
                    }
                )
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> LoadingSkeleton(lines = 10)
                lesson == null -> EmptyState(
                    title = stringResource(R.string.content_unavailable),
                    message = "This lesson may have been moved or removed."
                )
                else -> {
                    val readFraction = if (scrollState.maxValue > 0) {
                        scrollState.value.toFloat() / scrollState.maxValue
                    } else {
                        0f
                    }
                    // Gradient progress bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(3.dp)
                                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.07f))
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(readFraction.coerceIn(0f, 1f))
                                .height(3.dp)
                                .clip(RoundedCornerShape(topEnd = 2.dp, bottomEnd = 2.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.secondary
                                        )
                                    )
                                )
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 8.dp)) {
                            Text(
                                text = "LESSON ${lesson.order}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = lesson.title,
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                if (state.isCompleted) {
                                    Pill(text = "Completed", color = successColor(), icon = Icons.Outlined.CheckCircle)
                                }
                                if (lesson.quizQuestions.isNotEmpty()) {
                                    Pill(
                                        text = "${lesson.quizQuestions.size} questions",
                                        color = MaterialTheme.colorScheme.primary,
                                        icon = Icons.Outlined.Quiz
                                    )
                                }
                                if (lesson.codeSnippets.isNotEmpty()) {
                                    Pill(
                                        text = "${lesson.codeSnippets.size} snippets",
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }
                        }
                        MarkdownText(markdown = lesson.bodyMarkdown, onTermClick = onTermClick)
                        if (lesson.codeSnippets.isNotEmpty()) {
                            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                                SectionHeader(title = "In code", eyebrow = "Snippets")
                                for (snippet in lesson.codeSnippets) {
                                    CodeBlock(snippet = snippet)
                                }
                            }
                        }
                        lesson.practicePrompt?.let { prompt ->
                            KadiCard(onClick = onPractice, accent = MaterialTheme.colorScheme.secondary) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    IconBubble(
                                        icon = Icons.Outlined.Lightbulb,
                                        tint = MaterialTheme.colorScheme.secondary,
                                        size = 44.dp
                                    )
                                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                        Text(
                                            text = "PRACTICE PROMPT",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                        Text(
                                            text = prompt.instructions,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            maxLines = 3
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
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun LessonFooter(
    hasPractice: Boolean,
    hasQuiz: Boolean,
    hasDebugChallenge: Boolean,
    nextLessonId: String?,
    onPractice: () -> Unit,
    onQuiz: () -> Unit,
    onDebugChallenge: () -> Unit,
    onNextLesson: (String) -> Unit,
    onMarkComplete: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.92f),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, glassBorderColor())
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (hasPractice || hasQuiz || hasDebugChallenge) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        if (hasPractice) {
                            OutlinedButton(onClick = onPractice, modifier = Modifier.weight(1f)) {
                                Icon(Icons.Outlined.Lightbulb, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Practice")
                            }
                        }
                        if (hasQuiz) {
                            OutlinedButton(onClick = onQuiz, modifier = Modifier.weight(1f)) {
                                Icon(Icons.Outlined.Quiz, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Quiz")
                            }
                        }
                        if (hasDebugChallenge) {
                            OutlinedButton(onClick = onDebugChallenge, modifier = Modifier.weight(1f)) {
                                Icon(Icons.Outlined.BugReport, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Debug")
                            }
                        }
                    }
                }
                Button(onClick = onMarkComplete, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Outlined.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.action_mark_complete))
                }
                if (nextLessonId != null) {
                    FilledTonalButton(onClick = { onNextLesson(nextLessonId) }, modifier = Modifier.fillMaxWidth()) {
                        Text("Next lesson")
                        Spacer(Modifier.width(8.dp))
                        Icon(Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}
