package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal.BaseModal
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal.FormTextField
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMainOrange
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventModal(
    onDismissRequest: () -> Unit,
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

        val selectedStartDate = startDatePickerState.selectedDateMillis?.let {
            dateFormatter.format(Date(it))
        } ?: "Select Start Date"

        val selectedEndDate = endDatePickerState.selectedDateMillis?.let {
            dateFormatter.format(Date(it))
        } ?: "Select End Date"

        // Allow same dates, only prevent end date being before start date
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

            // Date Selection Cards
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
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

            // Error message for invalid date range
            if (isEndDateInvalid) {
                Text(
                    text = "End date cannot be before start date",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Date Picker Dialogs
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
                        ) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showStartDatePicker = false }) {
                            Text("Cancel")
                        }
                    },
                    colors = DatePickerDefaults.colors(
                        containerColor = Color.White
                    )
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
                    colors = DatePickerDefaults.colors(
                        containerColor = Color.White
                    )
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
                    val startMillis = startDatePickerState.selectedDateMillis ?: 0L
                    val endMillis = endDatePickerState.selectedDateMillis ?: 0L
                    onAddEvent(eventName, eventLocation, startMillis, endMillis)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = eventName.isNotBlank() && eventLocation.isNotBlank() &&
                        startDatePickerState.selectedDateMillis != null &&
                        endDatePickerState.selectedDateMillis != null &&
                        !isEndDateInvalid,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AlleyMainOrange)
            ) {
                Text(
                    text = "ADD",
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun DateSelectionCard(
    modifier: Modifier = Modifier,
    label: String,
    selectedDate: String,
    isSelected: Boolean,
    isError: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (isError) {
                MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
            } else {
                Color.Gray.copy(alpha = 0.5f)
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = if (isError) MaterialTheme.colorScheme.error else Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isError) MaterialTheme.colorScheme.error else Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }

            Text(
                text = selectedDate,
                style = MaterialTheme.typography.bodySmall,
                color = if (isError) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
            )
        }
    }
}
