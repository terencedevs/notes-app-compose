package com.example.notesmaker.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object defining Room database operations for table "notes".
 */
@Dao
interface NoteDao {
    /**
     * Retrieves all notes ordered by pinned state descending, then by timestamp descending.
     */
    @Query("SELECT * FROM notes ORDER BY isPinned DESC, timestamp DESC")
    fun getAllNotes(): Flow<List<Note>>

    /**
     * Inserts a new note into the database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note): Long

    /**
     * Updates an existing note.
     */
    @Update
    suspend fun updateNote(note: Note)

    /**
     * Deletes a note record from the database.
     */
    @Delete
    suspend fun deleteNote(note: Note)

    /**
     * Queries a single note by its ID.
     */
    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: Long): Note?
}

