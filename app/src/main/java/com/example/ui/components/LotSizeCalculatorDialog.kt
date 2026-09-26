package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TradezillerOrange

@Composable
fun LotSizeCalculatorDialog(
    tradeCount: Int,
    symbol: String,
    onDismiss: () -> Unit
) {
    var balanceText by remember { mutableStateOf("1000") }
    var riskPercentText by remember { mutableStateOf("1.0") }
    var stopLossPipsText by remember { mutableStateOf("35") }

    val balance = balanceText.toDoubleOrNull() ?: 1000.0
    val riskPercent = riskPercentText.toDoubleOrNull() ?: 1.0
    val slPips = stopLossPipsText.toDoubleOrNull() ?: 35.0

    val totalRiskAmount = balance * (riskPercent / 100.0)
    // Pip value approx $10 per standard lot for Forex / Gold / Crypto contracts
    val totalLotSize = if (slPips > 0) (totalRiskAmount / (slPips * 10.0)).coerceAtLeast(0.01) else 0.01
    val lotPerTrade = (totalLotSize / tradeCount).coerceAtLeast(0.01)

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("calculator_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = BorderStroke(1.dp, DarkCardBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Risk & Lot Calculator",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "$symbol • $tradeCount Position Split",
                    fontSize = 12.sp,
                    color = TradezillerOrange,
                    modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
                )

                // Account Balance Input
                OutlinedTextField(
                    value = balanceText,
                    onValueChange = { balanceText = it },
                    label = { Text("Account Balance ($)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TradezillerOrange,
                        unfocusedBorderColor = DarkCardBorder,
                        focusedLabelColor = TradezillerOrange
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("balance_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Risk % Input
                OutlinedTextField(
                    value = riskPercentText,
                    onValueChange = { riskPercentText = it },
                    label = { Text("Risk Percentage (%)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TradezillerOrange,
                        unfocusedBorderColor = DarkCardBorder,
                        focusedLabelColor = TradezillerOrange
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("risk_percent_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Stop Loss Pips
                OutlinedTextField(
                    value = stopLossPipsText,
                    onValueChange = { stopLossPipsText = it },
                    label = { Text("Stop Loss Distance (Pips/Points)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TradezillerOrange,
                        unfocusedBorderColor = DarkCardBorder,
                        focusedLabelColor = TradezillerOrange
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("sl_pips_input")
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Calculation Result Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DarkSurfaceVariant, RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total Risk ($):", color = TextSecondary, fontSize = 13.sp)
                            Text(String.format("$%.2f", totalRiskAmount), color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total Lot Size:", color = TextSecondary, fontSize = 13.sp)
                            Text(String.format("%.2f Lots", totalLotSize), color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Per Trade ($tradeCount entries):", color = TradezillerOrange, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            Text(String.format("%.2f Lots ea.", lotPerTrade), color = TradezillerOrange, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = TradezillerOrange),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("apply_calculator_button")
                ) {
                    Text("Done", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}
