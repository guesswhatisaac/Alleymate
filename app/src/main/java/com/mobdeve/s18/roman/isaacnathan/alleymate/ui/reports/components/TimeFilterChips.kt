package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.reports.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.EventSelectorBar
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMainOrange
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.reports.TimeFilter

@Composable
fun ReportFilters(
    events: List<Event>,
    selectedEvent: Event,
    onEventSelected: (Event) -> Unit,
    selectedTimeFilter: TimeFilter,
    onTimeFilterSelected: (TimeFilter) -> Unit
) {
    val isAllEventsSelected = selectedEvent.eventId == -1

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Event Selector
        EventSelectorBar(
            currentEvent = selectedEvent,
            events = events,
            onEventSelected = onEventSelected,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // Time Filter Chips
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(TimeFilter.entries.toTypedArray()) { filter ->
                val isSelected = selectedTimeFilter == filter
                val isEnabled = isAllEventsSelected || filter == TimeFilter.ALL_TIME
                val chipText = if (!isAllEventsSelected && filter == TimeFilter.ALL_TIME) {
                    "Entire Event"
                } else {
                    filter.displayName
                }

                // Use the custom chip component, passing the enabled state
                CustomReportFilterChip(
                    text = chipText,
                    isSelected = isSelected,
                    enabled = isEnabled,
                    onClick = { onTimeFilterSelected(filter) }
                )
            }
        }
    }
}


@Composable
private fun CustomReportFilterChip(
    text: String,
    isSelected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val selectedColor = AlleyMainOrange

    // Define colors based on both selected and enabled states
    val backgroundColor = when {
        isSelected -> selectedColor
        else -> Color.Transparent
    }
    val textColor = when {
        !enabled -> Color.LightGray
        isSelected -> Color.White
        else -> Color.DarkGray
    }
    val border = when {
        isSelected -> null
        !enabled -> BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
        else -> BorderStroke(1.dp, Color.LightGray)
    }

    Surface(
        shape = MaterialTheme.shapes.medium,
        color = backgroundColor,
        border = border,
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .combinedClickable(
                onClick = onClick,
                enabled = enabled
            )
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}