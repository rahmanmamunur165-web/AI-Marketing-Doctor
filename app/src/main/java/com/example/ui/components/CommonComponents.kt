package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LeadScore
import com.example.data.model.ScoreCategory
import com.example.data.model.Severity
import com.example.ui.theme.*

@Composable
fun HealthScoreCard(
    score: Int,
    modifier: Modifier = Modifier,
    onRunDiagnosis: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("health_score_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
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
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(TealContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MedicalServices,
                            contentDescription = "Doctor Icon",
                            tint = TealPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "MARKETING HEALTH SCORE",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp
                            ),
                            color = SlateTextMuted
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Status: ",
                                style = MaterialTheme.typography.bodyMedium,
                                color = SlateTextSecondary
                            )
                            Text(
                                text = if (score >= 80) "HEALTHY" else if (score >= 60) "ATTENTION NEEDED" else "CRITICAL",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = if (score >= 80) HealthGreen else if (score >= 60) DiagnosticWarning else DiagnosticCritical
                            )
                        }
                    }
                }

                Button(
                    onClick = onRunDiagnosis,
                    modifier = Modifier.testTag("run_ai_diagnosis_btn"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TealPrimary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Diagnosis",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Run AI Diagnosis", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$score",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 42.sp
                        ),
                        color = SlateTextPrimary
                    )
                    Text(
                        text = " / 100",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = SlateTextMuted,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }

                Text(
                    text = "High diagnostic velocity • 12 active signals",
                    style = MaterialTheme.typography.bodySmall,
                    color = SlateTextSecondary,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Score Progress Bar
            LinearProgressIndicator(
                progress = { score / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = if (score >= 80) HealthGreen else if (score >= 60) DiagnosticWarning else DiagnosticCritical,
                trackColor = SlateSurfaceContainerHigh
            )
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    icon: ImageVector,
    delta: String? = null,
    iconColor: Color = TealPrimary,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.testTag("metric_card_$title"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    color = SlateTextSecondary
                )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(iconColor.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = iconColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                ),
                color = SlateTextPrimary
            )
            if (delta != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = delta,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                    color = if (delta.startsWith("+") || delta.contains("healthy", ignoreCase = true)) HealthGreenDark else SlateTextMuted
                )
            }
        }
    }
}

@Composable
fun ScoreBadge(
    score: Int,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, icon) = when {
        score >= 81 -> Triple(Color(0xFFFEE2E2), Color(0xFFDC2626), "🔥")
        score >= 61 -> Triple(Color(0xFFFFEDD5), Color(0xFFEA580C), "🔥")
        score >= 31 -> Triple(Color(0xFFFEF3C7), Color(0xFFD97706), "🟡")
        else -> Triple(Color(0xFFE2E8F0), Color(0xFF64748B), "🟢")
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = bgColor,
        modifier = modifier
            .testTag("score_badge_$score")
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, fontSize = 12.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "$score",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = textColor
            )
        }
    }
}

@Composable
fun SeverityPill(severity: Severity) {
    val (bg, text) = when (severity) {
        Severity.CRITICAL -> DiagnosticCriticalContainer to DiagnosticCritical
        Severity.HIGH -> Color(0xFFFFEDD5) to Color(0xFFEA580C)
        Severity.MEDIUM -> DiagnosticWarningContainer to Color(0xFFD97706)
        Severity.LOW -> HealthGreenContainer to HealthGreenDark
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = severity.label,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = text
        )
    }
}

@Composable
fun DemoModeBadge() {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = Color(0xFFFEF3C7),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF59E0B))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFD97706))
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "DEMO MODE",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF92400E)
            )
        }
    }
}

@Composable
fun ExplainableScoreDialog(
    score: LeadScore,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Score Analysis: ${score.score}/100",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                ScoreBadge(score = score.score)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Explainable buying intent calculation based on authorized interaction signals:",
                    style = MaterialTheme.typography.bodySmall,
                    color = SlateTextSecondary
                )
                Spacer(modifier = Modifier.height(14.dp))
                score.breakdown.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = item.factor,
                            style = MaterialTheme.typography.bodyMedium,
                            color = SlateTextPrimary
                        )
                        Text(
                            text = "+${item.points}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = HealthGreenDark
                        )
                    }
                    Divider(color = SlateSurfaceContainerHigh, thickness = 0.5.dp)
                }
            }
        }
    )
}
