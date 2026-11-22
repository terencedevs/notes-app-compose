package com.example.notesmaker.ui.state

import com.example.notesmaker.data.local.Note
import com.example.notesmaker.data.preferences.ThemeMode

/**
 * Sort options for note listing.
 */
enum class SortOrder {
    NEWEST,
    OLDEST,
    TITLE_AZ
}

/**
 * Represents the complete UI state for the Notes screen.
 */
data class NotesUiState(
    val notes: List<Note> = emptyList(),
    val totalNoteCount: Int = 0,
    val searchQuery: String = "",
    val sortOrder: SortOrder = SortOrder.NEWEST,
    val isDialogVisible: Boolean = false,
    val editingNote: Note? = null,
    val recentlyDeletedNote: Note? = null,
    val themeMode: ThemeMode = ThemeMode.SYSTEM
)

