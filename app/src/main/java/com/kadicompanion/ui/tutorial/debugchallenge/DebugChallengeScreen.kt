package com.kadicompanion.ui.tutorial.debugchallenge

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.TouchApp
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kadicompanion.R
import com.kadicompanion.domain.model.CodeSnippet
import com.kadicompanion.ui.common.CodeBlock
import com.kadicompanion.ui.common.EmptyState
import com.kadicompanion.ui.common.GDScriptHighlighter
import com.kadicompanion.ui.common.HeroPanel
import com.kadicompanion.ui.common.IconBubble
import com.kadicompanion.ui.common.KadiCard
import com.kadicompanion.ui.common.KadiTopBar
import com.kadicompanion.ui.common.LoadingSkeleton
import com.kadicompanion.ui.common.MarkdownText
import com.kadicompanion.ui.common.Pill
import com.kadicompanion.ui.common.SectionHeader
import com.kadicompanion.ui.theme.CodeFontFamily
import com.kadicompanion.ui.theme.LocalReduceMotion
import com.kadicompanion.ui.theme.SyntaxColors
import com.kadicompanion.ui.theme.heroTextPrimary
import com.kadicompanion.ui.theme.heroTextSecondary
import com.kadicompanion.ui.theme.heroTextTertiary
import com.kadicompanion.ui.theme.isDarkScheme
import com.kadicompanion.ui.theme.successColor

