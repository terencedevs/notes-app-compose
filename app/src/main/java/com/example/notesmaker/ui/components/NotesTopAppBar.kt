package com.example.notesmaker.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.notesmaker.R
import com.example.notesmaker.ui.state.SortOrder

/**
 * Top App Bar displaying title, total note count, animated sun/moon theme toggle button, and sort menu.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesTopAppBar(
    totalNoteCount: Int,
    currentSortOrder: SortOrder,
    onSortOrderSelected: (SortOrder) -> Unit,
    isDarkTheme: Boolean = false,
    onToggleTheme: () -> Unit = {},
) {
    var isSortMenuExpanded by remember { mutableStateOf(false) }

    val noteCountText = when (totalNoteCount) {
        0 -> stringResource(R.string.notes_count_empty)
        1 -> stringResource(R.string.notes_count_single)
        else -> stringResource(R.string.notes_count, totalNoteCount)
    }

    TopAppBar(
        title = {
            Column {
                Text(
                    text = stringResource(R.string.top_bar_title),
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = noteCountText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        actions = {
            IconButton(onClick = onToggleTheme) {
                AnimatedContent(
                    targetState = isDarkTheme,
                    transitionSpec = {
                        (fadeIn(animationSpec = tween(300)) + scaleIn(animationSpec = tween(300))) togetherWith
                        (fadeOut(animationSpec = tween(300)) + scaleOut(animationSpec = tween(300)))
                    },
                    label = "ThemeToggleAnimation"
                ) { dark ->
                    Icon(
                        imageVector = if (dark) Icons.Default.LightMode else Icons.Default.DarkMode,
                        contentDescription = stringResource(
                            if (dark) R.string.switch_to_light_mode else R.string.switch_to_dark_mode
                        )
                    )
                }
            }

            IconButton(onClick = { isSortMenuExpanded = true }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Sort,
                    contentDescription = stringResource(R.string.cd_sort_icon)
                )
            }

            DropdownMenu(
                expanded = isSortMenuExpanded,
                onDismissRequest = { isSortMenuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.sort_newest)) },
                    onClick = {
                        onSortOrderSelected(SortOrder.NEWEST)
                        isSortMenuExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.sort_oldest)) },
                    onClick = {
                        onSortOrderSelected(SortOrder.OLDEST)
                        isSortMenuExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.sort_title)) },
                    onClick = {
                        onSortOrderSelected(SortOrder.TITLE_AZ)
                        isSortMenuExpanded = false
                    }
                )
            }
        }
    )
}


