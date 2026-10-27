package com.kadicompanion.ui.docs.doclist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DataObject
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kadicompanion.R
import com.kadicompanion.domain.model.GodotDocEntry
import com.kadicompanion.ui.common.EmptyState
import com.kadicompanion.ui.common.IconBubble
import com.kadicompanion.ui.common.KadiCard
import com.kadicompanion.ui.common.KadiTopBar
import com.kadicompanion.ui.common.LoadingSkeleton
import com.kadicompanion.ui.common.Pill
import com.kadicompanion.ui.common.RichInlineText
import com.kadicompanion.ui.common.SectionHeader
import com.kadicompanion.ui.common.studioCardSpacing
import com.kadicompanion.ui.common.studioScreenPadding
import com.kadicompanion.ui.glossary.GlossaryListScreen
import com.kadicompanion.ui.theme.CodeFontFamily
import com.kadicompanion.ui.theme.LocalReduceMotion
import com.kadicompanion.ui.theme.glassBorderColor

private val UI_CLASSES = setOf(
    "Control", "Button", "Label", "RichTextLabel", "Panel", "ColorRect", "TextureRect",
    "TextureButton", "OptionButton", "CheckButton", "LineEdit", "TextEdit", "ProgressBar",
    "Slider", "Container", "HBoxContainer", "VBoxContainer", "GridContainer", "MarginContainer",
    "ScrollContainer", "TabContainer", "Theme", "Window"
)

private val CARD_2D_CLASSES = setOf(
    "Node", "Node2D", "CanvasItem", "CanvasLayer", "Sprite2D", "Area2D", "CollisionShape2D",
    "CharacterBody2D", "RigidBody2D", "StaticBody2D", "Camera2D", "Marker2D", "CPUParticles2D",
    "GPUParticles2D", "Texture2D", "Viewport", "SceneTree", "PackedScene"
)

private val AUDIO_ANIM_CLASSES = setOf(
    "Tween", "Timer", "AnimationPlayer", "AnimationTree", "AudioStreamPlayer",
    "AudioStreamPlayer2D", "AudioStreamPlayer3D"
)

@Composable
fun DocsListScreen(
    onDocClick: (String) -> Unit,
    onOpenGlossary: (String) -> Unit,
    vm: DocsListViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    var selectedTab by rememberSaveable { mutableStateOf(0) }
    val hPad = studioScreenPadding()

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            KadiTopBar(
                title = stringResource(R.string.nav_docs),
                subtitle = "REFERENCE DESK",
                actions = {
                    if (state.all.isNotEmpty()) {
                        Pill(
                            text = "${state.all.size} classes",
                            modifier = Modifier.padding(end = 4.dp),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DocsSegmentedTabs(
                selected = selectedTab,
                onSelect = { selectedTab = it },
                modifier = Modifier.padding(horizontal = hPad, vertical = 4.dp)
            )
            when (selectedTab) {
                0 -> ClassesTab(state = state, onQueryChange = vm::setQuery, onDocClick = onDocClick)
                1 -> GlossaryListScreen(onTermClick = onOpenGlossary)
            }
        }
    }
}

@Composable
private fun DocsSegmentedTabs(selected: Int, onSelect: (Int) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .border(BorderStroke(1.dp, glassBorderColor()), MaterialTheme.shapes.medium)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        SegmentButton(
            label = "Godot Classes",
            icon = Icons.Outlined.DataObject,
            active = selected == 0,
            onClick = { onSelect(0) },
            modifier = Modifier.weight(1f)
        )
        SegmentButton(
            label = "Kadi Glossary",
            icon = Icons.Outlined.Translate,
            active = selected == 1,
            onClick = { onSelect(1) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SegmentButton(
    label: String,
    icon: ImageVector,
    active: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val content = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(if (active) MaterialTheme.colorScheme.primary.copy(alpha = 0.16f) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 9.dp, horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = null, tint = content, modifier = Modifier.size(16.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = content,
            fontWeight = if (active) FontWeight.Bold else FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 6.dp)
        )
    }
}

@Composable
private fun ClassesTab(state: DocsListUiState, onQueryChange: (String) -> Unit, onDocClick: (String) -> Unit) {
    var selectedCategory by rememberSaveable { mutableIntStateOf(0) }
    val categories = listOf("All", "UI & Controls", "2D & Scenes", "Audio & Timers", "Core & Data")
    val hPad = studioScreenPadding()
    val vGap = studioCardSpacing()

    val categoryFiltered = state.filtered.filter { entry ->
        when (selectedCategory) {
            1 -> entry.className in UI_CLASSES
            2 -> entry.className in CARD_2D_CLASSES
            3 -> entry.className in AUDIO_ANIM_CLASSES
            4 -> entry.className !in UI_CLASSES && entry.className !in CARD_2D_CLASSES && entry.className !in AUDIO_ANIM_CLASSES
            else -> true
        }
    }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SearchPill(
            query = state.query,
            onQueryChange = onQueryChange,
            placeholder = stringResource(R.string.search_docs_placeholder),
            modifier = Modifier.padding(horizontal = hPad)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = hPad),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEachIndexed { idx, label ->
                val active = selectedCategory == idx
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = if (active) MaterialTheme.colorScheme.secondary.copy(alpha = 0.20f)
                    else MaterialTheme.colorScheme.surfaceContainerHigh,
                    border = BorderStroke(
                        width = if (active) 1.5.dp else 1.dp,
                        color = if (active) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outlineVariant
                    ),
                    modifier = Modifier.clickable { selectedCategory = idx }
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (active) FontWeight.Bold else FontWeight.Medium,
                        color = if (active) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
        when {
            state.isLoading -> LoadingSkeleton(lines = 7)
            state.filtered.isEmpty() && state.query.isBlank() -> EmptyState(
                title = "Building search index",
                message = "Bundled Godot docs are being parsed on first launch."
            )
            categoryFiltered.isEmpty() -> EmptyState(
                title = "No matches",
                message = stringResource(R.string.empty_search)
            )
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = hPad, end = hPad, bottom = 28.dp),
                verticalArrangement = Arrangement.spacedBy(vGap)
            ) {
                item {
                    SectionHeader(
                        title = categories[selectedCategory],
                        eyebrow = "GODOT 4.3 CLASSES",
                        trailing = { Pill(text = "${categoryFiltered.size}", color = MaterialTheme.colorScheme.secondary) }
                    )
                }
                items(categoryFiltered, key = { it.className }) { entry ->
                    ClassRow(entry = entry, onClick = { onDocClick(entry.className) })
                }
            }
        }
    }
}

@Composable
private fun ClassRow(entry: GodotDocEntry, onClick: () -> Unit) {
    KadiCard(onClick = onClick) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            IconBubble(icon = Icons.AutoMirrored.Outlined.MenuBook, size = 42.dp)
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    text = entry.className,
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = CodeFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )
                entry.inherits?.let {
                    Text(
                        text = "extends $it",
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = CodeFontFamily,
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1
                    )
                }
                RichInlineText(
                    text = entry.shortDescription,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchPill(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    val duration = if (LocalReduceMotion.current) 0 else 150
    val container = MaterialTheme.colorScheme.surfaceContainerHigh
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        },
        leadingIcon = {
            Icon(Icons.Outlined.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = query.isNotEmpty(),
                enter = fadeIn(tween(duration)),
                exit = fadeOut(tween(duration))
            ) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        Icons.Outlined.Close,
                        contentDescription = "Clear search",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(50),
        textStyle = MaterialTheme.typography.bodyMedium,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = container,
            unfocusedContainerColor = container,
            disabledContainerColor = container,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.primary
        )
    )
}
