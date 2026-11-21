package com.kadicompanion.ui.tutorial.debugchallenge

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.heightIn
import androidx.compose.ui.Alignment
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
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
import com.kadicompanion.ui.common.RichInlineText
import com.kadicompanion.ui.common.SectionHeader
import com.kadicompanion.ui.common.studioCardSpacing
import com.kadicompanion.ui.common.studioScreenPadding
import com.kadicompanion.ui.theme.CodeFontFamily
import com.kadicompanion.ui.theme.LocalReduceMotion
import com.kadicompanion.ui.theme.LocalUiPreferences
import com.kadicompanion.ui.theme.heroTextPrimary
import com.kadicompanion.ui.theme.heroTextSecondary
import com.kadicompanion.ui.theme.heroTextTertiary
import com.kadicompanion.ui.theme.isDarkScheme
import com.kadicompanion.ui.theme.successColor
import com.kadicompanion.ui.theme.kadiColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebugChallengeScreen(onBack: () -> Unit, onComplete: () -> Unit, vm: DebugChallengeViewModel = hiltViewModel()) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    val reduceMotion = LocalReduceMotion.current
    val duration = if (reduceMotion) 0 else 260
    val hPad = studioScreenPadding()
    val vGap = studioCardSpacing()

    Scaffold(
        containerColor = Color.Transparent,
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0),
        topBar = { KadiTopBar(title = "Fix the Bug", subtitle = "INTERACTIVE DEBUG CHALLENGE", onBack = onBack) }
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
                            .padding(horizontal = hPad, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(vGap)
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
                            KadiCard(accent = accent, modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }) {
                                RichInlineText(
                                    text = feedback,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        if (!state.fixShown) {
                            Row(
                                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                if (!ch.hint.isNullOrBlank()) {
                                    OutlinedButton(
                                        onClick = vm::toggleHint,
                                        shape = MaterialTheme.shapes.medium,
                                        modifier = Modifier.weight(1f).heightIn(min = 50.dp).fillMaxHeight()
                                    ) {
                                        Icon(Icons.Outlined.Lightbulb, contentDescription = null, modifier = Modifier.size(18.dp))
                                        Spacer(Modifier.width(6.dp))
                                        Text(
                                            text = if (state.hintShown) "Hide Hint" else "Show Hint",
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                    }
                                }
                                OutlinedButton(
                                    onClick = vm::revealFix,
                                    shape = MaterialTheme.shapes.medium,
                                    modifier = Modifier.weight(1f).heightIn(min = 50.dp).fillMaxHeight()
                                ) {
                                    Icon(Icons.Outlined.Visibility, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(Modifier.width(6.dp))
                                    Text(
                                        text = stringResource(R.string.action_reveal_fix),
                                        maxLines = 1,
                                        softWrap = false
                                    )
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
                                    RichInlineText(
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
                                verticalArrangement = Arrangement.spacedBy(vGap)
                            ) {
                                SolvedBanner(solvedByAttempt = state.solvedByAttempt)

                                SectionHeader(
                                    title = stringResource(R.string.debug_fixed_code),
                                    eyebrow = "THE FIX"
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
                                    shape = MaterialTheme.shapes.medium,
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
    val uiPrefs = LocalUiPreferences.current
    val palette = remember(uiPrefs.codeThemePreset, isDark) {
        GDScriptHighlighter.paletteFor(uiPrefs.codeThemePreset, isDark)
    }
    val bg = palette.background
    val lines = remember(brokenCode) { brokenCode.lines() }
    val highlightedLines = remember(lines, isDark, uiPrefs.codeThemePreset) {
        lines.map { GDScriptHighlighter.highlight(it, isDark, uiPrefs.codeThemePreset) }
    }
    val success = successColor()
    val codeFontSize = (13f * uiPrefs.codeFontScale).sp
    val codeLineHeight = (20f * uiPrefs.codeFontScale).sp

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = bg,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f))
    ) {
        val scrollMod = if (uiPrefs.codeWordWrap) Modifier.fillMaxWidth() else Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())
        Column(
            modifier = scrollMod.padding(vertical = 10.dp)
        ) {
            lines.forEachIndexed { idx, _ ->
                val lineNum = idx + 1
                val isSelected = selectedLine == lineNum
                val isBuggy = lineNum == targetBuggyLine
                val rowBg = when {
                    fixShown && isBuggy -> success.copy(alpha = 0.18f)
                    isSelected && !isBuggy -> MaterialTheme.colorScheme.error.copy(alpha = 0.16f)
                    isSelected && isBuggy -> success.copy(alpha = 0.18f)
                    else -> Color.Transparent
                }
                val statusLabel = when {
                    fixShown && isBuggy -> "Buggy line"
                    isSelected && !isBuggy -> "Selected, no bug on this line"
                    isSelected && isBuggy -> "Selected, bug found"
                    else -> "Line $lineNum"
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 38.dp)
                        .background(rowBg)
                        .clickable(enabled = !fixShown, role = Role.Button) { onLineClick(lineNum) }
                        .semantics(mergeDescendants = true) { stateDescription = statusLabel }
                        .padding(horizontal = 14.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = lineNum.toString().padStart(2, ' '),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = CodeFontFamily,
                            fontSize = (12f * uiPrefs.codeFontScale).sp,
                            lineHeight = codeLineHeight
                        ),
                        color = if (fixShown && isBuggy) success else palette.comment,
                        fontWeight = if (isSelected || (fixShown && isBuggy)) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.width(28.dp)
                    )
                    Text(
                        text = highlightedLines[idx],
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = CodeFontFamily,
                            fontSize = codeFontSize,
                            lineHeight = codeLineHeight
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun BugHero(symptom: String) {
    val heroText = heroTextPrimary()
    val heroTextSub = heroTextSecondary()
    val heroTextMuted = heroTextTertiary()
    HeroPanel(
        gradient = Brush.linearGradient(
            colors = listOf(MaterialTheme.kadiColors.bugHeroGradientStart, MaterialTheme.kadiColors.bugHeroGradientMid, MaterialTheme.kadiColors.bugHeroGradientEnd)
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(heroText.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.BugReport,
                    contentDescription = null,
                    tint = heroText,
                    modifier = Modifier.size(22.dp)
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
                    fontWeight = FontWeight.Bold,
                    color = heroText
                )
            }
        }
        RichInlineText(
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
