package com.kadicompanion.ui.glossary

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kadicompanion.R
import com.kadicompanion.domain.model.GlossaryEntry
import com.kadicompanion.ui.common.EmptyState
import com.kadicompanion.ui.common.KadiCard
import com.kadicompanion.ui.common.LoadingSkeleton
import com.kadicompanion.ui.common.Pill
import com.kadicompanion.ui.common.RichInlineText
import com.kadicompanion.ui.common.studioCardSpacing
import com.kadicompanion.ui.common.studioScreenPadding
import com.kadicompanion.ui.theme.LocalReduceMotion

private val KADI_TERMS = setOf(
    "Kadi", "Penalty Cards", "Jump Cards", "Kickback Cards", "Question Cards",
    "Answer Cards", "Niko Kadi", "Draw Pile", "Discard Pile", "Suit", "Rank",
    "Cardless", "Cardless Status", "Joker", "A-Cards", "Starting Card",
    "Opening Discard", "Turn Order", "Turn Direction", "Penalty Stack",
    "Penalty Stacking", "Multi-Card Combo", "Question-Answer Combo", "Suit Lock", "Requested Rank"
)

private val ASSET_TERMS = setOf(
    "TextureRect", "Texture2D", "AtlasTexture", "Sprite Sheet", "Import Dock",
    "Mouse Filter", "TextureFilter", "StyleBoxTexture", "AudioStreamOggVorbis",
    "ResourceLoader", "res://"
)

private val GODOT_NODE_TERMS = setOf(
    "Node", "Control", "Resource", "Autoloads", "Scenes", "_ready()", "Signals",
    "State Machines", "Tweens", "PackedScene", "Object", "Button", "Label",
    "Timer", "@onready", "@export", "HBoxContainer", "CanvasLayer", "OptionButton",
    "Scene Tree", "Inspector", "FileSystem Dock", "Project Settings", "Main Scene",
    "Instantiation", "Freeing Nodes", "queue_free()"
) + ASSET_TERMS

@Composable
fun GlossaryListScreen(onTermClick: (String) -> Unit, vm: GlossaryListViewModel = hiltViewModel()) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    var selectedCategory by rememberSaveable { mutableIntStateOf(0) }
    val categories = listOf("All Terms", "Kadi Rules", "Downloaded Assets", "Godot Engine", "GDScript")
    val hPad = studioScreenPadding()
    val vGap = studioCardSpacing()

    val categoryFiltered = state.filtered.filter { entry ->
        when (selectedCategory) {
            1 -> entry.term in KADI_TERMS
            2 -> entry.term in ASSET_TERMS || entry.shortDefinition.contains("res://assets/", ignoreCase = true)
            3 -> entry.term in GODOT_NODE_TERMS || entry.relatedDocClassNames.isNotEmpty()
            4 -> entry.term !in KADI_TERMS && entry.term !in GODOT_NODE_TERMS && entry.relatedDocClassNames.isEmpty()
            else -> true
        }
    }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        GlossarySearchPill(
            query = state.query,
            onQueryChange = vm::setQuery,
            placeholder = stringResource(R.string.search_glossary_placeholder),
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
                    color = if (active) MaterialTheme.colorScheme.primary.copy(alpha = 0.20f)
                    else MaterialTheme.colorScheme.surfaceContainerHigh,
                    border = BorderStroke(
                        width = if (active) 1.5.dp else 1.dp,
                        color = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                    ),
                    modifier = Modifier.clickable { selectedCategory = idx }
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (active) FontWeight.Bold else FontWeight.Medium,
                        color = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
        when {
            state.isLoading -> LoadingSkeleton(lines = 7)
            categoryFiltered.isEmpty() -> EmptyState(
                title = "No terms",
                message = stringResource(R.string.empty_search)
            )
            else -> {
                val grouped = categoryFiltered.groupBy { it.term.firstOrNull()?.uppercaseChar() ?: '#' }
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = hPad, end = hPad, bottom = 28.dp),
                    verticalArrangement = Arrangement.spacedBy(vGap)
                ) {
                    grouped.forEach { (letter, entries) ->
                        item(key = "letter-$letter") { LetterRule(letter = letter) }
                        items(items = entries, key = { "term-${it.term}" }) { entry ->
                            TermCard(entry = entry, onClick = { onTermClick(entry.term) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LetterRule(letter: Char) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = letter.toString(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(MaterialTheme.colorScheme.outlineVariant)
        )
    }
}

@Composable
private fun TermCard(entry: GlossaryEntry, onClick: () -> Unit) {
    val firstLine = entry.shortDefinition.lineSequence().firstOrNull { it.isNotBlank() }.orEmpty()
    val hasCodeExample = entry.shortDefinition.contains("```")
    KadiCard(onClick = onClick) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            LetterBadge(letter = entry.term.firstOrNull()?.uppercaseChar() ?: '?')
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = entry.term,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (hasCodeExample) {
                        Pill(text = "Code", color = MaterialTheme.colorScheme.secondary)
                    }
                }
                RichInlineText(
                    text = firstLine,
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

@Composable
private fun LetterBadge(letter: Char) {
    Box(
        modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter.toString(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GlossarySearchPill(
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
