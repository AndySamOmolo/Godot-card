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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material.icons.outlined.Terminal
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
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
import com.kadicompanion.ui.common.SectionHeader
import com.kadicompanion.ui.theme.CodeFontFamily
import com.kadicompanion.ui.theme.LocalReduceMotion
import com.kadicompanion.ui.theme.SyntaxColors
import com.kadicompanion.ui.theme.isDarkScheme
import com.kadicompanion.ui.theme.successColor

/*
 * Practice reads like a coding exercise sheet: the task in a card, an optional
 * hint that expands in place, a dark editor-style scratch panel, and the model
 * solution kept behind a deliberate reveal.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeScreen(onBack: () -> Unit, vm: PracticeViewModel = hiltViewModel()) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    val reduceMotion = LocalReduceMotion.current
    val duration = if (reduceMotion) 0 else 240

    Scaffold(
        containerColor = Color.Transparent,
        topBar = { KadiTopBar(title = "Practice", subtitle = "Exercise", onBack = onBack) }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> LoadingSkeleton()
                state.lesson?.practicePrompt == null -> EmptyState(title = "No practice for this lesson", message = "Continue to the knowledge check.")
                else -> {
                    val lesson = state.lesson!!
                    val prompt = lesson.practicePrompt!!
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        KadiCard {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                IconBubble(icon = Icons.Outlined.EditNote, tint = MaterialTheme.colorScheme.primary)
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "YOUR TASK",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = lesson.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Pill(
                                    text = "Practice",
                                    color = MaterialTheme.colorScheme.secondary,
                                    icon = Icons.Outlined.Bolt
                                )
                            }
                            Text(
                                text = prompt.instructions,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        prompt.hint?.let { hint ->
                            if (!state.hintShown) {
                                OutlinedButton(
                                    onClick = vm::toggleHint,
                                    shape = MaterialTheme.shapes.large,
                                    modifier = Modifier.fillMaxWidth().height(50.dp)
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
                                    Text(
                                        text = hint,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        SectionHeader(title = "Your workspace", eyebrow = "Write it yourself")
                        EditorPanel()

                        prompt.solutionCode?.let { code ->
                            if (!state.solutionShown) {
                                Button(
                                    onClick = vm::revealSolution,
                                    shape = MaterialTheme.shapes.large,
                                    modifier = Modifier.fillMaxWidth().height(52.dp)
                                ) {
                                    Icon(Icons.Outlined.LockOpen, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text(stringResource(R.string.action_reveal_solution), fontWeight = FontWeight.Bold)
                                }
                                Text(
                                    text = "Try it in Godot first — the solution sticks better afterwards.",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            AnimatedVisibility(
                                visible = state.solutionShown,
                                enter = fadeIn(animationSpec = tween(durationMillis = duration)) +
                                    expandVertically(animationSpec = tween(durationMillis = duration))
                            ) {
                                SolutionPanel(code = code)
                            }
                        }

                        Spacer(Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}

/** Dark monospace scratch panel — the "type it here" surface of the exercise. */
@Composable
private fun EditorPanel() {
    val dark = isDarkScheme()
    val panel = if (dark) SyntaxColors.darkBackground else SyntaxColors.lightBackground
    val quiet = if (dark) SyntaxColors.darkComment else SyntaxColors.lightComment
    val shape = RoundedCornerShape(18.dp)
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
                .padding(horizontal = 14.dp, vertical = 8.dp),
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
                text = "SCRATCHPAD",
                style = MaterialTheme.typography.labelSmall,
                fontFamily = CodeFontFamily,
                fontWeight = FontWeight.Bold,
                color = quiet
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            EditorLine(1, "# write your attempt here", quiet)
            EditorLine(2, "# then compare with the solution", quiet)
            EditorLine(3, "", quiet)
        }
    }
}

@Composable
private fun EditorLine(number: Int, text: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = number.toString(),
            style = MaterialTheme.typography.bodySmall,
            fontFamily = CodeFontFamily,
            fontSize = 13.sp,
            color = color.copy(alpha = 0.6f),
            modifier = Modifier.width(24.dp)
        )
        if (text.isEmpty()) {
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(16.dp)
                    .background(color.copy(alpha = 0.7f))
            )
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                fontFamily = CodeFontFamily,
                fontSize = 13.sp,
                color = color
            )
        }
    }
}

/** The revealed solution gets its own success-tinted frame so it reads as an answer. */
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
                text = "SOLUTION",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = success
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(success.copy(alpha = 0.10f))
                .border(BorderStroke(1.dp, success.copy(alpha = 0.35f)), RoundedCornerShape(22.dp))
                .padding(10.dp)
        ) {
            CodeBlock(snippet = CodeSnippet(id = "sol", code = code, caption = "One possible solution"))
        }
    }
}
