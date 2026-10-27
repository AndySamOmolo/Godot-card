package com.kadicompanion.ui.tutorial.lessondetail

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.MenuBook
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kadicompanion.R
import com.kadicompanion.domain.model.Lesson
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
import com.kadicompanion.ui.theme.isDarkScheme
import com.kadicompanion.ui.theme.successColor
import kotlinx.coroutines.delay

/**
 * 3-Tab Interactive Learning Studio:
 * Instead of dumping 10 stacked cards into an endless vertical scroll,
 * the Lesson Studio organizes learning into 3 focused workspace tabs:
 *   - Tab 0: Guide & Code (Concept walkthrough, architecture tables, callouts, copyable GDScript)
 *   - Tab 1: Godot Steps  (Scene Tree hierarchy, interactive step-by-step Editor Checklist, F5/F6 verification, troubleshooting)
 *   - Tab 2: Lab & Quiz   (Key Takeaways summary + direct launch cards for Practice Workbench, Quiz, and Debug Lab)
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
    val context = LocalContext.current

    var activeTab by rememberSaveable(lesson?.id) { mutableIntStateOf(0) }
    val guideScroll = rememberScrollState()
    val stepsScroll = rememberScrollState()
    val labScroll = rememberScrollState()

    val checkedSteps = remember(lesson?.id, lesson?.editorSteps?.size) {
        mutableStateListOf<Boolean>().apply {
            repeat(lesson?.editorSteps?.size ?: 0) { add(false) }
        }
    }
    val completedStepsCount = checkedSteps.count { it }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            KadiTopBar(
                title = lesson?.title ?: "Lesson",
                subtitle = state.phaseTitle?.let {
                    val step = if (state.totalLessonsInPhase > 0) {
                        "LESSON ${lesson?.order ?: 1}/${state.totalLessonsInPhase}"
                    } else {
                        "LESSON ${lesson?.order ?: 1}"
                    }
                    "$step · $it"
                },
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
                StudioLessonBottomBar(
                    previousLessonId = state.previousLessonId,
                    nextLessonId = state.nextLessonId,
                    isCompleted = state.isCompleted,
                    hasQuiz = lesson.quizQuestions.isNotEmpty(),
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
                    val activeScroll = when (activeTab) {
                        0 -> guideScroll
                        1 -> stepsScroll
                        else -> labScroll
                    }
                    val readFraction = if (activeScroll.maxValue > 0) {
                        activeScroll.value.toFloat() / activeScroll.maxValue
                    } else {
                        0f
                    }

                    // Thin reading progress bar
                    LinearProgressIndicator(
                        progress = { readFraction.coerceIn(0f, 1f) },
                        modifier = Modifier.fillMaxWidth().height(2.dp),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)
                    )

                    // Compact Studio Header + 3-Tab Segmented Switcher
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
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
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                icon = Icons.Outlined.Schedule
                            )
                            lesson.targetFile?.let { filePath ->
                                CopyableFileChip(
                                    filePath = filePath,
                                    onCopy = {
                                        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        cm.setPrimaryClip(ClipData.newPlainText("Godot file path", filePath))
                                    }
                                )
                            }
                        }

                        // Segmented 3-Tab Studio Switcher
                        StudioSegmentedTabs(
                            activeTab = activeTab,
                            stepsCount = lesson.editorSteps.size,
                            completedSteps = completedStepsCount,
                            quizCount = lesson.quizQuestions.size,
                            onSelectTab = { activeTab = it }
                        )
                    }

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f)
                    )

                    // Tab Content Area
                    when (activeTab) {
                        0 -> GuideAndCodeTab(
                            lesson = lesson,
                            scrollState = guideScroll,
                            onTermClick = onTermClick,
                            onSwitchToSteps = { activeTab = 1 }
                        )
                        1 -> GodotStepsTab(
                            lesson = lesson,
                            checkedSteps = checkedSteps,
                            scrollState = stepsScroll,
                            onSwitchToLab = { activeTab = 2 }
                        )
                        else -> LabAndQuizTab(
                            lesson = lesson,
                            hasDebugChallenge = state.hasDebugChallenge && state.phaseId != null,
                            scrollState = labScroll,
                            onPractice = onPractice,
                            onQuiz = onQuiz,
                            onDebugChallenge = { state.phaseId?.let(onDebugChallenge) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CopyableFileChip(filePath: String, onCopy: () -> Unit) {
    var copied by remember { mutableStateOf(false) }
    val tint = if (copied) successColor() else MaterialTheme.colorScheme.primary

    LaunchedEffect(copied) {
        if (copied) {
            delay(1600)
            copied = false
        }
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = tint.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, tint.copy(alpha = 0.35f)),
        modifier = Modifier.clickable {
            onCopy()
            copied = true
        }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                imageVector = if (copied) Icons.Outlined.Check else Icons.Outlined.Description,
                contentDescription = "Copy file path",
                tint = tint,
                modifier = Modifier.size(12.dp)
            )
            Text(
                text = if (copied) "Copied $filePath" else filePath,
                style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                fontWeight = FontWeight.Bold,
                color = tint,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Icon(
                imageVector = Icons.Outlined.ContentCopy,
                contentDescription = null,
                tint = tint.copy(alpha = 0.75f),
                modifier = Modifier.size(11.dp)
            )
        }
    }
}

@Composable
private fun StudioSegmentedTabs(
    activeTab: Int,
    stepsCount: Int,
    completedSteps: Int,
    quizCount: Int,
    onSelectTab: (Int) -> Unit
) {
    val stepsLabel = if (stepsCount > 0) "Godot Steps ($completedSteps/$stepsCount)" else "Godot Steps"
    val labLabel = if (quizCount > 0) "Lab & Quiz ($quizCount)" else "Lab & Quiz"
    val tabs = listOf(
        Triple(0, "Guide & Code", Icons.AutoMirrored.Outlined.MenuBook),
        Triple(1, stepsLabel, Icons.Outlined.Checklist),
        Triple(2, labLabel, Icons.Outlined.Lightbulb)
    )

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            tabs.forEach { (idx, title, icon) ->
                val selected = activeTab == idx
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onSelectTab(idx) },
                    shape = RoundedCornerShape(8.dp),
                    color = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                            color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GuideAndCodeTab(
    lesson: Lesson,
    scrollState: androidx.compose.foundation.ScrollState,
    onTermClick: (String) -> Unit,
    onSwitchToSteps: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Main Deeply Explained Lesson Markdown Prose
        MarkdownText(markdown = lesson.bodyMarkdown, onTermClick = onTermClick)

        // Complete Reference Code Snippets
        if (lesson.codeSnippets.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                SectionHeader(
                    title = "Complete Reference Script",
                    eyebrow = "COPY-READY GDSCRIPT"
                )
                for (snippet in lesson.codeSnippets) {
                    CodeBlock(snippet = snippet)
                }
            }
        }

        // Prompt to jump to the Godot Steps tab
        if (lesson.editorSteps.isNotEmpty() || lesson.sceneTree != null) {
            KadiCard(
                onClick = onSwitchToSteps,
                accent = MaterialTheme.colorScheme.secondary
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IconBubble(
                        icon = Icons.Outlined.Checklist,
                        tint = MaterialTheme.colorScheme.secondary,
                        size = 38.dp
                    )
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "NEXT: BUILD IT IN GODOT 4.3",
                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "Open the Godot Steps tab for the Scene Tree & ${lesson.editorSteps.size}-step checklist",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
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
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun GodotStepsTab(
    lesson: Lesson,
    checkedSteps: MutableList<Boolean>,
    scrollState: androidx.compose.foundation.ScrollState,
    onSwitchToLab: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        lesson.sceneTree?.let { tree ->
            SceneTreeBlueprintCard(sceneTree = tree)
        }

        if (lesson.editorSteps.isNotEmpty()) {
            EditorChecklistStudioCard(
                steps = lesson.editorSteps,
                checkedStates = checkedSteps
            )
        }

        if (lesson.verificationSteps.isNotEmpty()) {
            VerificationStudioCard(steps = lesson.verificationSteps)
        }

        if (lesson.troubleshootingTips.isNotEmpty()) {
            TroubleshootingStudioCard(tips = lesson.troubleshootingTips)
        }

        KadiCard(
            onClick = onSwitchToLab,
            accent = MaterialTheme.colorScheme.primary
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconBubble(
                    icon = Icons.Outlined.Lightbulb,
                    tint = MaterialTheme.colorScheme.primary,
                    size = 38.dp
                )
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "READY TO TEST YOUR MASTERY?",
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Open Lab & Quiz to practice GDScript and take the lesson quiz",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Icon(
                    Icons.AutoMirrored.Outlined.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun LabAndQuizTab(
    lesson: Lesson,
    hasDebugChallenge: Boolean,
    scrollState: androidx.compose.foundation.ScrollState,
    onPractice: () -> Unit,
    onQuiz: () -> Unit,
    onDebugChallenge: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        if (lesson.keyTakeaways.isNotEmpty()) {
            KeyTakeawaysStudioCard(takeaways = lesson.keyTakeaways)
        }

        SectionHeader(
            title = "Interactive Exercises",
            eyebrow = "HANDS-ON LABS"
        )

        lesson.practicePrompt?.let { prompt ->
            LabLaunchCard(
                eyebrow = "GDSCRIPT WORKBENCH",
                title = "Interactive Code Practice",
                description = prompt.instructions,
                buttonLabel = "Open Code Workbench",
                icon = Icons.Outlined.Lightbulb,
                accent = MaterialTheme.colorScheme.secondary,
                onClick = onPractice
            )
        }

        if (lesson.quizQuestions.isNotEmpty()) {
            LabLaunchCard(
                eyebrow = "${lesson.quizQuestions.size} QUESTIONS",
                title = "Lesson Knowledge Quiz",
                description = "Check your understanding of ${lesson.title} with instant explanations.",
                buttonLabel = "Start Quiz",
                icon = Icons.Outlined.Quiz,
                accent = MaterialTheme.colorScheme.primary,
                onClick = onQuiz
            )
        }

        if (hasDebugChallenge) {
            LabLaunchCard(
                eyebrow = "PHASE BUG HUNT",
                title = "Interactive Debug Challenge",
                description = "Inspect a broken GDScript snippet from this module and tap the exact line causing the bug.",
                buttonLabel = "Launch Bug Hunt",
                icon = Icons.Outlined.BugReport,
                accent = MaterialTheme.colorScheme.tertiary,
                onClick = onDebugChallenge
            )
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun LabLaunchCard(
    eyebrow: String,
    title: String,
    description: String,
    buttonLabel: String,
    icon: ImageVector,
    accent: Color,
    onClick: () -> Unit
) {
    KadiCard(onClick = onClick, accent = accent) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconBubble(icon = icon, tint = accent, size = 40.dp)
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = eyebrow,
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                    fontWeight = FontWeight.Bold,
                    color = accent
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Pill(text = buttonLabel, color = accent)
        }
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun SceneTreeBlueprintCard(sceneTree: String) {
    val isDark = isDarkScheme()
    val bg = if (isDark) SyntaxColors.darkBackground else SyntaxColors.lightBackground
    val accent = MaterialTheme.colorScheme.secondary

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = bg,
        border = BorderStroke(1.dp, accent.copy(alpha = 0.40f))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(accent.copy(alpha = 0.12f))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
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
                    text = "GODOT 4.3 SCENE TREE HIERARCHY",
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                    fontWeight = FontWeight.Bold,
                    color = accent
                )
            }
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

@Composable
private fun EditorChecklistStudioCard(
    steps: List<String>,
    checkedStates: MutableList<Boolean>
) {
    val doneCount = checkedStates.count { it }
    val success = successColor()
    val accent = if (doneCount == steps.size && steps.isNotEmpty()) success else MaterialTheme.colorScheme.primary
    val fraction = if (steps.isEmpty()) 0f else doneCount.toFloat() / steps.size

    KadiCard(accent = accent) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IconBubble(icon = Icons.Outlined.Checklist, tint = accent, size = 36.dp)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "STEP-BY-STEP IN THE GODOT EDITOR",
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                    fontWeight = FontWeight.Bold,
                    color = accent
                )
                Text(
                    text = "Tap each step as you complete it in Godot 4.3",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Pill(text = "$doneCount/${steps.size}", color = accent)
        }

        LinearProgressIndicator(
            progress = { fraction },
            modifier = Modifier.fillMaxWidth().height(5.dp).clip(RoundedCornerShape(50)),
            color = accent,
            trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
            strokeCap = StrokeCap.Round
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            steps.forEachIndexed { idx, stepText ->
                val isChecked = checkedStates.getOrElse(idx) { false }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (isChecked) success.copy(alpha = 0.10f)
                            else MaterialTheme.colorScheme.surfaceContainerLow
                        )
                        .border(
                            width = 1.dp,
                            color = if (isChecked) success.copy(alpha = 0.40f)
                            else MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(10.dp)
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
                            .size(22.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                if (isChecked) success
                                else MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isChecked) {
                            Icon(
                                Icons.Outlined.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        } else {
                            Text(
                                text = "${idx + 1}",
                                style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
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
private fun VerificationStudioCard(steps: List<String>) {
    val accent = successColor()
    KadiCard(accent = accent) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IconBubble(icon = Icons.Outlined.PlayArrow, tint = accent, size = 34.dp)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "VERIFY IN GODOT (PRESS F5 / F6)",
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                    fontWeight = FontWeight.Bold,
                    color = accent
                )
                Text(
                    text = "Confirm your scene and script behave as expected",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            steps.forEachIndexed { i, item ->
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${i + 1}.",
                        style = MaterialTheme.typography.bodyMedium.copy(fontFamily = CodeFontFamily),
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

@Composable
private fun TroubleshootingStudioCard(tips: List<String>) {
    val accent = MaterialTheme.colorScheme.tertiary
    KadiCard(accent = accent) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IconBubble(icon = Icons.Outlined.Build, tint = accent, size = 34.dp)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "COMMON ERRORS & INSTANT FIXES",
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                    fontWeight = FontWeight.Bold,
                    color = accent
                )
                Text(
                    text = "If Godot reports an error in the Debugger tab, check these first",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
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

@Composable
private fun KeyTakeawaysStudioCard(takeaways: List<String>) {
    val accent = MaterialTheme.colorScheme.primary
    KadiCard(accent = accent) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IconBubble(icon = Icons.Outlined.StarBorder, tint = accent, size = 34.dp)
            Text(
                text = "KEY TAKEAWAYS",
                style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
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
private fun StudioLessonBottomBar(
    previousLessonId: String?,
    nextLessonId: String?,
    isCompleted: Boolean,
    hasQuiz: Boolean,
    onNavigateLesson: (String) -> Unit,
    onMarkComplete: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth().navigationBarsPadding()) {
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (previousLessonId != null) {
                    FilledTonalButton(
                        onClick = { onNavigateLesson(previousLessonId) },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(0.85f).height(44.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("Prev", fontWeight = FontWeight.SemiBold)
                    }
                }
                Button(
                    onClick = onMarkComplete,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1.4f).height(44.dp)
                ) {
                    Icon(
                        Icons.Outlined.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = when {
                            isCompleted && hasQuiz -> "Retake Quiz"
                            isCompleted -> "Completed"
                            hasQuiz -> "Complete & Quiz"
                            else -> stringResource(R.string.action_mark_complete)
                        },
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
                if (nextLessonId != null) {
                    FilledTonalButton(
                        onClick = { onNavigateLesson(nextLessonId) },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(0.85f).height(44.dp)
                    ) {
                        Text("Next", fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.width(4.dp))
                        Icon(
                            Icons.AutoMirrored.Outlined.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
