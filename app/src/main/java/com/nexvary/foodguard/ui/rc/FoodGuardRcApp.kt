package com.nexvary.foodguard.ui.rc

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
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ChevronRight
import androidx.compose.material.icons.outlined.BakeryDining
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.CenterFocusStrong
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.EggAlt
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.LocalCafe
import androidx.compose.material.icons.outlined.LocalDrink
import androidx.compose.material.icons.outlined.LunchDining
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SetMeal
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Spa
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
import androidx.compose.material3.NavigationBarItemDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
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
import com.nexvary.foodguard.model.FoodReferenceImage
import com.nexvary.foodguard.model.FoodReferenceState
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
import com.nexvary.foodguard.ui.theme.ElectricCyan
import com.nexvary.foodguard.ui.theme.ElectricViolet
import com.nexvary.foodguard.ui.theme.FoodGuardTheme
import com.nexvary.foodguard.ui.theme.Fresh
import com.nexvary.foodguard.ui.theme.GlowSilver
import com.nexvary.foodguard.ui.theme.RoyalGold
import java.util.Date
import java.util.Locale

private enum class Screen { HOME, CATALOG, SCANNER, HISTORY, SETTINGS, SAFETY, ABOUT, DETAIL }
private enum class ThemeMode { SYSTEM, LIGHT, DARK }

@Composable
fun FoodGuardRcApp() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("foodguard_settings", Context.MODE_PRIVATE) }
    var themeName by rememberSaveable { mutableStateOf(prefs.getString("theme_mode", ThemeMode.SYSTEM.name) ?: ThemeMode.SYSTEM.name) }
    val mode = runCatching { ThemeMode.valueOf(themeName) }.getOrDefault(ThemeMode.SYSTEM)
    val dark = when (mode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }
    FoodGuardTheme(darkTheme = dark) {
        Root(mode) {
            themeName = it.name
            prefs.edit().putString("theme_mode", it.name).apply()
        }
    }
}

@Composable
private fun Root(themeMode: ThemeMode, onThemeMode: (ThemeMode) -> Unit) {
    var screen by rememberSaveable { mutableStateOf(Screen.HOME) }
    var selectedFoodId by rememberSaveable { mutableStateOf("owaisi_mango") }
    val topLevel = screen in listOf(Screen.HOME, Screen.CATALOG, Screen.SCANNER, Screen.HISTORY, Screen.SETTINGS)

    fun openFood(id: String) {
        selectedFoodId = id
        screen = Screen.DETAIL
    }

    BackHandler(enabled = screen != Screen.HOME) {
        screen = when (screen) {
            Screen.DETAIL -> Screen.CATALOG
            Screen.SAFETY, Screen.ABOUT -> Screen.SETTINGS
            else -> Screen.HOME
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = { if (topLevel) BottomBar(screen) { screen = it } }
    ) { padding ->
        when (screen) {
            Screen.HOME -> HomeScreen(Modifier.padding(padding), { screen = Screen.SCANNER }, { screen = Screen.CATALOG }, { screen = Screen.HISTORY }, { screen = Screen.SAFETY }, ::openFood)
            Screen.CATALOG -> CatalogScreen(Modifier.padding(padding), ::openFood)
            Screen.SCANNER -> ScannerScreen(Modifier.padding(padding))
            Screen.HISTORY -> HistoryScreen(Modifier.padding(padding), ::openFood)
            Screen.SETTINGS -> SettingsScreen(Modifier.padding(padding), themeMode, onThemeMode, { screen = Screen.HISTORY }, { screen = Screen.SAFETY }, { screen = Screen.ABOUT })
            Screen.SAFETY -> SafetyScreen(Modifier.padding(padding)) { screen = Screen.SETTINGS }
            Screen.ABOUT -> AboutScreen(Modifier.padding(padding)) { screen = Screen.SETTINGS }
            Screen.DETAIL -> DetailScreen(Modifier.padding(padding), FoodCatalog.byId(selectedFoodId), { screen = Screen.CATALOG }, { screen = Screen.SCANNER })
        }
    }
}

@Composable
private fun BottomBar(screen: Screen, onNavigate: (Screen) -> Unit) {
    val entries = listOf(
        Triple(Screen.HOME, stringResource(R.string.home), Icons.Outlined.Home),
        Triple(Screen.CATALOG, stringResource(R.string.guide), Icons.Outlined.MenuBook),
        Triple(Screen.SCANNER, stringResource(R.string.scan_food), Icons.Outlined.CenterFocusStrong),
        Triple(Screen.HISTORY, stringResource(R.string.s550_history), Icons.Outlined.History),
        Triple(Screen.SETTINGS, stringResource(R.string.settings), Icons.Outlined.Settings)
    )
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.98f)) {
        entries.forEach { (target, label, icon) ->
            val selected = screen == target
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(target) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = RoyalGold,
                    selectedTextColor = RoyalGold,
                    indicatorColor = ElectricCyan.copy(alpha = 0.16f),
                    unselectedIconColor = GlowSilver.copy(alpha = 0.76f),
                    unselectedTextColor = GlowSilver.copy(alpha = 0.76f)
                ),
                icon = { PremiumIconBadge(icon, selected, label) },
                label = { Text(label, maxLines = 1, overflow = TextOverflow.Ellipsis, fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium) }
            )
        }
    }
}

