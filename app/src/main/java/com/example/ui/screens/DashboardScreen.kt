package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Conversation
import com.example.data.repository.MarketingRepository
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val businessProfile by MarketingRepository.businessProfile.collectAsState()
    val healthScore by MarketingRepository.healthScore.collectAsState()
    val diagnoses by MarketingRepository.diagnoses.collectAsState()
    val conversations by MarketingRepository.conversations.collectAsState()

    var showActionSnackbar by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(showActionSnackbar) {
        showActionSnackbar?.let {
            snackbarHostState.showSnackbar(it)
            showActionSnackbar = null
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier
    ) { padding ->
        // Responsive LazyVerticalGrid adapting between mobile (1 column), tablet (2 columns), and desktop (3+ columns)
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 300.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header (Spans all columns)
            item(span = { GridItemSpan(maxLineSpan) }) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Good morning, ${businessProfile.name}",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 22.sp
                            ),
                            color = SlateTextPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "AI Customer Intelligence & Marketing Diagnosis",
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateTextSecondary
                        )
                    }

                    if (businessProfile.isDemoMode) {
                        DemoModeBadge()
                    }
                }
            }

            // Summary Card 1: Marketing Health
            item {
                MarketingHealthSummaryCard(
                    score = healthScore,
                    onNavigate = onNavigate,
                    onRunDiagnosis = {
                        MarketingRepository.runAIDiagnosis()
                        showActionSnackbar = "AI Diagnosis complete! 1 fresh insight identified."
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Summary Card 2: Total Conversations
            item {
                TotalConversationsSummaryCard(
                    conversationsCount = "1,248",
                    onNavigate = onNavigate,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Summary Card 3: Qualified Leads
            item {
                QualifiedLeadsSummaryCard(
                    qualifiedCount = "428",
                    onNavigate = onNavigate,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Summary Card 4: Hot Leads (96 count)
            item {
                HotLeadsSummaryCard(
                    hotLeadsCount = "96",
                    onNavigate = onNavigate,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Summary Card 5: Total Revenue (৳342,500)
            item {
                TotalRevenueSummaryCard(
                    revenueAmount = "৳342,500",
                    onNavigate = onNavigate,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Secondary Metric Card: Conversion Rate
            item {
                MetricCard(
                    title = "Conversion Rate",
                    value = "8.4%",
                    icon = Icons.AutoMirrored.Filled.TrendingUp,
                    delta = "+1.8% vs benchmark",
                    iconColor = HealthGreen,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Secondary Metric Card: Tracked Revenue
            item {
                MetricCard(
                    title = "Tracked Revenue",
                    value = "৳287,000",
                    icon = Icons.Outlined.AccountBalanceWallet,
                    delta = "83.8% attributed",
                    iconColor = CyanSecondary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigate("revenue") }
                )
            }

            // Today's AI Diagnosis Card (Spans all columns)
            item(span = { GridItemSpan(maxLineSpan) }) {
                val primaryDiagnosis = diagnoses.firstOrNull()
                if (primaryDiagnosis != null) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SlateSurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, TealLight.copy(alpha = 0.35f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("today_ai_diagnosis_card")
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Psychology,
                                        contentDescription = "AI Doctor",
                                        tint = TealPrimary,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "TODAY'S AI DIAGNOSIS",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.1.sp
                                        ),
                                        color = TealPrimary
                                    )
                                }
                                SeverityPill(severity = primaryDiagnosis.severity)
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = primaryDiagnosis.diagnosisText,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    lineHeight = 22.sp
                                ),
                                color = SlateTextPrimary
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Evidence: ${primaryDiagnosis.evidence}",
                                style = MaterialTheme.typography.bodySmall,
                                color = SlateTextSecondary
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = TealContainer.copy(alpha = 0.45f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "RECOMMENDED ACTION:",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = TealDark
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = primaryDiagnosis.recommendedAction,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                        color = SlateTextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Estimated Opportunity: ${primaryDiagnosis.estimatedOpportunity}",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = HealthGreenDark
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = { onNavigate("conversations") },
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("action_view_leads_btn"),
                                    colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(primaryDiagnosis.buttonActionText)
                                }

                                OutlinedButton(
                                    onClick = { onNavigate("ai_doctor") },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Full Doctor Report")
                                }
                            }
                        }
                    }
                }
            }

            // Quick Actions Header (Spans all columns)
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column {
                    Text(
                        text = "QUICK ACTIONS",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = SlateTextMuted
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ActionChip(
                            label = "Hot Leads (96)",
                            icon = Icons.Default.LocalFireDepartment,
                            color = DiagnosticCritical,
                            onClick = { onNavigate("leads") },
                            modifier = Modifier.weight(1f)
                        )
                        ActionChip(
                            label = "Unanswered",
                            icon = Icons.Outlined.MarkChatUnread,
                            color = DiagnosticWarning,
                            onClick = { onNavigate("conversations") },
                            modifier = Modifier.weight(1f)
                        )
                        ActionChip(
                            label = "Ad Creative",
                            icon = Icons.Default.AutoAwesome,
                            color = CyanSecondary,
                            onClick = { onNavigate("content") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // High Intent Leads Header (Spans all columns)
            item(span = { GridItemSpan(maxLineSpan) }) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "HIGH-INTENT CONVERSATIONS",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = SlateTextMuted
                    )
                    TextButton(onClick = { onNavigate("conversations") }) {
                        Text("View Inbox", color = TealPrimary)
                    }
                }
            }

            // High-Intent Conversations
            items(
                items = conversations.take(4),
                key = { it.id },
                span = { GridItemSpan(maxLineSpan) }
            ) { conv ->
                ConversationItemRow(
                    conversation = conv,
                    onClick = {
                        MarketingRepository.selectConversation(conv.id)
                        onNavigate("conversations")
                    }
                )
            }
        }
    }
}

@Composable
fun MarketingHealthSummaryCard(
    score: Int,
    onNavigate: (String) -> Unit,
    onRunDiagnosis: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .testTag("marketing_health_card")
            .clickable { onNavigate("ai_doctor") },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(18.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(TealContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MedicalServices,
                            contentDescription = "Marketing Health Doctor Icon",
                            tint = TealPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Marketing Health",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = SlateTextPrimary
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (score >= 80) HealthGreenContainer else DiagnosticWarningContainer
                ) {
                    Text(
                        text = if (score >= 80) "HEALTHY" else "ATTENTION",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (score >= 80) HealthGreenDark else DiagnosticWarning,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "$score",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 38.sp
                    ),
                    color = SlateTextPrimary
                )
                Text(
                    text = " / 100",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = SlateTextMuted,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { score / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape),
                color = if (score >= 80) HealthGreen else DiagnosticWarning,
                trackColor = SlateSurfaceContainerHigh
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "12 active signals • High response velocity",
                style = MaterialTheme.typography.labelSmall,
                color = SlateTextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onRunDiagnosis,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("run_ai_diagnosis_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Icon(
                        Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Diagnose", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = { onNavigate("ai_doctor") },
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Text("Report", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun TotalConversationsSummaryCard(
    conversationsCount: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .testTag("total_conversations_card")
            .clickable { onNavigate("conversations") },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(18.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(CyanContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Chat,
                        contentDescription = "Total Conversations Icon",
                        tint = CyanSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFFEE2E2)
                ) {
                    Text(
                        text = "4 UNANSWERED",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp
                        ),
                        color = DiagnosticCritical,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Total Conversations",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                color = SlateTextSecondary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = conversationsCount,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 32.sp
                ),
                color = SlateTextPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "+14% velocity this week",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = HealthGreenDark
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Messenger • IG • WhatsApp",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = SlateTextMuted
                )
                Text(
                    text = "Inbox →",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = CyanSecondary
                    )
                )
            }
        }
    }
}

@Composable
fun QualifiedLeadsSummaryCard(
    qualifiedCount: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .testTag("qualified_leads_card")
            .clickable { onNavigate("leads") },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(18.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(TealContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Qualified Leads Icon",
                        tint = TealPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFFFEDD5)
                ) {
                    Text(
                        text = "96 HOT 🔥",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp
                        ),
                        color = Color(0xFFEA580C),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Qualified Leads",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                color = SlateTextSecondary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = qualifiedCount,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 32.sp
                ),
                color = SlateTextPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "34.3% intent conversion rate",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = HealthGreenDark
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "High buying intent",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = SlateTextMuted
                )
                Text(
                    text = "View Leads →",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TealPrimary
                    )
                )
            }
        }
    }
}

