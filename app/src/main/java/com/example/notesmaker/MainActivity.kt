package com.example.notesmaker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.notesmaker.data.local.AppDatabase
import com.example.notesmaker.data.repository.NoteRepositoryImpl
import com.example.notesmaker.ui.screens.NotesScreen
import com.example.notesmaker.ui.theme.NotesMakerTheme
import com.example.notesmaker.ui.viewmodel.NotesViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: NotesViewModel by viewModels {
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = NoteRepositoryImpl(database.noteDao())
        NotesViewModel.Factory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            NotesMakerTheme {
                NotesScreen(viewModel = viewModel)
            }
        }
    }
}
