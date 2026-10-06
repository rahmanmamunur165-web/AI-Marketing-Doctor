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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RevenueAttributionItem
import com.example.data.repository.MarketingRepository
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun RevenueScreen(
    modifier: Modifier = Modifier
) {
    val revenueList by MarketingRepository.revenueAttribution.collectAsState()
    var exportReportNotice by remember { mutableStateOf<String?>(null) }

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
                            text = "Revenue & Attribution",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = SlateTextPrimary
                        )
                        Text(
                            text = "Tracked marketing impact on customer orders",
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
            // Revenue summary card
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
                            Text(
                                text = "PROVEN MARKETING REVENUE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                ),
                                color = SlateTextMuted
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = HealthGreenContainer
                            ) {
                                Text(
                                    text = "83.8% ATTRIBUTED",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = HealthGreenDark,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "৳287,000",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 36.sp
                                ),
                                color = TealPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "out of ৳342,500 total sales",
                                style = MaterialTheme.typography.bodyMedium,
                                color = SlateTextSecondary,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Funnel visualization
                        Text(
                            text = "MARKETING TO REVENUE CONVERSION FUNNEL",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            ),
                            color = SlateTextMuted
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        FunnelStepRow("1. Content Reach", "12,400 engagements", 1.0f, SlateSurfaceContainerHigh)
                        FunnelStepRow("2. Conversations", "1,248 conversations (10.0%)", 0.65f, CyanContainer)
                        FunnelStepRow("3. Qualified Leads", "428 leads (34.3%)", 0.45f, TealContainer)
                        FunnelStepRow("4. Hot Leads", "96 hot leads (22.4%)", 0.30f, Color(0xFFFFEDD5))
                        FunnelStepRow("5. Purchases", "51 orders (53.1%)", 0.22f, HealthGreenContainer)
                    }
                }
            }

            // Sources List Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "REVENUE BY MARKETING SOURCE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = SlateTextMuted
                    )

                    Button(
                        onClick = {
                            exportReportNotice = "Attribution Report (CSV/PDF) generated successfully."
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Export Report")
                    }
                }
            }

            if (exportReportNotice != null) {
                item {
                    Text(
                        text = exportReportNotice!!,
                        style = MaterialTheme.typography.bodySmall,
                        color = HealthGreenDark
                    )
                }
            }

            items(revenueList) { item ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SlateSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
                    modifier = Modifier.fillMaxWidth().testTag("revenue_source_${item.sourceName}")
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.sourceName,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = SlateTextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${item.leads} leads • ${item.customers} buyers (${item.conversionRate}% conv)",
                                style = MaterialTheme.typography.bodySmall,
                                color = SlateTextSecondary
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "৳${"%,d".format(item.revenue.toInt())}",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                                color = TealPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = item.sourceType,
                                style = MaterialTheme.typography.labelSmall,
                                color = SlateTextMuted
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FunnelStepRow(title: String, subtitle: String, progress: Float, color: Color) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold), color = SlateTextPrimary)
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = SlateTextSecondary)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(SlateSurfaceContainerHigh)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color)
            )
        }
    }
}
