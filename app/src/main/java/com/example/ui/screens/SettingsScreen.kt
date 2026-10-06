package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BusinessProfile
import com.example.data.repository.MarketingRepository
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun SettingsScreen(
    onTriggerOnboarding: () -> Unit,
    modifier: Modifier = Modifier
) {
    val businessProfile by MarketingRepository.businessProfile.collectAsState()

    var businessName by remember(businessProfile) { mutableStateOf(businessProfile.name) }
    var category by remember(businessProfile) { mutableStateOf(businessProfile.category) }
    var avgOrderValue by remember(businessProfile) { mutableStateOf(businessProfile.averageOrderValue) }
    var saveNotice by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SlateSurface)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Settings & Workspace",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = SlateTextPrimary
                        )
                        Text(
                            text = "AI sensitivity, store information, and billing plans",
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateTextSecondary
                        )
                    }
                    DemoModeBadge()
                }
            }
        },
        modifier = modifier
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(SlateBackground)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Business Profile Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SlateSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "BUSINESS INFORMATION",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = TealPrimary
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = businessName,
                            onValueChange = { businessName = it },
                            label = { Text("Business Name") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = category,
                            onValueChange = { category = it },
                            label = { Text("Category") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = avgOrderValue,
                            onValueChange = { avgOrderValue = it },
                            label = { Text("Average Order Value (AOV)") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (saveNotice != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = saveNotice!!,
                                style = MaterialTheme.typography.bodySmall,
                                color = HealthGreenDark
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            OutlinedButton(
                                onClick = onTriggerOnboarding,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Restart Onboarding")
                            }

                            Button(
                                onClick = {
                                    MarketingRepository.updateBusinessProfile(
                                        businessProfile.copy(
                                            name = businessName,
                                            category = category,
                                            averageOrderValue = avgOrderValue
                                        )
                                    )
                                    saveNotice = "Business settings saved successfully."
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Save Settings")
                            }
                        }
                    }
                }
            }

            // AI Settings Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SlateSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "AI DOCTOR CALIBRATION",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = TealPrimary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Lead Scoring Sensitivity",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = SlateTextPrimary
                        )
                        Text(
                            text = "Calibrated for fast-moving retail e-commerce in Bangladesh (Dhaka, Chittagong, Sylhet)",
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateTextSecondary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SlateSurfaceContainer,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "High Intent Signals: Questions about Size 42, Cash on delivery, and urgent delivery automatically grant +20 bonus intent points.",
                                style = MaterialTheme.typography.bodySmall,
                                color = SlateTextPrimary,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }

            // Billing Plans Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SlateSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "SUBSCRIPTION & PRICING PLANS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = TealPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "All tiers include full AI Marketing Doctor diagnosis and explainable lead scoring.",
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateTextSecondary
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        BillingTierRow("Free", "$0/mo", "Up to 100 conversations/mo", false)
                        BillingTierRow("Starter", "$19/mo", "500 conversations + AI reply generator", false)
                        BillingTierRow("Business", "$49/mo", "Unlimited chats + Gemini Pro Creative Studio", true)
                        BillingTierRow("Agency", "$99/mo", "Multi-workspace + White label reporting", false)
                    }
                }
            }
        }
    }
}

@Composable
fun BillingTierRow(name: String, price: String, desc: String, isCurrent: Boolean) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isCurrent) TealContainer.copy(alpha = 0.45f) else SlateSurfaceContainer,
        border = if (isCurrent) androidx.compose.foundation.BorderStroke(1.dp, TealPrimary) else null,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = SlateTextPrimary
                    )
                    if (isCurrent) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = TealPrimary
                        ) {
                            Text(
                                text = "ACTIVE PLAN",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp
                                ),
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodySmall,
                    color = SlateTextSecondary
                )
            }

            Text(
                text = price,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = TealPrimary
            )
        }
    }
}
