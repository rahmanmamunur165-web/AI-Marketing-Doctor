package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Lead
import com.example.data.repository.MarketingRepository
import com.example.ui.components.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeadsScreen(
    onNavigateToConversation: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val leads by MarketingRepository.leads.collectAsState()
    var selectedFilter by remember { mutableStateOf("All") }
    var selectedSource by remember { mutableStateOf("All Sources") }
    var searchQuery by remember { mutableStateOf("") }

    val filteredLeads = leads.filter { lead ->
        val matchesSearch = lead.customerName.contains(searchQuery, ignoreCase = true) ||
                lead.product.contains(searchQuery, ignoreCase = true) ||
                lead.location.contains(searchQuery, ignoreCase = true)

        val matchesStatus = when (selectedFilter) {
            "Hot" -> lead.leadScore >= 70 && !lead.isConverted && !lead.isLost
            "Warm" -> lead.leadScore in 31..69 && !lead.isConverted && !lead.isLost
            "Cold" -> lead.leadScore <= 30 && !lead.isConverted && !lead.isLost
            "Converted" -> lead.isConverted
            "Lost" -> lead.isLost
            else -> true
        }

        val matchesSource = if (selectedSource == "All Sources") true else lead.source.contains(selectedSource, ignoreCase = true)

        matchesSearch && matchesStatus && matchesSource
    }

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
                            text = "Leads & Buying Intent Scoring",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = SlateTextPrimary
                        )
                        Text(
                            text = "${filteredLeads.size} leads filtered • Explainable 0-100 scoring",
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateTextSecondary
                        )
                    }
                    DemoModeBadge()
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Filter leads by customer name, product, or city...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = SlateTextMuted)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("leads_search_input"),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SlateSurface,
                        unfocusedContainerColor = SlateSurface
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() })
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Scrollable filters
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("All", "Hot", "Warm", "Cold", "Converted", "Lost").forEach { filter ->
                        FilterChip(
                            selected = selectedFilter == filter,
                            onClick = { selectedFilter = filter },
                            label = { Text(filter) }
                        )
                    }
                    Divider(modifier = Modifier.height(24.dp).width(1.dp), color = SlateBorder)
                    listOf("All Sources", "Reel", "Post", "Messenger", "Website").forEach { src ->
                        FilterChip(
                            selected = selectedSource == src,
                            onClick = { selectedSource = src },
                            label = { Text(src) }
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
            contentPadding = PaddingValues(vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredLeads) { lead ->
                LeadCardItem(
                    lead = lead,
                    onToggleConverted = {
                        MarketingRepository.updateLeadStatus(lead.id, !lead.isConverted)
                    },
                    onOpenConversation = {
                        MarketingRepository.selectConversation("conv_1")
                        onNavigateToConversation("conv_1")
                    }
                )
            }
        }
    }
}

@Composable
fun LeadCardItem(
    lead: Lead,
    onToggleConverted: () -> Unit,
    onOpenConversation: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("lead_card_${lead.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SlateSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Customer, Source, Score
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = lead.customerName,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = SlateTextPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "• ${lead.location}",
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateTextSecondary
                        )
                    }
                    Text(
                        text = "Source: ${lead.source}",
                        style = MaterialTheme.typography.labelSmall,
                        color = SlateTextMuted
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    ScoreBadge(score = lead.leadScore)
                    Spacer(modifier = Modifier.width(8.dp))
                    if (lead.isConverted) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = HealthGreenContainer
                        ) {
                            Text(
                                text = "CONVERTED",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = HealthGreenDark,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    } else if (lead.isLost) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = DiagnosticCriticalContainer
                        ) {
                            Text(
                                text = "LOST",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = DiagnosticCritical,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Product & Revenue Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.ShoppingBag,
                        contentDescription = "Product",
                        tint = TealPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = lead.product,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = SlateTextPrimary
                    )
                }

                if (lead.revenue > 0) {
                    Text(
                        text = "Value: ৳${"%,d".format(lead.revenue.toInt())}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = HealthGreenDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Tags
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                lead.tags.forEach { tag ->
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = SlateSurfaceContainerHigh
                    ) {
                        Text(
                            text = tag,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                            color = SlateTextSecondary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Recommended Action Box
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = TealContainer.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Bolt,
                        contentDescription = "Next Action",
                        tint = TealPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Next Action: ${lead.nextAction}",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = TealDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onToggleConverted,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(if (lead.isConverted) "Undo Convert" else "Mark Converted")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onOpenConversation,
                    colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Icon(
                        Icons.Outlined.Chat,
                        contentDescription = "Chat",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Open Chat")
                }
            }
        }
    }
}
