package com.nexvary.foodguard.ui.stage550

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.format.DateFormat
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexvary.foodguard.R
import com.nexvary.foodguard.analysis.ImageSignalReport
import com.nexvary.foodguard.analysis.VisualHeuristicEngine
import com.nexvary.foodguard.data.FoodCatalog
import com.nexvary.foodguard.domain.AssessmentSummary
import com.nexvary.foodguard.domain.SafetyRules
import com.nexvary.foodguard.model.FoodCategory
import com.nexvary.foodguard.model.FoodItem
import com.nexvary.foodguard.model.ManualSafetyCheck
import com.nexvary.foodguard.model.RiskTier
import com.nexvary.foodguard.model.SafetyAssessment
import com.nexvary.foodguard.model.SafetyReasonCode
import com.nexvary.foodguard.model.SafetyVerdict
import com.nexvary.foodguard.storage.ScanHistoryStore
import com.nexvary.foodguard.storage.ScanRecord
import com.nexvary.foodguard.ui.theme.Amber
import com.nexvary.foodguard.ui.theme.Danger
import com.nexvary.foodguard.ui.theme.ElectricBlue
import com.nexvary.foodguard.ui.theme.FoodGuardTheme
import com.nexvary.foodguard.ui.theme.Fresh
import com.nexvary.foodguard.ui.theme.Gold
import java.util.Date

private enum class Stage550Screen {
    HOME,
    CATALOG,
    SCANNER,
    HISTORY,
    SETTINGS,
    SAFETY,
    ABOUT,
    DETAIL
}

private enum class Stage550ThemeMode { SYSTEM, LIGHT, DARK }

@Composable
fun FoodGuardStage550App() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("foodguard_settings", Context.MODE_PRIVATE) }
    var themeModeName by rememberSaveable {
        mutableStateOf(prefs.getString("theme_mode", Stage550ThemeMode.SYSTEM.name) ?: Stage550ThemeMode.SYSTEM.name)
    }
    val themeMode = runCatching { Stage550ThemeMode.valueOf(themeModeName) }.getOrDefault(Stage550ThemeMode.SYSTEM)
    val darkTheme = when (themeMode) {
        Stage550ThemeMode.SYSTEM -> isSystemInDarkTheme()
        Stage550ThemeMode.LIGHT -> false
        Stage550ThemeMode.DARK -> true
    }

    FoodGuardTheme(darkTheme = darkTheme) {
        Stage550Root(
            themeMode = themeMode,
            onThemeMode = { newMode ->
                themeModeName = newMode.name
                prefs.edit().putString("theme_mode", newMode.name).apply()
            }
        )
    }
}

@Composable
private fun Stage550Root(
    themeMode: Stage550ThemeMode,
    onThemeMode: (Stage550ThemeMode) -> Unit
) {
    var screen by rememberSaveable { mutableStateOf(Stage550Screen.HOME) }
    var selectedFoodId by rememberSaveable { mutableStateOf("owaisi_mango") }

    val topLevel = screen in setOf(
        Stage550Screen.HOME,
        Stage550Screen.CATALOG,
        Stage550Screen.SCANNER,
        Stage550Screen.HISTORY,
        Stage550Screen.SETTINGS
    )

    fun openFood(id: String) {
        selectedFoodId = id
        screen = Stage550Screen.DETAIL
    }

    BackHandler(enabled = screen != Stage550Screen.HOME) {
        screen = when (screen) {
            Stage550Screen.DETAIL -> Stage550Screen.CATALOG
            Stage550Screen.SAFETY, Stage550Screen.ABOUT -> Stage550Screen.SETTINGS
            else -> Stage550Screen.HOME
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (topLevel) {
                Stage550BottomBar(screen = screen, onNavigate = { screen = it })
            }
        }
    ) { padding ->
        when (screen) {
            Stage550Screen.HOME -> Stage550HomeScreen(
                modifier = Modifier.padding(padding),
                onScan = { screen = Stage550Screen.SCANNER },
                onCatalog = { screen = Stage550Screen.CATALOG },
                onHistory = { screen = Stage550Screen.HISTORY },
                onSafety = { screen = Stage550Screen.SAFETY },
                onFood = ::openFood
            )
            Stage550Screen.CATALOG -> Stage550CatalogScreen(
                modifier = Modifier.padding(padding),
                onFood = ::openFood
            )
            Stage550Screen.SCANNER -> Stage550ScannerScreen(modifier = Modifier.padding(padding))
            Stage550Screen.HISTORY -> Stage550HistoryScreen(
                modifier = Modifier.padding(padding),
                onFood = ::openFood
            )
            Stage550Screen.SETTINGS -> Stage550SettingsScreen(
                modifier = Modifier.padding(padding),
                themeMode = themeMode,
                onThemeMode = onThemeMode,
                onHistory = { screen = Stage550Screen.HISTORY },
                onSafety = { screen = Stage550Screen.SAFETY },
                onAbout = { screen = Stage550Screen.ABOUT }
            )
            Stage550Screen.SAFETY -> Stage550SafetyScreen(
                modifier = Modifier.padding(padding),
                onBack = { screen = Stage550Screen.SETTINGS }
            )
            Stage550Screen.ABOUT -> Stage550AboutScreen(
                modifier = Modifier.padding(padding),
                onBack = { screen = Stage550Screen.SETTINGS }
            )
            Stage550Screen.DETAIL -> Stage550FoodDetailScreen(
                modifier = Modifier.padding(padding),
                food = FoodCatalog.byId(selectedFoodId),
                onBack = { screen = Stage550Screen.CATALOG },
                onScan = { screen = Stage550Screen.SCANNER }
            )
        }
    }
}

