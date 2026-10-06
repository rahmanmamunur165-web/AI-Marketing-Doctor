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
import com.example.data.repository.MarketingRepository
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun IntegrationsScreen(
    modifier: Modifier = Modifier
) {
    val businessProfile by MarketingRepository.businessProfile.collectAsState()
    var isConnectingMeta by remember { mutableStateOf(false) }
    var connectionNotice by remember { mutableStateOf<String?>(null) }

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
                            text = "Channel Integrations",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = SlateTextPrimary
                        )
                        Text(
                            text = "Authorized Meta/Facebook API & Webhook Architecture",
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
            // Mode Banner
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SlateSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Dataset Mode",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = SlateTextPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (businessProfile.isDemoMode)
                                    "Demo Mode Active: Pre-seeded realistic dataset for Urban Style BD (Clothing)"
                                else
                                    "Production Mode: Waiting for official Meta OAuth credentials",
                                style = MaterialTheme.typography.bodySmall,
                                color = SlateTextSecondary
                            )
                        }

                        Switch(
                            checked = businessProfile.isDemoMode,
                            onCheckedChange = { MarketingRepository.toggleDemoMode(it) },
                            modifier = Modifier.testTag("toggle_demo_mode_switch")
                        )
                    }
                }
            }

            // Facebook / Meta Official Integration Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SlateSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(Color(0xFF1877F2), RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Share,
                                        contentDescription = "Meta",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Facebook & Messenger",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = SlateTextPrimary
                                    )
                                    Text(
                                        text = "Status: ${if (businessProfile.isDemoMode) "Connected (Demo Mode)" else "OAuth Ready"}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (businessProfile.isDemoMode) HealthGreenDark else SlateTextMuted
                                    )
                                }
                            }

                            Button(
                                onClick = {
                                    isConnectingMeta = true
                                    connectionNotice = "OAuth Authorization simulation initiated: Page 'Urban Style BD Official' verified."
                                    isConnectingMeta = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1877F2)),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("connect_facebook_button")
                            ) {
                                Text("Connect Facebook")
                            }
                        }

                        if (connectionNotice != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = connectionNotice!!,
                                style = MaterialTheme.typography.bodySmall,
                                color = HealthGreenDark
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Divider(color = SlateSurfaceContainerHigh)
                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "ARCHITECTURE & PLATFORM COMPLIANCE RULES:",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = SlateTextMuted
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        ComplianceCheckRow("Official Meta OAuth 2.0 graph permissions only")
                        ComplianceCheckRow("Zero scraping of followers or password collection")
                        ComplianceCheckRow("No discovery of private Gmail or Messenger identities")
                        ComplianceCheckRow("Zero unsolicited bulk messaging")
                        ComplianceCheckRow("Strictly complies with Meta 24h messaging window")

                        Spacer(modifier = Modifier.height(14.dp))

                        // Service Abstraction
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = SlateSurfaceContainer,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "MetaMessengerService (Modular Architecture):",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = TealPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "• connect() & getPages()\n• subscribeWebhooks(['messages', 'messaging_postbacks'])\n• receiveWebhook() & normalizeConversations()\n• sendMessage(recipientId, text, eligibleWindowCheck = true)",
                                    style = MaterialTheme.typography.bodySmall.copy(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace),
                                    color = SlateTextPrimary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ComplianceCheckRow(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = "Compliant",
            tint = HealthGreenDark,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = SlateTextPrimary
        )
    }
}
