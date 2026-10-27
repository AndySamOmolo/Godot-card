package com.kadicompanion.ui.tutorial.lessondetail

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.AccountTree
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.kadicompanion.ui.theme.CodeFontFamily
import com.kadicompanion.ui.theme.SyntaxColors
import com.kadicompanion.ui.theme.glassBorderColor
import com.kadicompanion.ui.theme.isDarkScheme
import com.kadicompanion.ui.theme.successColor
import kotlinx.coroutines.delay

/**
 * The complete reading and building surface. Shows:
 * 1. Lesson progress & header pills (including estimated reading time)
 * 2. Target Godot File (`res://...`) banner with one-tap copy & animated "Copied!" confirmation
 * 3. Collapsible Godot Scene Tree Blueprint diagram (`lesson.sceneTree`)
 * 4. Interactive "Do This in Godot (Step-by-Step)" checklist (`lesson.editorSteps`)
 * 5. Deeply explained Lesson Markdown Prose (`MarkdownText` with tables, callouts & syntax-highlighted code)
 * 6. Complete reference code snippets (`lesson.codeSnippets`)
 * 7. Collapsible "Verify It Works in Godot Right Now (Press F5/F6)" (`lesson.verificationSteps`)
 * 8. Collapsible "Stuck? Common Errors & Instant Fixes" (`lesson.troubleshootingTips`)
 * 9. Key Takeaways summary (`lesson.keyTakeaways`)
 * 10. Practice Prompt card + pinned footer with Prev / Next / Practice / Quiz / Debug
 */
