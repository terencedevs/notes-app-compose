package com.example.notesmaker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.notesmaker.data.local.AppDatabase
import com.example.notesmaker.data.preferences.ThemeMode
import com.example.notesmaker.data.preferences.ThemePreferences
import com.example.notesmaker.data.repository.NoteRepositoryImpl
import com.example.notesmaker.ui.screens.NotesScreen
import com.example.notesmaker.ui.theme.NotesMakerTheme
import com.example.notesmaker.ui.viewmodel.NotesViewModel

/**
 * Main activity of the NotesMaker application.
 * Initializes database, repository, DataStore theme preferences, ViewModel, and edge-to-edge UI.
 */
class MainActivity : ComponentActivity() {

    private val viewModel: NotesViewModel by viewModels {
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = NoteRepositoryImpl(database.noteDao())
        val themePreferences = ThemePreferences(applicationContext)
        NotesViewModel.Factory(repository, themePreferences)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val systemInDark = isSystemInDarkTheme()
            val isDarkTheme = when (uiState.themeMode) {
                ThemeMode.SYSTEM -> systemInDark
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }

            NotesMakerTheme(darkTheme = isDarkTheme) {
                NotesScreen(
                    viewModel = viewModel,
                    isDarkTheme = isDarkTheme,
                )
            }
        }
    }
}

