package com.kadicompanion.ui.docs.docdetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.UnfoldLess
import androidx.compose.material.icons.outlined.UnfoldMore
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kadicompanion.domain.model.DocMember
import com.kadicompanion.domain.model.GodotDocEntry
import com.kadicompanion.ui.common.EmptyState
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

@Composable
fun DocDetailScreen(
    className: String,
    onBack: () -> Unit,
    onGlossaryTermClick: (String) -> Unit,
    onClassClick: (String) -> Unit = {},
    vm: DocDetailViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    Scaffold(
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            KadiTopBar(
                title = state.entry?.className ?: className,
                subtitle = state.entry?.inherits?.let { "extends $it" } ?: "GODOT 4.3 CLASS",
                onBack = onBack,
                actions = {
                    IconButton(
                        onClick = vm::toggleBookmark,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = if (state.isBookmarked) Icons.Outlined.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = if (state.isBookmarked) "Remove bookmark" else "Bookmark class",
                            tint = if (state.isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> LoadingSkeleton()
                state.entry == null -> EmptyState(
                    title = "Not found",
                    message = "This class isn't in the bundled docs."
                )
                else -> DocDetailContent(
                    entry = state.entry!!,
                    onGlossaryTermClick = onGlossaryTermClick,
                    onClassClick = onClassClick
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DocDetailContent(
    entry: GodotDocEntry,
    onGlossaryTermClick: (String) -> Unit,
    onClassClick: (String) -> Unit
) {
    val hPad = studioScreenPadding()
    val vGap = studioCardSpacing()
    var memberQuery by rememberSaveable { mutableStateOf("") }
    var expandAll by rememberSaveable { mutableStateOf(true) }

    val filteredProps = remember(entry.properties, memberQuery) {
        if (memberQuery.isBlank()) entry.properties
        else entry.properties.filter {
            it.name.contains(memberQuery, ignoreCase = true) ||
                (it.type?.contains(memberQuery, ignoreCase = true) == true) ||
                it.description.contains(memberQuery, ignoreCase = true)
        }
    }
    val filteredMethods = remember(entry.methods, memberQuery) {
        if (memberQuery.isBlank()) entry.methods
        else entry.methods.filter {
            it.name.contains(memberQuery, ignoreCase = true) ||
                (it.type?.contains(memberQuery, ignoreCase = true) == true) ||
                it.description.contains(memberQuery, ignoreCase = true)
        }
    }
    val filteredSignals = remember(entry.signals, memberQuery) {
        if (memberQuery.isBlank()) entry.signals
        else entry.signals.filter {
            it.name.contains(memberQuery, ignoreCase = true) ||
                it.description.contains(memberQuery, ignoreCase = true)
        }
    }

    val totalMembers = entry.properties.size + entry.methods.size + entry.signals.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(hPad),
        verticalArrangement = Arrangement.spacedBy(vGap)
    ) {
        KadiCard(accent = MaterialTheme.colorScheme.primary) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                entry.inherits?.let { parentClass ->
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.16f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.35f)),
                        modifier = Modifier.clickable { onClassClick(parentClass) }
                    ) {
                        Text(
                            text = "extends $parentClass ↗",
                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)
                ) {
                    Text(
                        text = "Godot ${entry.lastVerifiedAgainstGodotVersion}",
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
                if (totalMembers > 0) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.14f)
                    ) {
                        Text(
                            text = "${entry.properties.size}P · ${entry.methods.size}M · ${entry.signals.size}S",
                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }
            RichInlineText(
                text = entry.shortDescription,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                onTermClick = onGlossaryTermClick
            )
        }

        if (totalMembers > 0) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val container = MaterialTheme.colorScheme.surfaceContainerHigh
                TextField(
                    value = memberQuery,
                    onValueChange = { memberQuery = it },
                    modifier = Modifier.weight(1f),
                    placeholder = {
                        Text(
                            text = "Filter properties, methods & signals...",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    trailingIcon = {
                        if (memberQuery.isNotEmpty()) {
                            IconButton(onClick = { memberQuery = "" }) {
                                Icon(
                                    Icons.Outlined.Close,
                                    contentDescription = "Clear filter",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(50),
                    textStyle = MaterialTheme.typography.bodySmall,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = container,
                        unfocusedContainerColor = container,
                        disabledContainerColor = container,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )

                Pill(
                    text = if (expandAll) "Collapse" else "Expand",
                    icon = if (expandAll) Icons.Outlined.UnfoldLess else Icons.Outlined.UnfoldMore,
                    color = MaterialTheme.colorScheme.secondary,
                    onClick = { expandAll = !expandAll }
                )
            }
        }

        if (filteredProps.isNotEmpty()) {
            CollapsibleSection(
                title = "Properties",
                badgeLabel = "PROP",
                badgeColor = MaterialTheme.colorScheme.primary,
                count = filteredProps.size,
                members = filteredProps,
                forceExpanded = expandAll,
                onTermClick = onGlossaryTermClick
            )
        }
        if (filteredMethods.isNotEmpty()) {
            CollapsibleSection(
                title = "Methods",
                badgeLabel = "FUNC",
                badgeColor = MaterialTheme.colorScheme.secondary,
                count = filteredMethods.size,
                members = filteredMethods,
                forceExpanded = expandAll,
                onTermClick = onGlossaryTermClick
            )
        }
        if (filteredSignals.isNotEmpty()) {
            CollapsibleSection(
                title = "Signals",
                badgeLabel = "SIGNAL",
                badgeColor = MaterialTheme.colorScheme.tertiary,
                count = filteredSignals.size,
                members = filteredSignals,
                forceExpanded = expandAll,
                onTermClick = onGlossaryTermClick
            )
        }
        if (entry.bodyMarkdown.isNotBlank()) {
            SectionHeader(title = "Usage in Kadi & Godot 4.3", eyebrow = "GUIDE & CODE EXAMPLES")
            MarkdownText(
                markdown = entry.bodyMarkdown,
                onTermClick = onGlossaryTermClick
            )
        }
    }
}

@Composable
private fun CollapsibleSection(
    title: String,
    badgeLabel: String,
    badgeColor: Color,
    count: Int,
    members: List<DocMember>,
    forceExpanded: Boolean,
    onTermClick: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(true) }
    LaunchedEffect(forceExpanded) {
        expanded = forceExpanded
    }

    KadiCard(onClick = { expanded = !expanded }, accent = badgeColor) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Pill(text = "$count", color = badgeColor)
            }
            Icon(
                imageVector = if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                contentDescription = if (expanded) "Collapse" else "Expand",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        AnimatedVisibility(visible = expanded) {
            Column(
                modifier = Modifier.padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                members.forEachIndexed { idx, m ->
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = badgeColor.copy(alpha = 0.14f)
                            ) {
                                Text(
                                    text = badgeLabel,
                                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                                    fontWeight = FontWeight.Bold,
                                    color = badgeColor,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                )
                            }
                            Text(
                                text = m.name + (m.type?.let { ": $it" } ?: ""),
                                style = MaterialTheme.typography.bodyMedium.copy(fontFamily = CodeFontFamily),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        if (m.description.isNotBlank()) {
                            RichInlineText(
                                text = m.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                onTermClick = onTermClick
                            )
                        }
                    }
                    if (idx < members.lastIndex) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))
                    }
                }
            }
        }
    }
}
