package com.kadicompanion.ui.glossary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

@Composable
fun GlossaryDetailScreen(
    term: String,
    onBack: () -> Unit,
    onLessonClick: (String) -> Unit,
    onDocClick: (String) -> Unit,
    vm: GlossaryDetailViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    val hPad = studioScreenPadding()
    val vGap = studioCardSpacing()

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            KadiTopBar(
                title = state.entry?.term ?: term.substringBefore('|'),
                subtitle = "GLOSSARY TERM",
                onBack = onBack
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> LoadingSkeleton()
                state.entry == null -> EmptyState(
                    title = "Term not found",
                    message = "This glossary term isn't bundled."
                )
                else -> {
                    val entry = state.entry!!
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(hPad),
                        verticalArrangement = Arrangement.spacedBy(vGap)
                    ) {
                        item {
                            KadiCard(accent = MaterialTheme.colorScheme.primary) {
                                Text(
                                    text = entry.term,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                RichInlineText(
                                    text = entry.shortDefinition,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        if (entry.relatedLessonIds.isNotEmpty()) {
                            item {
                                SectionHeader(
                                    title = "Related Lessons",
                                    eyebrow = "LEARN MORE",
                                    trailing = {
                                        Pill(
                                            text = "${entry.relatedLessonIds.size}",
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                )
                            }
                            items(items = entry.relatedLessonIds) { lessonId ->
                                val lessonTitle = state.lessonTitles[lessonId] ?: lessonId
                                KadiCard(
                                    onClick = { onLessonClick(lessonId) },
                                    accent = MaterialTheme.colorScheme.primary
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        IconBubble(
                                            icon = Icons.Outlined.Book,
                                            tint = MaterialTheme.colorScheme.primary,
                                            size = 36.dp
                                        )
                                        Text(
                                            text = lessonTitle,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.weight(1f),
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Pill(text = "Open", color = MaterialTheme.colorScheme.primary)
                                    }
                                }
                            }
                        }
                        if (entry.relatedDocClassNames.isNotEmpty()) {
                            item {
                                SectionHeader(
                                    title = "Related Godot Docs",
                                    eyebrow = "GODOT 4.3 CLASSES",
                                    trailing = {
                                        Pill(
                                            text = "${entry.relatedDocClassNames.size}",
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                    }
                                )
                            }
                            items(items = entry.relatedDocClassNames) { className ->
                                KadiCard(
                                    onClick = { onDocClick(className) },
                                    accent = MaterialTheme.colorScheme.secondary
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        IconBubble(
                                            icon = Icons.Outlined.School,
                                            tint = MaterialTheme.colorScheme.secondary,
                                            size = 36.dp
                                        )
                                        Text(
                                            text = className,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Pill(text = "Open", color = MaterialTheme.colorScheme.secondary)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
