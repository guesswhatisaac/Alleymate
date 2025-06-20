package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal.BaseModal
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal.FormTextField
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMainOrange

@Composable
fun EditEventModal(
    event: Event,
    onDismissRequest: () -> Unit,
    onConfirmEdit: (Event) -> Unit
) {
    BaseModal(
        onDismissRequest = onDismissRequest,
        headerTitle = "Edit Event"
    ) {
        var eventName by remember { mutableStateOf(event.title) }
        var eventDate by remember { mutableStateOf(event.date) }
        var eventLocation by remember { mutableStateOf(event.location) }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FormTextField(
                label = "Event Name",
                value = eventName,
                onValueChange = { eventName = it }
            )
            FormTextField(
                label = "Date (e.g., Oct 25 - Oct 26)",
                value = eventDate,
                onValueChange = { eventDate = it }
            )
            FormTextField(
                label = "Location & Table",
                value = eventLocation,
                onValueChange = { eventLocation = it }
            )

            Button(
                onClick = {
                    val updatedEvent = event.copy(
                        title = eventName,
                        date = eventDate,
                        location = eventLocation
                    )
                    onConfirmEdit(updatedEvent)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(containerColor = AlleyMainOrange)
            ) {
                Text(
                    text = "SAVE CHANGES",
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