@Composable
private fun PremiumIconBadge(icon: ImageVector, selected: Boolean, description: String?) {
    val shape = RoundedCornerShape(12.dp)
    val brush = if (selected) Brush.linearGradient(listOf(RoyalGold.copy(alpha = 0.26f), ElectricCyan.copy(alpha = 0.18f))) else Brush.linearGradient(listOf(GlowSilver.copy(alpha = 0.10f), ElectricBlue.copy(alpha = 0.06f)))
    Box(
        Modifier
            .size(40.dp)
            .shadow(if (selected) 8.dp else 2.dp, shape)
            .clip(shape)
            .background(brush)
            .border(1.dp, if (selected) RoyalGold.copy(alpha = 0.78f) else GlowSilver.copy(alpha = 0.28f), shape),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, description, modifier = Modifier.size(23.dp), tint = if (selected) RoyalGold else GlowSilver.copy(alpha = 0.88f))
    }
}

@Composable
private fun HomeScreen(modifier: Modifier, onScan: () -> Unit, onCatalog: () -> Unit, onHistory: () -> Unit, onSafety: () -> Unit, onFood: (String) -> Unit) {
    val featured = FoodCatalog.items.firstOrNull { it.featured } ?: FoodCatalog.items.first()
    val higherRisk = FoodCatalog.items.filter { it.riskTier == RiskTier.HIGH }.take(4)
    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(15.dp)) {
        item { BrandHeader() }
        item { ScanHero(onScan) }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                Metric(Modifier.weight(1f), FoodCatalog.items.size.toString(), stringResource(R.string.seed_foods), ElectricCyan)
                Metric(Modifier.weight(1f), "RC", stringResource(R.string.s550_stage), RoyalGold)
                Metric(Modifier.weight(1f), "100%", stringResource(R.string.local_first), Fresh)
            }
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                items(FoodCategory.entries.toList()) { category -> CategoryChip(category) { onCatalog() } }
            }
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                OutlinedButton(onClick = onCatalog, modifier = Modifier.weight(1f)) { Icon(Icons.Outlined.MenuBook, null, tint = ElectricCyan); Spacer(Modifier.width(6.dp)); Text(stringResource(R.string.guide)) }
                OutlinedButton(onClick = onHistory, modifier = Modifier.weight(1f)) { Icon(Icons.Outlined.History, null, tint = GlowSilver); Spacer(Modifier.width(6.dp)); Text(stringResource(R.string.s550_history)) }
            }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = onSafety, modifier = Modifier.fillMaxWidth()) { Icon(Icons.Outlined.HealthAndSafety, null, tint = RoyalGold); Spacer(Modifier.width(7.dp)); Text(stringResource(R.string.s550_open_safety_guide)) }
        }
        item { SectionTitle(stringResource(R.string.s550_higher_risk)) }
        items(higherRisk, key = { it.id }) { FoodCard(it) { onFood(it.id) } }
        item { SectionTitle(stringResource(R.string.featured)) }
        item { FoodCard(featured) { onFood(featured.id) } }
        item { SafetyBanner() }
    }
}

@Composable
private fun BrandHeader() {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        val shape = RoundedCornerShape(18.dp)
        Box(
            Modifier.size(60.dp).shadow(8.dp, shape).clip(shape)
                .background(Brush.linearGradient(listOf(RoyalGold.copy(alpha = 0.30f), ElectricCyan.copy(alpha = 0.18f))))
                .border(1.dp, RoyalGold.copy(alpha = 0.72f), shape),
            contentAlignment = Alignment.Center
        ) { Icon(Icons.Outlined.HealthAndSafety, null, tint = RoyalGold, modifier = Modifier.size(34.dp)) }
        Spacer(Modifier.width(13.dp))
        Column(Modifier.weight(1f)) {
            Text(stringResource(R.string.app_name), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
            Text(stringResource(R.string.tagline), color = GlowSilver.copy(alpha = 0.82f), maxLines = 2)
        }
        Surface(shape = RoundedCornerShape(50), color = RoyalGold.copy(alpha = 0.12f), border = BorderStroke(1.dp, RoyalGold.copy(alpha = 0.35f))) {
            Text("RC", Modifier.padding(horizontal = 11.dp, vertical = 5.dp), color = RoyalGold, fontWeight = FontWeight.Black)
        }
    }
}

@Composable
private fun ScanHero(onClick: () -> Unit) {
    val shape = RoundedCornerShape(28.dp)
    Box(
        Modifier.fillMaxWidth().shadow(10.dp, shape).clip(shape)
            .background(Brush.linearGradient(listOf(Color(0xFF081A26), Color(0xFF0E2E37), Color(0xFF2D2410))))
            .border(1.dp, RoyalGold.copy(alpha = 0.45f), shape)
            .clickable(onClick = onClick).padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(66.dp).clip(CircleShape)
                    .background(Brush.linearGradient(listOf(ElectricCyan, ElectricBlue, RoyalGold)))
                    .border(1.dp, GlowSilver.copy(alpha = 0.78f), CircleShape),
                contentAlignment = Alignment.Center
            ) { Icon(Icons.Outlined.CameraAlt, null, tint = Color(0xFF031018), modifier = Modifier.size(34.dp)) }
            Spacer(Modifier.width(15.dp))
            Column(Modifier.weight(1f)) {
                Text(stringResource(R.string.scan_food), fontSize = 23.sp, fontWeight = FontWeight.Black, color = Color.White)
                Text(stringResource(R.string.scan_subtitle), color = Color.White.copy(alpha = 0.84f), maxLines = 2)
                Text(stringResource(R.string.no_upload_required), color = RoyalGold, fontWeight = FontWeight.Bold)
            }
            Icon(Icons.AutoMirrored.Outlined.ChevronRight, null, tint = GlowSilver)
        }
    }
}

@Composable
private fun Metric(modifier: Modifier, value: String, label: String, accent: Color) {
    Surface(modifier, RoundedCornerShape(18.dp), MaterialTheme.colorScheme.surface, border = BorderStroke(1.dp, accent.copy(alpha = 0.32f))) {
        Column(Modifier.padding(12.dp)) {
            Text(value, fontWeight = FontWeight.Black, fontSize = 20.sp, color = accent)
            Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2)
        }
    }
}

