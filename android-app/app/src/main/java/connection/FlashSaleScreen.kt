package com.example.avniproject.backendandroidconnection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun FlashSaleScreen(viewModel: SaleViewModel = hiltViewModel()) {

    var showDialog by remember { mutableStateOf(false) }
    var inputQuantity by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("iPhone 17 Pro", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(20.dp))
                Text("Flash sale Price $99", color = Color.Gray)
            }

        }
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            "Unit Left : ${viewModel.stockCount}", fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold,
            color = if (viewModel.stockCount < 10) Color.Red else Color.Black
        )

        Button(
            onClick = { viewModel.buy(1L) },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            enabled = viewModel.stockCount > 0,
            colors = ButtonDefaults.buttonColors(Color(0xFF6200EE))

        ) {
            Text("BUY NOW ⚡", color = Color.White, fontSize = 18.sp)
        }
        Text(text = viewModel.statusMessage, modifier = Modifier.padding(top = 16.dp))
        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = { showDialog = true }
        ) {
            Text("Admin: Update Stock", color = Color.Gray, fontSize = 14.sp)
        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Add Inventory") },
                text = {
                    OutlinedTextField(
                        value = inputQuantity,
                        onValueChange = { inputQuantity = it },
                        label = { Text("Enter Quantity") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                },
                confirmButton = {
                    Button(onClick = {
                        val qty = inputQuantity.toIntOrNull() ?: 0
                        if (qty > 0) {
                            viewModel.incrementStock(1L, qty) // ViewModel call
                            showDialog = false
                            inputQuantity = ""
                        }
                    }) { Text("Add") }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) { Text("Cancel") }
                }
            )
        }
    }

}



