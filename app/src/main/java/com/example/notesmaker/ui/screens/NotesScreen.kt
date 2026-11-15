package com.example.notesmaker.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.notesmaker.R
import com.example.notesmaker.ui.components.EmptyState
import com.example.notesmaker.ui.components.NoteDialog
import com.example.notesmaker.ui.components.NoteItem
import com.example.notesmaker.ui.components.NotesTopAppBar
import com.example.notesmaker.ui.viewmodel.NotesViewModel

@Composable
fun NotesScreen(
    viewModel: NotesViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val deletedMsg = stringResource(R.string.note_deleted)
    val undoMsg = stringResource(R.string.undo)

    LaunchedEffect(uiState.recentlyDeletedNote) {
        val deletedNote = uiState.recentlyDeletedNote
        if (deletedNote != null) {
            val result = snackbarHostState.showSnackbar(
                message = deletedMsg,
                actionLabel = undoMsg,
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.onUndoDelete()
            }
        }
    }

    Scaffold(
        topBar = {
            NotesTopAppBar(
                totalNoteCount = uiState.totalNoteCount,
                currentSortOrder = uiState.sortOrder,
                onSortOrderSelected = viewModel::onSortOrderChange
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = viewModel::onOpenAddDialog,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.cd_add_note_fab)
                )
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Live Search Bar
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = viewModel::onSearchQueryChange,
                placeholder = { Text(stringResource(R.string.search_placeholder)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.cd_search_icon)
                    )
                },
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = stringResource(R.string.clear_search)
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = OutlinedTextFieldDefaults.colors()
            )

            if (uiState.notes.isEmpty()) {
                EmptyState(
                    isSearchEmpty = uiState.searchQuery.isNotBlank(),
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 88.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(
                        items = uiState.notes,
                        key = { it.id }
                    ) { note ->
                        NoteItem(
                            note = note,
                            onCardClick = { viewModel.onOpenEditDialog(note) },
                            onTogglePin = { viewModel.onTogglePin(note) },
                            onDeleteClick = { viewModel.onDeleteNote(note) },
                            modifier = Modifier.animateItem()
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }

    if (uiState.isDialogVisible) {
        NoteDialog(
            editingNote = uiState.editingNote,
            onDismiss = viewModel::onDismissDialog,
            onSave = viewModel::onSaveNote
        )
    }
}
