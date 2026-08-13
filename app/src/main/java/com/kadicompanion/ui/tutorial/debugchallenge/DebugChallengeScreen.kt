package com.kadicompanion.ui.tutorial.debugchallenge

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.kadicompanion.domain.model.CodeSnippet
import com.kadicompanion.ui.common.CodeBlock
import com.kadicompanion.ui.common.EmptyState
import com.kadicompanion.ui.common.HeroPanel
import com.kadicompanion.ui.common.IconBubble
import com.kadicompanion.ui.common.KadiCard
import com.kadicompanion.ui.common.KadiTopBar
import com.kadicompanion.ui.common.LoadingSkeleton
import com.kadicompanion.ui.common.MarkdownText
import com.kadicompanion.ui.common.Pill
import com.kadicompanion.ui.common.SectionHeader
import com.kadicompanion.ui.theme.LocalReduceMotion
import com.kadicompanion.ui.theme.heroTextPrimary
import com.kadicompanion.ui.theme.heroTextSecondary
import com.kadicompanion.ui.theme.heroTextTertiary
import com.kadicompanion.ui.theme.successColor

/*
 * A bug hunt: a crimson hero states the symptom, the broken snippet sits in the
 * shared code panel, and solving it flips the screen into a green success state
 * with the fix and the explanation.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebugChallengeScreen(onBack: () -> Unit, onComplete: () -> Unit, vm: DebugChallengeViewModel = hiltViewModel()) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    val reduceMotion = LocalReduceMotion.current
    val duration = if (reduceMotion) 0 else 260

    Scaffold(
        containerColor = Color.Transparent,
        topBar = { KadiTopBar(title = "Fix the Bug", subtitle = "Debug challenge", onBack = onBack) }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> LoadingSkeleton()
                state.challenge == null -> EmptyState(title = "No debug challenge", message = "This phase doesn't have a debug challenge.")
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
                            title = stringResource(R.string.debug_broken_code),
                            eyebrow = "Bug hunt",
                            trailing = { Pill(text = "1 bug", color = MaterialTheme.colorScheme.tertiary, icon = Icons.Outlined.BugReport) }
                        )
                        CodeBlock(snippet = CodeSnippet(id = "broken", code = ch.brokenCode, caption = "What's wrong here?"))

                        if (!state.fixShown) {
                            Button(
                                onClick = vm::revealFix,
                                shape = MaterialTheme.shapes.large,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.tertiary,
                                    contentColor = MaterialTheme.colorScheme.onTertiary
                                ),
                                modifier = Modifier.fillMaxWidth().height(54.dp)
                            ) {
                                Icon(Icons.Outlined.Visibility, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(8.dp))
                                Text(stringResource(R.string.action_reveal_fix), fontWeight = FontWeight.Bold)
                            }
                            Text(
                                text = "Try to spot the bug first. Reveal the fix when you're ready.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
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
                                SolvedBanner()

                                SectionHeader(
                                    title = stringResource(R.string.debug_fixed_code),
                                    eyebrow = "The fix"
                                )
                                CodeBlock(snippet = CodeSnippet(id = "fixed", code = ch.fixedCode, caption = "One way to fix it"))

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

/** Crimson-accented hero that frames the symptom of the bug. */
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
                    text = "Something is misbehaving",
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

/** Celebratory strip shown once the fix is revealed. */
@Composable
private fun SolvedBanner() {
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
                    text = "BUG SQUASHED",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = success
                )
                Text(
                    text = "Nice catch — here's the fix",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
