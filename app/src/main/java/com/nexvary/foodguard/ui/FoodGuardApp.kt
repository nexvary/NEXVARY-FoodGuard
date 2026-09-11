package com.nexvary.foodguard.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.Settings
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
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
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
import com.nexvary.foodguard.domain.SafetyRules
import com.nexvary.foodguard.model.FoodCategory
import com.nexvary.foodguard.model.FoodItem
import com.nexvary.foodguard.model.ManualSafetyCheck
import com.nexvary.foodguard.model.RiskTier
import com.nexvary.foodguard.model.SafetyAssessment
import com.nexvary.foodguard.model.SafetyVerdict
import com.nexvary.foodguard.ui.theme.Amber
import com.nexvary.foodguard.ui.theme.Danger
import com.nexvary.foodguard.ui.theme.ElectricBlue
import com.nexvary.foodguard.ui.theme.FoodGuardTheme
import com.nexvary.foodguard.ui.theme.Fresh
import com.nexvary.foodguard.ui.theme.Gold
import java.util.Locale

private sealed interface Screen {
    data object Home : Screen
    data object Catalog : Screen
    data object Scanner : Screen
    data object Safety : Screen
    data object Settings : Screen
    data object About : Screen
    data class Detail(val foodId: String) : Screen
}

private enum class ThemeMode { SYSTEM, LIGHT, DARK }

@Composable
fun FoodGuardApp() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("foodguard_settings", Context.MODE_PRIVATE) }
    var themeModeName by rememberSaveable {
        mutableStateOf(prefs.getString("theme_mode", ThemeMode.SYSTEM.name) ?: ThemeMode.SYSTEM.name)
    }
    val themeMode = runCatching { ThemeMode.valueOf(themeModeName) }.getOrDefault(ThemeMode.SYSTEM)
    val darkTheme = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    FoodGuardTheme(darkTheme = darkTheme) {
        FoodGuardRoot(
            themeMode = themeMode,
            onThemeMode = { newMode ->
                themeModeName = newMode.name
                prefs.edit().putString("theme_mode", newMode.name).apply()
            }
        )
    }
}

@Composable
private fun FoodGuardRoot(themeMode: ThemeMode, onThemeMode: (ThemeMode) -> Unit) {
    var screen by rememberSaveable { mutableStateOf<Screen>(Screen.Home) }
    var previousScreen by rememberSaveable { mutableStateOf<Screen>(Screen.Home) }

    fun navigate(target: Screen) {
        if (target is Screen.Detail || target is Screen.About) previousScreen = screen
        screen = target
    }

    val isTopLevel = screen is Screen.Home || screen is Screen.Catalog || screen is Screen.Scanner || screen is Screen.Safety || screen is Screen.Settings
    BackHandler(enabled = !isTopLevel || screen !is Screen.Home) {
        screen = if (!isTopLevel) previousScreen else Screen.Home
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (isTopLevel) {
                FoodGuardBottomBar(screen = screen, onNavigate = { screen = it })
            }
        }
    ) { padding ->
        when (val current = screen) {
            Screen.Home -> HomeScreen(
                modifier = Modifier.padding(padding),
                onScan = { navigate(Screen.Scanner) },
                onCatalog = { navigate(Screen.Catalog) },
                onFood = { navigate(Screen.Detail(it)) }
            )
            Screen.Catalog -> CatalogScreen(
                modifier = Modifier.padding(padding),
                onFood = { navigate(Screen.Detail(it)) }
            )
            Screen.Scanner -> ScannerScreen(modifier = Modifier.padding(padding))
            Screen.Safety -> SafetyGuideScreen(modifier = Modifier.padding(padding))
            Screen.Settings -> SettingsScreen(
                modifier = Modifier.padding(padding),
                themeMode = themeMode,
                onThemeMode = onThemeMode,
                onAbout = { navigate(Screen.About) }
            )
            Screen.About -> AboutScreen(
                modifier = Modifier.padding(padding),
                onBack = { screen = previousScreen }
            )
            is Screen.Detail -> FoodDetailScreen(
                modifier = Modifier.padding(padding),
                food = FoodCatalog.byId(current.foodId),
                onBack = { screen = previousScreen },
                onScan = { screen = Screen.Scanner }
            )
        }
    }
}