@Composable
private fun CategoryChip(category: FoodCategory, onClick: () -> Unit) {
    val icon = categoryIcon(category)
    Surface(Modifier.clickable(onClick = onClick), RoundedCornerShape(16.dp), MaterialTheme.colorScheme.surface, border = BorderStroke(1.dp, ElectricBlue.copy(alpha = 0.24f))) {
        Row(Modifier.padding(horizontal = 12.dp, vertical = 9.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = categoryAccent(category), modifier = Modifier.size(20.dp)); Spacer(Modifier.width(7.dp)); Text(categoryLabel(category), fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable private fun SectionTitle(text: String) = Text(text, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)

@Composable
private fun CatalogScreen(modifier: Modifier, onFood: (String) -> Unit) {
    var query by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf<FoodCategory?>(null) }
    var risk by rememberSaveable { mutableStateOf<RiskTier?>(null) }
    val searched = remember(query) { FoodCatalog.search(query) }
    val filtered = remember(searched, category, risk) { searched.filter { (category == null || it.category == category) && (risk == null || it.riskTier == risk) } }

    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(11.dp)) {
        item { Text(stringResource(R.string.food_guide), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black); Text(stringResource(R.string.catalog_subtitle), color = MaterialTheme.colorScheme.onSurfaceVariant) }
        item { OutlinedTextField(query, { query = it }, Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(18.dp), leadingIcon = { Icon(Icons.Outlined.Search, null, tint = ElectricCyan) }, placeholder = { Text(stringResource(R.string.search_hint)) }) }
        item { LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) { item { AssistChip(onClick = { category = null }, label = { Text(stringResource(R.string.all)) }) }; items(FoodCategory.entries.toList()) { value -> AssistChip(onClick = { category = value }, label = { Text(categoryLabel(value)) }, leadingIcon = { Icon(categoryIcon(value), null, Modifier.size(17.dp), tint = categoryAccent(value)) }) } } }
        item { LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) { item { AssistChip(onClick = { risk = null }, label = { Text(stringResource(R.string.s550_all_risks)) }) }; items(RiskTier.entries.toList()) { value -> AssistChip(onClick = { risk = value }, label = { Text(riskLabel(value)) }) } } }
        item { Text("${filtered.size} ${stringResource(R.string.items)}", color = MaterialTheme.colorScheme.onSurfaceVariant) }
        items(filtered, key = { it.id }) { food -> FoodCard(food) { onFood(food.id) } }
        if (filtered.isEmpty()) item { EmptyState(stringResource(R.string.no_results)) }
    }
}

@Composable
private fun FoodCard(food: FoodItem, onClick: () -> Unit) {
    val riskAccent = riskColor(food.riskTier)
    val catAccent = categoryAccent(food.category)
    Card(Modifier.fillMaxWidth().clickable(onClick = onClick), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), border = BorderStroke(1.dp, GlowSilver.copy(alpha = 0.16f))) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            val shape = RoundedCornerShape(14.dp)
            Box(
                Modifier.size(50.dp).clip(shape)
                    .background(Brush.linearGradient(listOf(catAccent.copy(alpha = 0.23f), RoyalGold.copy(alpha = 0.08f))))
                    .border(1.dp, catAccent.copy(alpha = 0.52f), shape),
                contentAlignment = Alignment.Center
            ) { Icon(categoryIcon(food.category), null, tint = catAccent, modifier = Modifier.size(27.dp)) }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(food.localizedName(), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(categoryLabel(food.category), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (food.referenceImages.isNotEmpty()) Text(referenceCountText(food.referenceImages.size), style = MaterialTheme.typography.labelSmall, color = ElectricCyan)
            }
            RiskPill(food.riskTier); Spacer(Modifier.width(4.dp)); Icon(Icons.AutoMirrored.Outlined.ChevronRight, null, tint = GlowSilver.copy(alpha = 0.72f))
        }
    }
}

@Composable
private fun DetailScreen(modifier: Modifier, food: FoodItem?, onBack: () -> Unit, onScan: () -> Unit) {
    if (food == null) {
        Column(modifier.fillMaxSize().padding(18.dp)) { TopBar(stringResource(R.string.food_guide), onBack); EmptyState(stringResource(R.string.no_results)) }
        return
    }
    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(13.dp)) {
        item { TopBar(food.localizedName(), onBack) }
        item {
            val accent = categoryAccent(food.category)
            Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp)).background(Brush.linearGradient(listOf(MaterialTheme.colorScheme.surfaceVariant, accent.copy(alpha = 0.20f), RoyalGold.copy(alpha = 0.08f)))).border(1.dp, GlowSilver.copy(alpha = 0.16f), RoundedCornerShape(24.dp)).padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    PremiumFoodIcon(food.category); Spacer(Modifier.width(14.dp)); Column(Modifier.weight(1f)) { RiskPill(food.riskTier); Spacer(Modifier.height(8.dp)); Text(food.localizedName(), fontSize = 27.sp, fontWeight = FontWeight.Black); Text(categoryLabel(food.category), color = MaterialTheme.colorScheme.onSurfaceVariant) }
                }
            }
        }
        if (food.referenceImages.isNotEmpty()) item { ReferenceGallery(food) }
        item { DetailSection(stringResource(R.string.normal_signs), food.normalSigns, Fresh, Icons.Outlined.CheckCircle) }
        item { DetailSection(stringResource(R.string.spoilage_signs), food.spoilageSigns, Danger, Icons.Outlined.WarningAmber) }
        item { DetailSection(stringResource(R.string.storage), food.storageTips, ElectricBlue, Icons.Outlined.Inventory2) }
        item { OutlinedButton(onClick = onScan, modifier = Modifier.fillMaxWidth()) { Icon(Icons.Outlined.CenterFocusStrong, null, tint = RoyalGold); Spacer(Modifier.width(8.dp)); Text(stringResource(R.string.scan_this_food)) } }
        item { SafetyBanner() }
    }
}

