package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.data.model.AIDiagnosis
import com.example.data.model.Severity
import com.example.data.repository.MarketingRepository
import com.example.ui.components.*
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AIDoctorScreen(
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val healthScore by MarketingRepository.healthScore.collectAsState()
    val diagnoses by MarketingRepository.diagnoses.collectAsState()
    val isDiagnosing by MarketingRepository.isDiagnosing.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    var diagnosisSnackbar by remember { mutableStateOf<String?>(null) }
    var selectedTab by remember { mutableStateOf("All Diagnoses") }

    Scaffold(
        modifier = modifier
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(SlateBackground)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.MedicalServices,
                                contentDescription = "Doctor",
                                tint = TealPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "AI Marketing Doctor",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 24.sp
                                ),
                                color = SlateTextPrimary
                            )
                        }
                        Text(
                            text = "Your daily diagnosis of what is working, what is failing, and what to do next.",
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateTextSecondary
                        )
                    }

                    DemoModeBadge()
                }
            }

            // Health Score Gauge Card
            item {
                HealthScoreCard(
                    score = healthScore,
                    onRunDiagnosis = {
                        coroutineScope.launch {
                            MarketingRepository.runAIDiagnosis()
                            diagnosisSnackbar = "Doctor run complete: 1 new evening window diagnosis added."
                        }
                    }
                )
            }

            // Flagship Executive Summary Cards: What is Working vs What is Not Working
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // What is working card
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = HealthGreenContainer.copy(alpha = 0.5f)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, HealthGreen.copy(alpha = 0.4f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = "Working",
                                    tint = HealthGreenDark,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "WHAT IS WORKING?",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    ),
                                    color = HealthGreenDark
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Reels are generating 2.8x more qualified leads than static posts.",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                color = SlateTextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Opportunity: +৳65,000 monthly by shifting budget to 30s demonstration videos.",
                                style = MaterialTheme.typography.labelSmall,
                                color = SlateTextSecondary
                            )
                        }
                    }

                    // What is not working card
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = DiagnosticCriticalContainer.copy(alpha = 0.5f)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DiagnosticCritical.copy(alpha = 0.4f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Warning,
                                    contentDescription = "Failing",
                                    tint = DiagnosticCritical,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "WHAT IS NOT WORKING?",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    ),
                                    color = DiagnosticCritical
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "23% of hot leads are not receiving a response within 12 hours.",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                color = SlateTextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Root cause: Peak inquiry window occurs between 8 PM and 11 PM when staff is offline.",
                                style = MaterialTheme.typography.labelSmall,
                                color = SlateTextSecondary
                            )
                        }
                    }
                }
            }

            // Filter Tabs
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("All Diagnoses", "Critical & High", "Content", "Response Velocity").forEach { tab ->
                        FilterChip(
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            label = { Text(tab) }
                        )
                    }
                }
            }

            // Diagnoses List with Problem, Evidence, Impact, Recommended Action, Priority & Opportunity
            items(diagnoses) { diag ->
                DiagnosisItemCard(
                    diagnosis = diag,
                    onActionClick = {
                        when {
                            diag.buttonActionText.contains("Lead", ignoreCase = true) -> onNavigate("conversations")
                            diag.buttonActionText.contains("Idea", ignoreCase = true) -> onNavigate("content")
                            diag.buttonActionText.contains("Workflow", ignoreCase = true) -> onNavigate("conversations")
                            else -> onNavigate("conversations")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun DiagnosisItemCard(
    diagnosis: AIDiagnosis,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("diagnosis_card_${diagnosis.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SlateSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header with Category and Severity
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(TealContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.HealthAndSafety,
                            contentDescription = diagnosis.category,
                            tint = TealPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = diagnosis.category.uppercase(),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = TealPrimary
                    )
                }

                SeverityPill(severity = diagnosis.severity)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Problem Diagnosis Text
            Text(
                text = diagnosis.diagnosisText,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = SlateTextPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Evidence & Impact breakdown
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = SlateSurfaceContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row {
                        Text(
                            text = "Evidence: ",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = SlateTextPrimary
                        )
                        Text(
                            text = diagnosis.evidence,
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateTextSecondary
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row {
                        Text(
                            text = "Impact: ",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = DiagnosticCritical
                        )
                        Text(
                            text = diagnosis.impact,
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Recommended Action & Estimated Opportunity
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = TealContainer.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "RECOMMENDED NEXT ACTION:",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = TealDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = diagnosis.recommendedAction,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = SlateTextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Estimated Opportunity:",
                            style = MaterialTheme.typography.labelSmall,
                            color = SlateTextSecondary
                        )
                        Text(
                            text = diagnosis.estimatedOpportunity,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = HealthGreenDark
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onActionClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
                ) {
                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = "Action",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(diagnosis.buttonActionText)
                }
            }
        }
    }
}
