package com.example.notesmaker.ui.state

import com.example.notesmaker.data.local.Note

enum class SortOrder {
    NEWEST,
    OLDEST,
    TITLE_AZ
}

data class NotesUiState(
    val notes: List<Note> = emptyList(),
    val totalNoteCount: Int = 0,
    val searchQuery: String = "",
    val sortOrder: SortOrder = SortOrder.NEWEST,
    val isDialogVisible: Boolean = false,
    val editingNote: Note? = null,
    val recentlyDeletedNote: Note? = null
)
