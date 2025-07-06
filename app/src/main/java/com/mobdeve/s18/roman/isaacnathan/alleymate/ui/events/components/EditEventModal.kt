package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal.BaseModal
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal.FormTextField
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMainOrange
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
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
        var eventLocation by remember { mutableStateOf(event.location) }

        val startDatePickerState = rememberDatePickerState(initialSelectedDateMillis = event.startDate)
        var showStartDatePicker by remember { mutableStateOf(false) }

        val endDatePickerState = rememberDatePickerState(initialSelectedDateMillis = event.endDate)
        var showEndDatePicker by remember { mutableStateOf(false) }

        val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val selectedStartDate = startDatePickerState.selectedDateMillis?.let { dateFormatter.format(Date(it)) } ?: "Select Start Date"
        val selectedEndDate = endDatePickerState.selectedDateMillis?.let { dateFormatter.format(Date(it)) } ?: "Select End Date"


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
                label = "Location & Table",
                value = eventLocation,
                onValueChange = { eventLocation = it }
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { showStartDatePicker = true }, modifier = Modifier.weight(1f)) {
                    Text(selectedStartDate)
                }
                OutlinedButton(onClick = { showEndDatePicker = true }, modifier = Modifier.weight(1f)) {
                    Text(selectedEndDate)
                }
            }

            if (showStartDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showStartDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = { showStartDatePicker = false }) { Text("OK") }
                    }
                ) {
                    DatePicker(state = startDatePickerState)
                }
            }
            if (showEndDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showEndDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = { showEndDatePicker = false }) { Text("OK") }
                    }
                ) {
                    DatePicker(state = endDatePickerState)
                }
            }

            Button(
                onClick = {
                    val updatedEvent = event.copy(
                        title = eventName,
                        location = eventLocation,
                        startDate = startDatePickerState.selectedDateMillis ?: event.startDate,
                        endDate = endDatePickerState.selectedDateMillis ?: event.endDate
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