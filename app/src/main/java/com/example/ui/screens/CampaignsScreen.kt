package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.model.Campaign
import com.example.data.repository.MarketingRepository
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun CampaignsScreen(
    modifier: Modifier = Modifier
) {
    val campaigns by MarketingRepository.campaigns.collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }

    var newCampaignName by remember { mutableStateOf("") }
    var newObjective by remember { mutableStateOf("Lead Generation") }
    var newChannel by remember { mutableStateOf("Facebook & Messenger") }

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
                            text = "Marketing Campaigns",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = SlateTextPrimary
                        )
                        Text(
                            text = "Compliant customer outreach & intent-driven campaigns",
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateTextSecondary
                        )
                    }
                    DemoModeBadge()
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = TealPrimary,
                contentColor = Color.White,
                modifier = Modifier.testTag("create_campaign_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create Campaign")
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
            contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Meta Policy Notice Card
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = CyanContainer.copy(alpha = 0.5f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyanSecondary.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.VerifiedUser,
                            contentDescription = "Policy",
                            tint = CyanSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Meta Messaging Compliance Verified",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = OnCyanContainer
                            )
                            Text(
                                text = "Campaign automation strictly adheres to 24-hour messaging windows, user opt-in permissions, and authorized interactions without bulk unsolicited spam.",
                                style = MaterialTheme.typography.bodySmall,
                                color = SlateTextPrimary
                            )
                        }
                    }
                }
            }

            items(campaigns) { campaign ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SlateSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
                    modifier = Modifier.fillMaxWidth().testTag("campaign_card_${campaign.id}")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = campaign.name,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = SlateTextPrimary
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (campaign.status == "Active") HealthGreenContainer else SlateSurfaceContainerHigh
                            ) {
                                Text(
                                    text = campaign.status.uppercase(),
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (campaign.status == "Active") HealthGreenDark else SlateTextMuted,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Channel: ${campaign.channel} • Audience: ${campaign.audience}",
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateTextSecondary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = "Leads", style = MaterialTheme.typography.labelSmall, color = SlateTextMuted)
                                Text(text = "${campaign.leads}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = SlateTextPrimary)
                            }
                            Column {
                                Text(text = "Conversions", style = MaterialTheme.typography.labelSmall, color = SlateTextMuted)
                                Text(text = "${campaign.conversions}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = SlateTextPrimary)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(text = "Tracked Revenue", style = MaterialTheme.typography.labelSmall, color = SlateTextMuted)
                                Text(text = "৳${"%,d".format(campaign.revenue.toInt())}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold), color = TealPrimary)
                            }
                        }
                    }
                }
            }
        }

        if (showCreateDialog) {
            AlertDialog(
                onDismissRequest = { showCreateDialog = false },
                title = { Text("Launch Intent Campaign") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = newCampaignName,
                            onValueChange = { newCampaignName = it },
                            label = { Text("Campaign Name") },
                            placeholder = { Text("e.g. Sylhet Express Footwear") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = newObjective,
                            onValueChange = { newObjective = it },
                            label = { Text("Objective") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = newChannel,
                            onValueChange = { newChannel = it },
                            label = { Text("Channel") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newCampaignName.isNotBlank()) {
                                MarketingRepository.addCampaign(
                                    Campaign(
                                        id = "camp_${System.currentTimeMillis()}",
                                        name = newCampaignName,
                                        channel = newChannel,
                                        audience = "Active High-Intent Shoppers",
                                        objective = newObjective,
                                        status = "Active",
                                        leads = 0,
                                        conversions = 0,
                                        revenue = 0.0,
                                        messagingEligible = true
                                    )
                                )
                                showCreateDialog = false
                                newCampaignName = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
                    ) {
                        Text("Launch")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCreateDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
