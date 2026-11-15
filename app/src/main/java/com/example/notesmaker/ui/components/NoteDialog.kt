package com.example.notesmaker.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.notesmaker.R
import com.example.notesmaker.data.local.Note

private const val MAX_TITLE_LENGTH = 60

@Composable
fun NoteDialog(
    editingNote: Note?,
    onDismiss: () -> Unit,
    onSave: (title: String, body: String) -> Unit
) {
    var title by remember(editingNote) { mutableStateOf(editingNote?.title ?: "") }
    var body by remember(editingNote) { mutableStateOf(editingNote?.body ?: "") }

    val isSaveEnabled = (title.trim().isNotEmpty() || body.trim().isNotEmpty()) && title.length <= MAX_TITLE_LENGTH

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(if (editingNote != null) R.string.edit_note else R.string.add_note)
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { newValue ->
                        if (newValue.length <= MAX_TITLE_LENGTH) {
                            title = newValue
                        }
                    },
                    label = { Text(stringResource(R.string.title_hint)) },
                    singleLine = true,
                    supportingText = {
                        Text(
                            text = "${title.length}/$MAX_TITLE_LENGTH",
                            modifier = Modifier.align(Alignment.End),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = body,
                    onValueChange = { body = it },
                    label = { Text(stringResource(R.string.body_hint)) },
                    minLines = 3,
                    maxLines = 6,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(title, body) },
                enabled = isSaveEnabled
            ) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