/**
 * Interactive Bug Hunt:
 * - Crimson hero states the gameplay symptom
 * - Tappable line-by-line GDScript view lets the learner tap the exact buggy line to solve it!
 * - Optional Hint toggle and Reveal Fix fallback
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebugChallengeScreen(onBack: () -> Unit, onComplete: () -> Unit, vm: DebugChallengeViewModel = hiltViewModel()) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    val reduceMotion = LocalReduceMotion.current
    val duration = if (reduceMotion) 0 else 260

    Scaffold(
        containerColor = Color.Transparent,
        topBar = { KadiTopBar(title = "Fix the Bug", subtitle = "Interactive debug challenge", onBack = onBack) }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> LoadingSkeleton()
                state.challenge == null -> EmptyState(
                    title = "No debug challenge",
                    message = "This phase doesn't have a debug challenge."
                )
                else -> {
                    val ch = state.challenge!!
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        BugHero(symptom = ch.bugDescription)

                        SectionHeader(
                            title = "Tap the buggy line below",
                            eyebrow = stringResource(R.string.debug_broken_code),
                            trailing = {
                                Pill(
                                    text = "Tap a line",
                                    color = MaterialTheme.colorScheme.tertiary,
                                    icon = Icons.Outlined.TouchApp
                                )
                            }
                        )

                        TappableBrokenCodePanel(
                            brokenCode = ch.brokenCode,
                            selectedLine = state.selectedLineNumber,
                            targetBuggyLine = state.targetBuggyLine,
                            fixShown = state.fixShown,
                            onLineClick = vm::selectLine
                        )

                        state.attemptFeedback?.let { feedback ->
                            val isSolved = state.solvedByAttempt
                            val accent = if (isSolved) successColor() else MaterialTheme.colorScheme.tertiary
                            KadiCard(accent = accent) {
                                Text(
                                    text = feedback,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        if (!state.fixShown) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                if (!ch.hint.isNullOrBlank()) {
                                    OutlinedButton(
                                        onClick = vm::toggleHint,
                                        shape = MaterialTheme.shapes.large,
                                        modifier = Modifier.weight(1f).height(50.dp)
                                    ) {
                                        Icon(Icons.Outlined.Lightbulb, contentDescription = null, modifier = Modifier.size(18.dp))
                                        Spacer(Modifier.width(6.dp))
                                        Text(if (state.hintShown) "Hide Hint" else "Show Hint")
                                    }
                                }
                                OutlinedButton(
                                    onClick = vm::revealFix,
                                    shape = MaterialTheme.shapes.large,
                                    modifier = Modifier.weight(1f).height(50.dp)
                                ) {
                                    Icon(Icons.Outlined.Visibility, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(Modifier.width(6.dp))
                                    Text(stringResource(R.string.action_reveal_fix))
                                }
                            }

                            AnimatedVisibility(visible = state.hintShown && !ch.hint.isNullOrBlank()) {
                                KadiCard(accent = MaterialTheme.colorScheme.primary) {
                                    Text(
                                        text = "DEBUGGING HINT",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = ch.hint.orEmpty(),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        AnimatedVisibility(
                            visible = state.fixShown,
                            enter = fadeIn(animationSpec = tween(durationMillis = duration)) +
                                expandVertically(animationSpec = tween(durationMillis = duration))
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                SolvedBanner(solvedByAttempt = state.solvedByAttempt)

                                SectionHeader(
                                    title = stringResource(R.string.debug_fixed_code),
                                    eyebrow = "The fix"
                                )
                                CodeBlock(snippet = CodeSnippet(id = "fixed", code = ch.fixedCode, caption = "Corrected GDScript"))

                                KadiCard {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        IconBubble(
                                            icon = Icons.AutoMirrored.Outlined.MenuBook,
                                            tint = MaterialTheme.colorScheme.secondary,
                                            size = 32.dp
                                        )
                                        Text(
                                            text = stringResource(R.string.debug_explanation).uppercase(),
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                    }
                                    MarkdownText(markdown = ch.explanation)
                                }

                                Button(
                                    onClick = onComplete,
                                    shape = MaterialTheme.shapes.large,
                                    modifier = Modifier.fillMaxWidth().height(54.dp)
                                ) {
                                    Text("Continue", fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun TappableBrokenCodePanel(
    brokenCode: String,
    selectedLine: Int?,
    targetBuggyLine: Int,
    fixShown: Boolean,
    onLineClick: (Int) -> Unit
) {
    val isDark = isDarkScheme()
    val bg = if (isDark) SyntaxColors.darkBackground else SyntaxColors.lightBackground
    val lines = brokenCode.lines()
    val success = successColor()

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = bg,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 10.dp)
        ) {
            lines.forEachIndexed { idx, lineText ->
                val lineNum = idx + 1
                val isSelected = selectedLine == lineNum
                val isBuggy = lineNum == targetBuggyLine
                val rowBg = when {
                    fixShown && isBuggy -> success.copy(alpha = 0.18f)
                    isSelected && !isBuggy -> MaterialTheme.colorScheme.error.copy(alpha = 0.16f)
                    isSelected && isBuggy -> success.copy(alpha = 0.18f)
                    else -> Color.Transparent
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(rowBg)
                        .clickable(enabled = !fixShown) { onLineClick(lineNum) }
                        .padding(horizontal = 14.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = lineNum.toString().padStart(2, ' '),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = CodeFontFamily,
                            fontSize = 12.sp
                        ),
                        color = if (fixShown && isBuggy) success else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        fontWeight = if (isSelected || (fixShown && isBuggy)) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.width(28.dp)
                    )
                    Text(
                        text = GDScriptHighlighter.highlight(lineText, isDark),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = CodeFontFamily,
                            fontSize = 13.sp,
                            lineHeight = 20.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun BugHero(symptom: String) {
    val scheme = MaterialTheme.colorScheme
    val heroText = heroTextPrimary()
    val heroTextSub = heroTextSecondary()
    val heroTextMuted = heroTextTertiary()
    HeroPanel(
        gradient = Brush.linearGradient(
            colors = listOf(scheme.tertiary, scheme.tertiary.copy(alpha = 0.85f), scheme.primary)
        ),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(heroText.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.BugReport,
                    contentDescription = null,
                    tint = heroText,
                    modifier = Modifier.size(24.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.debug_symptom).uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = heroTextMuted
                )
                Text(
                    text = "Spot the bug in the GDScript",
                    style = MaterialTheme.typography.titleLarge,
                    color = heroText
                )
            }
        }
        Text(
            text = symptom,
            style = MaterialTheme.typography.bodyLarge,
            color = heroTextSub
        )
    }
}

@Composable
private fun SolvedBanner(solvedByAttempt: Boolean) {
    val success = successColor()
    KadiCard(accent = success) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(success.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.Check,
                    contentDescription = null,
                    tint = success,
                    modifier = Modifier.size(26.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (solvedByAttempt) "BUG SQUASHED ON YOUR OWN!" else "BUG REVEALED",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = success
                )
                Text(
                    text = if (solvedByAttempt) "Great debugging eyes — you tapped the exact buggy line!" else "Study the fix below and compare the two lines.",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
