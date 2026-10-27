package com.kadicompanion.ui.bookmarks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kadicompanion.R
import com.kadicompanion.domain.model.Bookmark
import com.kadicompanion.ui.common.EmptyState
import com.kadicompanion.ui.common.IconBubble
import com.kadicompanion.ui.common.KadiCard
import com.kadicompanion.ui.common.KadiTopBar
import com.kadicompanion.ui.common.LoadingSkeleton
import com.kadicompanion.ui.common.Pill
import com.kadicompanion.ui.common.SectionHeader
import com.kadicompanion.ui.common.cleanPlainText
import com.kadicompanion.ui.common.studioCardSpacing
import com.kadicompanion.ui.common.studioScreenPadding

@Composable
fun BookmarksScreen(
    onLessonClick: (String) -> Unit,
    onDocClick: (String) -> Unit,
    onGlossaryClick: (String) -> Unit,
    vm: BookmarksViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    val hPad = studioScreenPadding()
    val vGap = studioCardSpacing()

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            KadiTopBar(
                title = stringResource(R.string.nav_bookmarks),
                subtitle = "SAVED FOR LATER",
                actions = {
                    if (state.bookmarks.isNotEmpty()) {
                        Pill(
                            text = "${state.bookmarks.size}",
                            modifier = Modifier.padding(end = 4.dp),
                            icon = Icons.Outlined.Bookmarks
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> LoadingSkeleton()
                state.bookmarks.isEmpty() -> EmptyState(
                    title = "No bookmarks yet",
                    message = stringResource(R.string.empty_bookmarks),
                    icon = Icons.Outlined.Bookmark
                )
                else -> {
                    val grouped = state.bookmarks.groupBy { kindOf(it) }
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = hPad, end = hPad, top = 4.dp, bottom = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(vGap)
                    ) {
                        BookmarkKind.entries.forEach { kind ->
                            val rows = grouped[kind].orEmpty()
                            if (rows.isNotEmpty()) {
                                item(key = "header-${kind.name}") {
                                    SectionHeader(
                                        title = kind.groupTitle,
                                        modifier = Modifier.padding(top = 10.dp, bottom = 2.dp),
                                        eyebrow = "BOOKMARKED",
                                        trailing = { Pill(text = "${rows.size}", color = accentFor(kind)) }
                                    )
                                }
                                items(rows, key = { it.id }) { bookmark ->
                                    val displayTitle = bookmark.glossaryTerm
                                        ?: bookmark.lessonId?.let { state.lessonTitles[it] ?: it }
                                        ?: state.phaseTitles[bookmark.phaseId]
                                        ?: bookmark.phaseId
                                    val subtitle = if (bookmark.lessonId != null) {
                                        state.phaseTitles[bookmark.phaseId]
                                    } else {
                                        bookmark.note
                                    }
                                    BookmarkRow(
                                        displayTitle = cleanPlainText(displayTitle),
                                        subtitle = subtitle?.let(::cleanPlainText),
                                        kind = kind,
                                        accent = accentFor(kind),
                                        onClick = {
                                            when {
                                                bookmark.lessonId != null -> onLessonClick(bookmark.lessonId)
                                                bookmark.glossaryTerm != null -> onGlossaryClick(bookmark.glossaryTerm)
                                                else -> onDocClick(bookmark.phaseId)
                                            }
                                        },
                                        onRemove = { vm.remove(bookmark.id) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private enum class BookmarkKind(val label: String, val groupTitle: String) {
    LESSON(label = "Lesson", groupTitle = "Lessons"),
    DOC(label = "Godot class", groupTitle = "Godot classes"),
    GLOSSARY(label = "Glossary term", groupTitle = "Glossary terms")
}

private fun kindOf(bookmark: Bookmark): BookmarkKind = when {
    bookmark.lessonId != null -> BookmarkKind.LESSON
    bookmark.glossaryTerm != null -> BookmarkKind.GLOSSARY
    else -> BookmarkKind.DOC
}

private fun iconFor(kind: BookmarkKind): ImageVector = when (kind) {
    BookmarkKind.LESSON -> Icons.AutoMirrored.Outlined.MenuBook
    BookmarkKind.DOC -> Icons.Outlined.Code
    BookmarkKind.GLOSSARY -> Icons.Outlined.School
}

@Composable
private fun accentFor(kind: BookmarkKind): Color = when (kind) {
    BookmarkKind.LESSON -> MaterialTheme.colorScheme.primary
    BookmarkKind.DOC -> MaterialTheme.colorScheme.secondary
    BookmarkKind.GLOSSARY -> MaterialTheme.colorScheme.tertiary
}

@Composable
private fun BookmarkRow(
    displayTitle: String,
    subtitle: String?,
    kind: BookmarkKind,
    accent: Color,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    KadiCard(onClick = onClick, accent = accent) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconBubble(icon = iconFor(kind), tint = accent)
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = displayTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                subtitle?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Pill(text = kind.label, color = accent, icon = iconFor(kind))
            }
            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = stringResource(R.string.action_unbookmark),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
