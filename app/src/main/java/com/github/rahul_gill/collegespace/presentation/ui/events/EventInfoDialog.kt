package com.github.rahul_gill.collegespace.presentation.ui.events

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.rahul_gill.collegespace.presentation.ui.events.schedule.UiEvent

@Composable
fun EventInfoDialog(
    uiEvent: UiEvent,
    onEventEdit: (UiEvent) -> Unit,
    dialogShowing: Boolean = false,
    onDismiss: () -> Unit) {

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
                modifier = Modifier.padding(all = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onDismiss() },
                    content = { Text("Dismiss") }
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