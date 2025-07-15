package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal.BaseModal
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal.FormTextField
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMainOrange
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.EventUiModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEventModal(
    event: EventUiModel,
    onDismissRequest: () -> Unit,
    onConfirmEdit: (Event) -> Unit
) {
    BaseModal(
        onDismissRequest = onDismissRequest,
        headerTitle = "Edit Event"
    ) {
        // --- State initialized with existing event data ---
        var eventName by remember { mutableStateOf(event.title) }
        var eventLocation by remember { mutableStateOf(event.location) }

        // --- Date picker state initialized with existing event dates ---
        val startDatePickerState = rememberDatePickerState(initialSelectedDateMillis = event.startDate)
        var showStartDatePicker by remember { mutableStateOf(false) }

        val endDatePickerState = rememberDatePickerState(initialSelectedDateMillis = event.endDate)
        var showEndDatePicker by remember { mutableStateOf(false) }

        // --- Date formatting and validation logic ---
        val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

        val selectedStartDate = startDatePickerState.selectedDateMillis?.let {
            dateFormatter.format(Date(it))
        } ?: "Select Start Date"

        val selectedEndDate = endDatePickerState.selectedDateMillis?.let {
            dateFormatter.format(Date(it))
        } ?: "Select End Date"

        val isEndDateInvalid = startDatePickerState.selectedDateMillis != null &&
                endDatePickerState.selectedDateMillis != null &&
                endDatePickerState.selectedDateMillis!! < startDatePickerState.selectedDateMillis!!

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

            // --- Date Selection Cards ---
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DateSelectionCard(
                    modifier = Modifier.weight(1f),
                    label = "Start Date",
                    selectedDate = selectedStartDate,
                    isSelected = startDatePickerState.selectedDateMillis != null,
                    onClick = { showStartDatePicker = true }
                )

                DateSelectionCard(
                    modifier = Modifier.weight(1f),
                    label = "End Date",
                    selectedDate = selectedEndDate,
                    isSelected = endDatePickerState.selectedDateMillis != null,
                    isError = isEndDateInvalid,
                    onClick = { showEndDatePicker = true }
                )
            }

            // --- Error message for invalid date range ---
            if (isEndDateInvalid) {
                Text(
                    text = "End date cannot be before start date",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // --- Start Date Picker Dialog ---
            if (showStartDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showStartDatePicker = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showStartDatePicker = false
                                // Reset end date if it becomes invalid after changing start date
                                if (startDatePickerState.selectedDateMillis != null &&
                                    endDatePickerState.selectedDateMillis != null &&
                                    endDatePickerState.selectedDateMillis!! < startDatePickerState.selectedDateMillis!!) {
                                    endDatePickerState.selectedDateMillis = null
                                }
                            },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) { Text("OK") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showStartDatePicker = false }) { Text("Cancel") }
                    },
                    colors = DatePickerDefaults.colors(containerColor = Color.White)
                ) {
                    DatePicker(
                        state = startDatePickerState,
                        colors = DatePickerDefaults.colors(
                            selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                            todayDateBorderColor = MaterialTheme.colorScheme.primary,
                            containerColor = Color.White,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                            headlineContentColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            // --- End Date Picker Dialog (FIXED: Added complete color configuration) ---
            if (showEndDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showEndDatePicker = false },
                    confirmButton = {
                        TextButton(
                            onClick = { showEndDatePicker = false },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) { Text("OK") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showEndDatePicker = false }) { Text("Cancel") }
                    },
                    colors = DatePickerDefaults.colors(containerColor = Color.White)
                ) {
                    DatePicker(
                        state = endDatePickerState,
                        colors = DatePickerDefaults.colors(
                            selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                            todayDateBorderColor = MaterialTheme.colorScheme.primary,
                            containerColor = Color.White,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                            headlineContentColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            Button(
                onClick = {
                    val updatedEvent = Event(
                        eventId = event.eventId,
                        title = eventName,
                        location = eventLocation,
                        startDate = startDatePickerState.selectedDateMillis ?: event.startDate,
                        endDate = endDatePickerState.selectedDateMillis ?: event.endDate,
                        status = event.status
                    )
                    onConfirmEdit(updatedEvent)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = eventName.isNotBlank() && eventLocation.isNotBlank() && !isEndDateInvalid,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AlleyMainOrange)
            ) {
                Text(
                    text = "SAVE CHANGES",
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
