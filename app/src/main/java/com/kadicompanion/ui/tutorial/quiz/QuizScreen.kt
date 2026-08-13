package com.kadicompanion.ui.tutorial.quiz

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kadicompanion.R
import com.kadicompanion.ui.common.CLUB
import com.kadicompanion.ui.common.DIAMOND
import com.kadicompanion.ui.common.EmptyState
import com.kadicompanion.ui.common.HEART
import com.kadicompanion.ui.common.IconBubble
import com.kadicompanion.ui.common.KadiCard
import com.kadicompanion.ui.common.KadiTopBar
import com.kadicompanion.ui.common.LoadingSkeleton
import com.kadicompanion.ui.common.Pill
import com.kadicompanion.ui.common.SPADE
import com.kadicompanion.ui.theme.LocalReduceMotion
import com.kadicompanion.ui.theme.successColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(onBack: () -> Unit, onComplete: () -> Unit, vm: QuizViewModel = hiltViewModel()) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    val reduceMotion = LocalReduceMotion.current
    val duration = if (reduceMotion) 0 else 260

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            KadiTopBar(
                title = "Knowledge check",
                subtitle = if (!state.finished && state.questions.isNotEmpty()) {
                    "Question ${state.currentIndex + 1} of ${state.questions.size}"
                } else null,
                onBack = onBack,
                actions = {
                    if (!state.finished && state.questions.isNotEmpty()) {
                        Pill(
                            text = "${state.currentIndex + 1}/${state.questions.size}",
                            modifier = Modifier.padding(end = 12.dp)
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
                state.finished -> QuizCelebration(onComplete = onComplete)
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
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            LinearProgressIndicator(
                                progress = { (state.currentIndex + 1).toFloat() / state.questions.size },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.10f),
                                strokeCap = StrokeCap.Round
                            )
                            Text(
                                text = q.prompt,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(Modifier.height(4.dp))
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
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = stringResource(R.string.quiz_explanation),
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                    Text(
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
                                            modifier = Modifier.weight(1f),
                                            shape = MaterialTheme.shapes.medium
                                        ) { Text("Retry") }
                                        Button(
                                            onClick = vm::next,
                                            modifier = Modifier.weight(1f),
                                            shape = MaterialTheme.shapes.medium
                                        ) { Text("Next") }
                                    } else {
                                        Button(
                                            onClick = vm::next,
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = MaterialTheme.shapes.medium
                                        ) {
                                            Text(
                                                if (state.isLast) "Finish" else "Next question",
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

/** Celebratory completion screen with large trophy and suit glyph accents. */
@Composable
private fun QuizCelebration(onComplete: () -> Unit) {
    val success = successColor()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
                .background(success.copy(alpha = 0.14f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Outlined.EmojiEvents,
                contentDescription = null,
                tint = success,
                modifier = Modifier.size(56.dp)
            )
        }
        Spacer(Modifier.height(22.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(SPADE, HEART, CLUB, DIAMOND).forEach { glyph ->
                Text(
                    text = glyph,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = "All done!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "You finished this lesson's knowledge check.\nGreat job!",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(28.dp))
        Button(
            onClick = onComplete,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        ) {
            Icon(Icons.Outlined.Check, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Done", fontWeight = FontWeight.Bold)
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
        showResult && isCorrect -> success.copy(alpha = 0.40f)
        showResult && isSelected && !isCorrect -> MaterialTheme.colorScheme.error.copy(alpha = 0.40f)
        isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.40f)
        else -> MaterialTheme.colorScheme.outlineVariant
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
        onClick = onClick,
        enabled = !showResult,
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Letter badge (A, B, C, D)
            val letter = ('A' + index).toString()
            val badgeColor = when {
                showResult && isCorrect -> success
                showResult && isSelected && !isCorrect -> MaterialTheme.colorScheme.error
                isSelected -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            }
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(badgeColor.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center
            ) {
                if (showResult && isCorrect) {
                    Icon(Icons.Outlined.Check, contentDescription = null, tint = success, modifier = Modifier.size(18.dp))
                } else if (showResult && isSelected && !isCorrect) {
                    Icon(Icons.Outlined.Close, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                } else {
                    Text(
                        text = letter,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = badgeColor
                    )
                }
            }
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
