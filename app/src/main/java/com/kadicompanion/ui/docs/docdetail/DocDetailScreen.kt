package com.kadicompanion.ui.docs.docdetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
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

@Composable
fun DocDetailScreen(
    className: String,
    onBack: () -> Unit,
    onGlossaryTermClick: (String) -> Unit,
    vm: DocDetailViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            KadiTopBar(
                title = state.entry?.className ?: className,
                subtitle = state.entry?.inherits?.let { "extends $it" },
                onBack = onBack,
                actions = {
                    state.entry?.let {
                        Pill(
                            text = "Godot ${it.lastVerifiedAgainstGodotVersion}",
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
                state.entry == null -> EmptyState(
                    title = "Not found",
                    message = "This class isn't in the bundled docs."
                )
                else -> DocDetailContent(
                    entry = state.entry!!,
                    onGlossaryTermClick = onGlossaryTermClick
                )
            }
        }
    }
}

@Composable
private fun DocDetailContent(entry: GodotDocEntry, onGlossaryTermClick: (String) -> Unit) {
    val hPad = studioScreenPadding()
    val vGap = studioCardSpacing()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(hPad),
        verticalArrangement = Arrangement.spacedBy(vGap)
    ) {
        KadiCard(accent = MaterialTheme.colorScheme.primary) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                entry.inherits?.let {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.16f)
                    ) {
                        Text(
                            "inherits $it",
                            style = MaterialTheme.typography.labelSmall,
                            fontFamily = FontFamily.Monospace,
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
                        "Godot ${entry.lastVerifiedAgainstGodotVersion}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }
            RichInlineText(
                text = entry.shortDescription,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                onTermClick = onGlossaryTermClick
            )
        }
        if (entry.properties.isNotEmpty()) {
            CollapsibleSection(
                title = "Properties",
                count = entry.properties.size,
                members = entry.properties,
                onTermClick = onGlossaryTermClick
            )
        }
        if (entry.methods.isNotEmpty()) {
            CollapsibleSection(
                title = "Methods",
                count = entry.methods.size,
                members = entry.methods,
                onTermClick = onGlossaryTermClick
            )
        }
        if (entry.signals.isNotEmpty()) {
            CollapsibleSection(
                title = "Signals",
                count = entry.signals.size,
                members = entry.signals,
                onTermClick = onGlossaryTermClick
            )
        }
        if (entry.bodyMarkdown.isNotBlank()) {
            SectionHeader(title = "Description", eyebrow = "DETAILS")
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
    count: Int,
    members: List<DocMember>,
    onTermClick: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(true) }
    KadiCard(onClick = { expanded = !expanded }) {
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
                Pill(text = "$count", color = MaterialTheme.colorScheme.secondary)
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
                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = m.name,
                                style = MaterialTheme.typography.bodyMedium,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            m.type?.let {
                                Text(
                                    text = ": $it",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontFamily = FontFamily.Monospace,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
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
