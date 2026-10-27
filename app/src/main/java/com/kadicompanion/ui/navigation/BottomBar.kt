package com.kadicompanion.ui.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kadicompanion.data.local.datastore.BottomBarStyle
import com.kadicompanion.ui.theme.LocalReduceMotion
import com.kadicompanion.ui.theme.LocalUiPreferences

/**
 * Customizable Studio Bottom Navigation Bar supporting:
 * - PILL_TABS (Default icon + label with active pill)
 * - COMPACT_ICONS (Slim icon-only bar for maximum vertical reading room on phones)
 * - FLOATING_DOCK (Floating rounded dock inset from bottom screen edges)
 */
@Composable
fun KadiBottomBar(
    navController: NavHostController,
    destinations: List<TopLevelDestination> = TopLevelDestination.entries
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val style = LocalUiPreferences.current.bottomBarStyle

    when (style) {
        BottomBarStyle.FLOATING_DOCK -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    shadowElevation = 6.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        destinations.forEach { dest ->
                            val selected = currentRoute == dest.route ||
                                backStackEntry?.destination?.hierarchy?.any { it.route == dest.route } == true
                            StudioNavTab(
                                destination = dest,
                                selected = selected,
                                showLabel = true,
                                onClick = {
                                    navController.navigate(dest.route) {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
        else -> {
            val showLabel = style != BottomBarStyle.COMPACT_ICONS
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp,
                shadowElevation = 0.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth().navigationBarsPadding()) {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = if (showLabel) 6.dp else 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        destinations.forEach { dest ->
                            val selected = currentRoute == dest.route ||
                                backStackEntry?.destination?.hierarchy?.any { it.route == dest.route } == true
                            StudioNavTab(
                                destination = dest,
                                selected = selected,
                                showLabel = showLabel,
                                onClick = {
                                    navController.navigate(dest.route) {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StudioNavTab(
    destination: TopLevelDestination,
    selected: Boolean,
    showLabel: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val reduceMotion = LocalReduceMotion.current
    val spec = tween<Color>(durationMillis = if (reduceMotion) 0 else 180)
    val contentColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = spec,
        label = "navTint"
    )
    val pillBg by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.14f) else Color.Transparent,
        animationSpec = spec,
        label = "navPillBg"
    )

    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .selectable(selected = selected, onClick = onClick)
            .background(pillBg)
            .padding(vertical = if (showLabel) 6.dp else 8.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Icon(
            imageVector = if (selected) destination.selectedIcon else destination.icon,
            contentDescription = stringResource(destination.labelRes),
            tint = contentColor,
            modifier = Modifier.size(if (showLabel) 20.dp else 22.dp)
        )
        if (showLabel) {
            Text(
                text = stringResource(destination.labelRes),
                style = MaterialTheme.typography.labelSmall,
                color = contentColor,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