@Composable
private fun PremiumFoodIcon(category: FoodCategory) {
    val accent = categoryAccent(category)
    val shape = RoundedCornerShape(18.dp)
    Box(Modifier.size(70.dp).shadow(7.dp, shape).clip(shape).background(Brush.linearGradient(listOf(accent.copy(alpha = 0.26f), RoyalGold.copy(alpha = 0.10f)))).border(1.dp, accent.copy(alpha = 0.55f), shape), contentAlignment = Alignment.Center) {
        Icon(categoryIcon(category), null, tint = accent, modifier = Modifier.size(36.dp))
    }
}

@Composable
private fun ReferenceGallery(food: FoodItem) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(referenceTitle(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = RoyalGold)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(food.referenceImages) { ref -> ReferenceCard(food, ref) }
        }
    }
}

@Composable
private fun ReferenceCard(food: FoodItem, ref: FoodReferenceImage) {
    val context = LocalContext.current
    val drawableId = remember(ref.assetKey) { context.resources.getIdentifier(ref.assetKey, "drawable", context.packageName) }
    val accent = when (ref.state) {
        FoodReferenceState.HEALTHY -> Fresh
        FoodReferenceState.RIPE -> RoyalGold
        FoodReferenceState.OVERRIPE -> Amber
        FoodReferenceState.SPOILAGE -> Danger
    }
    Surface(Modifier.width(190.dp), RoundedCornerShape(18.dp), MaterialTheme.colorScheme.surface, border = BorderStroke(1.dp, accent.copy(alpha = 0.38f))) {
        Column {
            if (drawableId != 0) {
                Image(painterResource(drawableId), ref.caption, Modifier.fillMaxWidth().height(112.dp), contentScale = ContentScale.Crop)
            } else {
                Box(Modifier.fillMaxWidth().height(112.dp).background(Brush.linearGradient(listOf(categoryAccent(food.category).copy(alpha = 0.17f), accent.copy(alpha = 0.13f)))), contentAlignment = Alignment.Center) {
                    Icon(categoryIcon(food.category), null, tint = categoryAccent(food.category), modifier = Modifier.size(44.dp))
                }
            }
            Column(Modifier.padding(11.dp)) { Text(referenceStateLabel(ref.state), fontWeight = FontWeight.Black, color = accent); Text(ref.caption, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2, overflow = TextOverflow.Ellipsis) }
        }
    }
}

@Composable
private fun DetailSection(title: String, bullets: List<String>, accent: Color, icon: ImageVector) {
    Surface(Modifier.fillMaxWidth(), RoundedCornerShape(20.dp), MaterialTheme.colorScheme.surface, border = BorderStroke(1.dp, accent.copy(alpha = 0.28f))) {
        Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) { Icon(icon, null, tint = accent, modifier = Modifier.size(20.dp)); Spacer(Modifier.width(8.dp)); Text(title, fontWeight = FontWeight.Black, color = accent) }
            bullets.forEach { Text("• $it", color = MaterialTheme.colorScheme.onSurfaceVariant) }
        }
    }
}

@Composable
private fun ScannerScreen(modifier: Modifier) {
    val context = LocalContext.current
    val historyStore = remember { ScanHistoryStore(context) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var foodId by rememberSaveable { mutableStateOf("owaisi_mango") }
    var menu by remember { mutableStateOf(false) }
    var check by remember { mutableStateOf(ManualSafetyCheck()) }
    var assessment by remember { mutableStateOf<SafetyAssessment?>(null) }
    val camera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap = it; assessment = null }
    val gallery = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri -> if (uri != null) { val decoded = runCatching { context.contentResolver.openInputStream(uri)?.use(BitmapFactory::decodeStream) }.getOrNull(); if (decoded != null) { bitmap = decoded; assessment = null } } }
    val report = remember(bitmap) { bitmap?.let(VisualHeuristicEngine::analyze) }
    val food = FoodCatalog.byId(foodId)

    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(13.dp)) {
        item { Text(stringResource(R.string.local_analysis), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black); Text(stringResource(R.string.scanner_privacy), color = MaterialTheme.colorScheme.onSurfaceVariant) }
        item {
            Box {
                Surface(Modifier.fillMaxWidth().clickable { menu = true }, RoundedCornerShape(18.dp), MaterialTheme.colorScheme.surface, border = BorderStroke(1.dp, ElectricCyan.copy(alpha = 0.25f))) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) { if (food != null) { Icon(categoryIcon(food.category), null, tint = categoryAccent(food.category)); Spacer(Modifier.width(10.dp)) }; Column(Modifier.weight(1f)) { Text(stringResource(R.string.food_type), style = MaterialTheme.typography.bodySmall); Text(food?.localizedName() ?: stringResource(R.string.select_food), fontWeight = FontWeight.Bold) }; Icon(Icons.AutoMirrored.Outlined.ChevronRight, null, tint = GlowSilver) }
                }
                DropdownMenu(menu, onDismissRequest = { menu = false }) { FoodCatalog.items.forEach { item -> DropdownMenuItem(text = { Text(item.localizedName()) }, leadingIcon = { Icon(categoryIcon(item.category), null, tint = categoryAccent(item.category)) }, onClick = { foodId = item.id; menu = false; assessment = null }) } }
            }
        }
        item { Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(9.dp)) { OutlinedButton(onClick = { camera.launch(null) }, modifier = Modifier.weight(1f)) { Icon(Icons.Outlined.CameraAlt, null, tint = RoyalGold); Spacer(Modifier.width(6.dp)); Text(stringResource(R.string.camera)) }; OutlinedButton(onClick = { gallery.launch("image/*") }, modifier = Modifier.weight(1f)) { Icon(Icons.Outlined.PhotoLibrary, null, tint = ElectricCyan); Spacer(Modifier.width(6.dp)); Text(stringResource(R.string.gallery)) } } }
        if (bitmap != null) item { Image(bitmap!!.asImageBitmap(), stringResource(R.string.food_photo), Modifier.fillMaxWidth().height(250.dp).clip(RoundedCornerShape(22.dp)), contentScale = ContentScale.Crop) }
        if (report != null) item { ImageSignalCard(report) }
        item { Text(stringResource(R.string.manual_check), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black); Text(stringResource(R.string.manual_check_subtitle), color = MaterialTheme.colorScheme.onSurfaceVariant) }
        item { ManualCheckCard(check) { check = it; assessment = null } }
        item {
            Surface(Modifier.fillMaxWidth().clickable { val result = SafetyRules.evaluate(food, check); assessment = result; historyStore.append(ScanHistoryStore.createRecord(food, result, report?.needsRetake)) }, RoundedCornerShape(18.dp), RoyalGold, contentColor = Color(0xFF1D1300)) { Text(stringResource(R.string.evaluate), Modifier.padding(16.dp), fontWeight = FontWeight.Black) }
            Spacer(Modifier.height(6.dp)); Text(stringResource(R.string.s550_result_saved_note), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        if (assessment != null) item { AssessmentCard(assessment!!) { shareText(context, AssessmentSummary.plainText(food, assessment!!, report?.needsRetake)) } }
        item { SafetyBanner() }
    }
}

