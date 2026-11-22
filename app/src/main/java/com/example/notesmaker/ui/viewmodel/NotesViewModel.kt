package com.example.notesmaker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.notesmaker.data.local.Note
import com.example.notesmaker.data.preferences.ThemeMode
import com.example.notesmaker.data.preferences.ThemePreferences
import com.example.notesmaker.data.repository.NoteRepository
import com.example.notesmaker.ui.state.NotesUiState
import com.example.notesmaker.ui.state.SortOrder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel managing UI state and business logic for the Notes screen.
 * Handles note CRUD operations via [NoteRepository] and theme mode via [ThemePreferences].
 */
class NotesViewModel(
    private val repository: NoteRepository,
    private val themePreferences: ThemePreferences? = null
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _sortOrder = MutableStateFlow(SortOrder.NEWEST)
    private val _isDialogVisible = MutableStateFlow(false)
    private val _editingNote = MutableStateFlow<Note?>(null)
    private val _recentlyDeletedNote = MutableStateFlow<Note?>(null)
    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)

    init {
        themePreferences?.let { prefs ->
            viewModelScope.launch {
                prefs.themeMode.collect { mode ->
                    _themeMode.value = mode
                }
            }
        }
    }

    private val _dialogAndDeletedState = combine(_isDialogVisible, _editingNote, _recentlyDeletedNote) { isDialogShown, editNote, deletedNote ->
        Triple(isDialogShown, editNote, deletedNote)
    }

    val uiState: StateFlow<NotesUiState> = combine(
        repository.getAllNotes(),
        _searchQuery,
        _sortOrder,
        _dialogAndDeletedState,
        _themeMode
    ) { rawNotes, query, sort, (isDialogShown, editNote, deletedNote), theme ->
        val totalCount = rawNotes.size
        val filtered = if (query.isBlank()) {
            rawNotes
        } else {
            val q = query.trim().lowercase()
            rawNotes.filter {
                it.title.lowercase().contains(q) || it.body.lowercase().contains(q)
            }
        }

        val secondaryComparator: Comparator<Note> = when (sort) {
            SortOrder.NEWEST -> compareByDescending { it.timestamp }
            SortOrder.OLDEST -> compareBy { it.timestamp }
            SortOrder.TITLE_AZ -> compareBy { it.title.lowercase() }
        }

        val sorted = filtered.sortedWith(
            compareByDescending<Note> { it.isPinned }.then(secondaryComparator)
        )

        NotesUiState(
            notes = sorted,
            totalNoteCount = totalCount,
            searchQuery = query,
            sortOrder = sort,
            isDialogVisible = isDialogShown,
            editingNote = editNote,
            recentlyDeletedNote = deletedNote,
            themeMode = theme
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = NotesUiState()
    )

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onSortOrderChange(newSortOrder: SortOrder) {
        _sortOrder.value = newSortOrder
    }

    fun onOpenAddDialog() {
        _editingNote.value = null
        _isDialogVisible.value = true
    }

    fun onOpenEditDialog(note: Note) {
        _editingNote.value = note
        _isDialogVisible.value = true
    }

    fun onDismissDialog() {
        _isDialogVisible.value = false
        _editingNote.value = null
    }

    fun onSaveNote(title: String, body: String) {
        val trimmedTitle = title.trim()
        val trimmedBody = body.trim()

        if (trimmedTitle.isEmpty() && trimmedBody.isEmpty()) return

        val currentNote = _editingNote.value
        viewModelScope.launch {
            if (currentNote != null) {
                val updatedNote = currentNote.copy(
                    title = trimmedTitle,
                    body = trimmedBody,
                    timestamp = System.currentTimeMillis()
                )
                repository.updateNote(updatedNote)
            } else {
                val newNote = Note(
                    title = trimmedTitle,
                    body = trimmedBody,
                    timestamp = System.currentTimeMillis(),
                    isPinned = false
                )
                repository.insertNote(newNote)
            }
            onDismissDialog()
        }
    }

    fun onDeleteNote(note: Note) {
        viewModelScope.launch {
            _recentlyDeletedNote.value = note
            repository.deleteNote(note)
        }
    }

    fun onUndoDelete() {
        val noteToRestore = _recentlyDeletedNote.value ?: return
        viewModelScope.launch {
            repository.insertNote(noteToRestore)
            _recentlyDeletedNote.value = null
        }
    }

    fun onTogglePin(note: Note) {
        viewModelScope.launch {
            val updatedNote = note.copy(isPinned = !note.isPinned)
            repository.updateNote(updatedNote)
        }
    }

    fun onToggleTheme(isCurrentlyDark: Boolean) {
        val newMode = if (isCurrentlyDark) ThemeMode.LIGHT else ThemeMode.DARK
        viewModelScope.launch {
            themePreferences?.setThemeMode(newMode)
        }
    }

    class Factory(
        private val repository: NoteRepository,
        private val themePreferences: ThemePreferences? = null
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
                return NotesViewModel(repository, themePreferences) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