@Composable
private fun Stage550BottomBar(screen: Stage550Screen, onNavigate: (Stage550Screen) -> Unit) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        val items = listOf(
            Triple(Stage550Screen.HOME, stringResource(R.string.home), Icons.Outlined.Home),
            Triple(Stage550Screen.CATALOG, stringResource(R.string.guide), Icons.Outlined.Search),
            Triple(Stage550Screen.SCANNER, stringResource(R.string.scan_food), Icons.Outlined.CameraAlt),
            Triple(Stage550Screen.HISTORY, stringResource(R.string.s550_history), Icons.Outlined.History),
            Triple(Stage550Screen.SETTINGS, stringResource(R.string.settings), Icons.Outlined.Settings)
        )
        items.forEach { (target, label, icon) ->
            NavigationBarItem(
                selected = screen == target,
                onClick = { onNavigate(target) },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label, maxLines = 1, overflow = TextOverflow.Ellipsis) }
            )
        }
    }
}

@Composable
private fun Stage550HomeScreen(
    modifier: Modifier,
    onScan: () -> Unit,
    onCatalog: () -> Unit,
    onHistory: () -> Unit,
    onSafety: () -> Unit,
    onFood: (String) -> Unit
) {
    val featured = FoodCatalog.items.first { it.featured }
    val higherRisk = FoodCatalog.items.filter { it.riskTier == RiskTier.HIGH }.take(3)

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Stage550BrandHeader() }
        item { Stage550ScanHero(onScan) }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                Stage550Metric(Modifier.weight(1f), FoodCatalog.items.size.toString(), stringResource(R.string.seed_foods), Fresh)
                Stage550Metric(Modifier.weight(1f), "550", stringResource(R.string.s550_stage), Gold)
                Stage550Metric(Modifier.weight(1f), "100%", stringResource(R.string.local_first), ElectricBlue)
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = onCatalog, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Outlined.Search, null)
                    Spacer(Modifier.width(7.dp))
                    Text(stringResource(R.string.guide))
                }
                OutlinedButton(onClick = onHistory, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Outlined.History, null)
                    Spacer(Modifier.width(7.dp))
                    Text(stringResource(R.string.s550_history))
                }
            }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = onSafety, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Outlined.HealthAndSafety, null)
                Spacer(Modifier.width(7.dp))
                Text(stringResource(R.string.s550_open_safety_guide))
            }
        }
        item {
            Stage550SectionHeader(stringResource(R.string.s550_higher_risk))
            Spacer(Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                higherRisk.forEach { food ->
                    Stage550FoodCard(food = food, onClick = { onFood(food.id) })
                }
            }
        }
        item {
            Stage550SectionHeader(stringResource(R.string.featured))
            Spacer(Modifier.height(8.dp))
            Stage550FoodCard(food = featured, onClick = { onFood(featured.id) })
        }
        item { Stage550SafetyBanner() }
    }
}

