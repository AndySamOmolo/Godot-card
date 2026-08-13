package com.kadicompanion.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Animation
import androidx.compose.material.icons.outlined.ColorLens
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material.icons.outlined.Update
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kadicompanion.R
import com.kadicompanion.data.local.datastore.ThemePref
import com.kadicompanion.domain.model.LearnerTrack
import com.kadicompanion.ui.common.IconBubble
import com.kadicompanion.ui.common.KadiCard
import com.kadicompanion.ui.common.KadiTopBar
import com.kadicompanion.ui.common.Pill
import com.kadicompanion.ui.common.SectionHeader
import com.kadicompanion.ui.theme.successColor

/*
 * Settings is a list of quiet preference groups. Every row is the same shape —
 * a tinted bubble, a title, one line of explanation, and the control on the
 * trailing edge — so the eye can scan straight down the left and right rails.
 */

@Composable
fun SettingsScreen(onBack: () -> Unit, vm: SettingsViewModel = hiltViewModel()) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            KadiTopBar(
                title = stringResource(R.string.action_settings),
                subtitle = "Make it yours",
                onBack = onBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SectionHeader(title = stringResource(R.string.settings_theme), eyebrow = "Appearance")
            KadiCard {
                ThemePref.entries.forEach { pref ->
                    ChoiceRow(
                        icon = themeIcon(pref),
                        title = themeLabel(pref),
                        description = themeDescription(pref),
                        selected = state.theme == pref,
                        onClick = { vm.setTheme(pref) }
                    )
                }
            }
            KadiCard {
                ToggleRow(
                    icon = Icons.Outlined.ColorLens,
                    title = stringResource(R.string.settings_dynamic_color),
                    description = "Borrow accent colors from your wallpaper.",
                    checked = state.dynamicColor,
                    onCheckedChange = vm::setDynamicColor
                )
                ToggleRow(
                    icon = Icons.Outlined.Animation,
                    title = stringResource(R.string.settings_reduce_motion),
                    description = "Turn off transitions and progress animations.",
                    checked = state.reduceMotion,
                    onCheckedChange = vm::setReduceMotion
                )
            }

            SectionHeader(
                title = "Reading",
                modifier = Modifier.padding(top = 6.dp),
                eyebrow = "Reading"
            )
            KadiCard {
                ToggleRow(
                    icon = Icons.Outlined.TextFields,
                    title = stringResource(R.string.settings_dyslexia_font),
                    description = "Wider letter spacing for long lesson text.",
                    checked = state.dyslexiaFont,
                    onCheckedChange = vm::setDyslexiaFont
                )
                ToggleRow(
                    icon = Icons.Outlined.LockOpen,
                    title = stringResource(R.string.settings_skip_gating),
                    description = "Open any phase without finishing the previous one.",
                    checked = state.skipGating,
                    onCheckedChange = vm::setSkipGating
                )
            }

            SectionHeader(
                title = stringResource(R.string.settings_track),
                modifier = Modifier.padding(top = 6.dp),
                eyebrow = "Learning path"
            )
            KadiCard {
                LearnerTrack.entries.forEach { track ->
                    ChoiceRow(
                        icon = Icons.Outlined.Explore,
                        title = trackLabel(track),
                        description = trackDescription(track),
                        selected = state.track == track,
                        onClick = { vm.setTrack(track) }
                    )
                }
            }

            SectionHeader(
                title = stringResource(R.string.settings_about),
                modifier = Modifier.padding(top = 6.dp),
                eyebrow = "About"
            )
            KadiCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IconBubble(icon = Icons.Outlined.Update, tint = successColor())
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = stringResource(R.string.settings_content_version),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Bundled offline content",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Pill(text = state.contentVersion, color = successColor())
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IconBubble(icon = Icons.Outlined.Info, tint = MaterialTheme.colorScheme.secondary)
                    Text(
                        text = stringResource(R.string.settings_about_text),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun ToggleRow(
    icon: ImageVector,
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val accent = if (checked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        IconBubble(icon = icon, tint = accent)
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

/** A selectable row — reads as one segment of a segmented set inside its card. */
@Composable
private fun ChoiceRow(
    icon: ImageVector,
    title: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val accent = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
    KadiCard(onClick = onClick, accent = if (selected) MaterialTheme.colorScheme.primary else null) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconBubble(icon = icon, tint = accent, size = 36.dp)
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            RadioButton(selected = selected, onClick = onClick)
        }
    }
}

private fun themeIcon(pref: ThemePref): ImageVector = when (pref) {
    ThemePref.DARK -> Icons.Outlined.DarkMode
    ThemePref.LIGHT -> Icons.Outlined.LightMode
    ThemePref.SYSTEM -> Icons.Outlined.PhoneAndroid
}

@Composable
private fun themeLabel(pref: ThemePref): String = when (pref) {
    ThemePref.DARK -> stringResource(R.string.settings_theme_dark)
    ThemePref.LIGHT -> stringResource(R.string.settings_theme_light)
    ThemePref.SYSTEM -> stringResource(R.string.settings_theme_system)
}

private fun themeDescription(pref: ThemePref): String = when (pref) {
    ThemePref.DARK -> "Ink navy with gold accents."
    ThemePref.LIGHT -> "Warm ivory for bright rooms."
    ThemePref.SYSTEM -> "Follow the device setting."
}

@Composable
private fun trackLabel(track: LearnerTrack): String = when (track) {
    LearnerTrack.FOUNDATIONS -> stringResource(R.string.track_foundations)
    LearnerTrack.EXPERIENCED_DEV -> stringResource(R.string.track_experienced)
    LearnerTrack.GODOT_REFERENCE -> stringResource(R.string.track_reference)
}

@Composable
private fun trackDescription(track: LearnerTrack): String = when (track) {
    LearnerTrack.FOUNDATIONS -> stringResource(R.string.track_foundations_desc)
    LearnerTrack.EXPERIENCED_DEV -> stringResource(R.string.track_experienced_desc)
    LearnerTrack.GODOT_REFERENCE -> stringResource(R.string.track_reference_desc)
}
