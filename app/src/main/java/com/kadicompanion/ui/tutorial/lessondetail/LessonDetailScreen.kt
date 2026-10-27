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
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.material.icons.outlined.ViewStream
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
import com.kadicompanion.data.local.datastore.LessonLayoutMode
import com.kadicompanion.domain.model.Lesson
import com.kadicompanion.ui.common.CodeBlock
import com.kadicompanion.ui.common.EmptyState
import com.kadicompanion.ui.common.IconBubble
import com.kadicompanion.ui.common.KadiCard
import com.kadicompanion.ui.common.KadiTopBar
import com.kadicompanion.ui.common.LoadingSkeleton
import com.kadicompanion.ui.common.MarkdownText
import com.kadicompanion.ui.common.Pill
import com.kadicompanion.ui.common.RichInlineText
import com.kadicompanion.ui.common.SectionHeader
import com.kadicompanion.ui.common.studioCardSpacing
import com.kadicompanion.ui.common.studioScreenPadding
import com.kadicompanion.ui.theme.CodeFontFamily
import com.kadicompanion.ui.theme.LocalUiPreferences
import com.kadicompanion.ui.theme.SyntaxColors
import com.kadicompanion.ui.theme.isDarkScheme
import com.kadicompanion.ui.theme.successColor
import kotlinx.coroutines.delay