@Composable
private fun ImageSignalCard(report: ImageSignalReport) {
    val accent = if (report.needsRetake) Amber else Fresh
    Surface(Modifier.fillMaxWidth(), RoundedCornerShape(20.dp), accent.copy(alpha = 0.08f), border = BorderStroke(1.dp, accent.copy(alpha = 0.35f))) {
        Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) { Text(stringResource(R.string.image_quality_check), fontWeight = FontWeight.Black, color = accent); Text("Luma ${report.averageLuminance}/255 • Saturation ${report.averageSaturation}/255 • Samples ${report.sampledPixels}", style = MaterialTheme.typography.bodySmall); report.observations.forEach { Text("• $it", color = MaterialTheme.colorScheme.onSurfaceVariant) }; if (report.needsRetake) Text(stringResource(R.string.s550_photo_retake), color = Amber, fontWeight = FontWeight.Bold); Text(stringResource(R.string.not_spoilage_classifier), style = MaterialTheme.typography.bodySmall, color = Amber) }
    }
}

@Composable
private fun ManualCheckCard(check: ManualSafetyCheck, onChange: (ManualSafetyCheck) -> Unit) {
    Surface(Modifier.fillMaxWidth(), RoundedCornerShape(20.dp), MaterialTheme.colorScheme.surface, border = BorderStroke(1.dp, GlowSilver.copy(alpha = 0.18f))) {
        Column(Modifier.padding(11.dp)) { CheckRow(stringResource(R.string.check_mold), check.visibleMold) { onChange(check.copy(visibleMold = it)) }; CheckRow(stringResource(R.string.check_slime), check.slimeOrStickyFilm) { onChange(check.copy(slimeOrStickyFilm = it)) }; CheckRow(stringResource(R.string.check_odor), check.fermentedOrRottenOdor) { onChange(check.copy(fermentedOrRottenOdor = it)) }; CheckRow(stringResource(R.string.check_package), check.leakingOrBulgingPackage) { onChange(check.copy(leakingOrBulgingPackage = it)) }; CheckRow(stringResource(R.string.check_temperature), check.unsafeTimeTemperatureHistory) { onChange(check.copy(unsafeTimeTemperatureHistory = it)) }; CheckRow(stringResource(R.string.check_unknown_storage), check.unknownStorageHistory) { onChange(check.copy(unknownStorageHistory = it)) } }
    }
}

@Composable
private fun CheckRow(label: String, checked: Boolean, onChecked: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth().clickable { onChecked(!checked) }.padding(vertical = 5.dp), verticalAlignment = Alignment.CenterVertically) { Checkbox(checked, onChecked); Spacer(Modifier.width(6.dp)); Text(label, Modifier.weight(1f)) }
}

@Composable
private fun AssessmentCard(assessment: SafetyAssessment, onShare: () -> Unit) {
    val accent = verdictColor(assessment.verdict)
    val icon = when (assessment.verdict) { SafetyVerdict.NO_VISIBLE_RED_FLAGS -> Icons.Outlined.CheckCircle; SafetyVerdict.DISCARD -> Icons.Outlined.ErrorOutline; else -> Icons.Outlined.WarningAmber }
    Surface(Modifier.fillMaxWidth(), RoundedCornerShape(20.dp), accent.copy(alpha = 0.09f), border = BorderStroke(1.dp, accent.copy(alpha = 0.38f))) {
        Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { Row(verticalAlignment = Alignment.CenterVertically) { Icon(icon, null, tint = accent); Spacer(Modifier.width(8.dp)); Text(verdictLabel(assessment.verdict), fontWeight = FontWeight.Black, color = accent) }; Text("${stringResource(R.string.s550_red_flags)}: ${assessment.redFlagCount} • ${stringResource(R.string.s550_uncertainty)}: ${assessment.uncertaintyCount}", style = MaterialTheme.typography.bodySmall); if (assessment.reasonCodes.isNotEmpty()) assessment.reasonCodes.forEach { Text("• ${reasonLabel(it)}", color = MaterialTheme.colorScheme.onSurfaceVariant) } else assessment.reasons.forEach { Text("• $it", color = MaterialTheme.colorScheme.onSurfaceVariant) }; Text(stringResource(R.string.s550_saved_to_history), style = MaterialTheme.typography.bodySmall, color = Fresh); OutlinedButton(onClick = onShare, modifier = Modifier.fillMaxWidth()) { Icon(Icons.Outlined.Share, null, tint = ElectricCyan); Spacer(Modifier.width(7.dp)); Text(stringResource(R.string.s550_share_result)) } }
    }
}