@Composable
private fun Stage550BrandHeader() {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Surface(
            modifier = Modifier.size(58.dp),
            shape = RoundedCornerShape(18.dp),
            color = Fresh.copy(alpha = 0.12f),
            border = BorderStroke(1.dp, Gold.copy(alpha = 0.55f))
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Outlined.HealthAndSafety, null, tint = Fresh, modifier = Modifier.size(32.dp))
            }
        }
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(stringResource(R.string.app_name), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
            Text(stringResource(R.string.tagline), color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Surface(shape = RoundedCornerShape(50), color = Gold.copy(alpha = 0.12f)) {
            Text(stringResource(R.string.s550_stage), modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp), color = Gold, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun Stage550ScanHero(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Brush.linearGradient(listOf(Color(0xFF0D3328), Color(0xFF173948), Color(0xFF332813))))
            .clickable(onClick = onClick)
            .padding(22.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = CircleShape, color = Fresh, contentColor = Color(0xFF05130D)) {
                Box(Modifier.size(64.dp), contentAlignment = Alignment.Center) {
                    Icon(Icons.Outlined.CameraAlt, null, modifier = Modifier.size(32.dp))
                }
            }
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(stringResource(R.string.scan_food), fontSize = 22.sp, fontWeight = FontWeight.Black, color = Color.White)
                Text(stringResource(R.string.scan_subtitle), color = Color.White.copy(alpha = 0.82f))
                Spacer(Modifier.height(6.dp))
                Text(stringResource(R.string.no_upload_required), color = Gold, fontWeight = FontWeight.SemiBold)
            }
            Icon(Icons.Outlined.ChevronRight, null, tint = Color.White)
        }
    }
}

@Composable
private fun Stage550Metric(modifier: Modifier, value: String, label: String, accent: Color) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, accent.copy(alpha = 0.28f))
    ) {
        Column(Modifier.padding(13.dp)) {
            Text(value, fontWeight = FontWeight.Black, fontSize = 19.sp, color = accent)
            Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2)
        }
    }
}

@Composable
private fun Stage550SectionHeader(title: String) {
    Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
}

@Composable
private fun Stage550CatalogScreen(modifier: Modifier, onFood: (String) -> Unit) {
    var query by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf<FoodCategory?>(null) }
    var risk by rememberSaveable { mutableStateOf<RiskTier?>(null) }
    val searched = remember(query) { FoodCatalog.search(query) }
    val filtered = remember(searched, category, risk) {
        searched.filter { food ->
            (category == null || food.category == category) && (risk == null || food.riskTier == risk)
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(11.dp)
    ) {
        item {
            Text(stringResource(R.string.food_guide), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
            Text(stringResource(R.string.catalog_subtitle), color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(18.dp),
                leadingIcon = { Icon(Icons.Outlined.Search, null) },
                placeholder = { Text(stringResource(R.string.search_hint)) }
            )
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item { AssistChip(onClick = { category = null }, label = { Text(stringResource(R.string.all)) }) }
                items(FoodCategory.entries) { value ->
                    AssistChip(onClick = { category = value }, label = { Text(stage550CategoryLabel(value)) })
                }
            }
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item { AssistChip(onClick = { risk = null }, label = { Text(stringResource(R.string.s550_all_risks)) }) }
                items(RiskTier.entries) { value ->
                    AssistChip(onClick = { risk = value }, label = { Text(stage550RiskLabel(value)) })
                }
            }
        }
        item {
            Text("${filtered.size} ${stringResource(R.string.items)}", color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.SemiBold)
        }
        items(filtered, key = { it.id }) { food ->
            Stage550FoodCard(food = food, onClick = { onFood(food.id) })
        }
        if (filtered.isEmpty()) item { Stage550EmptyState(stringResource(R.string.no_results)) }
    }
}