@Composable
private fun FoodGuardBottomBar(screen: Screen, onNavigate: (Screen) -> Unit) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        val items = listOf(
            Triple(Screen.Home, stringResource(R.string.home), Icons.Outlined.Home),
            Triple(Screen.Catalog, stringResource(R.string.guide), Icons.Outlined.Search),
            Triple(Screen.Scanner, stringResource(R.string.scan_food), Icons.Outlined.CameraAlt),
            Triple(Screen.Safety, stringResource(R.string.safety), Icons.Outlined.HealthAndSafety),
            Triple(Screen.Settings, stringResource(R.string.settings), Icons.Outlined.Settings)
        )
        items.forEach { (target, label, icon) ->
            NavigationBarItem(
                selected = screen::class == target::class,
                onClick = { onNavigate(target) },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label, maxLines = 1, overflow = TextOverflow.Ellipsis) }
            )
        }
    }
}

@Composable
private fun HomeScreen(
    modifier: Modifier,
    onScan: () -> Unit,
    onCatalog: () -> Unit,
    onFood: (String) -> Unit
) {
    val featured = FoodCatalog.items.first { it.featured }
    val categoryCounts = FoodCatalog.items.groupingBy { it.category }.eachCount()
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item { BrandHeader() }
        item { PremiumScanHero(onScan) }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                MetricCard(Modifier.weight(1f), "${FoodCatalog.items.size}", stringResource(R.string.seed_foods), Fresh)
                MetricCard(Modifier.weight(1f), "7", stringResource(R.string.languages), Gold)
                MetricCard(Modifier.weight(1f), "100%", stringResource(R.string.local_first), ElectricBlue)
            }
        }
        item {
            SectionHeader(stringResource(R.string.categories), onClick = onCatalog)
            Spacer(Modifier.height(10.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                FoodCategory.entries.chunked(2).forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        row.forEach { category ->
                            CategorySummaryCard(
                                modifier = Modifier.weight(1f),
                                category = category,
                                count = categoryCounts[category] ?: 0,
                                onClick = onCatalog
                            )
                        }
                        if (row.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
        item {
            SectionHeader(stringResource(R.string.featured))
            Spacer(Modifier.height(10.dp))
            FoodCard(food = featured, onClick = { onFood(featured.id) })
        }
        item { SafetyBanner() }
    }
}

@Composable
private fun BrandHeader() {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Surface(
            modifier = Modifier.size(56.dp),
            shape = RoundedCornerShape(18.dp),
            color = Fresh.copy(alpha = 0.12f),
            border = BorderStroke(1.dp, Gold.copy(alpha = 0.55f))
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Outlined.HealthAndSafety, null, tint = Fresh, modifier = Modifier.size(30.dp))
            }
        }
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(stringResource(R.string.app_name), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
            Text(stringResource(R.string.tagline), color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun PremiumScanHero(onClick: () -> Unit) {
    val shape = RoundedCornerShape(28.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
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
                Spacer(Modifier.height(4.dp))
                Text(stringResource(R.string.scan_subtitle), color = Color.White.copy(alpha = 0.80f))
                Spacer(Modifier.height(8.dp))
                Text(stringResource(R.string.no_upload_required), color = Gold, fontWeight = FontWeight.SemiBold)
            }
            Icon(Icons.Outlined.ChevronRight, null, tint = Color.White)
        }
    }
}

@Composable
private fun MetricCard(modifier: Modifier, value: String, label: String, accent: Color) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, accent.copy(alpha = 0.28f))
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(value, fontWeight = FontWeight.Black, fontSize = 20.sp, color = accent)
            Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2)
        }
    }
}