@Composable
private fun HistoryScreen(modifier: Modifier, onFood: (String) -> Unit) {
    val context = LocalContext.current
    val store = remember { ScanHistoryStore(context) }
    var records by remember { mutableStateOf(store.load()) }
    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(11.dp)) {
        item { Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) { Column(Modifier.weight(1f)) { Text(stringResource(R.string.s550_history), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black); Text(stringResource(R.string.s550_history_local_note), color = MaterialTheme.colorScheme.onSurfaceVariant) }; if (records.isNotEmpty()) IconButton(onClick = { store.clear(); records = emptyList() }) { Icon(Icons.Outlined.DeleteOutline, stringResource(R.string.s550_clear_history), tint = Danger) } } }
        if (records.isEmpty()) item { EmptyState(stringResource(R.string.s550_history_empty)) } else { item { SectionTitle(stringResource(R.string.s550_recent_checks)) }; items(records, key = { it.id }) { record -> HistoryCard(record) { if (FoodCatalog.byId(record.foodId) != null) onFood(record.foodId) } }; item { TextButton(onClick = { store.clear(); records = emptyList() }, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.s550_clear_history)) } } }
    }
}

@Composable
private fun HistoryCard(record: ScanRecord, onClick: () -> Unit) {
    val context = LocalContext.current
    val accent = verdictColor(record.verdict)
    val formatted = remember(record.timestampEpochMs, context) { DateFormat.getMediumDateFormat(context).format(Date(record.timestampEpochMs)) + " • " + DateFormat.getTimeFormat(context).format(Date(record.timestampEpochMs)) }
    Surface(Modifier.fillMaxWidth().clickable(onClick = onClick), RoundedCornerShape(18.dp), MaterialTheme.colorScheme.surface, border = BorderStroke(1.dp, accent.copy(alpha = 0.28f))) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) { Row { Text(record.foodNameSnapshot, Modifier.weight(1f), fontWeight = FontWeight.Black); Text(verdictLabel(record.verdict), color = accent, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium) }; Text(formatted, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant); Text("${stringResource(R.string.s550_red_flags)}: ${record.redFlagCount} • ${stringResource(R.string.s550_uncertainty)}: ${record.uncertaintyCount}", style = MaterialTheme.typography.bodySmall); if (record.imageNeedsRetake == true) Text(stringResource(R.string.s550_photo_retake), color = Amber, style = MaterialTheme.typography.bodySmall) }
    }
}

@Composable
private fun SettingsScreen(modifier: Modifier, mode: ThemeMode, onTheme: (ThemeMode) -> Unit, onHistory: () -> Unit, onSafety: () -> Unit, onAbout: () -> Unit) {
    val context = LocalContext.current
    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { Text(stringResource(R.string.settings), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black); Text(stringResource(R.string.settings_subtitle), color = MaterialTheme.colorScheme.onSurfaceVariant) }
        item { SettingsCard(stringResource(R.string.appearance), if (mode == ThemeMode.DARK) Icons.Outlined.DarkMode else Icons.Outlined.LightMode) { LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) { items(ThemeMode.entries.toList()) { item -> AssistChip(onClick = { onTheme(item) }, label = { Text(themeLabel(item)) }) } } } }
        item { SettingsCard(stringResource(R.string.languages), Icons.Outlined.Language) { Text(stringResource(R.string.language_help)); Spacer(Modifier.height(7.dp)); OutlinedButton(onClick = { openLanguageSettings(context) }) { Text(stringResource(R.string.change_language)) } } }
        item { SettingsLink(stringResource(R.string.s550_history), Icons.Outlined.History, onHistory) }
        item { SettingsLink(stringResource(R.string.s550_open_safety_guide), Icons.Outlined.HealthAndSafety, onSafety) }
        item { SettingsCard(stringResource(R.string.privacy), Icons.Outlined.HealthAndSafety) { Text(stringResource(R.string.privacy_local)); Spacer(Modifier.height(6.dp)); Text(stringResource(R.string.s550_history_local_note), style = MaterialTheme.typography.bodySmall) } }
        item { SettingsLink(stringResource(R.string.about), Icons.Outlined.Info, onAbout) }
    }
}

@Composable
private fun SettingsCard(title: String, icon: ImageVector, content: @Composable () -> Unit) {
    Surface(Modifier.fillMaxWidth(), RoundedCornerShape(20.dp), MaterialTheme.colorScheme.surface, border = BorderStroke(1.dp, GlowSilver.copy(alpha = 0.18f))) {
        Column(Modifier.padding(15.dp)) { Row(verticalAlignment = Alignment.CenterVertically) { PremiumIconBadge(icon, true, null); Spacer(Modifier.width(9.dp)); Text(title, fontWeight = FontWeight.Black) }; Spacer(Modifier.height(11.dp)); content() }
    }
}

@Composable
private fun SettingsLink(title: String, icon: ImageVector, onClick: () -> Unit) {
    Surface(Modifier.fillMaxWidth().clickable(onClick = onClick), RoundedCornerShape(18.dp), MaterialTheme.colorScheme.surface, border = BorderStroke(1.dp, GlowSilver.copy(alpha = 0.18f))) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) { PremiumIconBadge(icon, false, null); Spacer(Modifier.width(10.dp)); Text(title, Modifier.weight(1f), fontWeight = FontWeight.Bold); Icon(Icons.AutoMirrored.Outlined.ChevronRight, null, tint = GlowSilver) }
    }
}