/**
 * Ultra-clean Lesson Studio:
 * - Slim top bar (`KadiTopBar`) + single-row 34dp tab switcher so ~85% of the phone screen is dedicated to reading.
 * - Metadata chips (`Completed`, `~4 min`, copyable `res://...` file path) live at the top of the scroll area so they scroll out of the way as you read!
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
    val uiPrefs = LocalUiPreferences.current

    var activeTab by rememberSaveable(lesson?.id) { mutableIntStateOf(0) }
    var singleScrollOverride by rememberSaveable(uiPrefs.lessonLayoutMode) {
        mutableStateOf(uiPrefs.lessonLayoutMode == LessonLayoutMode.SINGLE_SCROLL)
    }

    val guideScroll = rememberScrollState()
    val stepsScroll = rememberScrollState()
    val labScroll = rememberScrollState()
    val singlePageScroll = rememberScrollState()

    val checkedSteps = remember(lesson?.id, lesson?.editorSteps?.size) {
        mutableStateListOf<Boolean>().apply {
            repeat(lesson?.editorSteps?.size ?: 0) { add(false) }
        }
    }
    val completedStepsCount = checkedSteps.count { it }

    Scaffold(
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            KadiTopBar(
                title = lesson?.title ?: "Lesson",
                subtitle = state.phaseTitle?.let {
                    val step = if (state.totalLessonsInPhase > 0) {
                        "L${lesson?.order ?: 1}/${state.totalLessonsInPhase}"
                    } else {
                        "L${lesson?.order ?: 1}"
                    }
                    "$step · $it"
                },
                onBack = onBack,
                actions = {
                    // Toggle 1-Page vs 3-Tab right in the top bar actions to save vertical space!
                    IconButton(
                        onClick = { singleScrollOverride = !singleScrollOverride },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ViewStream,
                            contentDescription = "Toggle 1-Page / 3-Tab view",
                            tint = if (singleScrollOverride) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                    IconButton(
                        onClick = vm::toggleBookmark,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (state.isBookmarked) Icons.Outlined.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = stringResource(R.string.action_bookmark),
                            tint = if (state.isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
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
                    val activeScroll = when {
                        singleScrollOverride -> singlePageScroll
                        activeTab == 0 -> guideScroll
                        activeTab == 1 -> stepsScroll
                        else -> labScroll
                    }
                    val readFraction = if (activeScroll.maxValue > 0) {
                        activeScroll.value.toFloat() / activeScroll.maxValue
                    } else {
                        0f
                    }

                    // 2dp reading progress line
                    LinearProgressIndicator(
                        progress = { readFraction.coerceIn(0f, 1f) },
                        modifier = Modifier.fillMaxWidth().height(2.dp),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    )

                    val hPad = studioScreenPadding()
                    if (!singleScrollOverride) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(horizontal = hPad, vertical = 5.dp)
                        ) {
                            StudioSegmentedTabs(
                                activeTab = activeTab,
                                stepsCount = lesson.editorSteps.size,
                                completedSteps = completedStepsCount,
                                quizCount = lesson.quizQuestions.size,
                                onSelectTab = { activeTab = it }
                            )
                        }
                        HorizontalDivider(
                            thickness = 0.75.dp,
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
                        )
                    }

                    if (singleScrollOverride) {
                        SingleScrollLessonContent(
                            lesson = lesson,
                            isCompleted = state.isCompleted,
                            checkedSteps = checkedSteps,
                            hasDebugChallenge = state.hasDebugChallenge && state.phaseId != null,
                            scrollState = singlePageScroll,
                            context = context,
                            onTermClick = onTermClick,
                            onPractice = onPractice,
                            onQuiz = onQuiz,
                            onDebugChallenge = { state.phaseId?.let(onDebugChallenge) }
                        )
                    } else {
                        when (activeTab) {
                            0 -> GuideAndCodeTab(
                                lesson = lesson,
                                isCompleted = state.isCompleted,
                                scrollState = guideScroll,
                                context = context,
                                onTermClick = onTermClick,
                                onSwitchToSteps = { activeTab = 1 }
                            )
                            1 -> GodotStepsTab(
                                lesson = lesson,
                                checkedSteps = checkedSteps,
                                scrollState = stepsScroll,
                                onTermClick = onTermClick,
                                onSwitchToLab = { activeTab = 2 }
                            )
                            else -> LabAndQuizTab(
                                lesson = lesson,
                                hasDebugChallenge = state.hasDebugChallenge && state.phaseId != null,
                                scrollState = labScroll,
                                onTermClick = onTermClick,
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
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LessonMetadataPillsRow(
    lesson: Lesson,
    isCompleted: Boolean,
    context: Context
) {
    val readingMinutes = remember(lesson.id) {
        ((lesson.bodyMarkdown.length + lesson.codeSnippets.sumOf { it.code.length }) / 950)
            .coerceAtLeast(4)
    }
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        if (isCompleted) {
            Pill(text = "Completed", color = successColor(), icon = Icons.Outlined.CheckCircle)
        }
        Pill(
            text = "~$readingMinutes min",
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
        shape = MaterialTheme.shapes.extraSmall,
        color = tint.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, tint.copy(alpha = 0.35f)),
        modifier = Modifier.clickable {
            onCopy()
            copied = true
        }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = if (copied) Icons.Outlined.Check else Icons.Outlined.Description,
                contentDescription = "Copy file path",
                tint = tint,
                modifier = Modifier.size(11.dp)
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
                modifier = Modifier.size(10.dp)
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
    val stepsLabel = if (stepsCount > 0) "Steps ($completedSteps/$stepsCount)" else "Steps"
    val labLabel = if (quizCount > 0) "Lab ($quizCount)" else "Lab"
    val tabs = listOf(
        Triple(0, "Guide", Icons.AutoMirrored.Outlined.MenuBook),
        Triple(1, stepsLabel, Icons.Outlined.Checklist),
        Triple(2, labLabel, Icons.Outlined.Lightbulb)
    )

    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            tabs.forEach { (idx, title, icon) ->
                val selected = activeTab == idx
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(MaterialTheme.shapes.extraSmall)
                        .clickable { onSelectTab(idx) },
                    shape = MaterialTheme.shapes.extraSmall,
                    color = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(12.dp)
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
private fun SingleScrollLessonContent(
    lesson: Lesson,
    isCompleted: Boolean,
    checkedSteps: MutableList<Boolean>,
    hasDebugChallenge: Boolean,
    scrollState: androidx.compose.foundation.ScrollState,
    context: Context,
    onTermClick: (String) -> Unit,
    onPractice: () -> Unit,
    onQuiz: () -> Unit,
    onDebugChallenge: () -> Unit
) {
    val hPad = studioScreenPadding()
    val vGap = studioCardSpacing()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = hPad, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(vGap)
    ) {
        LessonMetadataPillsRow(lesson = lesson, isCompleted = isCompleted, context = context)

        MarkdownText(markdown = lesson.bodyMarkdown, onTermClick = onTermClick)

        if (lesson.codeSnippets.isNotEmpty()) {
            SectionHeader(title = "Complete Reference Script", eyebrow = "COPY-READY GDSCRIPT")
            for (snippet in lesson.codeSnippets) {
                CodeBlock(snippet = snippet)
            }
        }

        lesson.sceneTree?.let { tree ->
            SceneTreeBlueprintCard(sceneTree = tree)
        }

        if (lesson.editorSteps.isNotEmpty()) {
            EditorChecklistStudioCard(
                steps = lesson.editorSteps,
                checkedStates = checkedSteps,
                onTermClick = onTermClick
            )
        }

        if (lesson.verificationSteps.isNotEmpty()) {
            VerificationStudioCard(steps = lesson.verificationSteps, onTermClick = onTermClick)
        }

        if (lesson.troubleshootingTips.isNotEmpty()) {
            TroubleshootingStudioCard(tips = lesson.troubleshootingTips, onTermClick = onTermClick)
        }

        if (lesson.keyTakeaways.isNotEmpty()) {
            KeyTakeawaysStudioCard(takeaways = lesson.keyTakeaways, onTermClick = onTermClick)
        }

        SectionHeader(title = "Interactive Exercises", eyebrow = "HANDS-ON LABS")

        lesson.practicePrompt?.let { prompt ->
            LabLaunchCard(
                eyebrow = "GDSCRIPT WORKBENCH",
                title = "Interactive Code Practice",
                description = prompt.instructions,
                buttonLabel = "Practice",
                icon = Icons.Outlined.Lightbulb,
                accent = MaterialTheme.colorScheme.secondary,
                onClick = onPractice,
                onTermClick = onTermClick
            )
        }

        if (lesson.quizQuestions.isNotEmpty()) {
            LabLaunchCard(
                eyebrow = "${lesson.quizQuestions.size} QUESTIONS",
                title = "Lesson Knowledge Quiz",
                description = "Check your understanding of **${lesson.title}** with instant explanations.",
                buttonLabel = "Start Quiz",
                icon = Icons.Outlined.Quiz,
                accent = MaterialTheme.colorScheme.primary,
                onClick = onQuiz,
                onTermClick = onTermClick
            )
        }

        if (hasDebugChallenge) {
            LabLaunchCard(
                eyebrow = "PHASE BUG HUNT",
                title = "Interactive Debug Challenge",
                description = "Inspect a broken GDScript snippet from this module and tap the exact line causing the bug.",
                buttonLabel = "Bug Hunt",
                icon = Icons.Outlined.BugReport,
                accent = MaterialTheme.colorScheme.tertiary,
                onClick = onDebugChallenge,
                onTermClick = onTermClick
            )
        }
        Spacer(Modifier.height(14.dp))
    }
}

@Composable
private fun GuideAndCodeTab(
    lesson: Lesson,
    isCompleted: Boolean,
    scrollState: androidx.compose.foundation.ScrollState,
    context: Context,
    onTermClick: (String) -> Unit,
    onSwitchToSteps: () -> Unit
) {
    val hPad = studioScreenPadding()
    val vGap = studioCardSpacing()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = hPad, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(vGap)
    ) {
        LessonMetadataPillsRow(lesson = lesson, isCompleted = isCompleted, context = context)

        MarkdownText(markdown = lesson.bodyMarkdown, onTermClick = onTermClick)

        if (lesson.codeSnippets.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                SectionHeader(
                    title = "Complete Reference Script",
                    eyebrow = "COPY-READY GDSCRIPT"
                )
                for (snippet in lesson.codeSnippets) {
                    CodeBlock(snippet = snippet)
                }
            }
        }

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
                        size = 36.dp
                    )
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "NEXT: BUILD IT IN GODOT 4.3",
                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "Open the Steps tab for the Scene Tree & ${lesson.editorSteps.size}-step checklist",
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
        Spacer(Modifier.height(14.dp))
    }
}

@Composable
private fun GodotStepsTab(
    lesson: Lesson,
    checkedSteps: MutableList<Boolean>,
    scrollState: androidx.compose.foundation.ScrollState,
    onTermClick: (String) -> Unit,
    onSwitchToLab: () -> Unit
) {
    val hPad = studioScreenPadding()
    val vGap = studioCardSpacing()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = hPad, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(vGap)
    ) {
        lesson.sceneTree?.let { tree ->
            SceneTreeBlueprintCard(sceneTree = tree)
        }

        if (lesson.editorSteps.isNotEmpty()) {
            EditorChecklistStudioCard(
                steps = lesson.editorSteps,
                checkedStates = checkedSteps,
                onTermClick = onTermClick
            )
        }

        if (lesson.verificationSteps.isNotEmpty()) {
            VerificationStudioCard(steps = lesson.verificationSteps, onTermClick = onTermClick)
        }

        if (lesson.troubleshootingTips.isNotEmpty()) {
            TroubleshootingStudioCard(tips = lesson.troubleshootingTips, onTermClick = onTermClick)
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
                    size = 36.dp
                )
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "READY TO TEST YOUR MASTERY?",
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Open Lab to practice GDScript and take the lesson quiz",
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
        Spacer(Modifier.height(14.dp))
    }
}

@Composable
private fun LabAndQuizTab(
    lesson: Lesson,
    hasDebugChallenge: Boolean,
    scrollState: androidx.compose.foundation.ScrollState,
    onTermClick: (String) -> Unit,
    onPractice: () -> Unit,
    onQuiz: () -> Unit,
    onDebugChallenge: () -> Unit
) {
    val hPad = studioScreenPadding()
    val vGap = studioCardSpacing()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = hPad, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(vGap)
    ) {
        if (lesson.keyTakeaways.isNotEmpty()) {
            KeyTakeawaysStudioCard(takeaways = lesson.keyTakeaways, onTermClick = onTermClick)
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
                buttonLabel = "Practice",
                icon = Icons.Outlined.Lightbulb,
                accent = MaterialTheme.colorScheme.secondary,
                onClick = onPractice,
                onTermClick = onTermClick
            )
        }

        if (lesson.quizQuestions.isNotEmpty()) {
            LabLaunchCard(
                eyebrow = "${lesson.quizQuestions.size} QUESTIONS",
                title = "Lesson Knowledge Quiz",
                description = "Check your understanding of **${lesson.title}** with instant explanations.",
                buttonLabel = "Start Quiz",
                icon = Icons.Outlined.Quiz,
                accent = MaterialTheme.colorScheme.primary,
                onClick = onQuiz,
                onTermClick = onTermClick
            )
        }

        if (hasDebugChallenge) {
            LabLaunchCard(
                eyebrow = "PHASE BUG HUNT",
                title = "Interactive Debug Challenge",
                description = "Inspect a broken GDScript snippet from this module and tap the exact line causing the bug.",
                buttonLabel = "Bug Hunt",
                icon = Icons.Outlined.BugReport,
                accent = MaterialTheme.colorScheme.tertiary,
                onClick = onDebugChallenge,
                onTermClick = onTermClick
            )
        }
        Spacer(Modifier.height(14.dp))
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
    onClick: () -> Unit,
    onTermClick: (String) -> Unit
) {
    KadiCard(onClick = onClick, accent = accent) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconBubble(icon = icon, tint = accent, size = 38.dp)
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
        RichInlineText(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis,
            onTermClick = onTermClick
        )
    }
}

@Composable
private fun SceneTreeBlueprintCard(sceneTree: String) {
    val isDark = isDarkScheme()
    val uiPrefs = LocalUiPreferences.current
    val bg = if (isDark) SyntaxColors.darkBackground else SyntaxColors.lightBackground
    val accent = MaterialTheme.colorScheme.secondary

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        color = bg,
        border = BorderStroke(1.dp, accent.copy(alpha = 0.40f))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(accent.copy(alpha = 0.12f))
                    .padding(horizontal = 12.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.AccountTree,
                    contentDescription = null,
                    tint = accent,
                    modifier = Modifier.size(15.dp)
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
                    .padding(12.dp)
            ) {
                Text(
                    text = sceneTree,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = CodeFontFamily,
                        fontSize = (13f * uiPrefs.codeFontScale).sp,
                        lineHeight = (20f * uiPrefs.codeFontScale).sp
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
    checkedStates: MutableList<Boolean>,
    onTermClick: (String) -> Unit
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
            IconBubble(icon = Icons.Outlined.Checklist, tint = accent, size = 34.dp)
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
                        .clip(MaterialTheme.shapes.small)
                        .background(
                            if (isChecked) success.copy(alpha = 0.10f)
                            else MaterialTheme.colorScheme.surfaceContainerLow
                        )
                        .border(
                            width = 1.dp,
                            color = if (isChecked) success.copy(alpha = 0.40f)
                            else MaterialTheme.colorScheme.outlineVariant,
                            shape = MaterialTheme.shapes.small
                        )
                        .clickable {
                            if (idx < checkedStates.size) {
                                checkedStates[idx] = !checkedStates[idx]
                            }
                        }
                        .padding(horizontal = 12.dp, vertical = 9.dp),
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
                    RichInlineText(
                        text = stepText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isChecked) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                        textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None,
                        modifier = Modifier.weight(1f),
                        onTermClick = onTermClick
                    )
                }
            }
        }
    }
}

@Composable
private fun VerificationStudioCard(steps: List<String>, onTermClick: (String) -> Unit) {
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
                    RichInlineText(
                        text = item,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f),
                        onTermClick = onTermClick
                    )
                }
            }
        }
    }
}

@Composable
private fun TroubleshootingStudioCard(tips: List<String>, onTermClick: (String) -> Unit) {
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
                    RichInlineText(
                        text = item,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f),
                        onTermClick = onTermClick
                    )
                }
            }
        }
    }
}

@Composable
private fun KeyTakeawaysStudioCard(takeaways: List<String>, onTermClick: (String) -> Unit) {
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
                    RichInlineText(
                        text = item,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f),
                        onTermClick = onTermClick
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
    val hPad = studioScreenPadding()
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth().navigationBarsPadding()) {
            HorizontalDivider(thickness = 0.75.dp, color = MaterialTheme.colorScheme.outlineVariant)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = hPad, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (previousLessonId != null) {
                    FilledTonalButton(
                        onClick = { onNavigateLesson(previousLessonId) },
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier.weight(0.85f).height(38.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("Prev", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, maxLines = 1)
                    }
                }
                Button(
                    onClick = onMarkComplete,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.weight(1.4f).height(38.dp)
                ) {
                    Icon(
                        Icons.Outlined.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        text = when {
                            isCompleted && hasQuiz -> "Retake Quiz"
                            isCompleted -> "Completed"
                            hasQuiz -> "Complete & Quiz"
                            else -> stringResource(R.string.action_mark_complete)
                        },
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (nextLessonId != null) {
                    FilledTonalButton(
                        onClick = { onNavigateLesson(nextLessonId) },
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier.weight(0.85f).height(38.dp)
                    ) {
                        Text("Next", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, maxLines = 1)
                        Spacer(Modifier.width(4.dp))
                        Icon(
                            Icons.AutoMirrored.Outlined.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }
        }
    }
}