@Composable
private fun SectionHeader(title: String, onClick: (() -> Unit)? = null) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(title, modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
        if (onClick != null) TextButton(onClick = onClick) { Text(stringResource(R.string.view_all)) }
    }
}

@Composable
private fun CategorySummaryCard(modifier: Modifier, category: FoodCategory, count: Int, onClick: () -> Unit) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(categoryLabel(category), fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(3.dp))
            Text("$count ${stringResource(R.string.items)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun CatalogScreen(modifier: Modifier, onFood: (String) -> Unit) {
    var query by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf<FoodCategory?>(null) }
    val searched = remember(query) { FoodCatalog.search(query) }
    val filtered = remember(searched, category) { category?.let { c -> searched.filter { it.category == c } } ?: searched }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
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
                item {
                    AssistChip(onClick = { category = null }, label = { Text(stringResource(R.string.all)) })
                }
                items(FoodCategory.entries) { c ->
                    AssistChip(onClick = { category = c }, label = { Text(categoryLabel(c)) })
                }
            }
        }
        item {
            Text("${filtered.size} ${stringResource(R.string.items)}", color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.SemiBold)
        }
        items(filtered, key = { it.id }) { food ->
            FoodCard(food = food, onClick = { onFood(food.id) })
        }
        if (filtered.isEmpty()) {
            item {
                EmptyState(stringResource(R.string.no_results))
            }
        }
    }
}

@Composable
private fun FoodCard(food: FoodItem, onClick: () -> Unit) {
    val riskColor = riskColor(food.riskTier)
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, riskColor.copy(alpha = 0.22f)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = RoundedCornerShape(14.dp), color = riskColor.copy(alpha = 0.12f)) {
                Box(Modifier.size(48.dp), contentAlignment = Alignment.Center) {
                    Text(food.localizedName().take(1).uppercase(Locale.getDefault()), fontWeight = FontWeight.Black, color = riskColor)
                }
            }
            Spacer(Modifier.width(13.dp))
            Column(Modifier.weight(1f)) {
                Text(food.localizedName(), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(3.dp))
                Text(categoryLabel(food.category), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            RiskPill(food.riskTier)
            Spacer(Modifier.width(6.dp))
            Icon(Icons.Outlined.ChevronRight, null)
        }
    }
}

@Composable
private fun FoodDetailScreen(modifier: Modifier, food: FoodItem?, onBack: () -> Unit, onScan: () -> Unit) {
    if (food == null) {
        Column(modifier.fillMaxSize().padding(20.dp)) {
            SimpleTopBar(stringResource(R.string.food_guide), onBack)
            EmptyState(stringResource(R.string.no_results))
        }
        return
    }
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { SimpleTopBar(food.localizedName(), onBack) }
        item {
            DetailHero(food)
        }
        item { DetailSection(stringResource(R.string.normal_signs), food.normalSigns, Fresh) }
        item { DetailSection(stringResource(R.string.spoilage_signs), food.spoilageSigns, Danger) }
        item { DetailSection(stringResource(R.string.storage), food.storageTips, ElectricBlue) }
        item {
            OutlinedButton(onClick = onScan, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                Icon(Icons.Outlined.CameraAlt, null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.scan_this_food))
            }
        }
        item { SafetyBanner() }
    }
}

