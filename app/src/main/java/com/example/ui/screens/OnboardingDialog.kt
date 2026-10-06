package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.BusinessProfile
import com.example.data.repository.MarketingRepository
import com.example.ui.theme.*

@Composable
fun OnboardingDialog(
    onDismiss: () -> Unit
) {
    var step by remember { mutableStateOf(1) }
    var businessName by remember { mutableStateOf("Urban Style BD") }
    var selectedCategory by remember { mutableStateOf("Clothing") }
    var selectedChannel by remember { mutableStateOf("Facebook") }
    var leadsEstimate by remember { mutableStateOf("1,000 - 2,500") }
    var aov by remember { mutableStateOf("৳2,800") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = SlateSurface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                // Header with step indicator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ONBOARDING: STEP $step OF 6",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = TealPrimary
                    )

                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = SlateTextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                when (step) {
                    1 -> {
                        Text(
                            text = "What is your business name?",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = SlateTextPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "AI Marketing Doctor diagnoses customer conversations specifically for your brand.",
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateTextSecondary
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        OutlinedTextField(
                            value = businessName,
                            onValueChange = { businessName = it },
                            label = { Text("Business Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    2 -> {
                        Text(
                            text = "Select your business category:",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = SlateTextPrimary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        listOf("Clothing", "Restaurant", "Beauty", "Electronics", "Real Estate", "Travel", "Education", "Other").chunked(2).forEach { row ->
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                row.forEach { cat ->
                                    FilterChip(
                                        selected = selectedCategory == cat,
                                        onClick = { selectedCategory = cat },
                                        label = { Text(cat) },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                    }
                    3 -> {
                        Text(
                            text = "Primary sales channel:",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = SlateTextPrimary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        listOf("Facebook", "Instagram", "Website", "WhatsApp", "Multiple").forEach { ch ->
                            FilterChip(
                                selected = selectedChannel == ch,
                                onClick = { selectedChannel = ch },
                                label = { Text(ch) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                            )
                        }
                    }
                    4 -> {
                        Text(
                            text = "Estimated monthly leads:",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = SlateTextPrimary
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        OutlinedTextField(
                            value = leadsEstimate,
                            onValueChange = { leadsEstimate = it },
                            label = { Text("Monthly Inquiries / Leads") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    5 -> {
                        Text(
                            text = "Average order value (AOV):",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = SlateTextPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Helps calculate opportunity sizing and revenue attribution.",
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateTextSecondary
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        OutlinedTextField(
                            value = aov,
                            onValueChange = { aov = it },
                            label = { Text("Average Order Value (e.g. ৳2,800)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    6 -> {
                        Text(
                            text = "Connect Facebook or Start in Demo Mode:",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = SlateTextPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Demo Mode includes 250+ realistic conversations, 100+ customers, and proven attribution without requiring Meta API credentials.",
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateTextSecondary
                        )
                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = {
                                MarketingRepository.updateBusinessProfile(
                                    BusinessProfile(
                                        name = businessName,
                                        category = selectedCategory,
                                        primaryChannel = selectedChannel,
                                        monthlyLeadsEstimate = leadsEstimate,
                                        averageOrderValue = aov,
                                        isDemoMode = true
                                    )
                                )
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Start with Demo Mode")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedButton(
                            onClick = {
                                MarketingRepository.updateBusinessProfile(
                                    BusinessProfile(
                                        name = businessName,
                                        category = selectedCategory,
                                        primaryChannel = selectedChannel,
                                        monthlyLeadsEstimate = leadsEstimate,
                                        averageOrderValue = aov,
                                        isDemoMode = false
                                    )
                                )
                                onDismiss()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Connect Facebook")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (step < 6) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (step > 1) {
                            TextButton(onClick = { step-- }) {
                                Text("Back")
                            }
                        } else {
                            Spacer(modifier = Modifier.width(1.dp))
                        }

                        Button(
                            onClick = { step++ },
                            colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
                        ) {
                            Text("Next")
                        }
                    }
                }
            }
        }
    }
}
