package com.example.notesmaker

import com.example.notesmaker.data.local.Note
import com.example.notesmaker.data.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeNoteRepository : NoteRepository {

    private val notesFlow = MutableStateFlow<List<Note>>(emptyList())
    private var nextId = 1L

    override fun getAllNotes(): Flow<List<Note>> = notesFlow

    override suspend fun insertNote(note: Note): Long {
        val idToAssign = if (note.id == 0L) nextId++ else note.id
        val newNote = note.copy(id = idToAssign)
        notesFlow.update { list ->
            val existingIndex = list.indexOfFirst { it.id == idToAssign }
            if (existingIndex >= 0) {
                list.toMutableList().apply { set(existingIndex, newNote) }
            } else {
                list + newNote
            }
        }
        return idToAssign
    }

    override suspend fun updateNote(note: Note) {
        notesFlow.update { list ->
            list.map { if (it.id == note.id) note else it }
        }
    }

    override suspend fun deleteNote(note: Note) {
        notesFlow.update { list ->
            list.filterNot { it.id == note.id }
        }
    }
}
