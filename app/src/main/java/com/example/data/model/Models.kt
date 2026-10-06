package com.example.data.model

import android.graphics.Bitmap

enum class ScoreCategory(val label: String, val colorHex: Long) {
    COLD("Cold", 0xFF64748B),
    WARM("Warm", 0xFFF59E0B),
    HOT("Hot", 0xFFF97316),
    VERY_HOT("Very Hot", 0xFFEF4444)
}

enum class Severity(val label: String) {
    CRITICAL("Critical"),
    HIGH("High"),
    MEDIUM("Medium"),
    LOW("Low")
}

data class ScoreBreakdownItem(
    val factor: String,
    val points: Int
)

data class LeadScore(
    val score: Int,
    val category: ScoreCategory,
    val breakdown: List<ScoreBreakdownItem>
)

data class StructuredAIAnalysis(
    val productInterest: String = "Premium Sneaker",
    val category: String = "Footwear",
    val budget: String = "৳2,500 - ৳3,000",
    val location: String = "Sylhet",
    val size: String = "42",
    val color: String = "Black",
    val purchaseIntent: String = "HIGH",
    val urgency: String = "MEDIUM",
    val objections: List<String> = listOf("Delivery cost inquiry"),
    val questions: List<String> = listOf("Size 42 in stock?", "Delivery timeline to Sylhet"),
    val sentiment: String = "Positive",
    val customerStage: String = "Consideration",
    val recommendedNextAction: String = "Send size-42 availability confirmation and Sylhet delivery timeline.",
    val aiSummary: String = "Customer is highly interested in Premium Sneaker. Inquired regarding price, size 42 availability, and express shipping to Sylhet."
)

data class Message(
    val id: String,
    val conversationId: String,
    val senderName: String,
    val isCustomer: Boolean,
    val text: String,
    val timestamp: String
)

data class CustomerMemory(
    val id: String,
    val customerName: String,
    val note: String,
    val source: String
)

data class Conversation(
    val id: String,
    val customerName: String,
    val channel: String = "Messenger", // Messenger, Instagram, WhatsApp, Website
    val lastMessage: String,
    val timestamp: String,
    val leadScore: Int,
    val tags: List<String>,
    val isUnanswered: Boolean = false,
    val phoneOrHandle: String = "@user",
    val analysis: StructuredAIAnalysis,
    val messages: List<Message> = emptyList(),
    val memories: List<String> = emptyList()
)

data class Lead(
    val id: String,
    val customerName: String,
    val product: String,
    val leadScore: Int,
    val intent: String, // High, Medium, Low
    val stage: String, // Consideration, Decision, Awareness
    val lastActivity: String,
    val source: String, // Facebook Reel, Facebook Post, Messenger, Website
    val location: String,
    val revenue: Double,
    val nextAction: String,
    val tags: List<String>,
    val isConverted: Boolean = false,
    val isLost: Boolean = false,
    val lostReason: String? = null
)

data class AIDiagnosis(
    val id: String,
    val category: String, // Lead Response, Content, Pricing, Retention
    val diagnosisText: String,
    val severity: Severity,
    val evidence: String,
    val impact: String,
    val recommendedAction: String,
    val buttonActionText: String,
    val estimatedOpportunity: String
)

data class ContentItem(
    val id: String,
    val title: String,
    val type: String, // Reel, Post, Carousel
    val reach: Int,
    val engagement: Int,
    val conversations: Int,
    val qualifiedLeads: Int,
    val purchases: Int,
    val revenue: Double,
    val aiDiagnosis: String,
    val recommendation: String
)

data class ContentIdea(
    val id: String,
    val title: String,
    val format: String,
    val triggerQuestion: String,
    val hook: String,
    val outline: String
)

data class RevenueAttributionItem(
    val sourceName: String,
    val sourceType: String,
    val leads: Int,
    val qualifiedLeads: Int,
    val customers: Int,
    val revenue: Double,
    val conversionRate: Double
)

data class Campaign(
    val id: String,
    val name: String,
    val channel: String,
    val audience: String,
    val objective: String, // Lead Generation, Product Interest, Appointment, Sales, Retention
    val status: String, // Active, Scheduled, Paused
    val leads: Int,
    val conversions: Int,
    val revenue: Double,
    val messagingEligible: Boolean = true
)

data class GeneratedAdCreative(
    val id: String,
    val prompt: String,
    val imageSize: String, // 1K, 2K, 4K
    val aspectRatio: String, // 1:1, 16:9, 9:16
    val bitmap: Bitmap?,
    val headline: String,
    val cta: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class BusinessProfile(
    val name: String = "Urban Style BD",
    val category: String = "Clothing",
    val primaryChannel: String = "Facebook",
    val monthlyLeadsEstimate: String = "1,000 - 2,500",
    val averageOrderValue: String = "৳2,800",
    val currencySymbol: String = "৳",
    val isDemoMode: Boolean = true,
    val connectedPageName: String? = "Urban Style BD Official",
    val metaConnected: Boolean = true
)
