package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal.BaseModal
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal.FormTextField
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMainOrange
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMateTheme

@Composable
fun ExpensesTrackerModal(
    eventName: String,
    onDismissRequest: () -> Unit,
    onAddExpense: (description: String, amount: Double) -> Unit
) {
    BaseModal(
        onDismissRequest = onDismissRequest,
        headerTitle = "$eventName Expenses"
    ) {
        var description by remember { mutableStateOf("") }
        var amount by remember { mutableStateOf("") }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // TODO: Add a list of existing expenses here

            FormTextField(
                label = "Expense Description",
                value = description,
                onValueChange = { description = it }
            )
            FormTextField(
                label = "Amount",
                value = amount,
                onValueChange = { amount = it },
                keyboardType = KeyboardType.Number
            )

            Button(
                onClick = {
                    onAddExpense(description, amount.toDoubleOrNull() ?: 0.0)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(containerColor = AlleyMainOrange),
                enabled = description.isNotBlank() && (amount.toDoubleOrNull() ?: 0.0) > 0
            ) {
                Text(
                    text = "ADD EXPENSE",
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun ExpensesTrackerModalPreview() {
    AlleyMateTheme {
        ExpensesTrackerModal(
            eventName = "KOMIKET '25",
            onDismissRequest = {},
            onAddExpense = {_,_ ->}
        )
    }
}