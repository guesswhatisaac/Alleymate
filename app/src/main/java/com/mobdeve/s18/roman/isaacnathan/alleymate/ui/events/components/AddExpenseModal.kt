package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal.BaseModal
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal.FormTextField
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMainOrange

@Composable
fun AddExpenseModal(
    onDismissRequest: () -> Unit,
    onAddExpense: (description: String, amount: Double) -> Unit
) {
    var expenseDesc by remember { mutableStateOf("") }
    var expenseAmount by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    BaseModal(
        onDismissRequest = onDismissRequest,
        headerTitle = "Add New Expense"
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FormTextField(
                label = "Expense Description",
                value = expenseDesc,
                onValueChange = {
                    expenseDesc = it
                    showError = false
                },
                isError = showError && expenseDesc.isBlank(),
                errorMessage = "Description is required"
            )

            FormTextField(
                label = "Amount (₱)",
                value = expenseAmount,
                onValueChange = {
                    expenseAmount = it
                    showError = false
                },
                keyboardType = KeyboardType.Number,
                isError = showError && (expenseAmount.toDoubleOrNull() ?: 0.0) <= 0,
                errorMessage = "Please enter a valid amount"
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismissRequest,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }

                Button(
                    onClick = {
                        val amount = expenseAmount.toDoubleOrNull() ?: 0.0
                        if (expenseDesc.isNotBlank() && amount > 0) {
                            onAddExpense(expenseDesc, amount)
                        } else {
                            showError = true
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AlleyMainOrange
                    )
                ) {
                    Text("Add Expense")
                }
            }
        }
    }
}