@Composable
private fun Stage550FoodCard(food: FoodItem, onClick: () -> Unit) {
    val accent = stage550RiskColor(food.riskTier)
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, accent.copy(alpha = 0.25f)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(Modifier.padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = RoundedCornerShape(14.dp), color = accent.copy(alpha = 0.12f)) {
                Box(Modifier.size(48.dp), contentAlignment = Alignment.Center) {
                    Text(food.localizedName().take(1), fontWeight = FontWeight.Black, color = accent)
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(food.localizedName(), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text(stage550CategoryLabel(food.category), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Stage550RiskPill(food.riskTier)
            Spacer(Modifier.width(5.dp))
            Icon(Icons.Outlined.ChevronRight, null)
        }
    }
}

@Composable
private fun Stage550FoodDetailScreen(modifier: Modifier, food: FoodItem?, onBack: () -> Unit, onScan: () -> Unit) {
    if (food == null) {
        Column(modifier.fillMaxSize().padding(20.dp)) {
            Stage550TopBar(stringResource(R.string.food_guide), onBack)
            Stage550EmptyState(stringResource(R.string.no_results))
        }
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        item { Stage550TopBar(food.localizedName(), onBack) }
        item {
            val accent = stage550RiskColor(food.riskTier)
            Box(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Brush.linearGradient(listOf(MaterialTheme.colorScheme.surfaceVariant, accent.copy(alpha = 0.25f))))
                    .padding(20.dp)
            ) {
                Column {
                    Stage550RiskPill(food.riskTier)
                    Spacer(Modifier.height(12.dp))
                    Text(food.localizedName(), fontSize = 28.sp, fontWeight = FontWeight.Black)
                    Text(stage550CategoryLabel(food.category), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        item { Stage550DetailSection(stringResource(R.string.normal_signs), food.normalSigns, Fresh) }
        item { Stage550DetailSection(stringResource(R.string.spoilage_signs), food.spoilageSigns, Danger) }
        item { Stage550DetailSection(stringResource(R.string.storage), food.storageTips, ElectricBlue) }
        item {
            OutlinedButton(onClick = onScan, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                Icon(Icons.Outlined.CameraAlt, null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.scan_this_food))
            }
        }
        item { Stage550SafetyBanner() }
    }
}

@Composable
private fun Stage550DetailSection(title: String, bullets: List<String>, accent: Color) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, accent.copy(alpha = 0.25f))
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Text(title, fontWeight = FontWeight.Black, color = accent, style = MaterialTheme.typography.titleMedium)
            bullets.forEach { bullet ->
                Row(verticalAlignment = Alignment.Top) {
                    Text("•", color = accent, fontWeight = FontWeight.Black)
                    Spacer(Modifier.width(8.dp))
                    Text(bullet, modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun Stage550ScannerScreen(modifier: Modifier) {
    val context = LocalContext.current
    val historyStore = remember { ScanHistoryStore(context) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var selectedFoodId by rememberSaveable { mutableStateOf("owaisi_mango") }
    var foodMenu by remember { mutableStateOf(false) }
    var manualCheck by remember { mutableStateOf(ManualSafetyCheck()) }
    var assessment by remember { mutableStateOf<SafetyAssessment?>(null) }

    val camera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { photo ->
        bitmap = photo
        assessment = null
    }
    val gallery = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            val decoded = runCatching {
                context.contentResolver.openInputStream(uri)?.use { stream -> BitmapFactory.decodeStream(stream) }
            }.getOrNull()
            if (decoded != null) {
                bitmap = decoded
                assessment = null
            }
        }
    }

    val report = remember(bitmap) { bitmap?.let(VisualHeuristicEngine::analyze) }
    val selectedFood = FoodCatalog.byId(selectedFoodId)

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        item {
            Text(stringResource(R.string.local_analysis), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
            Text(stringResource(R.string.scanner_privacy), color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item {
            Box {
                Surface(
                    modifier = Modifier.fillMaxWidth().clickable { foodMenu = true },
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Row(Modifier.padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(stringResource(R.string.food_type), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(selectedFood?.localizedName() ?: stringResource(R.string.select_food), fontWeight = FontWeight.Bold)
                        }
                        Icon(Icons.Outlined.ChevronRight, null)
                    }
                }
                DropdownMenu(expanded = foodMenu, onDismissRequest = { foodMenu = false }) {
                    FoodCatalog.items.forEach { food ->
                        DropdownMenuItem(
                            text = { Text(food.localizedName()) },
                            onClick = {
                                selectedFoodId = food.id
                                foodMenu = false
                                assessment = null
                            }
                        )
                    }
                }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = { camera.launch(null) }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp)) {
                    Icon(Icons.Outlined.CameraAlt, null)
                    Spacer(Modifier.width(7.dp))
                    Text(stringResource(R.string.camera))
                }
                OutlinedButton(onClick = { gallery.launch("image/*") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp)) {
                    Icon(Icons.Outlined.PhotoLibrary, null)
                    Spacer(Modifier.width(7.dp))
                    Text(stringResource(R.string.gallery))
                }
            }
        }
        if (bitmap != null) {
            item {
                Image(
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = stringResource(R.string.food_photo),
                    modifier = Modifier.fillMaxWidth().height(240.dp).clip(RoundedCornerShape(22.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
        if (report != null) item { Stage550ImageSignalCard(report) }
        item {
            Text(stringResource(R.string.manual_check), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
            Text(stringResource(R.string.manual_check_subtitle), color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item {
            Stage550ManualCheckCard(check = manualCheck) {
                manualCheck = it
                assessment = null
            }
        }
        item {
            Surface(
                modifier = Modifier.fillMaxWidth().clickable {
                    val result = SafetyRules.evaluate(selectedFood, manualCheck)
                    assessment = result
                    historyStore.append(
                        ScanHistoryStore.createRecord(
                            food = selectedFood,
                            assessment = result,
                            imageNeedsRetake = report?.needsRetake
                        )
                    )
                },
                shape = RoundedCornerShape(18.dp),
                color = Fresh,
                contentColor = Color(0xFF03150D)
            ) {
                Text(
                    stringResource(R.string.evaluate),
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Black,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(Modifier.height(6.dp))
            Text(stringResource(R.string.s550_result_saved_note), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        if (assessment != null) {
            item {
                Stage550AssessmentCard(
                    assessment = assessment!!,
                    onShare = {
                        val text = AssessmentSummary.plainText(selectedFood, assessment!!, report?.needsRetake)
                        stage550ShareText(context, text)
                    }
                )
            }
        }
        item { Stage550SafetyBanner() }
    }
}

@Composable
private fun Stage550ImageSignalCard(report: ImageSignalReport) {
    val accent = if (report.needsRetake) Amber else Fresh
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = accent.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, accent.copy(alpha = 0.35f))
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Text(stringResource(R.string.image_quality_check), fontWeight = FontWeight.Black, color = accent)
            Text("Luma ${report.averageLuminance}/255  •  Saturation ${report.averageSaturation}/255  •  Samples ${report.sampledPixels}", style = MaterialTheme.typography.bodySmall)
            report.observations.forEach { Text("• $it", color = MaterialTheme.colorScheme.onSurfaceVariant) }
            if (report.needsRetake) {
                Text(stringResource(R.string.s550_photo_retake), color = Amber, fontWeight = FontWeight.Bold)
            }
            Text(stringResource(R.string.not_spoilage_classifier), style = MaterialTheme.typography.bodySmall, color = Amber)
        }
    }
}

@Composable
private fun Stage550ManualCheckCard(check: ManualSafetyCheck, onChange: (ManualSafetyCheck) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(Modifier.padding(12.dp)) {
            Stage550CheckRow(stringResource(R.string.check_mold), check.visibleMold) { onChange(check.copy(visibleMold = it)) }
            Stage550CheckRow(stringResource(R.string.check_slime), check.slimeOrStickyFilm) { onChange(check.copy(slimeOrStickyFilm = it)) }
            Stage550CheckRow(stringResource(R.string.check_odor), check.fermentedOrRottenOdor) { onChange(check.copy(fermentedOrRottenOdor = it)) }
            Stage550CheckRow(stringResource(R.string.check_package), check.leakingOrBulgingPackage) { onChange(check.copy(leakingOrBulgingPackage = it)) }
            Stage550CheckRow(stringResource(R.string.check_temperature), check.unsafeTimeTemperatureHistory) { onChange(check.copy(unsafeTimeTemperatureHistory = it)) }
            Stage550CheckRow(stringResource(R.string.check_unknown_storage), check.unknownStorageHistory) { onChange(check.copy(unknownStorageHistory = it)) }
        }
    }
}

@Composable
private fun Stage550CheckRow(label: String, checked: Boolean, onChecked: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onChecked(!checked) }.padding(vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = checked, onCheckedChange = onChecked)
        Spacer(Modifier.width(6.dp))
        Text(label, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun Stage550AssessmentCard(assessment: SafetyAssessment, onShare: () -> Unit) {
    val color = when (assessment.verdict) {
        SafetyVerdict.NO_VISIBLE_RED_FLAGS -> Fresh
        SafetyVerdict.CAUTION, SafetyVerdict.INSUFFICIENT_INFORMATION -> Amber
        SafetyVerdict.DISCARD -> Danger
    }
    val icon = when (assessment.verdict) {
        SafetyVerdict.NO_VISIBLE_RED_FLAGS -> Icons.Outlined.CheckCircle
        SafetyVerdict.CAUTION, SafetyVerdict.INSUFFICIENT_INFORMATION -> Icons.Outlined.WarningAmber
        SafetyVerdict.DISCARD -> Icons.Outlined.ErrorOutline
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = color.copy(alpha = 0.09f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.38f))
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, tint = color)
                Spacer(Modifier.width(8.dp))
                Text(stage550VerdictLabel(assessment.verdict), modifier = Modifier.weight(1f), fontWeight = FontWeight.Black, color = color, style = MaterialTheme.typography.titleMedium)
            }
            Text("${stringResource(R.string.s550_red_flags)}: ${assessment.redFlagCount}  •  ${stringResource(R.string.s550_uncertainty)}: ${assessment.uncertaintyCount}", style = MaterialTheme.typography.bodySmall)
            assessment.reasonCodes.forEach { code ->
                Text("• ${stage550ReasonLabel(code)}", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (assessment.reasonCodes.isEmpty()) {
                assessment.reasons.forEach { Text("• $it", color = MaterialTheme.colorScheme.onSurfaceVariant) }
            }
            Text(stringResource(R.string.s550_saved_to_history), style = MaterialTheme.typography.bodySmall, color = Fresh)
            OutlinedButton(onClick = onShare, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Outlined.Share, null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.s550_share_result))
            }
        }
    }
}

@Composable
private fun Stage550HistoryScreen(modifier: Modifier, onFood: (String) -> Unit) {
    val context = LocalContext.current
    val store = remember { ScanHistoryStore(context) }
    var records by remember { mutableStateOf(store.load()) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(11.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.weight(1f)) {
                    Text(stringResource(R.string.s550_history), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                    Text(stringResource(R.string.s550_history_local_note), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                if (records.isNotEmpty()) {
                    IconButton(onClick = {
                        store.clear()
                        records = emptyList()
                    }) {
                        Icon(Icons.Outlined.DeleteOutline, stringResource(R.string.s550_clear_history))
                    }
                }
            }
        }
        if (records.isEmpty()) {
            item { Stage550EmptyState(stringResource(R.string.s550_history_empty)) }
        } else {
            item { Text(stringResource(R.string.s550_recent_checks), fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleMedium) }
            items(records, key = { it.id }) { record ->
                Stage550HistoryCard(record = record, onClick = {
                    if (FoodCatalog.byId(record.foodId) != null) onFood(record.foodId)
                })
            }
            item {
                TextButton(onClick = {
                    store.clear()
                    records = emptyList()
                }, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.s550_clear_history))
                }
            }
        }
    }
}

@Composable
private fun Stage550HistoryCard(record: ScanRecord, onClick: () -> Unit) {
    val accent = stage550VerdictColor(record.verdict)
    val formatted = remember(record.timestampEpochMs) {
        DateFormat.getMediumDateFormat(LocalContext.current).format(Date(record.timestampEpochMs)) + " • " +
            DateFormat.getTimeFormat(LocalContext.current).format(Date(record.timestampEpochMs))
    }
    Surface(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, accent.copy(alpha = 0.28f))
    ) {
        Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(record.foodNameSnapshot, modifier = Modifier.weight(1f), fontWeight = FontWeight.Black)
                Text(stage550VerdictLabel(record.verdict), color = accent, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
            }
            Text(formatted, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("${stringResource(R.string.s550_red_flags)}: ${record.redFlagCount}  •  ${stringResource(R.string.s550_uncertainty)}: ${record.uncertaintyCount}", style = MaterialTheme.typography.bodySmall)
            if (record.imageNeedsRetake == true) {
                Text(stringResource(R.string.s550_photo_retake), color = Amber, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun Stage550SettingsScreen(
    modifier: Modifier,
    themeMode: Stage550ThemeMode,
    onThemeMode: (Stage550ThemeMode) -> Unit,
    onHistory: () -> Unit,
    onSafety: () -> Unit,
    onAbout: () -> Unit
) {
    val context = LocalContext.current
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        item {
            Text(stringResource(R.string.settings), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
            Text(stringResource(R.string.settings_subtitle), color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item {
            Stage550SettingsCard(stringResource(R.string.appearance), if (themeMode == Stage550ThemeMode.DARK) Icons.Outlined.DarkMode else Icons.Outlined.LightMode) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(Stage550ThemeMode.entries) { mode ->
                        AssistChip(onClick = { onThemeMode(mode) }, label = { Text(stage550ThemeLabel(mode)) })
                    }
                }
            }
        }
        item {
            Stage550SettingsCard(stringResource(R.string.languages), Icons.Outlined.Language) {
                Text(stringResource(R.string.language_help), color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(8.dp))
                OutlinedButton(onClick = { stage550OpenLanguageSettings(context) }) {
                    Text(stringResource(R.string.change_language))
                }
            }
        }
        item {
            Stage550SettingsLink(stringResource(R.string.s550_history), Icons.Outlined.History, onHistory)
        }
        item {
            Stage550SettingsLink(stringResource(R.string.s550_open_safety_guide), Icons.Outlined.HealthAndSafety, onSafety)
        }
        item {
            Stage550SettingsCard(stringResource(R.string.privacy), Icons.Outlined.HealthAndSafety) {
                Text(stringResource(R.string.privacy_local), color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(6.dp))
                Text(stringResource(R.string.s550_history_local_note), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        item {
            Stage550SettingsLink(stringResource(R.string.about), Icons.Outlined.Info, onAbout)
        }
    }
}

@Composable
private fun Stage550SettingsCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, tint = Gold)
                Spacer(Modifier.width(9.dp))
                Text(title, fontWeight = FontWeight.Black)
            }
            Spacer(Modifier.height(11.dp))
            content()
        }
    }
}

@Composable
private fun Stage550SettingsLink(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = Gold)
            Spacer(Modifier.width(10.dp))
            Text(title, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            Icon(Icons.Outlined.ChevronRight, null)
        }
    }
}

@Composable
private fun Stage550SafetyScreen(modifier: Modifier, onBack: () -> Unit) {
    val guides = listOf(
        stringResource(R.string.safety_cans_title) to stringResource(R.string.safety_cans_body),
        stringResource(R.string.safety_meat_title) to stringResource(R.string.safety_meat_body),
        stringResource(R.string.safety_bread_title) to stringResource(R.string.safety_bread_body),
        stringResource(R.string.safety_leftovers_title) to stringResource(R.string.safety_leftovers_body),
        stringResource(R.string.safety_fish_title) to stringResource(R.string.safety_fish_body)
    )
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Stage550TopBar(stringResource(R.string.safety), onBack) }
        item { Text(stringResource(R.string.safety_intro), color = MaterialTheme.colorScheme.onSurfaceVariant) }
        items(guides) { (title, body) ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, Gold.copy(alpha = 0.22f))
            ) {
                Column(Modifier.padding(17.dp)) {
                    Text(title, fontWeight = FontWeight.Black, color = Gold)
                    Spacer(Modifier.height(6.dp))
                    Text(body, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        item { Stage550SafetyBanner() }
    }
}

@Composable
private fun Stage550AboutScreen(modifier: Modifier, onBack: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    val links = listOf(
        "Website" to "https://nexvary.com/",
        "Facebook" to "https://www.facebook.com/share/14p9krEn5ij/",
        "Email" to "mailto:info@nexvary.com",
        "YouTube" to "https://www.youtube.com/@NexvaryInc",
        "X" to "https://x.com/Nexvary"
    )
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Stage550TopBar(stringResource(R.string.about), onBack) }
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, Gold.copy(alpha = 0.35f))
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text("NEXVARY FoodGuard", fontSize = 27.sp, fontWeight = FontWeight.Black)
                    Spacer(Modifier.height(6.dp))
                    Text(stringResource(R.string.about_description), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(10.dp))
                    Text("v0.5 • ${stringResource(R.string.s550_stage)}", color = Gold, fontWeight = FontWeight.SemiBold)
                }
            }
        }
        items(links) { (name, url) ->
            Surface(
                modifier = Modifier.fillMaxWidth().clickable { uriHandler.openUri(url) },
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(name, modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                    Icon(Icons.Outlined.ChevronRight, null)
                }
            }
        }
    }
}

@Composable
private fun Stage550SafetyBanner() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Amber.copy(alpha = 0.09f),
        border = BorderStroke(1.dp, Amber.copy(alpha = 0.30f))
    ) {
        Row(Modifier.padding(15.dp), verticalAlignment = Alignment.Top) {
            Icon(Icons.Outlined.WarningAmber, null, tint = Amber)
            Spacer(Modifier.width(10.dp))
            Text(stringResource(R.string.safety_note), modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun Stage550RiskPill(risk: RiskTier) {
    val color = stage550RiskColor(risk)
    Surface(shape = RoundedCornerShape(50), color = color.copy(alpha = 0.12f), border = BorderStroke(1.dp, color.copy(alpha = 0.32f))) {
        Text(stage550RiskLabel(risk), modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp), color = color, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
private fun Stage550TopBar(title: String, onBack: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, stringResource(R.string.back)) }
        Spacer(Modifier.width(5.dp))
        Text(title, modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, maxLines = 2, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun Stage550EmptyState(message: String) {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.surface) {
        Text(message, modifier = Modifier.padding(28.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun stage550CategoryLabel(category: FoodCategory): String = when (category) {
    FoodCategory.FRUIT -> stringResource(R.string.fruits)
    FoodCategory.VEGETABLE -> stringResource(R.string.vegetables)
    FoodCategory.MEAT -> stringResource(R.string.meat)
    FoodCategory.POULTRY -> stringResource(R.string.poultry)
    FoodCategory.SEAFOOD -> stringResource(R.string.fish)
    FoodCategory.DAIRY -> stringResource(R.string.dairy)
    FoodCategory.BAKERY -> stringResource(R.string.bakery)
    FoodCategory.PREPARED -> stringResource(R.string.prepared)
    FoodCategory.DRINK -> stringResource(R.string.drinks)
    FoodCategory.PACKAGED -> stringResource(R.string.canned)
}

@Composable
private fun stage550RiskLabel(risk: RiskTier): String = when (risk) {
    RiskTier.LOW -> stringResource(R.string.risk_low)
    RiskTier.MEDIUM -> stringResource(R.string.risk_medium)
    RiskTier.HIGH -> stringResource(R.string.risk_high)
}

@Composable
private fun stage550VerdictLabel(verdict: SafetyVerdict): String = when (verdict) {
    SafetyVerdict.NO_VISIBLE_RED_FLAGS -> stringResource(R.string.no_red_flags)
    SafetyVerdict.CAUTION -> stringResource(R.string.caution)
    SafetyVerdict.DISCARD -> stringResource(R.string.danger)
    SafetyVerdict.INSUFFICIENT_INFORMATION -> stringResource(R.string.insufficient_info)
}

@Composable
private fun stage550ReasonLabel(code: SafetyReasonCode): String = when (code) {
    SafetyReasonCode.PACKAGE_COMPROMISED -> stringResource(R.string.check_package)
    SafetyReasonCode.VISIBLE_MOLD -> stringResource(R.string.check_mold)
    SafetyReasonCode.SLIME_OR_STICKY_FILM -> stringResource(R.string.check_slime)
    SafetyReasonCode.ABNORMAL_ODOR -> stringResource(R.string.check_odor)
    SafetyReasonCode.UNSAFE_TIME_TEMPERATURE -> stringResource(R.string.check_temperature)
    SafetyReasonCode.UNKNOWN_STORAGE_HISTORY -> stringResource(R.string.check_unknown_storage)
    SafetyReasonCode.NO_REPORTED_RED_FLAGS -> stringResource(R.string.no_red_flags)
    SafetyReasonCode.NOT_PROOF_OF_MICROBIOLOGICAL_SAFETY -> stringResource(R.string.safety_note)
}

@Composable
private fun stage550ThemeLabel(mode: Stage550ThemeMode): String = when (mode) {
    Stage550ThemeMode.SYSTEM -> stringResource(R.string.system_theme)
    Stage550ThemeMode.LIGHT -> stringResource(R.string.light_theme)
    Stage550ThemeMode.DARK -> stringResource(R.string.dark_theme)
}

private fun stage550RiskColor(risk: RiskTier): Color = when (risk) {
    RiskTier.LOW -> Fresh
    RiskTier.MEDIUM -> Amber
    RiskTier.HIGH -> Danger
}

private fun stage550VerdictColor(verdict: SafetyVerdict): Color = when (verdict) {
    SafetyVerdict.NO_VISIBLE_RED_FLAGS -> Fresh
    SafetyVerdict.CAUTION, SafetyVerdict.INSUFFICIENT_INFORMATION -> Amber
    SafetyVerdict.DISCARD -> Danger
}

private fun stage550OpenLanguageSettings(context: Context) {
    val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Intent(Settings.ACTION_APP_LOCALE_SETTINGS, Uri.parse("package:${context.packageName}"))
    } else {
        Intent(Settings.ACTION_LOCALE_SETTINGS)
    }
    runCatching { context.startActivity(intent) }
}

private fun stage550ShareText(context: Context, text: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    runCatching { context.startActivity(Intent.createChooser(intent, null)) }
}
