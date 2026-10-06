package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.MarketingRepository
import com.example.ui.components.DemoModeBadge
import com.example.ui.screens.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch

data class NavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val outlinedIcon: ImageVector
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContainer()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContainer() {
    var currentScreen by remember { mutableStateOf("dashboard") }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val businessProfile by MarketingRepository.businessProfile.collectAsState()
    var showOnboardingDialog by remember { mutableStateOf(false) }
    var showNotificationDialog by remember { mutableStateOf(false) }

    // Intercept hardware/system back button
    BackHandler(enabled = currentScreen != "dashboard") {
        currentScreen = "dashboard"
    }

    val navItems = listOf(
        NavItem("dashboard", "Dashboard", Icons.Default.Dashboard, Icons.Outlined.Dashboard),
        NavItem("conversations", "Conversations", Icons.Default.Chat, Icons.Outlined.Chat),
        NavItem("leads", "Leads", Icons.Default.People, Icons.Outlined.People),
        NavItem("ai_doctor", "AI Doctor", Icons.Default.MedicalServices, Icons.Outlined.MedicalServices),
        NavItem("content", "Content", Icons.Default.AutoAwesome, Icons.Outlined.AutoAwesome),
        NavItem("revenue", "Revenue", Icons.Default.Payments, Icons.Outlined.Payments),
        NavItem("campaigns", "Campaigns", Icons.Default.Campaign, Icons.Outlined.Campaign),
        NavItem("analytics", "Analytics", Icons.Default.BarChart, Icons.Outlined.BarChart),
        NavItem("integrations", "Integrations", Icons.Default.Hub, Icons.Outlined.Hub),
        NavItem("settings", "Settings", Icons.Default.Settings, Icons.Outlined.Settings)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = SlateSurface,
                modifier = Modifier.width(300.dp)
            ) {
                // Drawer Header
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(TealPrimary)
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.MedicalServices,
                                contentDescription = "Logo",
                                tint = TealPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "AI Marketing Doctor",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                            Text(
                                text = "Customer Intelligence SaaS",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Workspace selector pill
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White.copy(alpha = 0.15f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                coroutineScope.launch { drawerState.close() }
                                currentScreen = "settings"
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = businessProfile.name,
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White
                                )
                                Text(
                                    text = "${businessProfile.category} • Demo Workspace",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                            Icon(
                                Icons.Default.UnfoldMore,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Navigation list in Drawer
                navItems.forEach { item ->
                    val isSelected = currentScreen == item.route
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                if (isSelected) item.icon else item.outlinedIcon,
                                contentDescription = item.label,
                                tint = if (isSelected) TealPrimary else SlateTextSecondary
                            )
                        },
                        label = {
                            Text(
                                text = item.label,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) TealPrimary else SlateTextPrimary
                            )
                        },
                        selected = isSelected,
                        onClick = {
                            currentScreen = item.route
                            coroutineScope.launch { drawerState.close() }
                        },
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 2.dp)
                            .testTag("nav_item_${item.route}")
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Divider(color = SlateSurfaceContainerHigh)

                Text(
                    text = "AI Marketing Doctor v1.0 • Meta Compliant",
                    style = MaterialTheme.typography.labelSmall,
                    color = SlateTextMuted,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    ) {
        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing,
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                coroutineScope.launch { drawerState.open() }
                            }
                        ) {
                            Text(
                                text = "Marketing Doctor",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = SlateTextPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = TealContainer
                            ) {
                                Text(
                                    text = businessProfile.name,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 11.sp
                                    ),
                                    color = TealDark,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                coroutineScope.launch { drawerState.open() }
                            },
                            modifier = Modifier.testTag("open_drawer_button")
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = SlateTextPrimary)
                        }
                    },
                    actions = {
                        IconButton(onClick = { currentScreen = "conversations" }) {
                            Icon(Icons.Default.Search, contentDescription = "Search", tint = SlateTextSecondary)
                        }
                        IconButton(onClick = { showNotificationDialog = true }) {
                            BadgedBox(
                                badge = {
                                    Badge(containerColor = DiagnosticCritical) {
                                        Text("3")
                                    }
                                }
                            ) {
                                Icon(Icons.Outlined.Notifications, contentDescription = "Notifications", tint = SlateTextSecondary)
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = SlateSurface)
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = SlateSurface,
                    tonalElevation = 8.dp,
                    windowInsets = WindowInsets.navigationBars
                ) {
                    val bottomItems = listOf(
                        navItems[0], // Dashboard
                        navItems[1], // Conversations
                        navItems[2], // Leads
                        navItems[3], // AI Doctor
                        navItems[4]  // Content
                    )
                    bottomItems.forEach { item ->
                        val isSelected = currentScreen == item.route
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    if (isSelected) item.icon else item.outlinedIcon,
                                    contentDescription = item.label
                                )
                            },
                            label = {
                                Text(
                                    text = item.label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            selected = isSelected,
                            onClick = { currentScreen = item.route },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = TealPrimary,
                                selectedTextColor = TealPrimary,
                                indicatorColor = TealContainer
                            )
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (currentScreen) {
                    "dashboard" -> DashboardScreen(onNavigate = { currentScreen = it })
                    "conversations" -> ConversationsScreen()
                    "leads" -> LeadsScreen(onNavigateToConversation = { currentScreen = "conversations" })
                    "ai_doctor" -> AIDoctorScreen(onNavigate = { currentScreen = it })
                    "content" -> ContentStudioScreen(onNavigate = { currentScreen = it })
                    "revenue" -> RevenueScreen()
                    "campaigns" -> CampaignsScreen()
                    "analytics" -> AnalyticsScreen()
                    "integrations" -> IntegrationsScreen()
                    "settings" -> SettingsScreen(onTriggerOnboarding = { showOnboardingDialog = true })
                    else -> DashboardScreen(onNavigate = { currentScreen = it })
                }
            }
        }
    }

    if (showOnboardingDialog) {
        OnboardingDialog(onDismiss = { showOnboardingDialog = false })
    }

    if (showNotificationDialog) {
        AlertDialog(
            onDismissRequest = { showNotificationDialog = false },
            title = { Text("Recent Doctor Alerts") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "• 17 high-intent leads waiting over 24h for size/delivery answers.",
                        style = MaterialTheme.typography.bodySmall,
                        color = DiagnosticCritical
                    )
                    Text(
                        text = "• Reel #12 achieved 28% conversion rate. 5 ideas generated.",
                        style = MaterialTheme.typography.bodySmall,
                        color = HealthGreenDark
                    )
                    Text(
                        text = "• Peak evening traffic starting in 2 hours (8 PM - 11 PM window).",
                        style = MaterialTheme.typography.bodySmall,
                        color = SlateTextPrimary
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showNotificationDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}
