package com.github.rahul_gill.collegespace.presentation.ui.events

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.rahul_gill.collegespace.presentation.ui.events.schedule.UiEvent

@Composable
fun EventInfoDialog(
    uiEvent: UiEvent,
    onEventEdit: (UiEvent) -> Unit,
    onEventDelete: (UiEvent) -> Unit,
    dialogShowing: Boolean = false,
    onDismiss: () -> Unit
) {

    if(dialogShowing) AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(text = uiEvent.name)
        },
        text = {
            Column {
                Text(uiEvent.start.format(viewFormat))
                Text(uiEvent.end.format(viewFormat))
                uiEvent.description?.let { Text(uiEvent.description!!) }

            }
        },
        buttons = {
            Row(
                modifier = Modifier.padding(all = 8.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { onDismiss() },
                    content = { Text("Dismiss") }
                )
                Button(
                    onClick = {
                        onEventEdit(uiEvent)
                        onDismiss()
                        onEventDelete(uiEvent)
                    },
                    content = { Text("Delete") },
                    modifier = Modifier.background(Color.Red)
                )
                Button(
                    onClick = {
                        onEventEdit(uiEvent)
                        onDismiss()
                    },
                    content = { Text("Edit") }
                )
            }
        }
    )
}