@Composable
private fun SafetyScreen(modifier: Modifier, onBack: () -> Unit) {
    val guides = listOf(stringResource(R.string.safety_cans_title) to stringResource(R.string.safety_cans_body), stringResource(R.string.safety_meat_title) to stringResource(R.string.safety_meat_body), stringResource(R.string.safety_bread_title) to stringResource(R.string.safety_bread_body), stringResource(R.string.safety_leftovers_title) to stringResource(R.string.safety_leftovers_body), stringResource(R.string.safety_fish_title) to stringResource(R.string.safety_fish_body))
    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { TopBar(stringResource(R.string.safety), onBack) }
        item { Text(stringResource(R.string.safety_intro), color = MaterialTheme.colorScheme.onSurfaceVariant) }
        items(guides) { (title, body) -> Surface(Modifier.fillMaxWidth(), RoundedCornerShape(20.dp), MaterialTheme.colorScheme.surface, border = BorderStroke(1.dp, RoyalGold.copy(alpha = 0.25f))) { Column(Modifier.padding(16.dp)) { Text(title, fontWeight = FontWeight.Black, color = RoyalGold); Spacer(Modifier.height(6.dp)); Text(body) } } }
        item { SafetyBanner() }
    }
}

@Composable
private fun AboutScreen(modifier: Modifier, onBack: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    val links = listOf("Website" to "https://nexvary.com/", "Facebook" to "https://www.facebook.com/share/14p9krEn5ij/", "Email" to "mailto:info@nexvary.com", "YouTube" to "https://www.youtube.com/@NexvaryInc", "X" to "https://x.com/Nexvary")
    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { TopBar(stringResource(R.string.about), onBack) }
        item { Surface(Modifier.fillMaxWidth(), RoundedCornerShape(24.dp), MaterialTheme.colorScheme.surface, border = BorderStroke(1.dp, RoyalGold.copy(alpha = 0.38f))) { Column(Modifier.padding(19.dp)) { Text("NEXVARY FoodGuard", fontSize = 28.sp, fontWeight = FontWeight.Black); Text(stringResource(R.string.about_description)); Spacer(Modifier.height(8.dp)); Text("v0.6 RC1", color = RoyalGold, fontWeight = FontWeight.Bold) } } }
        items(links) { (name, url) -> Surface(Modifier.fillMaxWidth().clickable { uriHandler.openUri(url) }, RoundedCornerShape(16.dp), MaterialTheme.colorScheme.surface, border = BorderStroke(1.dp, GlowSilver.copy(alpha = 0.12f))) { Row(Modifier.padding(15.dp)) { Text(name, Modifier.weight(1f), fontWeight = FontWeight.SemiBold); Icon(Icons.AutoMirrored.Outlined.ChevronRight, null, tint = ElectricCyan) } } }
    }
}

@Composable
private fun SafetyBanner() {
    Surface(Modifier.fillMaxWidth(), RoundedCornerShape(18.dp), Amber.copy(alpha = 0.09f), border = BorderStroke(1.dp, Amber.copy(alpha = 0.30f))) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.Top) { Icon(Icons.Outlined.WarningAmber, null, tint = Amber); Spacer(Modifier.width(10.dp)); Text(stringResource(R.string.safety_note), Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurfaceVariant) }
    }
}

@Composable
private fun RiskPill(risk: RiskTier) {
    val accent = riskColor(risk)
    Surface(shape = RoundedCornerShape(50), color = accent.copy(alpha = 0.12f), border = BorderStroke(1.dp, accent.copy(alpha = 0.34f))) { Text(riskLabel(risk), Modifier.padding(horizontal = 9.dp, vertical = 4.dp), color = accent, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall) }
}

@Composable
private fun TopBar(title: String, onBack: () -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Outlined.ArrowBack, stringResource(R.string.back), tint = RoyalGold) }; Text(title, Modifier.weight(1f), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black) }
}

@Composable private fun EmptyState(message: String) = Surface(Modifier.fillMaxWidth(), RoundedCornerShape(20.dp), MaterialTheme.colorScheme.surface) { Text(message, Modifier.padding(26.dp), color = MaterialTheme.colorScheme.onSurfaceVariant) }

private fun categoryIcon(category: FoodCategory): ImageVector = when (category) {
    FoodCategory.FRUIT -> Icons.Outlined.Spa
    FoodCategory.VEGETABLE -> Icons.Outlined.Eco
    FoodCategory.MEAT -> Icons.Outlined.Restaurant
    FoodCategory.POULTRY -> Icons.Outlined.EggAlt
    FoodCategory.SEAFOOD -> Icons.Outlined.SetMeal
    FoodCategory.DAIRY -> Icons.Outlined.LocalDrink
    FoodCategory.BAKERY -> Icons.Outlined.BakeryDining
    FoodCategory.PREPARED -> Icons.Outlined.LunchDining
    FoodCategory.DRINK -> Icons.Outlined.LocalCafe
    FoodCategory.PACKAGED -> Icons.Outlined.Inventory2
}

private fun categoryAccent(category: FoodCategory): Color = when (category) {
    FoodCategory.FRUIT -> Fresh
    FoodCategory.VEGETABLE -> Color(0xFF7DFF6A)
    FoodCategory.MEAT -> Color(0xFFFF6B78)
    FoodCategory.POULTRY -> RoyalGold
    FoodCategory.SEAFOOD -> ElectricCyan
    FoodCategory.DAIRY -> GlowSilver
    FoodCategory.BAKERY -> Color(0xFFFFB45A)
    FoodCategory.PREPARED -> ElectricViolet
    FoodCategory.DRINK -> ElectricBlue
    FoodCategory.PACKAGED -> Color(0xFFC7D2DA)
}

