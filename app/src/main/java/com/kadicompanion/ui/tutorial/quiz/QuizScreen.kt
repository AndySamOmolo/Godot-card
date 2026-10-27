package com.kadicompanion.ui.tutorial.quiz

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kadicompanion.R
import com.kadicompanion.ui.common.EmptyState
import com.kadicompanion.ui.common.IconBubble
import com.kadicompanion.ui.common.KadiCard
import com.kadicompanion.ui.common.KadiTopBar
import com.kadicompanion.ui.common.LoadingSkeleton
import com.kadicompanion.ui.common.Pill
import com.kadicompanion.ui.common.ProgressRing
import com.kadicompanion.ui.common.RichInlineText
import com.kadicompanion.ui.common.studioCardSpacing
import com.kadicompanion.ui.common.studioScreenPadding
import com.kadicompanion.ui.theme.CodeFontFamily
import com.kadicompanion.ui.theme.LocalReduceMotion
import com.kadicompanion.ui.theme.successColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(onBack: () -> Unit, onComplete: () -> Unit, vm: QuizViewModel = hiltViewModel()) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    val reduceMotion = LocalReduceMotion.current
    val duration = if (reduceMotion) 0 else 220
    val hPad = studioScreenPadding()
    val vGap = studioCardSpacing()

    Scaffold(
        containerColor = Color.Transparent,
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0),
        topBar = {
            KadiTopBar(
                title = "Knowledge Check",
                subtitle = if (!state.finished && state.questions.isNotEmpty()) {
                    "QUESTION ${state.currentIndex + 1} OF ${state.questions.size}"
                } else "QUIZ RESULTS",
                onBack = onBack,
                actions = {
                    if (!state.finished && state.questions.isNotEmpty()) {
                        Pill(
                            text = "${state.currentIndex + 1}/${state.questions.size}",
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> LoadingSkeleton()
                state.questions.isEmpty() -> EmptyState(
                    title = "No quiz yet",
                    message = "This lesson doesn't have a quiz."
                )
                state.finished -> QuizCelebration(
                    correctCount = state.firstTryCorrectCount,
                    totalCount = state.questions.size,
                    fraction = state.scoreFraction,
                    onRetake = vm::restartQuiz,
                    onComplete = onComplete
                )
                else -> {
                    AnimatedContent(
                        targetState = state.currentIndex,
                        transitionSpec = {
                            fadeIn(tween(duration)) togetherWith fadeOut(tween(duration))
                        },
                        label = "quizTransition"
                    ) { _ ->
                        val q = state.current!!
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = hPad, vertical = 14.dp),
                            verticalArrangement = Arrangement.spacedBy(vGap)
                        ) {
                            LinearProgressIndicator(
                                progress = { (state.currentIndex + 1).toFloat() / state.questions.size },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.10f),
                                strokeCap = StrokeCap.Round
                            )
                            Pill(text = q.concept, color = MaterialTheme.colorScheme.secondary)
                            RichInlineText(
                                text = q.prompt,
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            q.choices.forEachIndexed { idx, choice ->
                                AnswerOption(
                                    index = idx,
                                    text = choice,
                                    isSelected = state.selectedChoice == idx,
                                    isCorrect = idx == q.correctChoiceIndex,
                                    showResult = state.wasCorrect != null,
                                    onClick = { vm.selectAnswer(idx) }
                                )
                            }
                            state.wasCorrect?.let { correct ->
                                val success = successColor()
                                KadiCard(
                                    accent = if (correct) success else MaterialTheme.colorScheme.error
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        IconBubble(
                                            icon = if (correct) Icons.Outlined.Check else Icons.Outlined.Close,
                                            tint = if (correct) success else MaterialTheme.colorScheme.error,
                                            size = 36.dp
                                        )
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = if (correct) stringResource(R.string.quiz_correct)
                                                else stringResource(R.string.quiz_wrong),
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = stringResource(R.string.quiz_explanation).uppercase(),
                                                style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                                                fontWeight = FontWeight.Bold,
                                                color = if (correct) success else MaterialTheme.colorScheme.error
                                            )
                                        }
                                    }
                                    RichInlineText(
                                        text = q.explanation,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    if (!correct && state.canRetry) {
                                        OutlinedButton(
                                            onClick = vm::retry,
                                            modifier = Modifier.weight(1f).height(46.dp),
                                            shape = MaterialTheme.shapes.small
                                        ) { Text("Try Again", fontWeight = FontWeight.SemiBold) }
                                        Button(
                                            onClick = vm::next,
                                            modifier = Modifier.weight(1f).height(46.dp),
                                            shape = MaterialTheme.shapes.small
                                        ) { Text(if (state.isLast) "Finish" else "Next", fontWeight = FontWeight.Bold) }
                                    } else {
                                        Button(
                                            onClick = vm::next,
                                            modifier = Modifier.fillMaxWidth().height(46.dp),
                                            shape = MaterialTheme.shapes.small
                                        ) {
                                            Text(
                                                if (state.isLast) "See Results" else "Next Question",
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuizCelebration(
    correctCount: Int,
    totalCount: Int,
    fraction: Float,
    onRetake: () -> Unit,
    onComplete: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surfaceContainer,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                ProgressRing(fraction = fraction, size = 104.dp, strokeWidth = 9.dp, label = "score")
                Text(
                    text = if (correctCount == totalCount) "Perfect First-Try Score!" else "Quiz Completed",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "You answered $correctCount of $totalCount questions right on your first attempt.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (correctCount < totalCount) {
                        OutlinedButton(
                            onClick = onRetake,
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = MaterialTheme.shapes.small
                        ) {
                            Icon(Icons.Outlined.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Retake", fontWeight = FontWeight.SemiBold)
                        }
                    }
                    Button(
                        onClick = onComplete,
                        modifier = Modifier.weight(1.2f).height(48.dp),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Icon(Icons.Outlined.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Back to Lesson", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun AnswerOption(
    index: Int,
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    showResult: Boolean,
    onClick: () -> Unit
) {
    val success = successColor()
    val containerColor = when {
        showResult && isCorrect -> success.copy(alpha = 0.12f)
        showResult && isSelected && !isCorrect -> MaterialTheme.colorScheme.error.copy(alpha = 0.12f)
        isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
        else -> MaterialTheme.colorScheme.surfaceContainer
    }
    val borderColor = when {
        showResult && isCorrect -> success
        showResult && isSelected && !isCorrect -> MaterialTheme.colorScheme.error
        isSelected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outlineVariant
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            disabledContainerColor = containerColor
        ),
        border = BorderStroke(1.dp, borderColor),
        onClick = onClick,
        enabled = !showResult,
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val letter = ('A' + index).toString()
            val badgeColor = when {
                showResult && isCorrect -> success
                showResult && isSelected && !isCorrect -> MaterialTheme.colorScheme.error
                isSelected -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            }
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .background(badgeColor.copy(alpha = 0.15f))
                    .border(1.dp, badgeColor.copy(alpha = 0.35f), MaterialTheme.shapes.extraSmall),
                contentAlignment = Alignment.Center
            ) {
                if (showResult && isCorrect) {
                    Icon(Icons.Outlined.Check, contentDescription = null, tint = success, modifier = Modifier.size(16.dp))
                } else if (showResult && isSelected && !isCorrect) {
                    Icon(Icons.Outlined.Close, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
                } else {
                    Text(
                        text = letter,
                        style = MaterialTheme.typography.labelMedium.copy(fontFamily = CodeFontFamily),
                        fontWeight = FontWeight.Bold,
                        color = badgeColor
                    )
                }
            }
            RichInlineText(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