@OptIn(ExperimentalLayoutApi::class)
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
    val context = LocalContext.current

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
                    previousLessonId = state.previousLessonId,
                    nextLessonId = state.nextLessonId,
                    isCompleted = state.isCompleted,
                    onPractice = onPractice,
                    onQuiz = onQuiz,
                    onDebugChallenge = { state.phaseId?.let(onDebugChallenge) },
                    onNavigateLesson = onNextLesson,
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
                    // Gradient reading progress bar
                    Box(modifier = Modifier.fillMaxWidth().height(3.dp)) {
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
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 8.dp)) {
                            val stepBadge = if (state.totalLessonsInPhase > 0) {
                                "LESSON ${lesson.order} OF ${state.totalLessonsInPhase}"
                            } else {
                                "LESSON ${lesson.order}"
                            }
                            Text(
                                text = stepBadge,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = lesson.title,
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            val readingMinutes = remember(lesson.id) {
                                ((lesson.bodyMarkdown.length + lesson.codeSnippets.sumOf { it.code.length }) / 950)
                                    .coerceAtLeast(4)
                            }
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                if (state.isCompleted) {
                                    Pill(text = "Completed", color = successColor(), icon = Icons.Outlined.CheckCircle)
                                }
                                Pill(
                                    text = "~$readingMinutes min read",
                                    color = MaterialTheme.colorScheme.tertiary,
                                    icon = Icons.Outlined.Schedule
                                )
                                if (lesson.editorSteps.isNotEmpty()) {
                                    Pill(
                                        text = "${lesson.editorSteps.size} editor steps",
                                        color = MaterialTheme.colorScheme.primary,
                                        icon = Icons.Outlined.Checklist
                                    )
                                }
                                if (lesson.quizQuestions.isNotEmpty()) {
                                    Pill(
                                        text = "${lesson.quizQuestions.size} quiz Qs",
                                        color = MaterialTheme.colorScheme.secondary,
                                        icon = Icons.Outlined.Quiz
                                    )
                                }
                            }
                        }

                        // Target Godot File banner
                        lesson.targetFile?.let { filePath ->
                            TargetFileBanner(
                                filePath = filePath,
                                onCopy = {
                                    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    cm.setPrimaryClip(ClipData.newPlainText("Godot file path", filePath))
                                }
                            )
                        }

                        // Visual Scene Tree Blueprint
                        lesson.sceneTree?.let { tree ->
                            SceneTreeCard(sceneTree = tree)
                        }

                        // Interactive Step-by-Step Godot Editor Checklist
                        if (lesson.editorSteps.isNotEmpty()) {
                            EditorChecklistCard(lessonId = lesson.id, steps = lesson.editorSteps)
                        }

                        // Main Deeply Explained Lesson Markdown Prose
                        MarkdownText(markdown = lesson.bodyMarkdown, onTermClick = onTermClick)

                        // Complete Code Snippets
                        if (lesson.codeSnippets.isNotEmpty()) {
                            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                                SectionHeader(title = "Complete script & snippets", eyebrow = "Copy-ready GDScript")
                                for (snippet in lesson.codeSnippets) {
                                    CodeBlock(snippet = snippet)
                                }
                            }
                        }

                        // Verification Steps ("How to Test It Right Now in Godot")
                        if (lesson.verificationSteps.isNotEmpty()) {
                            VerificationCard(steps = lesson.verificationSteps)
                        }

                        // Troubleshooting & Common Errors Card
                        if (lesson.troubleshootingTips.isNotEmpty()) {
                            TroubleshootingCard(tips = lesson.troubleshootingTips)
                        }

                        // Key Takeaways
                        if (lesson.keyTakeaways.isNotEmpty()) {
                            KeyTakeawaysCard(takeaways = lesson.keyTakeaways)
                        }

                        // Practice Prompt Card
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
                                            text = "HANDS-ON PRACTICE WORKBENCH",
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
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun TargetFileBanner(filePath: String, onCopy: () -> Unit) {
    val scheme = MaterialTheme.colorScheme
    val success = successColor()
    var copied by remember { mutableStateOf(false) }

    LaunchedEffect(copied) {
        if (copied) {
            delay(1800)
            copied = false
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onCopy()
                copied = true
            },
        shape = RoundedCornerShape(16.dp),
        color = scheme.primary.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, if (copied) success.copy(alpha = 0.55f) else scheme.primary.copy(alpha = 0.35f))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Description,
                contentDescription = null,
                tint = if (copied) success else scheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (copied) "COPIED TO CLIPBOARD!" else "WHERE THIS LIVES IN YOUR GODOT PROJECT",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (copied) success else scheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = filePath,
                    style = MaterialTheme.typography.bodyMedium.copy(fontFamily = CodeFontFamily),
                    fontWeight = FontWeight.SemiBold,
                    color = scheme.onSurface
                )
            }
            IconButton(
                onClick = {
                    onCopy()
                    copied = true
                },
                modifier = Modifier.size(34.dp)
            ) {
                Icon(
                    imageVector = if (copied) Icons.Outlined.Check else Icons.Outlined.ContentCopy,
                    contentDescription = "Copy file path",
                    tint = if (copied) success else scheme.primary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun SceneTreeCard(sceneTree: String) {
    val isDark = isDarkScheme()
    val bg = if (isDark) SyntaxColors.darkBackground else SyntaxColors.lightBackground
    val accent = MaterialTheme.colorScheme.secondary
    var expanded by remember { mutableStateOf(true) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = bg,
        border = BorderStroke(1.dp, accent.copy(alpha = 0.40f))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(accent.copy(alpha = 0.12f))
                    .clickable { expanded = !expanded }
                    .padding(horizontal = 14.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.AccountTree,
                    contentDescription = null,
                    tint = accent,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "GODOT SCENE TREE HIERARCHY",
                    style = MaterialTheme.typography.labelSmall,
                    fontFamily = CodeFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = accent,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = accent,
                    modifier = Modifier.size(18.dp)
                )
            }
            AnimatedVisibility(visible = expanded) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(14.dp)
                ) {
                    Text(
                        text = sceneTree,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = CodeFontFamily,
                            fontSize = 13.sp,
                            lineHeight = 20.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun EditorChecklistCard(lessonId: String, steps: List<String>) {
    val checkedStates = remember(lessonId, steps.size) {
        mutableStateListOf<Boolean>().apply { repeat(steps.size) { add(false) } }
    }
    val doneCount = checkedStates.count { it }
    val success = successColor()
    val accent = if (doneCount == steps.size) success else MaterialTheme.colorScheme.primary

    KadiCard(accent = accent) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IconBubble(icon = Icons.Outlined.Checklist, tint = accent, size = 36.dp)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "DO THIS IN GODOT (STEP-BY-STEP)",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = accent
                )
                Text(
                    text = "Tap each step as you complete it in the Godot 4.3 editor",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Pill(text = "$doneCount/${steps.size}", color = accent)
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            steps.forEachIndexed { idx, stepText ->
                val isChecked = checkedStates.getOrElse(idx) { false }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isChecked) success.copy(alpha = 0.10f)
                            else MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f)
                        )
                        .border(
                            width = 1.dp,
                            color = if (isChecked) success.copy(alpha = 0.35f)
                            else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            if (idx < checkedStates.size) {
                                checkedStates[idx] = !checkedStates[idx]
                            }
                        }
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(if (isChecked) success else MaterialTheme.colorScheme.primary.copy(alpha = 0.16f)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isChecked) {
                            Icon(Icons.Outlined.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(15.dp))
                        } else {
                            Text(
                                text = "${idx + 1}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Text(
                        text = stepText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isChecked) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                        textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun VerificationCard(steps: List<String>) {
    val accent = successColor()
    var expanded by remember { mutableStateOf(true) }
    KadiCard(accent = accent) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IconBubble(icon = Icons.Outlined.PlayArrow, tint = accent, size = 34.dp)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "VERIFY IT WORKS RIGHT NOW (PRESS F5 / F6)",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = accent
                )
                Text(
                    text = "Confirm your setup before moving to the next lesson",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                contentDescription = if (expanded) "Collapse" else "Expand",
                tint = accent
            )
        }
        AnimatedVisibility(visible = expanded) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                steps.forEachIndexed { i, item ->
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "${i + 1}.",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = accent
                        )
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TroubleshootingCard(tips: List<String>) {
    val accent = MaterialTheme.colorScheme.tertiary
    var expanded by remember { mutableStateOf(true) }
    KadiCard(accent = accent) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IconBubble(icon = Icons.Outlined.Build, tint = accent, size = 34.dp)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "STUCK? COMMON ERRORS & INSTANT FIXES",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = accent
                )
                Text(
                    text = "If Godot shows a red error in the Debugger tab, check these first",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                contentDescription = if (expanded) "Collapse" else "Expand",
                tint = accent
            )
        }
        AnimatedVisibility(visible = expanded) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                tips.forEach { item ->
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "\u26A0",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = accent
                        )
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun KeyTakeawaysCard(takeaways: List<String>) {
    val accent = MaterialTheme.colorScheme.primary
    KadiCard(accent = accent) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IconBubble(icon = Icons.Outlined.StarBorder, tint = accent, size = 34.dp)
            Text(
                text = "KEY TAKEAWAYS",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = accent
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            takeaways.forEach { item ->
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "\u2713",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = successColor()
                    )
                    Text(
                        text = item,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
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
    previousLessonId: String?,
    nextLessonId: String?,
    isCompleted: Boolean,
    onPractice: () -> Unit,
    onQuiz: () -> Unit,
    onDebugChallenge: () -> Unit,
    onNavigateLesson: (String) -> Unit,
    onMarkComplete: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.94f),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        border = BorderStroke(0.5.dp, glassBorderColor())
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (hasPractice || hasQuiz || hasDebugChallenge) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (hasPractice) {
                            OutlinedButton(onClick = onPractice, modifier = Modifier.weight(1f)) {
                                Icon(Icons.Outlined.Lightbulb, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(6.dp))
                                Text("Practice")
                            }
                        }
                        if (hasQuiz) {
                            OutlinedButton(onClick = onQuiz, modifier = Modifier.weight(1f)) {
                                Icon(Icons.Outlined.Quiz, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(6.dp))
                                Text("Quiz")
                            }
                        }
                        if (hasDebugChallenge) {
                            OutlinedButton(onClick = onDebugChallenge, modifier = Modifier.weight(1f)) {
                                Icon(Icons.Outlined.BugReport, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(6.dp))
                                Text("Debug")
                            }
                        }
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (previousLessonId != null) {
                        FilledTonalButton(
                            onClick = { onNavigateLesson(previousLessonId) },
                            modifier = Modifier.weight(0.9f)
                        ) {
                            Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Prev")
                        }
                    }
                    Button(
                        onClick = onMarkComplete,
                        modifier = Modifier.weight(1.3f)
                    ) {
                        Icon(Icons.Outlined.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(if (isCompleted) "Done (Quiz)" else stringResource(R.string.action_mark_complete))
                    }
                    if (nextLessonId != null) {
                        FilledTonalButton(
                            onClick = { onNavigateLesson(nextLessonId) },
                            modifier = Modifier.weight(0.9f)
                        ) {
                            Text("Next")
                            Spacer(Modifier.width(6.dp))
                            Icon(Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}