@Composable
private fun DetailHero(food: FoodItem) {
    val color = riskColor(food.riskTier)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .background(Brush.linearGradient(listOf(MaterialTheme.colorScheme.surfaceVariant, color.copy(alpha = 0.26f))))
            .padding(22.dp)
    ) {
        Column {
            RiskPill(food.riskTier)
            Spacer(Modifier.height(18.dp))
            Text(food.localizedName(), fontSize = 30.sp, fontWeight = FontWeight.Black)
            Text(categoryLabel(food.category), color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (food.id == "owaisi_mango") {
                Spacer(Modifier.height(12.dp))
                Text(stringResource(R.string.owaisi_stage200_note), color = Gold, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun DetailSection(title: String, bullets: List<String>, accent: Color) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, accent.copy(alpha = 0.24f))
    ) {
        Column(Modifier.padding(17.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
private fun ScannerScreen(modifier: Modifier) {
    val context = LocalContext.current
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
        verticalArrangement = Arrangement.spacedBy(14.dp)
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
        if (report != null) item { ImageSignalCard(report) }
        item {
            Text(stringResource(R.string.manual_check), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
            Text(stringResource(R.string.manual_check_subtitle), color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item {
            ManualCheckCard(
                check = manualCheck,
                onChange = {
                    manualCheck = it
                    assessment = null
                }
            )
        }
        item {
            Surface(
                modifier = Modifier.fillMaxWidth().clickable { assessment = SafetyRules.evaluate(selectedFood, manualCheck) },
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
        }
        if (assessment != null) item { AssessmentCard(assessment!!) }
        item { SafetyBanner() }
    }
}

@Composable
private fun ImageSignalCard(report: ImageSignalReport) {
    val accent = if (report.needsRetake) Amber else Fresh
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = accent.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, accent.copy(alpha = 0.35f))
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(stringResource(R.string.image_quality_check), fontWeight = FontWeight.Black, color = accent)
            Text("Luma ${report.averageLuminance}/255  •  Saturation ${report.averageSaturation}/255  •  Samples ${report.sampledPixels}", style = MaterialTheme.typography.bodySmall)
            report.observations.forEach { Text("• $it", color = MaterialTheme.colorScheme.onSurfaceVariant) }
            Text(stringResource(R.string.not_spoilage_classifier), style = MaterialTheme.typography.bodySmall, color = Amber)
        }
    }
}

@Composable
private fun ManualCheckCard(check: ManualSafetyCheck, onChange: (ManualSafetyCheck) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(Modifier.padding(12.dp)) {
            CheckRow(stringResource(R.string.check_mold), check.visibleMold) { onChange(check.copy(visibleMold = it)) }
            CheckRow(stringResource(R.string.check_slime), check.slimeOrStickyFilm) { onChange(check.copy(slimeOrStickyFilm = it)) }
            CheckRow(stringResource(R.string.check_odor), check.fermentedOrRottenOdor) { onChange(check.copy(fermentedOrRottenOdor = it)) }
            CheckRow(stringResource(R.string.check_package), check.leakingOrBulgingPackage) { onChange(check.copy(leakingOrBulgingPackage = it)) }
            CheckRow(stringResource(R.string.check_temperature), check.unsafeTimeTemperatureHistory) { onChange(check.copy(unsafeTimeTemperatureHistory = it)) }
            CheckRow(stringResource(R.string.check_unknown_storage), check.unknownStorageHistory) { onChange(check.copy(unknownStorageHistory = it)) }
        }
    }
}

@Composable
private fun CheckRow(label: String, checked: Boolean, onChecked: (Boolean) -> Unit) {
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
private fun AssessmentCard(assessment: SafetyAssessment) {
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
    val title = when (assessment.verdict) {
        SafetyVerdict.NO_VISIBLE_RED_FLAGS -> stringResource(R.string.no_red_flags)
        SafetyVerdict.CAUTION -> stringResource(R.string.caution)
        SafetyVerdict.DISCARD -> stringResource(R.string.danger)
        SafetyVerdict.INSUFFICIENT_INFORMATION -> stringResource(R.string.insufficient_info)
    }
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = color.copy(alpha = 0.09f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.38f))
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, tint = color)
                Spacer(Modifier.width(8.dp))
                Text(title, fontWeight = FontWeight.Black, color = color, style = MaterialTheme.typography.titleMedium)
            }
            assessment.reasons.forEach { Text("• $it", color = MaterialTheme.colorScheme.onSurfaceVariant) }
        }
    }
}

@Composable
private fun SafetyGuideScreen(modifier: Modifier) {
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
        item {
            Text(stringResource(R.string.safety), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
            Text(stringResource(R.string.safety_intro), color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
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
        item { SafetyBanner() }
    }
}

@Composable
private fun SettingsScreen(modifier: Modifier, themeMode: ThemeMode, onThemeMode: (ThemeMode) -> Unit, onAbout: () -> Unit) {
    val context = LocalContext.current
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(stringResource(R.string.settings), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
            Text(stringResource(R.string.settings_subtitle), color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item {
            SettingsCard(title = stringResource(R.string.appearance), icon = if (themeMode == ThemeMode.DARK) Icons.Outlined.DarkMode else Icons.Outlined.LightMode) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ThemeMode.entries.forEach { mode ->
                        AssistChip(
                            onClick = { onThemeMode(mode) },
                            label = { Text(themeModeLabel(mode)) }
                        )
                    }
                }
            }
        }
        item {
            SettingsCard(title = stringResource(R.string.languages), icon = Icons.Outlined.Language) {
                Text(stringResource(R.string.language_help), color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(8.dp))
                OutlinedButton(onClick = { openLanguageSettings(context) }) {
                    Text(stringResource(R.string.change_language))
                }
            }
        }
        item {
            SettingsCard(title = stringResource(R.string.privacy), icon = Icons.Outlined.HealthAndSafety) {
                Text(stringResource(R.string.privacy_local), color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        item {
            Surface(
                modifier = Modifier.fillMaxWidth().clickable(onClick = onAbout),
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Info, null)
                    Spacer(Modifier.width(12.dp))
                    Text(stringResource(R.string.about), modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                    Icon(Icons.Outlined.ChevronRight, null)
                }
            }
        }
    }
}

@Composable
private fun SettingsCard(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, content: @Composable () -> Unit) {
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
            Spacer(Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun AboutScreen(modifier: Modifier, onBack: () -> Unit) {
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
        item { SimpleTopBar(stringResource(R.string.about), onBack) }
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
                    Text("v0.2 • Stage 200 foundation", color = Gold, fontWeight = FontWeight.SemiBold)
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
private fun SafetyBanner() {
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
private fun RiskPill(risk: RiskTier) {
    val color = riskColor(risk)
    val label = when (risk) {
        RiskTier.LOW -> stringResource(R.string.risk_low)
        RiskTier.MEDIUM -> stringResource(R.string.risk_medium)
        RiskTier.HIGH -> stringResource(R.string.risk_high)
    }
    Surface(shape = RoundedCornerShape(50), color = color.copy(alpha = 0.12f), border = BorderStroke(1.dp, color.copy(alpha = 0.32f))) {
        Text(label, modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp), color = color, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
    }
}

private fun riskColor(risk: RiskTier): Color = when (risk) {
    RiskTier.LOW -> Fresh
    RiskTier.MEDIUM -> Amber
    RiskTier.HIGH -> Danger
}

@Composable
private fun SimpleTopBar(title: String, onBack: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, stringResource(R.string.back)) }
        Spacer(Modifier.width(5.dp))
        Text(title, modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, maxLines = 2, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun EmptyState(message: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Text(message, modifier = Modifier.padding(30.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun categoryLabel(category: FoodCategory): String = when (category) {
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
private fun themeModeLabel(mode: ThemeMode): String = when (mode) {
    ThemeMode.SYSTEM -> stringResource(R.string.system_theme)
    ThemeMode.LIGHT -> stringResource(R.string.light_theme)
    ThemeMode.DARK -> stringResource(R.string.dark_theme)
}

private fun openLanguageSettings(context: Context) {
    val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Intent(Settings.ACTION_APP_LOCALE_SETTINGS, Uri.parse("package:${context.packageName}"))
    } else {
        Intent(Settings.ACTION_LOCALE_SETTINGS)
    }
    runCatching { context.startActivity(intent) }
}
