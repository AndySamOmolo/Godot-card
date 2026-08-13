package com.kadicompanion.ui.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kadicompanion.R
import com.kadicompanion.domain.model.LearnerTrack
import com.kadicompanion.ui.common.CLUB
import com.kadicompanion.ui.common.DIAMOND
import com.kadicompanion.ui.common.HEART
import com.kadicompanion.ui.common.HeroPanel
import com.kadicompanion.ui.common.IconBubble
import com.kadicompanion.ui.common.Pill
import com.kadicompanion.ui.common.SPADE
import com.kadicompanion.ui.common.SectionHeader
import com.kadicompanion.ui.theme.DisplayHero
import com.kadicompanion.ui.theme.LocalReduceMotion
import com.kadicompanion.ui.theme.brandGradient
import com.kadicompanion.ui.theme.heroTextPrimary
import com.kadicompanion.ui.theme.heroTextSecondary
import com.kadicompanion.ui.theme.heroTextTertiary

/*
 * First impression: a gradient hero with a fanned card-suit motif, then the two
 * placement questions rendered as large selectable cards, and a pinned CTA.
 */

@Composable
fun OnboardingScreen(onFinished: () -> Unit, vm: OnboardingViewModel = hiltViewModel()) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    val reduceMotion = LocalReduceMotion.current
    val motion = tween<Float>(durationMillis = if (reduceMotion) 0 else 260)

    Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Spacer(Modifier.height(12.dp))
            OnboardingHero()

            StepDots(
                total = 2,
                answered = listOf(state.programmedBefore != null, state.usedGodotBefore != null).count { it },
                modifier = Modifier.fillMaxWidth().padding(top = 2.dp)
            )

            SectionHeader(
                title = stringResource(R.string.onboarding_q1),
                eyebrow = "Question 1 of 2"
            )
            ChoiceCard(
                icon = Icons.Outlined.Code,
                title = "Yes",
                description = "I have written code in some language before.",
                selected = state.programmedBefore == true,
                enabled = state.programmedBefore != true,
                onClick = { vm.setProgrammedBefore(true) }
            )
            ChoiceCard(
                icon = Icons.Outlined.School,
                title = "No",
                description = "This is my first time programming.",
                selected = state.programmedBefore == false,
                enabled = state.programmedBefore != false,
                onClick = { vm.setProgrammedBefore(false) }
            )

            SectionHeader(
                title = stringResource(R.string.onboarding_q2),
                eyebrow = "Question 2 of 2",
                modifier = Modifier.padding(top = 6.dp)
            )
            ChoiceCard(
                icon = Icons.Outlined.SportsEsports,
                title = "Yes",
                description = "I have opened the Godot editor and built something.",
                selected = state.usedGodotBefore == true,
                enabled = state.usedGodotBefore != true,
                onClick = { vm.setUsedGodotBefore(true) }
            )
            ChoiceCard(
                icon = Icons.Outlined.Explore,
                title = "No",
                description = "Godot is brand new territory for me.",
                selected = state.usedGodotBefore == false,
                enabled = state.usedGodotBefore != false,
                onClick = { vm.setUsedGodotBefore(false) }
            )

            AnimatedVisibility(
                visible = state.resolvedTrack != null,
                enter = fadeIn(animationSpec = motion) + expandVertically(
                    animationSpec = tween(durationMillis = if (reduceMotion) 0 else 260)
                )
            ) {
                state.resolvedTrack?.let { track ->
                    TrackPreview(track = track, modifier = Modifier.padding(top = 8.dp))
                }
            }

            Spacer(Modifier.height(8.dp))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { vm.finish(); onFinished() },
                enabled = state.canFinish,
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Icon(Icons.Outlined.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(10.dp))
                Text(
                    stringResource(R.string.onboarding_finish),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = if (state.canFinish) "You can change this later in Settings." else "Answer both questions to continue.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun OnboardingHero() {
    val heroText = heroTextPrimary()
    val heroTextSub = heroTextSecondary()
    HeroPanel(
        gradient = brandGradient(),
        shape = RoundedCornerShape(34.dp),
        contentPadding = PaddingValues(
            start = 22.dp,
            end = 22.dp,
            top = 28.dp,
            bottom = 26.dp
        )
    ) {
        SuitFan()
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.app_name),
            style = DisplayHero,
            color = heroText,
            fontWeight = FontWeight.Black
        )
        Text(
            text = "Learn GDScript by building the card game you already love.",
            style = MaterialTheme.typography.bodyMedium,
            color = heroTextSub
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.onboarding_title),
            style = MaterialTheme.typography.titleLarge,
            color = heroText
        )
    }
}

