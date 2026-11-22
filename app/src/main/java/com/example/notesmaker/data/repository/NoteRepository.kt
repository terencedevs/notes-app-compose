package com.example.notesmaker.data.repository

import com.example.notesmaker.data.local.Note
import com.example.notesmaker.data.local.NoteDao
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface exposing CRUD data operations for notes.
 */
interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>
    suspend fun insertNote(note: Note): Long
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(note: Note)
}

/**
 * Implementation of [NoteRepository] wrapping Room's [NoteDao].
 */
class NoteRepositoryImpl(
    private val noteDao: NoteDao
) : NoteRepository {
    override fun getAllNotes(): Flow<List<Note>> = noteDao.getAllNotes()

    override suspend fun insertNote(note: Note): Long = noteDao.insertNote(note)

    override suspend fun updateNote(note: Note) = noteDao.updateNote(note)

    override suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)
}

