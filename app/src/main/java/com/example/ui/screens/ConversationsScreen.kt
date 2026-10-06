package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Conversation
import com.example.data.repository.MarketingRepository
import com.example.network.GeminiApiClient
import com.example.ui.components.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationsScreen(
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val conversations by MarketingRepository.conversations.collectAsState()
    val selectedConversation by MarketingRepository.selectedConversation.collectAsState()

    var activeFilter by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    var showScoreDialog by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    var isGeneratingReply by remember { mutableStateOf(false) }
    var draftReplyText by remember { mutableStateOf("") }
    var replyNotice by remember { mutableStateOf<String?>(null) }

    val filteredList = conversations.filter { conv ->
        val matchesSearch = conv.customerName.contains(searchQuery, ignoreCase = true) ||
                conv.lastMessage.contains(searchQuery, ignoreCase = true) ||
                conv.analysis.productInterest.contains(searchQuery, ignoreCase = true)

        val matchesFilter = when (activeFilter) {
            "Hot" -> conv.leadScore >= 70
            "Unanswered" -> conv.isUnanswered
            "Sylhet / Express" -> conv.analysis.location.contains("Sylhet", ignoreCase = true) || conv.lastMessage.contains("কালকে", ignoreCase = true)
            else -> true
        }
        matchesSearch && matchesFilter
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SlateSurface)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Conversations & Intent Inbox",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = SlateTextPrimary
                        )
                        Text(
                            text = "Live customer chat analysis & lead scoring",
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateTextSecondary
                        )
                    }
                    DemoModeBadge()
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by customer, message, or product...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = SlateTextMuted)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("conversations_search_input"),
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = activeFilter == "All",
                        onClick = { activeFilter = "All" },
                        label = { Text("All (${conversations.size})") }
                    )
                    FilterChip(
                        selected = activeFilter == "Hot",
                        onClick = { activeFilter = "Hot" },
                        label = { Text("🔥 Hot Leads") }
                    )
                    FilterChip(
                        selected = activeFilter == "Unanswered",
                        onClick = { activeFilter = "Unanswered" },
                        label = { Text("Unanswered (4)") }
                    )
                    FilterChip(
                        selected = activeFilter == "Sylhet / Express",
                        onClick = { activeFilter = "Sylhet / Express" },
                        label = { Text("Sylhet & Urgent") }
                    )
                }
            }
        },
        modifier = modifier
    ) { padding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Left list of conversations
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(SlateBackground)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredList) { conv ->
                    val isSelected = conv.id == selectedConversation?.id
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) TealContainer.copy(alpha = 0.5f) else SlateSurface
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) TealPrimary else SlateBorder.copy(alpha = 0.5f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                MarketingRepository.selectConversation(conv.id)
                                draftReplyText = ""
                                replyNotice = null
                            }
                            .testTag("inbox_item_${conv.id}")
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = conv.customerName,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        color = SlateTextPrimary
                                    )
                                    if (conv.isUnanswered) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(DiagnosticCritical)
                                        )
                                    }
                                }
                                ScoreBadge(score = conv.leadScore)
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "\"${conv.lastMessage}\"",
                                style = MaterialTheme.typography.bodySmall,
                                color = SlateTextSecondary,
                                maxLines = 1
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    conv.tags.take(2).forEach { tag ->
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = SlateSurfaceContainerHigh
                                        ) {
                                            Text(
                                                text = tag,
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                                color = SlateTextSecondary,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }

                                Text(
                                    text = "${conv.channel} • ${conv.timestamp}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                    color = SlateTextMuted
                                )
                            }
                        }
                    }
                }
            }

            // Right Detail panel
            selectedConversation?.let { conv ->
                val leadScoreObj = remember(conv) { MarketingRepository.calculateLeadScore(conv) }

                LazyColumn(
                    modifier = Modifier
                        .weight(1.3f)
                        .fillMaxHeight()
                        .background(SlateSurface)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Header Detail Card
                    item {
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = SlateSurfaceContainer)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = conv.customerName,
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                            color = SlateTextPrimary
                                        )
                                        Text(
                                            text = "${conv.channel} • ${conv.phoneOrHandle}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = SlateTextSecondary
                                        )
                                    }

                                    ScoreBadge(
                                        score = conv.leadScore,
                                        onClick = { showScoreDialog = true }
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Why this score exists? (Click score)",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TealPrimary,
                                        modifier = Modifier.clickable { showScoreDialog = true }
                                    )
                                    Text(
                                        text = "Intent: ${conv.analysis.purchaseIntent}",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = if (conv.analysis.purchaseIntent == "HIGH") DiagnosticCritical else HealthGreenDark
                                    )
                                }
                            }
                        }
                    }

                    // AI Summary Card
                    item {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = TealContainer.copy(alpha = 0.35f)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, TealLight.copy(alpha = 0.4f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.AutoAwesome,
                                        contentDescription = "AI Summary",
                                        tint = TealPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "AI CONVERSATION SUMMARY",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.sp
                                        ),
                                        color = TealDark
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = conv.analysis.aiSummary,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = SlateTextPrimary
                                )
                            }
                        }
                    }

                    // Structured AI Information Extraction (JSON-like schema visualization)
                    item {
                        Column {
                            Text(
                                text = "STRUCTURED INTENT EXTRACTION",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                ),
                                color = SlateTextMuted
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = SlateSurfaceContainer)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    ExtractedFieldRow("Product Interest", conv.analysis.productInterest)
                                    ExtractedFieldRow("Category", conv.analysis.category)
                                    ExtractedFieldRow("Budget", conv.analysis.budget)
                                    ExtractedFieldRow("Location", conv.analysis.location)
                                    ExtractedFieldRow("Size / Color", "${conv.analysis.size} • ${conv.analysis.color}")
                                    ExtractedFieldRow("Customer Stage", conv.analysis.customerStage)
                                    ExtractedFieldRow("Sentiment", conv.analysis.sentiment)
                                    ExtractedFieldRow(
                                        "Key Objections",
                                        conv.analysis.objections.joinToString(", ").ifEmpty { "None cited" }
                                    )
                                }
                            }
                        }
                    }

                    // Customer Memory
                    item {
                        Column {
                            Text(
                                text = "CUSTOMER MEMORY",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                ),
                                color = SlateTextMuted
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            conv.memories.forEach { mem ->
                                Row(
                                    modifier = Modifier.padding(vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Bookmark,
                                        contentDescription = "Memory",
                                        tint = CyanSecondary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = mem,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = SlateTextPrimary
                                    )
                                }
                            }
                        }
                    }

                    // Messages transcript
                    item {
                        Text(
                            text = "AUTHORIZED CONVERSATION HISTORY",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = SlateTextMuted
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }

                    items(conv.messages) { msg ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            contentAlignment = if (msg.isCustomer) Alignment.CenterStart else Alignment.CenterEnd
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (msg.isCustomer) SlateSurfaceContainer else TealPrimary,
                                tonalElevation = 1.dp
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = msg.senderName,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = if (msg.isCustomer) SlateTextSecondary else Color.White.copy(alpha = 0.8f)
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = msg.text,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (msg.isCustomer) SlateTextPrimary else Color.White
                                    )
                                    Text(
                                        text = msg.timestamp,
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                        color = if (msg.isCustomer) SlateTextMuted else Color.White.copy(alpha = 0.7f),
                                        modifier = Modifier.align(Alignment.End)
                                    )
                                }
                            }
                        }
                    }

                    // Recommended Next Action & AI Reply Generator
                    item {
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = SlateSurface),
                            border = androidx.compose.foundation.BorderStroke(1.dp, TealLight.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "RECOMMENDED NEXT ACTION:",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    ),
                                    color = TealPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = conv.analysis.recommendedNextAction,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = SlateTextPrimary
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "AI Reply Generator",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = SlateTextPrimary
                                    )

                                    Button(
                                        onClick = {
                                            coroutineScope.launch {
                                                isGeneratingReply = true
                                                val generated = GeminiApiClient.generateMarketingReply(
                                                    customerMessage = conv.lastMessage,
                                                    productInterest = conv.analysis.productInterest,
                                                    location = conv.analysis.location
                                                )
                                                draftReplyText = generated
                                                isGeneratingReply = false
                                            }
                                        },
                                        modifier = Modifier.testTag("generate_reply_btn"),
                                        colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        if (isGeneratingReply) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(14.dp),
                                                color = Color.White,
                                                strokeWidth = 2.dp
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Thinking...")
                                        } else {
                                            Icon(
                                                Icons.Default.AutoAwesome,
                                                contentDescription = "Generate",
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Generate Reply")
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = draftReplyText,
                                    onValueChange = { draftReplyText = it },
                                    placeholder = {
                                        Text("Click 'Generate Reply' or type custom message to customer...")
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(min = 90.dp)
                                        .testTag("reply_text_input"),
                                    shape = RoundedCornerShape(10.dp),
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                                    keyboardActions = KeyboardActions(onSend = {
                                        if (draftReplyText.isNotBlank()) {
                                            keyboardController?.hide()
                                            MarketingRepository.markMessageReplied(conv.id, draftReplyText)
                                            replyNotice = "Reply sent successfully & logged in customer memory!"
                                            draftReplyText = ""
                                        }
                                    })
                                )

                                if (replyNotice != null) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = replyNotice!!,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = HealthGreenDark
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Button(
                                        onClick = {
                                            if (draftReplyText.isNotBlank()) {
                                                keyboardController?.hide()
                                                MarketingRepository.markMessageReplied(conv.id, draftReplyText)
                                                replyNotice = "Reply sent successfully & logged in customer memory!"
                                                draftReplyText = ""
                                            }
                                        },
                                        enabled = draftReplyText.isNotBlank(),
                                        modifier = Modifier.testTag("send_reply_btn"),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Send,
                                            contentDescription = "Send",
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Send Reply")
                                    }
                                }
                            }
                        }
                    }
                }

                if (showScoreDialog) {
                    ExplainableScoreDialog(
                        score = leadScoreObj,
                        onDismiss = { showScoreDialog = false }
                    )
                }
            }
        }
    }
}

@Composable
fun ExtractedFieldRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = SlateTextSecondary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            color = SlateTextPrimary
        )
    }
}
