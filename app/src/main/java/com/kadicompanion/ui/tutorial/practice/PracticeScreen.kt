package com.kadicompanion.ui.tutorial.practice

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Terminal
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kadicompanion.R
import com.kadicompanion.domain.model.CodeSnippet
import com.kadicompanion.ui.common.CodeBlock
import com.kadicompanion.ui.common.EmptyState
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
import com.kadicompanion.ui.theme.LocalReduceMotion
import com.kadicompanion.ui.theme.LocalUiPreferences
import com.kadicompanion.ui.theme.SyntaxColors
import com.kadicompanion.ui.theme.isDarkScheme
import com.kadicompanion.ui.theme.successColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeScreen(onBack: () -> Unit, vm: PracticeViewModel = hiltViewModel()) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    val reduceMotion = LocalReduceMotion.current
    val duration = if (reduceMotion) 0 else 240
    val hPad = studioScreenPadding()
    val vGap = studioCardSpacing()

    Scaffold(
        containerColor = Color.Transparent,
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0),
        topBar = { KadiTopBar(title = "Practice Workbench", subtitle = "INTERACTIVE EXERCISE", onBack = onBack) }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> LoadingSkeleton()
                state.lesson?.practicePrompt == null -> EmptyState(
                    title = "No practice for this lesson",
                    message = "Continue to the knowledge check."
                )
                else -> {
                    val lesson = state.lesson!!
                    val prompt = lesson.practicePrompt!!
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = hPad, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(vGap)
                    ) {
                        KadiCard(accent = MaterialTheme.colorScheme.primary) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                IconBubble(icon = Icons.Outlined.EditNote, tint = MaterialTheme.colorScheme.primary)
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "YOUR CODING TASK",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = cleanPlainText(lesson.title),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Pill(
                                    text = "GDScript 4.3",
                                    color = MaterialTheme.colorScheme.secondary,
                                    icon = Icons.Outlined.Bolt
                                )
                            }
                            RichInlineText(
                                text = prompt.instructions,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            lesson.targetFile?.let { path ->
                                Text(
                                    text = "Target file in Godot: $path",
                                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        prompt.hint?.let { hint ->
                            if (!state.hintShown) {
                                OutlinedButton(
                                    onClick = vm::toggleHint,
                                    shape = MaterialTheme.shapes.medium,
                                    modifier = Modifier.fillMaxWidth().height(48.dp)
                                ) {
                                    Icon(Icons.Outlined.Lightbulb, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text(stringResource(R.string.action_reveal_hint), fontWeight = FontWeight.SemiBold)
                                }
                            }
                            AnimatedVisibility(
                                visible = state.hintShown,
                                enter = fadeIn(animationSpec = tween(durationMillis = duration)) +
                                    expandVertically(animationSpec = tween(durationMillis = duration)),
                                exit = fadeOut(animationSpec = tween(durationMillis = duration)) +
                                    shrinkVertically(animationSpec = tween(durationMillis = duration))
                            ) {
                                KadiCard(accent = MaterialTheme.colorScheme.primary) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        IconBubble(
                                            icon = Icons.Outlined.Lightbulb,
                                            tint = MaterialTheme.colorScheme.primary,
                                            size = 32.dp
                                        )
                                        Text(
                                            text = "HINT",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    RichInlineText(
                                        text = hint,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        SectionHeader(title = "Interactive GDScript Editor", eyebrow = "WRITE & CHECK YOUR CODE")

                        InteractiveEditorPanel(
                            code = state.userCode,
                            onCodeChange = vm::updateUserCode,
                            onInsertToken = vm::insertToken,
                            onReset = vm::resetCode
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = vm::checkCode,
                                shape = MaterialTheme.shapes.medium,
                                modifier = Modifier.weight(1.3f).height(50.dp)
                            ) {
                                Icon(Icons.Outlined.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(6.dp))
                                Text("Check My Code", fontWeight = FontWeight.Bold)
                            }
                            if (!state.solutionShown && prompt.solutionCode != null) {
                                FilledTonalButton(
                                    onClick = vm::revealSolution,
                                    shape = MaterialTheme.shapes.medium,
                                    modifier = Modifier.weight(1f).height(50.dp)
                                ) {
                                    Icon(Icons.Outlined.LockOpen, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(Modifier.width(6.dp))
                                    Text("Solution", fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }

                        state.checkFeedback?.let { feedback ->
                            val passed = state.checkPassed == true
                            val accent = if (passed) successColor() else MaterialTheme.colorScheme.tertiary
                            KadiCard(accent = accent) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    IconBubble(
                                        icon = if (passed) Icons.Outlined.CheckCircle else Icons.Outlined.ErrorOutline,
                                        tint = accent,
                                        size = 34.dp
                                    )
                                    Text(
                                        text = if (passed) "CHECK PASSED!" else "KEEP TWEAKING",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = accent
                                    )
                                }
                                RichInlineText(
                                    text = feedback,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        prompt.solutionCode?.let { code ->
                            AnimatedVisibility(
                                visible = state.solutionShown || state.checkPassed == true,
                                enter = fadeIn(animationSpec = tween(durationMillis = duration)) +
                                    expandVertically(animationSpec = tween(durationMillis = duration))
                            ) {
                                SolutionPanel(code = code)
                            }
                        }

                        Spacer(Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

private val QUICK_TOKENS = listOf(
    "func ", "var ", "const ", "if ", "elif ", "else:", "for ", "in ",
    "while ", "match ", "return ", "-> ", ": ", "==", "!=", "and ", "or ", "true", "false"
)

@Composable
private fun InteractiveEditorPanel(
    code: String,
    onCodeChange: (String) -> Unit,
    onInsertToken: (String) -> Unit,
    onReset: () -> Unit
) {
    val dark = isDarkScheme()
    val uiPrefs = LocalUiPreferences.current
    val panel = if (dark) SyntaxColors.darkBackground else SyntaxColors.lightBackground
    val quiet = if (dark) SyntaxColors.darkComment else SyntaxColors.lightComment
    val shape = MaterialTheme.shapes.medium

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(panel)
            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f)), shape)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (dark) Color.White.copy(alpha = 0.05f) else Color.Black.copy(alpha = 0.04f))
                .padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                Icons.Outlined.Terminal,
                contentDescription = null,
                tint = quiet,
                modifier = Modifier.size(15.dp)
            )
            Text(
                text = "GDSCRIPT WORKBENCH",
                style = MaterialTheme.typography.labelSmall,
                fontFamily = CodeFontFamily,
                fontWeight = FontWeight.Bold,
                color = quiet,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onReset, modifier = Modifier.size(32.dp)) {
                Icon(
                    Icons.Outlined.Refresh,
                    contentDescription = "Reset starter code",
                    tint = quiet,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            QUICK_TOKENS.forEach { token ->
                Surface(
                    shape = MaterialTheme.shapes.extraSmall,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.14f),
                    border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)),
                    modifier = Modifier.clickable { onInsertToken(token) }
                ) {
                    Text(
                        text = token.trim(),
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        OutlinedTextField(
            value = code,
            onValueChange = onCodeChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            textStyle = TextStyle(
                fontFamily = CodeFontFamily,
                fontSize = (13f * uiPrefs.codeFontScale).sp,
                lineHeight = (20f * uiPrefs.codeFontScale).sp,
                color = MaterialTheme.colorScheme.onSurface
            ),
            minLines = 5,
            maxLines = 14,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                unfocusedBorderColor = Color.Transparent
            )
        )
    }
}

@Composable
private fun SolutionPanel(code: String) {
    val success = successColor()
    Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IconBubble(icon = Icons.Outlined.LockOpen, tint = success, size = 32.dp)
            Text(
                text = "REFERENCE SOLUTION",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = success
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .background(success.copy(alpha = 0.10f))
                .border(BorderStroke(1.dp, success.copy(alpha = 0.35f)), MaterialTheme.shapes.medium)
                .padding(10.dp)
        ) {
            CodeBlock(snippet = CodeSnippet(id = "sol", code = code, caption = "Copy this into your Godot script and press **F5** to test!"))
        }
    }
}
