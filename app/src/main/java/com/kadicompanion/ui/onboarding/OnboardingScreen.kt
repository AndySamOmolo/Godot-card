package com.kadicompanion.ui.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.material.icons.outlined.Terminal
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kadicompanion.R
import com.kadicompanion.domain.model.LearnerTrack
import com.kadicompanion.ui.common.HeroPanel
import com.kadicompanion.ui.common.IconBubble
import com.kadicompanion.ui.common.Pill
import com.kadicompanion.ui.common.SectionHeader
import com.kadicompanion.ui.theme.CodeFontFamily
import com.kadicompanion.ui.theme.LocalReduceMotion
import com.kadicompanion.ui.theme.brandGradient
import com.kadicompanion.ui.theme.heroTextPrimary
import com.kadicompanion.ui.theme.heroTextSecondary
import com.kadicompanion.ui.theme.heroTextTertiary

@Composable
fun OnboardingScreen(onFinished: () -> Unit, vm: OnboardingViewModel = hiltViewModel()) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    val reduceMotion = LocalReduceMotion.current
    val motion = tween<Float>(durationMillis = if (reduceMotion) 0 else 240)

    Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Spacer(Modifier.height(10.dp))
            StudioOnboardingHero()

            StepDots(
                total = 2,
                answered = listOf(state.programmedBefore != null, state.usedGodotBefore != null).count { it },
                modifier = Modifier.fillMaxWidth().padding(top = 2.dp)
            )

            SectionHeader(
                title = stringResource(R.string.onboarding_q1),
                eyebrow = "STEP 01 OF 02"
            )
            ChoiceCard(
                icon = Icons.Outlined.Code,
                title = "Yes, I've coded before",
                description = "I know variables, loops, and functions in at least one language.",
                selected = state.programmedBefore == true,
                enabled = state.programmedBefore != true,
                onClick = { vm.setProgrammedBefore(true) }
            )
            ChoiceCard(
                icon = Icons.Outlined.School,
                title = "No, I'm brand new to coding",
                description = "Teach me programming fundamentals from scratch using Kadi examples.",
                selected = state.programmedBefore == false,
                enabled = state.programmedBefore != false,
                onClick = { vm.setProgrammedBefore(false) }
            )

            SectionHeader(
                title = stringResource(R.string.onboarding_q2),
                eyebrow = "STEP 02 OF 02",
                modifier = Modifier.padding(top = 4.dp)
            )
            ChoiceCard(
                icon = Icons.Outlined.SportsEsports,
                title = "Yes, I've used Godot",
                description = "I have opened the Godot editor and worked with Scenes and Nodes.",
                selected = state.usedGodotBefore == true,
                enabled = state.usedGodotBefore != true,
                onClick = { vm.setUsedGodotBefore(true) }
            )
            ChoiceCard(
                icon = Icons.Outlined.Explore,
                title = "No, Godot is new to me",
                description = "Walk me through every click, node, and Inspector setting in Godot 4.3.",
                selected = state.usedGodotBefore == false,
                enabled = state.usedGodotBefore != false,
                onClick = { vm.setUsedGodotBefore(false) }
            )

            AnimatedVisibility(
                visible = state.resolvedTrack != null,
                enter = fadeIn(animationSpec = motion) + expandVertically(
                    animationSpec = tween(durationMillis = if (reduceMotion) 0 else 240)
                )
            ) {
                state.resolvedTrack?.let { track ->
                    TrackPreview(track = track, modifier = Modifier.padding(top = 6.dp))
                }
            }

            Spacer(Modifier.height(12.dp))
        }

        Surface(color = MaterialTheme.colorScheme.surface) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = { vm.finish(); onFinished() },
                        enabled = state.canFinish,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        Icon(Icons.Outlined.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            stringResource(R.string.onboarding_finish),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = if (state.canFinish) "You can switch your learning track anytime in Settings."
                        else "Select both answers above to unlock your curriculum.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun StudioOnboardingHero() {
    val heroText = heroTextPrimary()
    val heroTextSub = heroTextSecondary()
    HeroPanel(
        gradient = brandGradient(),
        shape = RoundedCornerShape(18.dp),
        contentPadding = PaddingValues(22.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF38BDF8).copy(alpha = 0.22f),
                border = BorderStroke(1.dp, Color(0xFF38BDF8).copy(alpha = 0.45f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        Icons.Outlined.Terminal,
                        contentDescription = null,
                        tint = Color(0xFF7DD3FC),
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "GODOT 4.3 · GDSCRIPT STUDIO",
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF7DD3FC)
                    )
                }
            }
        }
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.displaySmall,
            color = heroText,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = "Master GDScript and Godot 4.3 by building a complete, playable Kenyan Kadi card game from scratch.",
            style = MaterialTheme.typography.bodyMedium,
            color = heroTextSub
        )
    }
}

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
                targetValue = if (done) 28.dp else 8.dp,
                animationSpec = tween(durationMillis = if (reduceMotion) 0 else 200),
                label = "dotWidth"
            )
            val color by animateColorAsState(
                targetValue = if (done) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                animationSpec = tween(durationMillis = if (reduceMotion) 0 else 200),
                label = "dotColor"
            )
            Box(
                modifier = Modifier
                    .width(width)
                    .height(6.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

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
    val spec = tween<Color>(durationMillis = if (reduceMotion) 0 else 180)
    val container by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)
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
    val borderWidth by animateDpAsState(
        targetValue = if (selected) 1.5.dp else 1.dp,
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
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp, disabledElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconBubble(
                icon = icon,
                tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                size = 42.dp
            )
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (selected) {
                Pill(text = "Selected", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
private fun TrackPreview(track: LearnerTrack, modifier: Modifier = Modifier) {
    val title = when (track) {
        LearnerTrack.FOUNDATIONS -> R.string.track_foundations
        LearnerTrack.EXPERIENCED_DEV -> R.string.track_experienced
        LearnerTrack.GODOT_REFERENCE -> R.string.track_reference
    }
    val desc = when (track) {
        LearnerTrack.FOUNDATIONS -> R.string.track_foundations_desc
        LearnerTrack.EXPERIENCED_DEV -> R.string.track_experienced_desc
        LearnerTrack.GODOT_REFERENCE -> R.string.track_reference_desc
    }
    val heroText = heroTextPrimary()
    val heroTextSub = heroTextSecondary()
    val heroTextMuted = heroTextTertiary()
    HeroPanel(
        modifier = modifier,
        gradient = brandGradient(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = "RECOMMENDED TRACK",
            style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
            fontWeight = FontWeight.Bold,
            color = heroTextMuted
        )
        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = heroText
        )
        Text(
            text = stringResource(desc),
            style = MaterialTheme.typography.bodyMedium,
            color = heroTextSub
        )
    }
}
