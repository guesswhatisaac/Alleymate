package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal.BaseModal
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal.FormTextField
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMainOrange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventModal(
    onDismissRequest: () -> Unit,
    // The signature now passes the real data types
    onAddEvent: (title: String, location: String, startDate: Long, endDate: Long) -> Unit
) {
    BaseModal(
        onDismissRequest = onDismissRequest,
        headerTitle = "Add Event"
    ) {
        var eventName by remember { mutableStateOf("") }
        var eventLocation by remember { mutableStateOf("") }

        val startDatePickerState = rememberDatePickerState()
        var showStartDatePicker by remember { mutableStateOf(false) }

        val endDatePickerState = rememberDatePickerState()
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
                    },
                    dismissButton = {
                        TextButton(onClick = { showStartDatePicker = false }) { Text("Cancel") }
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
                    },
                    dismissButton = {
                        TextButton(onClick = { showEndDatePicker = false }) { Text("Cancel") }
                    }
                ) {
                    DatePicker(state = endDatePickerState)
                }
            }

            Button(
                onClick = {
                    // Get the selected timestamps, providing a default if null
                    val startMillis = startDatePickerState.selectedDateMillis ?: 0L
                    val endMillis = endDatePickerState.selectedDateMillis ?: 0L

                    onAddEvent(eventName, eventLocation, startMillis, endMillis)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = eventName.isNotBlank() && eventLocation.isNotBlank() &&
                        startDatePickerState.selectedDateMillis != null &&
                        endDatePickerState.selectedDateMillis != null,
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(containerColor = AlleyMainOrange)
            ) {
                Text(
                    text = "ADD",
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}