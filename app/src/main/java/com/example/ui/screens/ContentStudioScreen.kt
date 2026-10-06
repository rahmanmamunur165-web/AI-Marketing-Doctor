package com.example.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ContentIdea
import com.example.data.model.ContentItem
import com.example.data.model.GeneratedAdCreative
import com.example.data.repository.MarketingRepository
import com.example.network.GeminiApiClient
import com.example.ui.components.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentStudioScreen(
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val contentItems by MarketingRepository.contentItems.collectAsState()
    val contentIdeas by MarketingRepository.contentIdeas.collectAsState()
    val adCreatives by MarketingRepository.adCreatives.collectAsState()

    var activeTab by remember { mutableStateOf("Ad Creative Studio") } // "Ad Creative Studio", "Content Doctor", "Idea Generator"

    // Gemini Image Generation State
    var promptText by remember { mutableStateOf("Sylhet Express Delivery for Premium Sneakers with Cash on Delivery badge") }
    var selectedImageSize by remember { mutableStateOf("1K") } // 1K, 2K, 4K affordance
    var selectedAspectRatio by remember { mutableStateOf("1:1") } // 1:1, 16:9, 9:16, 4:3
    var isGeneratingImage by remember { mutableStateOf(false) }
    var currentGeneratedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var generationStatusMessage by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

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
                            text = "Content & Creative Studio",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = SlateTextPrimary
                        )
                        Text(
                            text = "Content performance doctor & AI image generator",
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateTextSecondary
                        )
                    }
                    DemoModeBadge()
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Studio Navigation Tabs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Ad Creative Studio", "Content Doctor", "Idea Generator").forEach { tab ->
                        FilterChip(
                            selected = activeTab == tab,
                            onClick = { activeTab = tab },
                            label = { Text(tab) }
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
            when (activeTab) {
                "Ad Creative Studio" -> {
                    // Header Card
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = SlateSurface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Default.AutoAwesome,
                                            contentDescription = "Gemini",
                                            tint = TealPrimary,
                                            modifier = Modifier.size(22.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "GEMINI PRO IMAGE GENERATOR",
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                letterSpacing = 1.sp
                                            ),
                                            color = TealPrimary
                                        )
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = CyanContainer
                                    ) {
                                        Text(
                                            text = "gemini-3-pro-image-preview",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = OnCyanContainer,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Generate commercial social media creatives, ad banners, and promotional posters directly aligned with customer buying intent.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SlateTextSecondary
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                // Prompt Input
                                OutlinedTextField(
                                    value = promptText,
                                    onValueChange = { promptText = it },
                                    label = { Text("Creative Prompt") },
                                    placeholder = { Text("Describe the ad banner or product visual...") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(min = 90.dp)
                                        .testTag("creative_prompt_input"),
                                    shape = RoundedCornerShape(10.dp),
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                // Quick Prompt Templates based on customer questions
                                Text(
                                    text = "IN-DEMAND TEMPLATES (BASED ON CHAT QUESTIONS):",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    ),
                                    color = SlateTextMuted
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    SuggestionChip(
                                        onClick = {
                                            promptText = "Sylhet Express 48h Delivery announcement banner for Premium Sneaker size 42 with Cash on Delivery badge"
                                        },
                                        label = { Text("Sylhet Delivery Banner") }
                                    )
                                    SuggestionChip(
                                        onClick = {
                                            promptText = "Premium Sneaker Triple Black edition close-up photoshoot, clean studio lighting, luxury sneakers"
                                        },
                                        label = { Text("Triple Black Sneaker") }
                                    )
                                    SuggestionChip(
                                        onClick = {
                                            promptText = "Oxford Cotton Shirt 10% Discount Eid promotional banner with buy now on Messenger callout"
                                        },
                                        label = { Text("Oxford Shirt Promo") }
                                    )
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Mandatory Image Size Affordance (1K, 2K, 4K)
                                Column {
                                    Text(
                                        text = "SELECT IMAGE RESOLUTION (REQUIRED):",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.sp
                                        ),
                                        color = SlateTextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        listOf("1K", "2K", "4K").forEach { sizeOption ->
                                            val isSelected = selectedImageSize == sizeOption
                                            FilterChip(
                                                selected = isSelected,
                                                onClick = { selectedImageSize = sizeOption },
                                                label = {
                                                    Text(
                                                        text = "$sizeOption Resolution",
                                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                                    )
                                                },
                                                leadingIcon = {
                                                    if (isSelected) {
                                                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                                                    }
                                                },
                                                modifier = Modifier.weight(1f).testTag("image_size_$sizeOption")
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Aspect Ratio Selector
                                Column {
                                    Text(
                                        text = "ASPECT RATIO:",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.sp
                                        ),
                                        color = SlateTextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        listOf("1:1", "16:9", "9:16", "4:3").forEach { ratio ->
                                            FilterChip(
                                                selected = selectedAspectRatio == ratio,
                                                onClick = { selectedAspectRatio = ratio },
                                                label = { Text(ratio) },
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Generate Button
                                Button(
                                    onClick = {
                                        keyboardController?.hide()
                                        coroutineScope.launch {
                                            isGeneratingImage = true
                                            generationStatusMessage = "Synthesizing with gemini-3-pro-image-preview at $selectedImageSize..."
                                            val result = GeminiApiClient.generateImage(
                                                prompt = promptText,
                                                imageSize = selectedImageSize,
                                                aspectRatio = selectedAspectRatio
                                            )
                                            result.onSuccess { (bitmap, msg) ->
                                                currentGeneratedBitmap = bitmap
                                                generationStatusMessage = msg
                                                MarketingRepository.addAdCreative(
                                                    GeneratedAdCreative(
                                                        id = "creat_${System.currentTimeMillis()}",
                                                        prompt = promptText,
                                                        imageSize = selectedImageSize,
                                                        aspectRatio = selectedAspectRatio,
                                                        bitmap = bitmap,
                                                        headline = "Urban Style BD - $selectedImageSize Special",
                                                        cta = "Message on Messenger"
                                                    )
                                                )
                                            }.onFailure { err ->
                                                generationStatusMessage = "Error: ${err.localizedMessage}"
                                            }
                                            isGeneratingImage = false
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("generate_image_button"),
                                    colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                                    shape = RoundedCornerShape(10.dp),
                                    enabled = !isGeneratingImage && promptText.isNotBlank()
                                ) {
                                    if (isGeneratingImage) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(18.dp),
                                            color = Color.White,
                                            strokeWidth = 2.dp
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Generating with gemini-3-pro-image-preview...")
                                    } else {
                                        Icon(Icons.Default.Image, contentDescription = "Generate")
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Generate Ad Creative ($selectedImageSize)")
                                    }
                                }

                                if (generationStatusMessage != null) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = generationStatusMessage!!,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (generationStatusMessage!!.startsWith("Error")) DiagnosticCritical else TealPrimary
                                    )
                                }
                            }
                        }
                    }

                    // Display Current Generated Image Canvas
                    if (currentGeneratedBitmap != null) {
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "GENERATED AD ASSET",
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                            color = SlateTextPrimary
                                        )
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = HealthGreenContainer
                                        ) {
                                            Text(
                                                text = "RESOLUTION: $selectedImageSize",
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                color = HealthGreenDark,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Image(
                                        bitmap = currentGeneratedBitmap!!.asImageBitmap(),
                                        contentDescription = "Generated Marketing Creative",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .heightIn(min = 220.dp, max = 340.dp)
                                            .clip(RoundedCornerShape(12.dp)),
                                        contentScale = ContentScale.Fit
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        OutlinedButton(
                                            onClick = {
                                                // Pre-fill campaign creation
                                                onNavigate("campaigns")
                                            },
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Icon(Icons.Default.Campaign, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Use in Campaign")
                                        }

                                        Button(
                                            onClick = {
                                                generationStatusMessage = "Asset saved to media library."
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Save Asset")
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Gallery of Generated Creatives
                    if (adCreatives.isNotEmpty()) {
                        item {
                            Text(
                                text = "RECENT CREATIVES (${adCreatives.size})",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                ),
                                color = SlateTextMuted
                            )
                        }

                        items(adCreatives) { creative ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (creative.bitmap != null) {
                                        Image(
                                            bitmap = creative.bitmap.asImageBitmap(),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(72.dp)
                                                .clip(RoundedCornerShape(8.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = creative.prompt,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                            maxLines = 2,
                                            color = SlateTextPrimary
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Res: ${creative.imageSize} • ${creative.aspectRatio}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = SlateTextMuted
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                "Content Doctor" -> {
                    item {
                        Text(
                            text = "CONTENT ROI DIAGNOSIS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = SlateTextMuted
                        )
                    }

                    items(contentItems) { item ->
                        ContentPerformanceCard(
                            content = item,
                            onGenerateIdeas = {
                                activeTab = "Idea Generator"
                            }
                        )
                    }
                }

                "Idea Generator" -> {
                    item {
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = TealContainer.copy(alpha = 0.45f)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, TealLight.copy(alpha = 0.4f))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Lightbulb,
                                        contentDescription = "Idea",
                                        tint = TealPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "AI CONTENT DOCTOR ENGINE",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = TealDark
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Ideas synthesized directly from high-intent customer inquiries (e.g., delivery charges, sizing, COD guarantee).",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SlateTextPrimary
                                )
                            }
                        }
                    }

                    items(contentIdeas) { idea ->
                        ContentIdeaCard(
                            idea = idea,
                            onCreateCreative = {
                                promptText = "${idea.title} - Social media visual for Urban Style BD, high resolution clean studio marketing shot"
                                activeTab = "Ad Creative Studio"
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ContentPerformanceCard(
    content: ContentItem,
    onGenerateIdeas: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SlateSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = content.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = SlateTextPrimary
                )
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = SlateSurfaceContainerHigh
                ) {
                    Text(
                        text = content.type,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = SlateTextSecondary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Metrics row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SmallMetricCol("Reach", "${"%,d".format(content.reach)}", Modifier.weight(1f))
                SmallMetricCol("Leads", "${content.conversations}", Modifier.weight(1f))
                SmallMetricCol("Sales", "${content.purchases}", Modifier.weight(1f))
                SmallMetricCol("Revenue", "৳${"%,d".format(content.revenue.toInt())}", Modifier.weight(1.2f))
            }

            Spacer(modifier = Modifier.height(12.dp))

            // AI Diagnosis Box
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = SlateSurfaceContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "AI DOCTOR DIAGNOSIS:",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = TealPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = content.aiDiagnosis,
                        style = MaterialTheme.typography.bodySmall,
                        color = SlateTextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Recommendation: ${content.recommendation}",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = SlateTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onGenerateIdeas,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Generate 5 Ideas")
                }
            }
        }
    }
}

@Composable
fun SmallMetricCol(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(SlateSurfaceContainer, RoundedCornerShape(8.dp))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = SlateTextMuted)
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = SlateTextPrimary
        )
    }
}

@Composable
fun ContentIdeaCard(
    idea: ContentIdea,
    onCreateCreative: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SlateSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = idea.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = SlateTextPrimary
                )
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = TealContainer
                ) {
                    Text(
                        text = idea.format,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = TealDark,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Triggered by inquiry: \"${idea.triggerQuestion}\"",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                color = DiagnosticWarning
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Opening Hook: ${idea.hook}",
                style = MaterialTheme.typography.bodyMedium,
                color = SlateTextPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Outline: ${idea.outline}",
                style = MaterialTheme.typography.bodySmall,
                color = SlateTextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = onCreateCreative,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Brush, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Design Ad Creative")
                }
            }
        }
    }
}
