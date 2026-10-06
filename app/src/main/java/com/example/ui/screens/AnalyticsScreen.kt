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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun AnalyticsScreen(
    modifier: Modifier = Modifier
) {
    var selectedDateRange by remember { mutableStateOf("30 Days") }

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
                            text = "Customer Intelligence Analytics",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = SlateTextPrimary
                        )
                        Text(
                            text = "Longitudinal conversation trends & objection analysis",
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateTextSecondary
                        )
                    }
                    DemoModeBadge()
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("7 Days", "30 Days", "90 Days", "All Time").forEach { range ->
                        FilterChip(
                            selected = selectedDateRange == range,
                            onClick = { selectedDateRange = range },
                            label = { Text(range) }
                        )
                    }
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
            // Lead Score Distribution Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SlateSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "LEAD SCORE DISTRIBUTION",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = SlateTextPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "51% of conversations exhibit Hot or Very Hot buying intent.",
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateTextSecondary
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        AnalyticsBarRow("Very Hot (81-100)", "148 leads (12%)", 0.12f, DiagnosticCritical)
                        AnalyticsBarRow("Hot (61-80)", "486 leads (39%)", 0.39f, Color(0xFFEA580C))
                        AnalyticsBarRow("Warm (31-60)", "436 leads (35%)", 0.35f, DiagnosticWarning)
                        AnalyticsBarRow("Cold (0-30)", "178 leads (14%)", 0.14f, SlateTextMuted)
                    }
                }
            }

            // Top Customer Questions Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SlateSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "TOP CUSTOMER INQUIRIES & OBJECTIONS",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = SlateTextPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Extracted from natural language Bengali & English customer messages.",
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateTextSecondary
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        AnalyticsBarRow("1. Delivery charge to Sylhet / Ctg", "38% of inquiries", 0.38f, TealPrimary)
                        AnalyticsBarRow("2. Size 42 availability & fit", "29% of inquiries", 0.29f, CyanSecondary)
                        AnalyticsBarRow("3. Cash on Delivery confirmation", "24% of inquiries", 0.24f, HealthGreenDark)
                        AnalyticsBarRow("4. Fabric / 100% Cotton material", "9% of inquiries", 0.09f, SlateTextSecondary)
                    }
                }
            }

            // Lost Lead Reasons
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SlateSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "WHY LEADS ARE LOST (OBJECTION ANALYSIS)",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = DiagnosticCritical
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Based on unanswered leads and drop-off sentiment tracking.",
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateTextSecondary
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        AnalyticsBarRow("1. Price higher than perceived budget", "43% of lost leads", 0.43f, DiagnosticCritical)
                        AnalyticsBarRow("2. Fit / Sizing uncertainty", "31% of lost leads", 0.31f, DiagnosticWarning)
                        AnalyticsBarRow("3. Courier delivery too slow for event", "18% of lost leads", 0.18f, CyanSecondary)
                        AnalyticsBarRow("4. Preferred color out of stock", "8% of lost leads", 0.08f, SlateTextMuted)
                    }
                }
            }
        }
    }
}

@Composable
fun AnalyticsBarRow(label: String, valueText: String, fraction: Float, color: Color) {
    Column(modifier = Modifier.padding(vertical = 5.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium), color = SlateTextPrimary)
            Text(text = valueText, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = color)
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
                    .fillMaxWidth(fraction)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color)
            )
        }
    }
}