@Composable private fun categoryLabel(category: FoodCategory): String = when (category) { FoodCategory.FRUIT -> stringResource(R.string.fruits); FoodCategory.VEGETABLE -> stringResource(R.string.vegetables); FoodCategory.MEAT -> stringResource(R.string.meat); FoodCategory.POULTRY -> stringResource(R.string.poultry); FoodCategory.SEAFOOD -> stringResource(R.string.fish); FoodCategory.DAIRY -> stringResource(R.string.dairy); FoodCategory.BAKERY -> stringResource(R.string.bakery); FoodCategory.PREPARED -> stringResource(R.string.prepared); FoodCategory.DRINK -> stringResource(R.string.drinks); FoodCategory.PACKAGED -> stringResource(R.string.canned) }
@Composable private fun riskLabel(risk: RiskTier): String = when (risk) { RiskTier.LOW -> stringResource(R.string.risk_low); RiskTier.MEDIUM -> stringResource(R.string.risk_medium); RiskTier.HIGH -> stringResource(R.string.risk_high) }
@Composable private fun verdictLabel(verdict: SafetyVerdict): String = when (verdict) { SafetyVerdict.NO_VISIBLE_RED_FLAGS -> stringResource(R.string.no_red_flags); SafetyVerdict.CAUTION -> stringResource(R.string.caution); SafetyVerdict.DISCARD -> stringResource(R.string.danger); SafetyVerdict.INSUFFICIENT_INFORMATION -> stringResource(R.string.insufficient_info) }
@Composable private fun reasonLabel(code: SafetyReasonCode): String = when (code) { SafetyReasonCode.PACKAGE_COMPROMISED -> stringResource(R.string.check_package); SafetyReasonCode.VISIBLE_MOLD -> stringResource(R.string.check_mold); SafetyReasonCode.SLIME_OR_STICKY_FILM -> stringResource(R.string.check_slime); SafetyReasonCode.ABNORMAL_ODOR -> stringResource(R.string.check_odor); SafetyReasonCode.UNSAFE_TIME_TEMPERATURE -> stringResource(R.string.check_temperature); SafetyReasonCode.UNKNOWN_STORAGE_HISTORY -> stringResource(R.string.check_unknown_storage); SafetyReasonCode.NO_REPORTED_RED_FLAGS -> stringResource(R.string.no_red_flags); SafetyReasonCode.NOT_PROOF_OF_MICROBIOLOGICAL_SAFETY -> stringResource(R.string.safety_note) }
@Composable private fun themeLabel(mode: ThemeMode): String = when (mode) { ThemeMode.SYSTEM -> stringResource(R.string.system_theme); ThemeMode.LIGHT -> stringResource(R.string.light_theme); ThemeMode.DARK -> stringResource(R.string.dark_theme) }
private fun riskColor(risk: RiskTier): Color = when (risk) { RiskTier.LOW -> Fresh; RiskTier.MEDIUM -> Amber; RiskTier.HIGH -> Danger }
private fun verdictColor(verdict: SafetyVerdict): Color = when (verdict) { SafetyVerdict.NO_VISIBLE_RED_FLAGS -> Fresh; SafetyVerdict.DISCARD -> Danger; else -> Amber }

private fun referenceTitle(): String = when (Locale.getDefault().language) { "ar" -> "الصور المرجعية"; "tr" -> "Referans görüntüler"; "fr" -> "Images de référence"; "es" -> "Imágenes de referencia"; "de" -> "Referenzbilder"; "it" -> "Immagini di riferimento"; else -> "Reference images" }
private fun referenceCountText(count: Int): String = when (Locale.getDefault().language) { "ar" -> "$count صور مرجعية"; "tr" -> "$count referans görsel"; "fr" -> "$count images de référence"; "es" -> "$count imágenes de referencia"; "de" -> "$count Referenzbilder"; "it" -> "$count immagini di riferimento"; else -> "$count reference images" }
private fun referenceStateLabel(state: FoodReferenceState): String {
    val lang = Locale.getDefault().language
    return when (state) {
        FoodReferenceState.HEALTHY -> when (lang) { "ar" -> "طبيعي"; "tr" -> "Sağlıklı"; "fr" -> "Normal"; "es" -> "Normal"; "de" -> "Normal"; "it" -> "Normale"; else -> "Healthy" }
        FoodReferenceState.RIPE -> when (lang) { "ar" -> "ناضج"; "tr" -> "Olgun"; "fr" -> "Mûr"; "es" -> "Maduro"; "de" -> "Reif"; "it" -> "Maturo"; else -> "Ripe" }
        FoodReferenceState.OVERRIPE -> when (lang) { "ar" -> "ناضج جدًا"; "tr" -> "Aşırı olgun"; "fr" -> "Très mûr"; "es" -> "Muy maduro"; "de" -> "Überreif"; "it" -> "Molto maturo"; else -> "Overripe" }
        FoodReferenceState.SPOILAGE -> when (lang) { "ar" -> "تلف مرئي"; "tr" -> "Görsel bozulma"; "fr" -> "Altération visible"; "es" -> "Deterioro visible"; "de" -> "Sichtbarer Verderb"; "it" -> "Deterioramento visibile"; else -> "Visible spoilage" }
    }
}

private fun openLanguageSettings(context: Context) {
    val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Intent(Settings.ACTION_APP_LOCALE_SETTINGS, Uri.parse("package:${context.packageName}")) else Intent(Settings.ACTION_LOCALE_SETTINGS)
    runCatching { context.startActivity(intent) }
}

private fun shareText(context: Context, text: String) {
    val intent = Intent(Intent.ACTION_SEND).apply { type = "text/plain"; putExtra(Intent.EXTRA_TEXT, text) }
    runCatching { context.startActivity(Intent.createChooser(intent, null)) }
}