/** Four suit glyphs fanned out like a hand of cards — larger and more dramatic. */
@Composable
private fun SuitFan() {
    val heroText = heroTextPrimary()
    Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        SuitGlyph(SPADE, heroText, -22f, 0.88f)
        SuitGlyph(HEART, heroText, -8f, 1f)
        SuitGlyph(CLUB, heroText, 8f, 1f)
        SuitGlyph(DIAMOND, heroText, 22f, 0.88f)
    }
}

@Composable
private fun SuitGlyph(glyph: String, color: Color, rotation: Float, opacity: Float) {
    Text(
        text = glyph,
        fontSize = 38.sp,
        fontWeight = FontWeight.Black,
        color = color,
        modifier = Modifier.rotate(rotation).alpha(opacity)
    )
}

/** Progress dots for the two placement questions. */
@Composable
private fun StepDots(total: Int, answered: Int, modifier: Modifier = Modifier) {
    val reduceMotion = LocalReduceMotion.current
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(total) { index ->
            val done = index < answered
            val width by animateDpAsState(
                targetValue = if (done) 26.dp else 8.dp,
                animationSpec = tween(durationMillis = if (reduceMotion) 0 else 220),
                label = "dotWidth"
            )
            val color by animateColorAsState(
                targetValue = if (done) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                animationSpec = tween(durationMillis = if (reduceMotion) 0 else 220),
                label = "dotColor"
            )
            Box(
                modifier = Modifier
                    .width(width)
                    .height(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

/** Large tappable option card with an icon bubble and a selected state. */
@Composable
private fun ChoiceCard(
    icon: ImageVector,
    title: String,
    description: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val reduceMotion = LocalReduceMotion.current
    val spec = tween<Color>(durationMillis = if (reduceMotion) 0 else 200)
    val container by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
        } else {
            MaterialTheme.colorScheme.surfaceContainer
        },
        animationSpec = spec,
        label = "choiceContainer"
    )
    val borderColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
        animationSpec = spec,
        label = "choiceBorder"
    )
    // Animate the scale for a satisfying tap feel
    val borderWidth by animateDpAsState(
        targetValue = if (selected) 2.dp else 1.dp,
        animationSpec = tween(durationMillis = if (reduceMotion) 0 else 150),
        label = "choiceBorderWidth"
    )
    Card(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = container, disabledContainerColor = container),
        border = BorderStroke(borderWidth, borderColor),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (selected) 4.dp else 1.dp,
            disabledElevation = if (selected) 4.dp else 1.dp
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            IconBubble(
                icon = icon,
                tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                size = 46.dp
            )
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (selected) {
                Pill(text = "Picked", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
private fun TrackPreview(track: LearnerTrack, modifier: Modifier = Modifier) {
    val title = when (track) { LearnerTrack.FOUNDATIONS -> R.string.track_foundations; LearnerTrack.EXPERIENCED_DEV -> R.string.track_experienced; LearnerTrack.GODOT_REFERENCE -> R.string.track_reference }
    val desc = when (track) { LearnerTrack.FOUNDATIONS -> R.string.track_foundations_desc; LearnerTrack.EXPERIENCED_DEV -> R.string.track_experienced_desc; LearnerTrack.GODOT_REFERENCE -> R.string.track_reference_desc }
    val heroText = heroTextPrimary()
    val heroTextSub = heroTextSecondary()
    val heroTextMuted = heroTextTertiary()
    HeroPanel(
        modifier = modifier,
        gradient = brandGradient(),
        shape = RoundedCornerShape(26.dp)
    ) {
        Text(
            text = "WE'LL START YOU ON",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = heroTextMuted
        )
        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.headlineSmall,
            color = heroText
        )
        Text(
            text = stringResource(desc),
            style = MaterialTheme.typography.bodyMedium,
            color = heroTextSub
        )
    }
}