@Composable
fun HotLeadsSummaryCard(
    hotLeadsCount: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .testTag("hot_leads_card")
            .clickable { onNavigate("leads") },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(18.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFEDD5)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "Hot Leads Icon",
                        tint = Color(0xFFEA580C),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = DiagnosticCriticalContainer
                ) {
                    Text(
                        text = "PRIORITY ACTION",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp
                        ),
                        color = DiagnosticCritical,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Hot Leads",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                color = SlateTextSecondary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = hotLeadsCount,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 32.sp
                ),
                color = SlateTextPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "+19% surge • 17 need < 24h follow-up",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFFEA580C)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "High purchase intent",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = SlateTextMuted
                )
                Text(
                    text = "Close Now →",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFEA580C)
                    )
                )
            }
        }
    }
}

@Composable
fun TotalRevenueSummaryCard(
    revenueAmount: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .testTag("total_revenue_card")
            .clickable { onNavigate("revenue") },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(18.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(HealthGreenContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccountBalanceWallet,
                        contentDescription = "Total Revenue Icon",
                        tint = HealthGreenDark,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = HealthGreenContainer
                ) {
                    Text(
                        text = "83.8% ATTRIBUTED",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp
                        ),
                        color = HealthGreenDark,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Total Revenue",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                color = SlateTextSecondary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = revenueAmount,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 32.sp
                ),
                color = SlateTextPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "৳287,000 from tracked marketing",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = HealthGreenDark
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ROI: 4.8x ad efficiency",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = SlateTextMuted
                )
                Text(
                    text = "Revenue Audit →",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = HealthGreenDark
                    )
                )
            }
        }
    }
}

@Composable
fun ActionChip(
    label: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = SlateSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                color = SlateTextPrimary,
                maxLines = 1
            )
        }
    }
}

@Composable
fun ConversationItemRow(
    conversation: Conversation,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("conv_item_${conversation.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SlateSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(TealContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = conversation.customerName.take(1),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TealDark
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = conversation.customerName,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = SlateTextPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "• ${conversation.channel}",
                            style = MaterialTheme.typography.labelSmall,
                            color = SlateTextMuted
                        )
                    }
                    Text(
                        text = "\"${conversation.lastMessage}\"",
                        style = MaterialTheme.typography.bodySmall,
                        color = SlateTextSecondary,
                        maxLines = 1
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                ScoreBadge(score = conversation.leadScore)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = conversation.timestamp,
                    style = MaterialTheme.typography.labelSmall,
                    color = SlateTextMuted
                )
            }
        }
    }
}
