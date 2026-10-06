package com.example.data.repository

import android.graphics.Bitmap
import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object MarketingRepository {

    private val _businessProfile = MutableStateFlow(BusinessProfile())
    val businessProfile: StateFlow<BusinessProfile> = _businessProfile.asStateFlow()

    private val _healthScore = MutableStateFlow(84)
    val healthScore: StateFlow<Int> = _healthScore.asStateFlow()

    private val _conversations = MutableStateFlow<List<Conversation>>(emptyList())
    val conversations: StateFlow<List<Conversation>> = _conversations.asStateFlow()

    private val _selectedConversation = MutableStateFlow<Conversation?>(null)
    val selectedConversation: StateFlow<Conversation?> = _selectedConversation.asStateFlow()

    private val _leads = MutableStateFlow<List<Lead>>(emptyList())
    val leads: StateFlow<List<Lead>> = _leads.asStateFlow()

    private val _diagnoses = MutableStateFlow<List<AIDiagnosis>>(emptyList())
    val diagnoses: StateFlow<List<AIDiagnosis>> = _diagnoses.asStateFlow()

    private val _contentItems = MutableStateFlow<List<ContentItem>>(emptyList())
    val contentItems: StateFlow<List<ContentItem>> = _contentItems.asStateFlow()

    private val _contentIdeas = MutableStateFlow<List<ContentIdea>>(emptyList())
    val contentIdeas: StateFlow<List<ContentIdea>> = _contentIdeas.asStateFlow()

    private val _campaigns = MutableStateFlow<List<Campaign>>(emptyList())
    val campaigns: StateFlow<List<Campaign>> = _campaigns.asStateFlow()

    private val _revenueAttribution = MutableStateFlow<List<RevenueAttributionItem>>(emptyList())
    val revenueAttribution: StateFlow<List<RevenueAttributionItem>> = _revenueAttribution.asStateFlow()

    private val _adCreatives = MutableStateFlow<List<GeneratedAdCreative>>(emptyList())
    val adCreatives: StateFlow<List<GeneratedAdCreative>> = _adCreatives.asStateFlow()

    private val _isDiagnosing = MutableStateFlow(false)
    val isDiagnosing: StateFlow<Boolean> = _isDiagnosing.asStateFlow()

    init {
        loadSeedData()
    }

    fun toggleDemoMode(enabled: Boolean) {
        _businessProfile.update { it.copy(isDemoMode = enabled) }
        if (enabled) {
            loadSeedData()
        }
    }

    fun updateBusinessProfile(updated: BusinessProfile) {
        _businessProfile.value = updated
    }

    fun selectConversation(id: String) {
        val found = _conversations.value.firstOrNull { it.id == id }
        _selectedConversation.value = found
    }

    fun addAdCreative(creative: GeneratedAdCreative) {
        _adCreatives.update { listOf(creative) + it }
    }

    fun markMessageReplied(conversationId: String, replyText: String) {
        _conversations.update { list ->
            list.map { conv ->
                if (conv.id == conversationId) {
                    val updatedMessages = conv.messages + Message(
                        id = "msg_${System.currentTimeMillis()}",
                        conversationId = conv.id,
                        senderName = "Urban Style BD Agent",
                        isCustomer = false,
                        text = replyText,
                        timestamp = "Just now"
                    )
                    conv.copy(
                        isUnanswered = false,
                        lastMessage = "Agent: $replyText",
                        messages = updatedMessages
                    )
                } else conv
            }
        }
        if (_selectedConversation.value?.id == conversationId) {
            selectConversation(conversationId)
        }
    }

    fun runAIDiagnosis() {
        _isDiagnosing.value = true
        // Simulating live AI diagnosis update
        val updated = listOf(
            AIDiagnosis(
                id = "diag_fresh_${System.currentTimeMillis()}",
                category = "Response Velocity",
                diagnosisText = "AI analyzed 14 fresh conversations: Evening response lag between 8 PM - 11 PM accounts for 62% of dropped sneaker inquiries.",
                severity = Severity.HIGH,
                evidence = "62% of high-intent sneaker leads drop off after 35+ min response delay during prime shopping hours.",
                impact = "Lost estimated ৳52,000 monthly in ready-to-buy orders.",
                recommendedAction = "Deploy automated instant size-confirmation bot or assign 1 evening triage agent.",
                buttonActionText = "Deploy Evening Workflow",
                estimatedOpportunity = "৳52,000"
            )
        ) + _diagnoses.value
        _diagnoses.value = updated
        _healthScore.value = 86
        _isDiagnosing.value = false
    }

    fun addLead(lead: Lead) {
        _leads.update { listOf(lead) + it }
    }

    fun updateLeadStatus(id: String, converted: Boolean) {
        _leads.update { list ->
            list.map { if (it.id == id) it.copy(isConverted = converted, stage = if (converted) "Converted" else it.stage) else it }
        }
    }

    fun addCampaign(campaign: Campaign) {
        _campaigns.update { listOf(campaign) + it }
    }

    private fun loadSeedData() {
        // Diagnoses
        _diagnoses.value = listOf(
            AIDiagnosis(
                id = "diag_1",
                category = "Lead Velocity",
                diagnosisText = "Your engagement is strong, but 34% of high-intent leads are not receiving a follow-up within 24 hours.",
                severity = Severity.MEDIUM,
                evidence = "17 high-intent leads currently waiting over 24 hours for size or delivery answers.",
                impact = "Leads cool down rapidly; conversion probability drops by 78% after 24h silence.",
                recommendedAction = "Prioritize the 17 high-intent leads waiting for a response.",
                buttonActionText = "View 17 Leads",
                estimatedOpportunity = "৳48,000"
            ),
            AIDiagnosis(
                id = "diag_2",
                category = "Customer Objections",
                diagnosisText = "Customers are frequently asking about delivery cost. Add a clear delivery-price section to your product page.",
                severity = Severity.LOW,
                evidence = "118 conversations inquired specifically about Sylhet & Chittagong delivery rates.",
                impact = "Repetitive manual queries increase agent workload and cause checkout friction.",
                recommendedAction = "Publish FAQ highlighting ৳120 nationwide shipping & Cash on Delivery.",
                buttonActionText = "Create Recommendation",
                estimatedOpportunity = "৳32,000"
            ),
            AIDiagnosis(
                id = "diag_3",
                category = "Content Intelligence",
                diagnosisText = "Reels are generating 2.8x more qualified leads than static posts.",
                severity = Severity.LOW,
                evidence = "Reels generated 95 leads vs 18 leads from static posts despite 60% lower reach.",
                impact = "Ad spend allocated to static posts yields 4.2x lower ROI than video showcases.",
                recommendedAction = "Create 5 more content pieces using the same topic, CTA, and video hook format.",
                buttonActionText = "Generate 5 Ideas",
                estimatedOpportunity = "৳65,000"
            ),
            AIDiagnosis(
                id = "diag_4",
                category = "Evening Window",
                diagnosisText = "23% of hot leads are not receiving a response within 12 hours. Most unanswered conversations occur between 8 PM and 11 PM.",
                severity = Severity.HIGH,
                evidence = "Evening peak traffic generates 42% of inquiries but only 14% of immediate replies.",
                impact = "Ready buyers purchase from competitor pages while waiting overnight.",
                recommendedAction = "Create an evening response workflow or assign an evening agent.",
                buttonActionText = "Assign Evening Workflow",
                estimatedOpportunity = "৳52,000"
            ),
            AIDiagnosis(
                id = "diag_5",
                category = "Product Opportunity",
                diagnosisText = "78 customers showed interest in Premium Sneaker but did not purchase due to size hesitation.",
                severity = Severity.MEDIUM,
                evidence = "Size 42 and 43 inquiries peaked with 78 abandonments after asking about fit.",
                impact = "Unrealized high-ticket footwear revenue.",
                recommendedAction = "Send size guide chart + free size exchange guarantee.",
                buttonActionText = "Send Size Assurance",
                estimatedOpportunity = "৳70,200"
            ),
            AIDiagnosis(
                id = "diag_6",
                category = "Pricing Risk",
                diagnosisText = "Casual Tee has high inquiry volume but low conversion (3.2%). Price objection is cited in 43% of dropouts.",
                severity = Severity.HIGH,
                evidence = "43% of lost leads asked about price and stated budget lower than ৳1,400.",
                impact = "High traffic without checkout margin.",
                recommendedAction = "Test a limited-time bundle offer (Buy 2 at ৳1,999) to lower per-unit hurdle.",
                buttonActionText = "Launch Bundle Offer",
                estimatedOpportunity = "৳38,000"
            )
        )

        // Realistic Conversations (English and Bengali)
        _conversations.value = listOf(
            Conversation(
                id = "conv_1",
                customerName = "Rahim Ahmed",
                channel = "Messenger",
                lastMessage = "Do you have size 42?",
                timestamp = "12m ago",
                leadScore = 92,
                tags = listOf("Hot Lead", "Size Question", "High Intent"),
                isUnanswered = true,
                phoneOrHandle = "+880 1712-384910",
                analysis = StructuredAIAnalysis(
                    productInterest = "Premium Sneaker",
                    category = "Footwear",
                    budget = "৳2,500 - ৳3,000",
                    location = "Sylhet",
                    size = "42",
                    color = "Black & White",
                    purchaseIntent = "HIGH",
                    urgency = "HIGH",
                    objections = listOf("Delivery timeline to Sylhet"),
                    questions = listOf("Size 42 availability", "Delivery to Sylhet"),
                    sentiment = "Positive",
                    customerStage = "Decision",
                    recommendedNextAction = "Send size-42 availability and Sylhet express delivery information.",
                    aiSummary = "Customer is highly interested in Premium Sneaker. Asked specifically about price, size 42 and delivery to Sylhet."
                ),
                messages = listOf(
                    Message("m1", "conv_1", "Rahim Ahmed", true, "Hello, Premium Sneaker post dekhe ashlam.", "10:15 AM"),
                    Message("m2", "conv_1", "Rahim Ahmed", true, "Do you have size 42?", "10:16 AM"),
                    Message("m3", "conv_1", "Rahim Ahmed", true, "Sylhet delivery koto din lagbe?", "10:18 AM")
                ),
                memories = listOf(
                    "Interested in size 42 sneakers.",
                    "Delivery location: Sylhet City.",
                    "Prefers Cash on Delivery."
                )
            ),
            Conversation(
                id = "conv_2",
                customerName = "Karim Ullah",
                channel = "Messenger",
                lastMessage = "How much is delivery?",
                timestamp = "35m ago",
                leadScore = 61,
                tags = listOf("Warm", "Delivery Question", "Price Sensitive"),
                isUnanswered = true,
                phoneOrHandle = "@karim_ctg",
                analysis = StructuredAIAnalysis(
                    productInterest = "Slim Fit Chinos",
                    category = "Apparel",
                    budget = "৳1,500 - ৳2,000",
                    location = "Chittagong",
                    size = "34",
                    color = "Khaki",
                    purchaseIntent = "MEDIUM",
                    urgency = "MEDIUM",
                    objections = listOf("Delivery charge sensitivity"),
                    questions = listOf("Delivery fee to Chittagong GEC circle"),
                    sentiment = "Neutral",
                    customerStage = "Consideration",
                    recommendedNextAction = "Confirm ৳120 flat delivery with Cash on Delivery and try-on return.",
                    aiSummary = "Customer asked for Chinos price and delivery fees to Chittagong."
                ),
                messages = listOf(
                    Message("m4", "conv_2", "Karim Ullah", true, "Assalamu Alaikum. Chinos 34 size available?", "09:40 AM"),
                    Message("m5", "conv_2", "Urban Style BD", false, "Walaikum Assalam! Yes sir, Khaki 34 size is in stock.", "09:45 AM"),
                    Message("m6", "conv_2", "Karim Ullah", true, "How much is delivery?", "09:55 AM")
                ),
                memories = listOf(
                    "Interested in Khaki Chinos size 34.",
                    "Chittagong resident."
                )
            ),
            Conversation(
                id = "conv_3",
                customerName = "Tanvir Hossain",
                channel = "Facebook Page",
                lastMessage = "ভাই এইটার দাম কত?",
                timestamp = "1h ago",
                leadScore = 78,
                tags = listOf("Hot Lead", "Price Question", "Product Interested"),
                isUnanswered = true,
                phoneOrHandle = "+880 1823-994821",
                analysis = StructuredAIAnalysis(
                    productInterest = "Oxford Cotton Shirt",
                    category = "Clothing",
                    budget = "৳1,800 - ৳2,200",
                    location = "Dhaka, Dhanmondi",
                    size = "L",
                    color = "Sky Blue",
                    purchaseIntent = "HIGH",
                    urgency = "MEDIUM",
                    objections = listOf("Awaiting pricing"),
                    questions = listOf("Product price in BDT", "Fabric 100% cotton?"),
                    sentiment = "Positive",
                    customerStage = "Consideration",
                    recommendedNextAction = "Share ৳1,850 price with current 10% promo code and size chart.",
                    aiSummary = "Direct inquiry in Bengali asking for price on Oxford Cotton Shirt. High purchase potential."
                ),
                messages = listOf(
                    Message("m7", "conv_3", "Tanvir Hossain", true, "ভাই এইটার দাম কত?", "09:00 AM")
                ),
                memories = listOf(
                    "First time contacting.",
                    "Dhanmondi, Dhaka."
                )
            ),
            Conversation(
                id = "conv_4",
                customerName = "Nusrat Jahan",
                channel = "Instagram",
                lastMessage = "Cash on delivery আছে?",
                timestamp = "2h ago",
                leadScore = 88,
                tags = listOf("Very Hot", "High Intent", "Repeat Customer"),
                isUnanswered = false,
                phoneOrHandle = "@nusrat_j",
                analysis = StructuredAIAnalysis(
                    productInterest = "Winter Fleece Hoodie",
                    category = "Winterwear",
                    budget = "৳2,200",
                    location = "Rajshahi",
                    size = "M",
                    color = "Oatmeal",
                    purchaseIntent = "HIGH",
                    urgency = "HIGH",
                    objections = listOf("Prefers payment after inspection"),
                    questions = listOf("Cash on delivery in Rajshahi"),
                    sentiment = "Positive",
                    customerStage = "Decision",
                    recommendedNextAction = "Confirm Cash on Delivery and send order confirmation form.",
                    aiSummary = "Repeat customer asking for Cash on Delivery confirmation on Winter Hoodie. Ready to close."
                ),
                messages = listOf(
                    Message("m8", "conv_4", "Nusrat Jahan", true, "Ager jacket ta khub valo chilo. Hoodie ta order korbo.", "08:15 AM"),
                    Message("m9", "conv_4", "Nusrat Jahan", true, "Cash on delivery আছে?", "08:16 AM"),
                    Message("m10", "conv_4", "Urban Style BD", false, "Ji apu! All over Bangladesh Cash on Delivery available.", "08:20 AM")
                ),
                memories = listOf(
                    "Previously purchased Denim Jacket (Order #BD-4891).",
                    "Prefers Oatmeal / pastel shades.",
                    "High customer satisfaction."
                )
            ),
            Conversation(
                id = "conv_5",
                customerName = "Mahmudul Hasan",
                channel = "WhatsApp",
                lastMessage = "কালকে পাব?",
                timestamp = "3h ago",
                leadScore = 85,
                tags = listOf("Hot Lead", "High Intent", "Urgent Delivery"),
                isUnanswered = true,
                phoneOrHandle = "+880 1911-002938",
                analysis = StructuredAIAnalysis(
                    productInterest = "Denim Jacket",
                    category = "Outerwear",
                    budget = "৳3,200",
                    location = "Mirpur, Dhaka",
                    size = "XL",
                    color = "Vintage Blue",
                    purchaseIntent = "HIGH",
                    urgency = "HIGH",
                    objections = listOf("Next-day delivery needed for event"),
                    questions = listOf("Same-day or next-day delivery in Mirpur"),
                    sentiment = "Positive",
                    customerStage = "Decision",
                    recommendedNextAction = "Confirm same-day express rider dispatch to Mirpur.",
                    aiSummary = "Urgent buyer asking for next-day delivery for Denim Jacket. Immediate conversion candidate."
                ),
                messages = listOf(
                    Message("m11", "conv_5", "Mahmudul Hasan", true, "Denim Jacket XL hobe?", "07:30 AM"),
                    Message("m12", "conv_5", "Urban Style BD", false, "Yes brother, available!", "07:32 AM"),
                    Message("m13", "conv_5", "Mahmudul Hasan", true, "কালকে পাব? Amar ekta function ase kal shondhay.", "07:35 AM")
                ),
                memories = listOf(
                    "Mirpur 10, Dhaka.",
                    "Requires same-day or 24h courier delivery."
                )
            ),
            Conversation(
                id = "conv_6",
                customerName = "Fahim Al-Mamun",
                channel = "Messenger",
                lastMessage = "Black color available?",
                timestamp = "4h ago",
                leadScore = 74,
                tags = listOf("Hot Lead", "Product Interested", "Color Inquiry"),
                isUnanswered = false,
                phoneOrHandle = "@fahim_bd",
                analysis = StructuredAIAnalysis(
                    productInterest = "Premium Sneaker",
                    category = "Footwear",
                    budget = "৳2,800",
                    location = "Sylhet",
                    size = "41",
                    color = "Black",
                    purchaseIntent = "HIGH",
                    urgency = "MEDIUM",
                    objections = emptyList(),
                    questions = listOf("Black color in 41"),
                    sentiment = "Positive",
                    customerStage = "Consideration",
                    recommendedNextAction = "Offer size 41 in triple black edition with free return guarantee.",
                    aiSummary = "Customer inquired about Triple Black color option for Premium Sneaker."
                ),
                messages = listOf(
                    Message("m14", "conv_6", "Fahim Al-Mamun", true, "Black color available?", "06:10 AM"),
                    Message("m15", "conv_6", "Urban Style BD", false, "Yes Fahim! Black edition is available in sizes 40, 41, 42.", "06:15 AM")
                ),
                memories = listOf("Prefers all-black aesthetics.")
            ),
            Conversation(
                id = "conv_7",
                customerName = "Hasan Ali",
                channel = "Website Chat",
                lastMessage = "Thank you",
                timestamp = "5h ago",
                leadScore = 23,
                tags = listOf("Cold", "Inquiry Completed"),
                isUnanswered = false,
                phoneOrHandle = "guest_8923",
                analysis = StructuredAIAnalysis(
                    productInterest = "Polo Shirt",
                    category = "Clothing",
                    budget = "৳950",
                    location = "Dhaka",
                    size = "L",
                    color = "Navy",
                    purchaseIntent = "LOW",
                    urgency = "LOW",
                    objections = listOf("Just browsing"),
                    questions = emptyList(),
                    sentiment = "Neutral",
                    customerStage = "Awareness",
                    recommendedNextAction = "Do not contact yet; send seasonal catalog upon next campaign launch.",
                    aiSummary = "Casual query, already acknowledged. Low buying urgency."
                ),
                messages = listOf(
                    Message("m16", "conv_7", "Hasan Ali", true, "Are you open on Friday?", "05:00 AM"),
                    Message("m17", "conv_7", "Urban Style BD", false, "Yes, online support is 24/7!", "05:05 AM"),
                    Message("m18", "conv_7", "Hasan Ali", true, "Thank you", "05:10 AM")
                ),
                memories = listOf("General store timing query.")
            ),
            Conversation(
                id = "conv_8",
                customerName = "Sadia Islam",
                channel = "Messenger",
                lastMessage = "আমি পরে জানাবো।",
                timestamp = "6h ago",
                leadScore = 38,
                tags = listOf("Warm", "Price Sensitive", "Follow Up"),
                isUnanswered = false,
                phoneOrHandle = "+880 1682-119283",
                analysis = StructuredAIAnalysis(
                    productInterest = "Casual Tee",
                    category = "Apparel",
                    budget = "৳800 - ৳1,000",
                    location = "Khulna",
                    size = "M",
                    color = "White",
                    purchaseIntent = "LOW",
                    urgency = "LOW",
                    objections = listOf("Price perceived higher than budget"),
                    questions = listOf("Discount code available?"),
                    sentiment = "Hesitant",
                    customerStage = "Consideration",
                    recommendedNextAction = "Send 10% first-order voucher or bundle deal after 48 hours.",
                    aiSummary = "Price sensitivity expressed with 'আমি পরে জানাবো'. Re-engage with bundle discount."
                ),
                messages = listOf(
                    Message("m19", "conv_8", "Sadia Islam", true, "Price koto?", "04:00 AM"),
                    Message("m20", "conv_8", "Urban Style BD", false, "Price is ৳1,250 with premium combed cotton.", "04:15 AM"),
                    Message("m21", "conv_8", "Sadia Islam", true, "আমি পরে জানাবো।", "04:20 AM")
                ),
                memories = listOf("Price sensitive. Target for end-of-month flash sale.")
            )
        )
        _selectedConversation.value = _conversations.value.first()

        // Leads list
        _leads.value = listOf(
            Lead(
                id = "lead_1",
                customerName = "Rahim Ahmed",
                product = "Premium Sneaker",
                leadScore = 92,
                intent = "High",
                stage = "Decision",
                lastActivity = "12m ago",
                source = "Facebook Reel #12",
                location = "Sylhet",
                revenue = 2800.0,
                nextAction = "Confirm size 42 availability & Sylhet shipping",
                tags = listOf("Hot Lead", "Size Question", "High Intent")
            ),
            Lead(
                id = "lead_2",
                customerName = "Nusrat Jahan",
                product = "Winter Fleece Hoodie",
                leadScore = 88,
                intent = "High",
                stage = "Decision",
                lastActivity = "2h ago",
                source = "Messenger Direct",
                location = "Rajshahi",
                revenue = 2200.0,
                nextAction = "Dispatch Cash on Delivery order form",
                tags = listOf("Very Hot", "Repeat Customer")
            ),
            Lead(
                id = "lead_3",
                customerName = "Mahmudul Hasan",
                product = "Denim Jacket",
                leadScore = 85,
                intent = "High",
                stage = "Decision",
                lastActivity = "3h ago",
                source = "Facebook Post #45",
                location = "Mirpur, Dhaka",
                revenue = 3200.0,
                nextAction = "Offer same-day express rider delivery",
                tags = listOf("Hot Lead", "Urgent Delivery")
            ),
            Lead(
                id = "lead_4",
                customerName = "Tanvir Hossain",
                product = "Oxford Cotton Shirt",
                leadScore = 78,
                intent = "High",
                stage = "Consideration",
                lastActivity = "1h ago",
                source = "Facebook Reel #12",
                location = "Dhaka",
                revenue = 1850.0,
                nextAction = "Send ৳1,850 pricing & size guide",
                tags = listOf("Hot Lead", "Price Question")
            ),
            Lead(
                id = "lead_5",
                customerName = "Fahim Al-Mamun",
                product = "Premium Sneaker",
                leadScore = 74,
                intent = "High",
                stage = "Consideration",
                lastActivity = "4h ago",
                source = "Facebook Reel #12",
                location = "Sylhet",
                revenue = 2800.0,
                nextAction = "Share Black color stock link",
                tags = listOf("Hot Lead", "Color Inquiry")
            ),
            Lead(
                id = "lead_6",
                customerName = "Karim Ullah",
                product = "Slim Fit Chinos",
                leadScore = 61,
                intent = "Medium",
                stage = "Consideration",
                lastActivity = "35m ago",
                source = "Website",
                location = "Chittagong",
                revenue = 1650.0,
                nextAction = "Send ৳120 flat delivery details",
                tags = listOf("Warm", "Delivery Question")
            ),
            Lead(
                id = "lead_7",
                customerName = "Sadia Islam",
                product = "Casual Tee",
                leadScore = 38,
                intent = "Low",
                stage = "Consideration",
                lastActivity = "6h ago",
                source = "Facebook Post #45",
                location = "Khulna",
                revenue = 1250.0,
                nextAction = "Send 10% bundle coupon in 48h",
                tags = listOf("Warm", "Price Sensitive")
            ),
            Lead(
                id = "lead_8",
                customerName = "Zubair Rahman",
                product = "Premium Sneaker",
                leadScore = 95,
                intent = "High",
                stage = "Converted",
                lastActivity = "Yesterday",
                source = "Facebook Reel #12",
                location = "Uttara, Dhaka",
                revenue = 2800.0,
                nextAction = "Send tracking number",
                tags = listOf("Converted", "Paid COD"),
                isConverted = true
            ),
            Lead(
                id = "lead_9",
                customerName = "Mustafa Kamal",
                product = "Casual Tee",
                leadScore = 20,
                intent = "Low",
                stage = "Lost",
                lastActivity = "2 days ago",
                source = "Messenger Direct",
                location = "Bogura",
                revenue = 0.0,
                nextAction = "Archive lead",
                tags = listOf("Lost", "Price Sensitive"),
                isLost = true,
                lostReason = "Price too high compared to local market"
            )
        )

        // Content items
        _contentItems.value = listOf(
            ContentItem(
                id = "cnt_1",
                title = "Facebook Reel #12 (Sneaker Water Resistance Test)",
                type = "Reel",
                reach = 8000,
                engagement = 1450,
                conversations = 95,
                qualifiedLeads = 68,
                purchases = 21,
                revenue = 67500.0,
                aiDiagnosis = "Post B has lower reach but 5.3x higher lead conversion compared to static posts.",
                recommendation = "Create 5 more content pieces using the same topic, demonstration format, and Sylhet delivery CTA."
            ),
            ContentItem(
                id = "cnt_2",
                title = "Facebook Post #45 (Static Catalog Grid)",
                type = "Post",
                reach = 20000,
                engagement = 820,
                conversations = 18,
                qualifiedLeads = 8,
                purchases = 2,
                revenue = 6000.0,
                aiDiagnosis = "High reach but very low conversion. Viewers scroll without intent.",
                recommendation = "Convert static photos into behind-the-scenes video try-ons with clear pricing text."
            ),
            ContentItem(
                id = "cnt_3",
                title = "Facebook Reel #18 (Oxford Cotton Styling Guide)",
                type = "Reel",
                reach = 14500,
                engagement = 2100,
                conversations = 82,
                qualifiedLeads = 54,
                purchases = 17,
                revenue = 54400.0,
                aiDiagnosis = "Strong engagement among corporate buyers in Dhaka. Average order value ৳3,200.",
                recommendation = "Promote 2-pack bundle to increase cart size."
            )
        )

        // Content ideas based on customer queries
        _contentIdeas.value = listOf(
            ContentIdea(
                id = "idea_1",
                title = "Sylhet Delivery & Cash on Delivery Guide",
                format = "Reel (30s)",
                triggerQuestion = "Is delivery available in Sylhet? Delivery koto din lagbe?",
                hook = "\"Sylhet e delivery ki shotti 48 hours e pawa jay? Shunun amader courier partner theke...\"",
                outline = "1. Show packaging parcel. 2. Mention ৳120 flat charge. 3. Show customer opening and checking before paying."
            ),
            ContentIdea(
                id = "idea_2",
                title = "Sneaker Size 41 vs 42 Fit Comparison",
                format = "Short Video",
                triggerQuestion = "Size 42 ache? Fit ki standard?",
                hook = "\"Sneakers kinte giye size niye tension? Eivabe mepe nin apnar foot size in 30 seconds!\"",
                outline = "1. Ruler measurement technique. 2. Wearing size 41 vs 42. 3. Free size replacement guarantee CTA."
            ),
            ContentIdea(
                id = "idea_3",
                title = "How Much Does Delivery Cost Across BD?",
                format = "Carousel FAQ",
                triggerQuestion = "Delivery charge koto?",
                hook = "\"No hidden charges! Complete delivery fee breakdown for Dhaka, Chittagong, Sylhet & All Districts.\"",
                outline = "Slide 1: Inside Dhaka ৳70. Slide 2: Outside Dhaka ৳120. Slide 3: Cash on Delivery steps."
            ),
            ContentIdea(
                id = "idea_4",
                title = "3 Things to Know Before Ordering Urban Style",
                format = "Reel",
                triggerQuestion = "Quality kemon? Cash on delivery ase?",
                hook = "\"Order korar age eita obosshoi dekhun - 100% original fabric inspection guarantee.\"",
                outline = "Highlight 7-day exchange, Cash on delivery, and genuine stitching."
            )
        )

        // Campaigns
        _campaigns.value = listOf(
            Campaign(
                id = "camp_1",
                name = "Eid Sneaker Showcase",
                channel = "Facebook & Messenger",
                audience = "Footwear Enthusiasts, 18-35 (Sylhet & Dhaka)",
                objective = "Sales",
                status = "Active",
                leads = 184,
                conversions = 42,
                revenue = 124000.0,
                messagingEligible = true
            ),
            Campaign(
                id = "camp_2",
                name = "Sylhet Fast-Track Express Delivery",
                channel = "Facebook Reels",
                audience = "Sylhet Division Shoppers",
                objective = "Lead Generation",
                status = "Active",
                leads = 92,
                conversions = 26,
                revenue = 68000.0,
                messagingEligible = true
            ),
            Campaign(
                id = "camp_3",
                name = "Abandoned Size Inquiries Re-engagement",
                channel = "Messenger",
                audience = "Asked for Size 42 within 7 days",
                objective = "Retention",
                status = "Active",
                leads = 45,
                conversions = 14,
                revenue = 39200.0,
                messagingEligible = true
            )
        )

        // Revenue Attribution
        _revenueAttribution.value = listOf(
            RevenueAttributionItem(
                sourceName = "Facebook Reel #12 (Sneaker Demo)",
                sourceType = "Facebook Reel",
                leads = 75,
                qualifiedLeads = 52,
                customers = 21,
                revenue = 74000.0,
                conversionRate = 28.0
            ),
            RevenueAttributionItem(
                sourceName = "Messenger Direct Chat",
                sourceType = "Messenger",
                leads = 142,
                qualifiedLeads = 94,
                customers = 38,
                revenue = 112000.0,
                conversionRate = 26.7
            ),
            RevenueAttributionItem(
                sourceName = "Facebook Reel #18 (Styling Guide)",
                sourceType = "Facebook Reel",
                leads = 54,
                qualifiedLeads = 36,
                customers = 17,
                revenue = 54400.0,
                conversionRate = 31.4
            ),
            RevenueAttributionItem(
                sourceName = "Website Catalog Link",
                sourceType = "Website",
                leads = 88,
                qualifiedLeads = 42,
                customers = 14,
                revenue = 42000.0,
                conversionRate = 15.9
            ),
            RevenueAttributionItem(
                sourceName = "Instagram DM Direct",
                sourceType = "Instagram",
                leads = 48,
                qualifiedLeads = 24,
                customers = 9,
                revenue = 27500.0,
                conversionRate = 18.7
            ),
            RevenueAttributionItem(
                sourceName = "Facebook Post #45 (Static Catalog)",
                sourceType = "Facebook Post",
                leads = 31,
                qualifiedLeads = 12,
                customers = 4,
                revenue = 11500.0,
                conversionRate = 12.9
            )
        )
    }

    fun calculateLeadScore(conversation: Conversation): LeadScore {
        val breakdown = mutableListOf<ScoreBreakdownItem>()
        var total = 10
        breakdown.add(ScoreBreakdownItem("Conversation started", 10))

        if (conversation.lastMessage.contains("?", ignoreCase = true) || conversation.lastMessage.contains("দাম", ignoreCase = true)) {
            total += 10
            breakdown.add(ScoreBreakdownItem("Product question", 10))
        }
        if (conversation.lastMessage.contains("দাম", ignoreCase = true) || conversation.lastMessage.contains("price", ignoreCase = true) || conversation.lastMessage.contains("cost", ignoreCase = true)) {
            total += 15
            breakdown.add(ScoreBreakdownItem("Price question", 15))
        }
        if (conversation.lastMessage.contains("size", ignoreCase = true) || conversation.lastMessage.contains("color", ignoreCase = true) || conversation.lastMessage.contains("42", ignoreCase = true)) {
            total += 15
            breakdown.add(ScoreBreakdownItem("Size / Color question", 15))
        }
        if (conversation.lastMessage.contains("delivery", ignoreCase = true) || conversation.lastMessage.contains("কালকে", ignoreCase = true) || conversation.lastMessage.contains("sylhet", ignoreCase = true)) {
            total += 10
            breakdown.add(ScoreBreakdownItem("Delivery / Location question", 10))
        }
        if (conversation.tags.contains("High Intent") || conversation.lastMessage.contains("cash on delivery", ignoreCase = true)) {
            total += 20
            breakdown.add(ScoreBreakdownItem("Purchase intent signal", 20))
        }
        if (conversation.tags.contains("Repeat Customer")) {
            total += 10
            breakdown.add(ScoreBreakdownItem("Repeat interaction", 10))
        }
        val remainder = minOf(22, 100 - total)
        if (remainder > 0) {
            total += remainder
            breakdown.add(ScoreBreakdownItem("Active response & sentiment", remainder))
        }

        val clamped = total.coerceIn(0, 100)
        val category = when {
            clamped <= 30 -> ScoreCategory.COLD
            clamped <= 60 -> ScoreCategory.WARM
            clamped <= 80 -> ScoreCategory.HOT
            else -> ScoreCategory.VERY_HOT
        }
        return LeadScore(clamped, category, breakdown)
    }
}
