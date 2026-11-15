package com.example.notesmaker

import com.example.notesmaker.data.local.Note
import com.example.notesmaker.ui.state.SortOrder
import com.example.notesmaker.ui.viewmodel.NotesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: FakeNoteRepository
    private lateinit var viewModel: NotesViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeNoteRepository()
        viewModel = NotesViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun addNote_updatesUiStateList() = runTest {
        viewModel.onSaveNote("Test Title", "Test Body")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.notes.size)
        assertEquals("Test Title", state.notes[0].title)
        assertEquals("Test Body", state.notes[0].body)
    }

    @Test
    fun editNote_updatesExistingNoteInState() = runTest {
        viewModel.onSaveNote("Original Title", "Original Body")
        testDispatcher.scheduler.advanceUntilIdle()

        val initialNote = viewModel.uiState.value.notes[0]
        viewModel.onOpenEditDialog(initialNote)
        viewModel.onSaveNote("Updated Title", "Updated Body")
        testDispatcher.scheduler.advanceUntilIdle()

        val updatedState = viewModel.uiState.value
        assertEquals(1, updatedState.notes.size)
        assertEquals("Updated Title", updatedState.notes[0].title)
        assertEquals("Updated Body", updatedState.notes[0].body)
    }

    @Test
    fun deleteNote_andUndo_restoresNote() = runTest {
        viewModel.onSaveNote("Delete Me", "Body")
        testDispatcher.scheduler.advanceUntilIdle()

        val noteToDelete = viewModel.uiState.value.notes[0]
        viewModel.onDeleteNote(noteToDelete)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(0, viewModel.uiState.value.notes.size)
        assertNotNull(viewModel.uiState.value.recentlyDeletedNote)

        viewModel.onUndoDelete()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, viewModel.uiState.value.notes.size)
        assertEquals("Delete Me", viewModel.uiState.value.notes[0].title)
        assertNull(viewModel.uiState.value.recentlyDeletedNote)
    }

    @Test
    fun searchQuery_filtersNotesByTitleAndBody() = runTest {
        viewModel.onSaveNote("Kotlin Tips", "Learn Coroutines")
        viewModel.onSaveNote("Grocery List", "Buy Milk and Eggs")
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onSearchQueryChange("milk")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.notes.size)
        assertEquals("Grocery List", state.notes[0].title)
    }

    @Test
    fun pinnedNote_remainsAtTopRegardlessOfTimestamp() = runTest {
        repository.insertNote(Note(id = 1, title = "Unpinned 1", body = "", timestamp = 2000L, isPinned = false))
        repository.insertNote(Note(id = 2, title = "Pinned Note", body = "", timestamp = 1000L, isPinned = true))
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onSortOrderChange(SortOrder.NEWEST)
        testDispatcher.scheduler.advanceUntilIdle()

        val notes = viewModel.uiState.value.notes
        assertEquals(2, notes.size)
        assertEquals("Pinned Note", notes[0].title)
        assertTrue(notes[0].isPinned)
        assertFalse(notes[1].isPinned)
    }
}
