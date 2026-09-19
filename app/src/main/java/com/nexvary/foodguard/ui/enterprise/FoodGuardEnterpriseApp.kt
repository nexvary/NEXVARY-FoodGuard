package com.nexvary.foodguard.ui.enterprise

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.format.DateFormat
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ChevronRight
import androidx.compose.material.icons.outlined.BakeryDining
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.CenterFocusStrong
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.EggAlt
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Email
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.nexvary.foodguard.data.FoodGuidanceLocalizer
import com.nexvary.foodguard.domain.SafetyRules
import com.nexvary.foodguard.model.FoodCategory
import com.nexvary.foodguard.model.FoodItem
import com.nexvary.foodguard.model.FoodReferenceImage
import com.nexvary.foodguard.model.FoodReferenceState
import com.nexvary.foodguard.model.ManualSafetyCheck
import com.nexvary.foodguard.model.RiskTier
import com.nexvary.foodguard.model.SafetyAssessment
import com.nexvary.foodguard.model.SafetyVerdict
import com.nexvary.foodguard.storage.ScanHistoryStore
import com.nexvary.foodguard.storage.ScanRecord
import com.nexvary.foodguard.ui.theme.Amber
import com.nexvary.foodguard.ui.theme.Danger
import com.nexvary.foodguard.ui.theme.ElectricBlue
import com.nexvary.foodguard.ui.theme.ElectricCyan
import com.nexvary.foodguard.ui.theme.FoodGuardTheme
import com.nexvary.foodguard.ui.theme.Fresh
import com.nexvary.foodguard.ui.theme.GlowSilver
import com.nexvary.foodguard.ui.theme.RoyalGold
import java.util.Date
import java.util.Locale

private val EnterpriseNavy = Color(0xFF01050A)
private val EnterprisePanel = Color(0xFF04101A)
private val EnterprisePanelRaised = Color(0xFF071A28)
private val EnterpriseGoldDeep = Color(0xFF7A5B12)

private enum class EnterpriseScreen { HOME, CATALOG, SCANNER, HISTORY, SETTINGS, ABOUT, DETAIL }
private enum class EnterpriseTheme { SYSTEM, LIGHT, DARK }

@Composable
fun FoodGuardEnterpriseApp() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("foodguard_settings", Context.MODE_PRIVATE) }
    var themeName by rememberSaveable {
        mutableStateOf(prefs.getString("theme_mode", EnterpriseTheme.DARK.name) ?: EnterpriseTheme.DARK.name)
    }
    val mode = runCatching { EnterpriseTheme.valueOf(themeName) }.getOrDefault(EnterpriseTheme.DARK)
    val dark = when (mode) {
        EnterpriseTheme.SYSTEM -> true
        EnterpriseTheme.LIGHT -> false
        EnterpriseTheme.DARK -> true
    }

    FoodGuardTheme(darkTheme = dark) {
        EnterpriseRoot(mode) {
            themeName = it.name
            prefs.edit().putString("theme_mode", it.name).apply()
        }
    }
}

@Composable
private fun EnterpriseRoot(themeMode: EnterpriseTheme, onThemeMode: (EnterpriseTheme) -> Unit) {
    var screen by rememberSaveable { mutableStateOf(EnterpriseScreen.HOME) }
    var selectedFoodId by rememberSaveable { mutableStateOf("owaisi_mango") }

    fun openFood(id: String) {
        selectedFoodId = id
        screen = EnterpriseScreen.DETAIL
    }

    BackHandler(enabled = screen != EnterpriseScreen.HOME) {
        screen = if (screen == EnterpriseScreen.DETAIL) EnterpriseScreen.CATALOG else EnterpriseScreen.HOME
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        containerColor = EnterpriseNavy,
        bottomBar = {
            if (screen != EnterpriseScreen.DETAIL && screen != EnterpriseScreen.ABOUT) EnterpriseNav(screen) { screen = it }
        }
    ) { padding ->
        when (screen) {
            EnterpriseScreen.HOME -> EnterpriseHome(
                Modifier.padding(padding),
                onScan = { screen = EnterpriseScreen.SCANNER },
                onCatalog = { screen = EnterpriseScreen.CATALOG },
                onHistory = { screen = EnterpriseScreen.HISTORY },
                onSettings = { screen = EnterpriseScreen.SETTINGS },
                onAbout = { screen = EnterpriseScreen.ABOUT }
            )
            EnterpriseScreen.CATALOG -> EnterpriseCatalog(Modifier.padding(padding), ::openFood)
            EnterpriseScreen.SCANNER -> EnterpriseScanner(Modifier.padding(padding))
            EnterpriseScreen.HISTORY -> EnterpriseHistory(Modifier.padding(padding), ::openFood)
            EnterpriseScreen.SETTINGS -> EnterpriseSettings(Modifier.padding(padding), themeMode, onThemeMode)
            EnterpriseScreen.ABOUT -> EnterpriseAbout(Modifier.padding(padding)) { screen = EnterpriseScreen.HOME }
            EnterpriseScreen.DETAIL -> EnterpriseDetail(
                Modifier.padding(padding),
                FoodCatalog.byId(selectedFoodId),
                onBack = { screen = EnterpriseScreen.CATALOG },
                onScan = { screen = EnterpriseScreen.SCANNER }
            )
        }
    }
}

@Composable
private fun EnterpriseNav(screen: EnterpriseScreen, onNavigate: (EnterpriseScreen) -> Unit) {
    val entries = listOf(
        Triple(EnterpriseScreen.HOME, stringResource(R.string.home), Icons.Outlined.Home),
        Triple(EnterpriseScreen.CATALOG, stringResource(R.string.guide), Icons.Outlined.MenuBook),
        Triple(EnterpriseScreen.SCANNER, stringResource(R.string.scan_food), Icons.Outlined.CenterFocusStrong),
        Triple(EnterpriseScreen.HISTORY, localized("History", "السجل", "Geçmiş", "Historique", "Historial", "Verlauf", "Cronologia"), Icons.Outlined.History),
        Triple(EnterpriseScreen.SETTINGS, stringResource(R.string.settings), Icons.Outlined.Settings)
    )

    NavigationBar(containerColor = MaterialTheme.colorScheme.surface, tonalElevation = 0.dp) {
        entries.forEach { (target, label, icon) ->
            val selected = target == screen
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(target) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = RoyalGold,
                    selectedTextColor = RoyalGold,
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = GlowSilver.copy(alpha = 0.58f),
                    unselectedTextColor = GlowSilver.copy(alpha = 0.58f)
                ),
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(Modifier.width(28.dp).height(2.dp).background(if (selected) RoyalGold else Color.Transparent))
                        Spacer(Modifier.height(6.dp))
                        Icon(icon, label, Modifier.size(22.dp))
                    }
                },
                label = { Text(label, maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 10.sp, fontWeight = FontWeight.SemiBold) }
            )
        }
    }
}

@Composable
private fun EnterpriseHome(
    modifier: Modifier,
    onScan: () -> Unit,
    onCatalog: () -> Unit,
    onHistory: () -> Unit,
    onSettings: () -> Unit,
    onAbout: () -> Unit
) {
    LazyColumn(
        modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item { EnterpriseHeader() }
        item {
            Text(
                localized("Main menu", "القائمة الرئيسية", "Ana menü", "Menu principal", "Menú principal", "Hauptmenü", "Menu principale"),
                color = RoyalGold, fontSize = 12.sp, fontWeight = FontWeight.Black, letterSpacing = 1.2.sp
            )
        }
        item {
            EnterpriseMenuRow(
                Icons.Outlined.CenterFocusStrong,
                localized("Inspect food", "فحص الطعام", "Gıdayı incele", "Inspecter un aliment", "Inspeccionar alimento", "Lebensmittel prüfen", "Ispeziona alimento"),
                localized("Camera or gallery • on-device", "الكاميرا أو المعرض • الفحص على الجهاز", "Kamera veya galeri • cihazda", "Caméra ou galerie • sur l’appareil", "Cámara o galería • en dispositivo", "Kamera oder Galerie • lokal", "Fotocamera o galleria • sul dispositivo"),
                RoyalGold,
                onScan
            )
        }
        item {
            EnterpriseMenuRow(
                Icons.Outlined.MenuBook,
                localized("Food guide", "دليل الأغذية", "Gıda rehberi", "Guide alimentaire", "Guía de alimentos", "Lebensmittel-Leitfaden", "Guida alimentare"),
                localized("${FoodCatalog.items.size} indexed foods", "${FoodCatalog.items.size} صنفًا مسجلًا", "${FoodCatalog.items.size} kayıt", "${FoodCatalog.items.size} aliments", "${FoodCatalog.items.size} alimentos", "${FoodCatalog.items.size} Einträge", "${FoodCatalog.items.size} alimenti"),
                ElectricCyan,
                onCatalog
            )
        }
        item {
            EnterpriseMenuRow(
                Icons.Outlined.History,
                localized("Inspection history", "سجل الفحوصات", "İnceleme geçmişi", "Historique des inspections", "Historial de inspecciones", "Prüfverlauf", "Cronologia ispezioni"),
                localized("Private records stored on this device", "سجل خاص محفوظ على هذا الجهاز", "Bu cihazda özel kayıt", "Historique privé sur cet appareil", "Historial privado en este dispositivo", "Private lokale Historie", "Cronologia privata sul dispositivo"),
                GlowSilver,
                onHistory
            )
        }
        item {
            EnterpriseMenuRow(
                Icons.Outlined.Settings,
                stringResource(R.string.settings),
                localized("Language, appearance and product settings", "اللغة والمظهر وإعدادات المنتج", "Dil, görünüm ve ürün ayarları", "Langue, apparence et réglages", "Idioma, apariencia y ajustes", "Sprache, Darstellung und Einstellungen", "Lingua, aspetto e impostazioni"),
                ElectricBlue,
                onSettings
            )
        }
        item {
            EnterpriseMenuRow(
                Icons.Outlined.Info,
                localized("About", "عنا", "Hakkında", "À propos", "Acerca de", "Über", "Informazioni"),
                localized("NEXVARY links and product information", "روابط NEXVARY ومعلومات التطبيق", "NEXVARY bağlantıları ve ürün bilgisi", "Liens NEXVARY et informations produit", "Enlaces NEXVARY e información", "NEXVARY-Links und Produktinfos", "Link NEXVARY e informazioni"),
                Fresh,
                onAbout
            )
        }
    }
}

@Composable
private fun EnterpriseMenuRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    accent: Color,
    onClick: () -> Unit
) {
    Surface(
        Modifier.fillMaxWidth().clickable(onClick = onClick),
        RoundedCornerShape(7.dp),
        MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, GlowSilver.copy(alpha = 0.14f))
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.width(3.dp).height(76.dp).background(accent))
            Box(
                Modifier
                    .padding(start = 13.dp)
                    .size(48.dp)
                    .background(Brush.radialGradient(listOf(accent.copy(alpha = 0.20f), Color(0xFF06111A))), RoundedCornerShape(10.dp))
                    .border(1.dp, GlowSilver.copy(alpha = 0.28f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    Modifier.size(36.dp).border(1.dp, accent.copy(alpha = 0.72f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, null, tint = accent, modifier = Modifier.size(24.dp))
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f).padding(vertical = 12.dp)) {
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(2.dp))
                Text(subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
            Icon(Icons.AutoMirrored.Outlined.ChevronRight, null, tint = GlowSilver.copy(alpha = 0.52f), modifier = Modifier.padding(horizontal = 12.dp).size(20.dp))
        }
    }
}

@Composable
private fun EnterpriseHeader() {
    Surface(
        Modifier.fillMaxWidth(),
        RoundedCornerShape(10.dp),
        Color(0xFF02070D),
        border = BorderStroke(1.dp, ElectricBlue.copy(alpha = 0.30f))
    ) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NexvaryBrandMark(Modifier.size(66.dp))
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text("NEXVARY", color = GlowSilver, fontSize = 21.sp, fontWeight = FontWeight.Black, letterSpacing = 3.sp)
                Text("FoodGuard", color = ElectricBlue, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(
                    localized(
                        "Know your food before you eat it", "اعرف طعامك قبل أن تأكله", "Yemeden önce gıdanı tanı",
                        "Connaissez vos aliments avant de les manger", "Conoce tus alimentos antes de comerlos",
                        "Kenne dein Essen, bevor du es isst", "Conosci il tuo cibo prima di mangiarlo"
                    ),
                    color = GlowSilver.copy(alpha = 0.82f), fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun NexvaryBrandMark(modifier: Modifier = Modifier) {
    Box(
        modifier.border(1.dp, ElectricBlue.copy(alpha = 0.62f), CircleShape).padding(5.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            drawCircle(ElectricBlue.copy(alpha = 0.22f), radius = w * 0.47f, style = Stroke(width = 1.2f))
            drawLine(GlowSilver, Offset(w*.22f,h*.25f), Offset(w*.22f,h*.74f), strokeWidth=w*.075f)
            drawLine(GlowSilver, Offset(w*.22f,h*.25f), Offset(w*.50f,h*.67f), strokeWidth=w*.075f)
            drawLine(GlowSilver, Offset(w*.50f,h*.67f), Offset(w*.78f,h*.25f), strokeWidth=w*.075f)
            drawLine(GlowSilver, Offset(w*.78f,h*.25f), Offset(w*.78f,h*.74f), strokeWidth=w*.075f)
            drawLine(ElectricBlue, Offset(w*.49f,h*.30f), Offset(w*.60f,h*.15f), strokeWidth=w*.085f)
            drawCircle(ElectricBlue, radius=w*.035f, center=Offset(w*.50f,h*.84f))
        }
    }
}

@Composable
private fun OperationsDeck(onScan: () -> Unit, onCatalog: () -> Unit) {
    Surface(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, GlowSilver.copy(alpha = 0.20f))
    ) {
        Column(
            Modifier.background(Brush.linearGradient(listOf(Color.Transparent, EnterprisePanelRaised.copy(alpha = 0.62f), EnterpriseGoldDeep.copy(alpha = 0.10f)))).padding(17.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("FOOD SAFETY OPERATIONS", color = ElectricCyan, fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 1.5.sp)
                    Spacer(Modifier.height(4.dp))
                    Text(stringResource(R.string.scan_food), fontSize = 27.sp, fontWeight = FontWeight.Black)
                    Text(stringResource(R.string.scan_subtitle), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Box(
                    Modifier.size(56.dp).border(1.dp, RoyalGold.copy(alpha = 0.76f), RoundedCornerShape(6.dp)).clickable(onClick = onScan),
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Outlined.CenterFocusStrong, null, tint = RoyalGold, modifier = Modifier.size(30.dp)) }
            }
            ThinRule(RoyalGold.copy(alpha = 0.25f))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                EnterpriseAction(Modifier.weight(1f), Icons.Outlined.CameraAlt, localized("Inspect", "فحص", "İncele", "Inspecter", "Inspeccionar", "Prüfen", "Ispeziona"), localized("On-device", "على الجهاز", "Cihazda", "Sur l’appareil", "En dispositivo", "Lokal", "Sul dispositivo"), RoyalGold, onScan)
                EnterpriseAction(Modifier.weight(1f), Icons.Outlined.MenuBook, stringResource(R.string.guide), "${FoodCatalog.items.size} INDEXED", ElectricCyan, onCatalog)
            }
        }
    }
}

@Composable
private fun EnterpriseAction(modifier: Modifier, icon: ImageVector, title: String, subtitle: String, accent: Color, onClick: () -> Unit) {
    Surface(modifier.clickable(onClick = onClick), RoundedCornerShape(6.dp), Color.Transparent, border = BorderStroke(1.dp, accent.copy(alpha = 0.30f))) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = accent, modifier = Modifier.size(21.dp))
            Spacer(Modifier.width(9.dp))
            Column(Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, maxLines = 1)
                Text(subtitle, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
            }
            Icon(Icons.AutoMirrored.Outlined.ChevronRight, null, tint = GlowSilver.copy(alpha = 0.50f), modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun TelemetryStrip(foods: Int, highRisk: Int, references: Int, scans: Int) {
    Surface(Modifier.fillMaxWidth(), RoundedCornerShape(6.dp), MaterialTheme.colorScheme.surface, border = BorderStroke(1.dp, GlowSilver.copy(alpha = 0.14f))) {
        Row(Modifier.padding(vertical = 10.dp)) {
            TelemetryCell(Modifier.weight(1f), foods.toString(), "FOODS", ElectricCyan)
            TelemetryDivider()
            TelemetryCell(Modifier.weight(1f), highRisk.toString(), "HIGH RISK", Danger)
            TelemetryDivider()
            TelemetryCell(Modifier.weight(1f), references.toString(), "REFERENCES", RoyalGold)
            TelemetryDivider()
            TelemetryCell(Modifier.weight(1f), scans.toString(), "SCANS", Fresh)
        }
    }
}

@Composable
private fun TelemetryCell(modifier: Modifier, value: String, label: String, accent: Color) {
    Column(modifier.padding(horizontal = 6.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.Black, color = accent)
        Text(label, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = GlowSilver.copy(alpha = 0.56f), maxLines = 1)
    }
}

@Composable
private fun TelemetryDivider() {
    Box(Modifier.width(1.dp).height(34.dp).background(GlowSilver.copy(alpha = 0.12f)))
}

@Composable
private fun CompactCommand(modifier: Modifier, icon: ImageVector, label: String, accent: Color, onClick: () -> Unit) {
    Surface(modifier.clickable(onClick = onClick), RoundedCornerShape(5.dp), Color.Transparent, border = BorderStroke(1.dp, accent.copy(alpha = 0.28f))) {
        Row(Modifier.padding(horizontal = 8.dp, vertical = 9.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Icon(icon, null, tint = accent, modifier = Modifier.size(17.dp))
            Spacer(Modifier.width(5.dp))
            Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun SectionHeader(title: String, code: String) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(code, fontSize = 9.sp, fontWeight = FontWeight.Black, letterSpacing = 1.4.sp, color = RoyalGold)
            Spacer(Modifier.weight(1f))
            Box(Modifier.width(42.dp).height(1.dp).background(ElectricCyan.copy(alpha = 0.42f)))
        }
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun CategoryChip(category: FoodCategory, onClick: () -> Unit) {
    val accent = categoryAccent(category)
    Surface(Modifier.clickable(onClick = onClick), RoundedCornerShape(5.dp), MaterialTheme.colorScheme.surface, border = BorderStroke(1.dp, GlowSilver.copy(alpha = 0.15f))) {
        Row(Modifier.padding(horizontal = 11.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(categoryIcon(category), null, tint = accent, modifier = Modifier.size(17.dp))
            Spacer(Modifier.width(7.dp))
            Text(categoryLabel(category), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun FoodRow(food: FoodItem, onClick: () -> Unit) {
    val risk = riskColor(food.riskTier)
    val accent = categoryAccent(food.category)
    Card(
        Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, GlowSilver.copy(alpha = 0.13f))
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.width(3.dp).height(72.dp).background(risk))
            Row(Modifier.padding(horizontal = 12.dp, vertical = 10.dp).weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(38.dp).border(1.dp, accent.copy(alpha = 0.38f), RoundedCornerShape(5.dp)), contentAlignment = Alignment.Center) {
                    Icon(categoryIcon(food.category), null, tint = accent, modifier = Modifier.size(21.dp))
                }
                Spacer(Modifier.width(11.dp))
                Column(Modifier.weight(1f)) {
                    Text(food.localizedName(), fontWeight = FontWeight.Bold, fontSize = 15.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(categoryLabel(food.category) + if (food.referenceImages.isNotEmpty()) "  •  ${food.referenceImages.size} REF" else "", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(riskLabel(food.riskTier).uppercase(Locale.getDefault()), fontSize = 9.sp, fontWeight = FontWeight.Black, color = risk)
                    Spacer(Modifier.height(4.dp))
                    Icon(Icons.AutoMirrored.Outlined.ChevronRight, null, tint = GlowSilver.copy(alpha = 0.50f), modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
private fun EnterpriseCatalog(modifier: Modifier, onFood: (String) -> Unit) {
    var query by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf<FoodCategory?>(null) }
    var risk by rememberSaveable { mutableStateOf<RiskTier?>(null) }
    val searched = remember(query) { FoodCatalog.search(query) }
    val filtered = remember(searched, category, risk) {
        searched.filter { (category == null || it.category == category) && (risk == null || it.riskTier == risk) }
    }

    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(11.dp)) {
        item { ScreenHeader(localized("Food intelligence index", "دليل ذكاء الغذاء", "Gıda bilgi indeksi", "Index d’intelligence alimentaire", "Índice de inteligencia alimentaria", "Lebensmittel-Intelligenzindex", "Indice intelligence alimentare"), "${FoodCatalog.items.size} RECORDS") }
        item {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(6.dp),
                leadingIcon = { Icon(Icons.Outlined.Search, null, tint = ElectricCyan) },
                placeholder = { Text(stringResource(R.string.search_hint)) }
            )
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                item { AssistChip(onClick = { category = null }, label = { Text(localized("All", "الكل", "Tümü", "Tout", "Todo", "Alle", "Tutti")) }) }
                items(FoodCategory.entries.toList()) { value ->
                    AssistChip(onClick = { category = value }, label = { Text(categoryLabel(value)) }, leadingIcon = { Icon(categoryIcon(value), null, Modifier.size(16.dp), tint = categoryAccent(value)) })
                }
            }
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                item { AssistChip(onClick = { risk = null }, label = { Text(localized("All risk", "كل المخاطر", "Tüm risk", "Tous risques", "Todo riesgo", "Alle Risiken", "Tutti i rischi")) }) }
                items(RiskTier.entries.toList()) { value -> AssistChip(onClick = { risk = value }, label = { Text(riskLabel(value)) }) }
            }
        }
        item { Text("${filtered.size} ${localized("results", "نتيجة", "sonuç", "résultats", "resultados", "Ergebnisse", "risultati")}", fontSize = 11.sp, color = GlowSilver.copy(alpha = 0.62f)) }
        items(filtered, key = { it.id }) { food -> FoodRow(food) { onFood(food.id) } }
    }
}

@Composable
private fun EnterpriseDetail(modifier: Modifier, food: FoodItem?, onBack: () -> Unit, onScan: () -> Unit) {
    if (food == null) {
        Column(modifier.fillMaxSize().padding(18.dp)) {
            BackHeader(stringResource(R.string.guide), onBack)
            Spacer(Modifier.height(18.dp))
            Text(localized("Record unavailable", "السجل غير متاح", "Kayıt yok", "Fiche indisponible", "Registro no disponible", "Datensatz nicht verfügbar", "Record non disponibile"))
        }
        return
    }

    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { BackHeader(food.localizedName(), onBack) }
        item { FoodIdentity(food) }
        if (food.referenceImages.isNotEmpty()) item { EvidenceRail(food) }
        item { EvidenceSection(localized("Normal profile", "العلامات الطبيعية", "Normal profil", "Profil normal", "Perfil normal", "Normales Profil", "Profilo normale"), FoodGuidanceLocalizer.normal(food), Fresh, Icons.Outlined.CheckCircle) }
        item { EvidenceSection(localized("Visible spoilage indicators", "مؤشرات التلف الظاهرة", "Görünür bozulma", "Signes visibles d’altération", "Indicadores visibles de deterioro", "Sichtbare Verderbniszeichen", "Segni visibili di deterioramento"), FoodGuidanceLocalizer.spoilage(food), Danger, Icons.Outlined.WarningAmber) }
        item { EvidenceSection(stringResource(R.string.storage), FoodGuidanceLocalizer.storage(food), ElectricBlue, Icons.Outlined.Inventory2) }
        item {
            Surface(Modifier.fillMaxWidth().clickable(onClick = onScan), RoundedCornerShape(6.dp), RoyalGold, contentColor = Color(0xFF1B1200)) {
                Row(Modifier.padding(14.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.CenterFocusStrong, null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(localized("Inspect this food", "افحص هذا الطعام", "Bu gıdayı incele", "Inspecter cet aliment", "Inspeccionar este alimento", "Dieses Lebensmittel prüfen", "Ispeziona questo alimento"), fontWeight = FontWeight.Black)
                }
            }
        }
        item { SafetyNotice() }
    }
}

@Composable
private fun FoodIdentity(food: FoodItem) {
    val accent = categoryAccent(food.category)
    val risk = riskColor(food.riskTier)
    Surface(Modifier.fillMaxWidth(), RoundedCornerShape(8.dp), MaterialTheme.colorScheme.surface, border = BorderStroke(1.dp, GlowSilver.copy(alpha = 0.16f))) {
        Column {
            Box(Modifier.fillMaxWidth().height(3.dp).background(Brush.horizontalGradient(listOf(accent, RoyalGold, risk))))
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(54.dp).border(1.dp, accent.copy(alpha = 0.45f), RoundedCornerShape(5.dp)), contentAlignment = Alignment.Center) {
                    Icon(categoryIcon(food.category), null, tint = accent, modifier = Modifier.size(29.dp))
                }
                Spacer(Modifier.width(13.dp))
                Column(Modifier.weight(1f)) {
                    Text(localized("FOOD RECORD", "سجل الغذاء", "GIDA KAYDI", "FICHE ALIMENT", "REGISTRO ALIMENTO", "LEBENSMITTELAKTE", "SCHEDA ALIMENTO") + " / ${food.id.uppercase(Locale.ROOT)}", fontSize = 9.sp, color = ElectricCyan, fontWeight = FontWeight.Black)
                    Text(food.localizedName(), fontSize = 24.sp, fontWeight = FontWeight.Black)
                    Text(categoryLabel(food.category), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(localized("RISK", "الخطورة", "RİSK", "RISQUE", "RIESGO", "RISIKO", "RISCHIO"), fontSize = 9.sp, color = GlowSilver.copy(alpha = 0.56f))
                    Text(riskLabel(food.riskTier).uppercase(Locale.getDefault()), fontWeight = FontWeight.Black, color = risk)
                }
            }
        }
    }
}

@Composable
private fun EvidenceRail(food: FoodItem) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionHeader(localized("Reference evidence", "الأدلة المرجعية", "Referans kanıt", "Références visuelles", "Evidencia de referencia", "Referenzbelege", "Evidenza di riferimento"), localized("VISUAL EVIDENCE", "أدلة بصرية", "GÖRSEL KANIT", "PREUVES VISUELLES", "EVIDENCIA VISUAL", "VISUELLE BELEGE", "EVIDENZA VISIVA"))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(9.dp)) { items(food.referenceImages) { EvidenceCard(food, it) } }
    }
}

@Composable
private fun EvidenceCard(food: FoodItem, ref: FoodReferenceImage) {
    val context = LocalContext.current
    val drawableId = remember(ref.assetKey) { context.resources.getIdentifier(ref.assetKey, "drawable", context.packageName) }
    val caption = FoodGuidanceLocalizer.referenceCaption(ref.state, ref.caption)
    val accent = when (ref.state) {
        FoodReferenceState.HEALTHY -> Fresh
        FoodReferenceState.RIPE -> RoyalGold
        FoodReferenceState.OVERRIPE -> Amber
        FoodReferenceState.SPOILAGE -> Danger
    }
    Surface(Modifier.width(182.dp), RoundedCornerShape(6.dp), MaterialTheme.colorScheme.surface, border = BorderStroke(1.dp, accent.copy(alpha = 0.34f))) {
        Column {
            if (drawableId != 0) {
                Image(painterResource(drawableId), caption, Modifier.fillMaxWidth().height(105.dp), contentScale = ContentScale.Crop)
            } else {
                Box(Modifier.fillMaxWidth().height(105.dp).background(EnterprisePanelRaised), contentAlignment = Alignment.Center) {
                    Icon(categoryIcon(food.category), null, tint = categoryAccent(food.category), modifier = Modifier.size(36.dp))
                }
            }
            Column(Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(referenceStateLabel(ref.state).uppercase(Locale.getDefault()), fontSize = 9.sp, fontWeight = FontWeight.Black, color = accent)
                Text(caption, style = MaterialTheme.typography.bodySmall, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
private fun EvidenceSection(title: String, bullets: List<String>, accent: Color, icon: ImageVector) {
    Surface(Modifier.fillMaxWidth(), RoundedCornerShape(6.dp), MaterialTheme.colorScheme.surface, border = BorderStroke(1.dp, GlowSilver.copy(alpha = 0.14f))) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, tint = accent, modifier = Modifier.size(19.dp))
                Spacer(Modifier.width(8.dp))
                Text(title, fontWeight = FontWeight.Black)
            }
            bullets.forEach { bullet ->
                Row(verticalAlignment = Alignment.Top) {
                    Box(Modifier.padding(top = 8.dp).size(4.dp).background(accent, RoundedCornerShape(2.dp)))
                    Spacer(Modifier.width(9.dp))
                    Text(bullet, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun EnterpriseScanner(modifier: Modifier) {
    val context = LocalContext.current
    val historyStore = remember { ScanHistoryStore(context) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var foodId by rememberSaveable { mutableStateOf("owaisi_mango") }
    var menu by remember { mutableStateOf(false) }
    var check by remember { mutableStateOf(ManualSafetyCheck()) }
    var assessment by remember { mutableStateOf<SafetyAssessment?>(null) }

    val camera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap = it; assessment = null }
    val gallery = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            val decoded = runCatching { context.contentResolver.openInputStream(uri)?.use(BitmapFactory::decodeStream) }.getOrNull()
            if (decoded != null) { bitmap = decoded; assessment = null }
        }
    }
    val report = remember(bitmap) { bitmap?.let(VisualHeuristicEngine::analyze) }
    val food = FoodCatalog.byId(foodId)

    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { ScreenHeader(stringResource(R.string.local_analysis), "ON-DEVICE / NO UPLOAD") }
        item {
            Surface(Modifier.fillMaxWidth(), RoundedCornerShape(6.dp), MaterialTheme.colorScheme.surface, border = BorderStroke(1.dp, ElectricCyan.copy(alpha = 0.22f))) {
                Box {
                    Row(Modifier.fillMaxWidth().clickable { menu = true }.padding(13.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(food?.let { categoryIcon(it.category) } ?: Icons.Outlined.Restaurant, null, tint = food?.let { categoryAccent(it.category) } ?: ElectricCyan)
                        Spacer(Modifier.width(10.dp))
                        Column(Modifier.weight(1f)) {
                            Text("TARGET FOOD", fontSize = 9.sp, color = GlowSilver.copy(alpha = 0.56f), fontWeight = FontWeight.Black)
                            Text(food?.localizedName() ?: localized("Select food", "اختر الطعام", "Gıda seç", "Choisir", "Seleccionar", "Auswählen", "Seleziona"), fontWeight = FontWeight.Bold)
                        }
                        Icon(Icons.AutoMirrored.Outlined.ChevronRight, null, tint = GlowSilver.copy(alpha = 0.50f))
                    }
                    DropdownMenu(expanded = menu, onDismissRequest = { menu = false }) {
                        FoodCatalog.items.forEach { item ->
                            DropdownMenuItem(text = { Text(item.localizedName()) }, leadingIcon = { Icon(categoryIcon(item.category), null, tint = categoryAccent(item.category)) }, onClick = { foodId = item.id; menu = false; assessment = null })
                        }
                    }
                }
            }
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ScannerCommand(Modifier.weight(1f), Icons.Outlined.CameraAlt, localized("Capture", "التقاط", "Çek", "Capturer", "Capturar", "Aufnehmen", "Scatta"), RoyalGold) { camera.launch(null) }
                ScannerCommand(Modifier.weight(1f), Icons.Outlined.PhotoLibrary, localized("Import", "استيراد", "İçe aktar", "Importer", "Importar", "Importieren", "Importa"), ElectricCyan) { gallery.launch("image/*") }
            }
        }
        if (bitmap != null) item { Image(bitmap!!.asImageBitmap(), null, Modifier.fillMaxWidth().height(250.dp), contentScale = ContentScale.Crop) }
        if (report != null) item { SignalPanel(report) }
        item { SectionHeader(localized("Manual safety verification", "التحقق اليدوي من السلامة", "Manuel güvenlik kontrolü", "Vérification manuelle", "Verificación manual", "Manuelle Sicherheitsprüfung", "Verifica manuale"), "SAFETY CHECKLIST") }
        item { SafetyChecklist(check) { check = it; assessment = null } }
        item {
            Surface(Modifier.fillMaxWidth().clickable {
                val result = SafetyRules.evaluate(food, check)
                assessment = result
                historyStore.append(ScanHistoryStore.createRecord(food, result, report?.needsRetake))
            }, RoundedCornerShape(6.dp), RoyalGold, contentColor = Color(0xFF1B1200)) {
                Text(localized("RUN SAFETY ASSESSMENT", "تشغيل تقييم السلامة", "GÜVENLİK DEĞERLENDİRMESİ", "LANCER L’ÉVALUATION", "EJECUTAR EVALUACIÓN", "SICHERHEITSPRÜFUNG", "ESEGUI VALUTAZIONE"), Modifier.padding(14.dp), fontWeight = FontWeight.Black)
            }
        }
        if (assessment != null) item { AssessmentPanel(assessment!!) { shareAssessment(context, food, assessment!!) } }
        item { SafetyNotice() }
    }
}

@Composable
private fun ScannerCommand(modifier: Modifier, icon: ImageVector, title: String, accent: Color, onClick: () -> Unit) {
    Surface(modifier.clickable(onClick = onClick), RoundedCornerShape(6.dp), MaterialTheme.colorScheme.surface, border = BorderStroke(1.dp, accent.copy(alpha = 0.36f))) {
        Row(Modifier.padding(13.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = accent, modifier = Modifier.size(20.dp)); Spacer(Modifier.width(8.dp)); Text(title, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SignalPanel(report: ImageSignalReport) {
    val accent = if (report.needsRetake) Amber else Fresh
    Surface(Modifier.fillMaxWidth(), RoundedCornerShape(6.dp), MaterialTheme.colorScheme.surface, border = BorderStroke(1.dp, accent.copy(alpha = 0.30f))) {
        Column(Modifier.padding(13.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Row { Text("IMAGE SIGNAL", fontSize = 9.sp, color = accent, fontWeight = FontWeight.Black); Spacer(Modifier.weight(1f)); Text(if (report.needsRetake) "RETAKE" else "ACCEPTABLE", fontSize = 9.sp, color = accent, fontWeight = FontWeight.Black) }
            Text("LUMA ${report.averageLuminance}/255   •   SAT ${report.averageSaturation}/255   •   ${report.sampledPixels} SAMPLES", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            report.observations.forEach { Text("• $it", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall) }
            Text(localized("Image quality analysis is not a spoilage classifier.", "تحليل جودة الصورة ليس مصنفًا لفساد الطعام.", "Görüntü kalite analizi bozulma sınıflandırıcısı değildir.", "L’analyse d’image ne prouve pas l’altération.", "El análisis de imagen no clasifica deterioro.", "Bildanalyse ist kein Verderbnis-Klassifikator.", "L’analisi immagine non classifica il deterioramento."), color = Amber, fontSize = 11.sp)
        }
    }
}

@Composable
private fun SafetyChecklist(check: ManualSafetyCheck, onChange: (ManualSafetyCheck) -> Unit) {
    Surface(Modifier.fillMaxWidth(), RoundedCornerShape(6.dp), MaterialTheme.colorScheme.surface, border = BorderStroke(1.dp, GlowSilver.copy(alpha = 0.14f))) {
        Column {
            CheckRow(localized("Visible mold", "عفن ظاهر", "Görünür küf", "Moisissure visible", "Moho visible", "Sichtbarer Schimmel", "Muffa visibile"), check.visibleMold) { onChange(check.copy(visibleMold = it)) }
            CheckRow(localized("Slime or sticky film", "طبقة لزجة أو مخاطية", "Yapışkan tabaka", "Film visqueux", "Capa viscosa", "Schleimige Schicht", "Patina viscida"), check.slimeOrStickyFilm) { onChange(check.copy(slimeOrStickyFilm = it)) }
            CheckRow(localized("Fermented / rotten odor", "رائحة تخمر أو تعفن", "Fermente / çürük koku", "Odeur fermentée", "Olor fermentado", "Fauliger Geruch", "Odore fermentato"), check.fermentedOrRottenOdor) { onChange(check.copy(fermentedOrRottenOdor = it)) }
            CheckRow(localized("Leaking or bulging package", "عبوة منتفخة أو متسربة", "Şişmiş ambalaj", "Emballage gonflé", "Envase hinchado", "Aufgeblähte Verpackung", "Confezione gonfia"), check.leakingOrBulgingPackage) { onChange(check.copy(leakingOrBulgingPackage = it)) }
            CheckRow(localized("Unsafe time / temperature", "وقت أو حرارة تخزين غير آمنة", "Güvensiz sıcaklık", "Temps/température à risque", "Tiempo/temperatura insegura", "Unsichere Temperatur", "Tempo/temperatura non sicuri"), check.unsafeTimeTemperatureHistory) { onChange(check.copy(unsafeTimeTemperatureHistory = it)) }
            CheckRow(localized("Storage history unknown", "تاريخ التخزين غير معروف", "Saklama geçmişi bilinmiyor", "Stockage inconnu", "Almacenamiento desconocido", "Lagerung unbekannt", "Conservazione sconosciuta"), check.unknownStorageHistory) { onChange(check.copy(unknownStorageHistory = it)) }
        }
    }
}

@Composable
private fun CheckRow(label: String, checked: Boolean, onChecked: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth().clickable { onChecked(!checked) }.padding(horizontal = 10.dp, vertical = 3.dp), verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = checked, onCheckedChange = onChecked); Spacer(Modifier.width(5.dp)); Text(label, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun AssessmentPanel(assessment: SafetyAssessment, onShare: () -> Unit) {
    val accent = verdictColor(assessment.verdict)
    Surface(Modifier.fillMaxWidth(), RoundedCornerShape(6.dp), MaterialTheme.colorScheme.surface, border = BorderStroke(1.dp, accent.copy(alpha = 0.42f))) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("ASSESSMENT", fontSize = 9.sp, color = accent, fontWeight = FontWeight.Black); Spacer(Modifier.weight(1f)); IconButton(onClick = onShare, modifier = Modifier.size(32.dp)) { Icon(Icons.Outlined.Share, null, tint = GlowSilver) }
            }
            Text(verdictLabel(assessment.verdict), fontSize = 21.sp, fontWeight = FontWeight.Black, color = accent)
            Text("${assessment.redFlagCount} RED FLAGS   •   ${assessment.uncertaintyCount} UNCERTAINTY", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
            assessment.reasons.forEach { Text("• $it", color = MaterialTheme.colorScheme.onSurfaceVariant) }
        }
    }
}

@Composable
private fun EnterpriseHistory(modifier: Modifier, onFood: (String) -> Unit) {
    val context = LocalContext.current
    val store = remember { ScanHistoryStore(context) }
    var records by remember { mutableStateOf(store.load()) }
    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                ScreenHeader(localized("Inspection history", "سجل الفحوصات", "İnceleme geçmişi", "Historique des inspections", "Historial de inspecciones", "Prüfverlauf", "Cronologia ispezioni"), "LOCAL / ${records.size} RECORDS", Modifier.weight(1f))
                if (records.isNotEmpty()) TextButton(onClick = { store.clear(); records = emptyList() }) { Text(localized("Clear", "مسح", "Temizle", "Effacer", "Borrar", "Löschen", "Cancella"), color = Danger) }
            }
        }
        if (records.isEmpty()) item { EmptyPanel(localized("No inspections recorded yet.", "لا توجد فحوصات مسجلة حتى الآن.", "Henüz kayıt yok.", "Aucune inspection enregistrée.", "No hay inspecciones registradas.", "Noch keine Prüfungen.", "Nessuna ispezione registrata.")) }
        items(records, key = { it.id }) { record -> HistoryRow(record) { if (record.foodId.isNotBlank()) onFood(record.foodId) } }
    }
}

@Composable
private fun HistoryRow(record: ScanRecord, onClick: () -> Unit) {
    val context = LocalContext.current
    val accent = verdictColor(record.verdict)
    val date = remember(record.timestampEpochMs) {
        val d = Date(record.timestampEpochMs)
        "${DateFormat.getMediumDateFormat(context).format(d)}  ${DateFormat.getTimeFormat(context).format(d)}"
    }
    Surface(Modifier.fillMaxWidth().clickable(onClick = onClick), RoundedCornerShape(6.dp), MaterialTheme.colorScheme.surface, border = BorderStroke(1.dp, GlowSilver.copy(alpha = 0.13f))) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.width(3.dp).height(74.dp).background(accent))
            Column(Modifier.padding(12.dp).weight(1f)) {
                Text(record.foodNameSnapshot, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(date, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("${record.redFlagCount} FLAGS  •  ${record.uncertaintyCount} UNCERTAINTY", fontSize = 9.sp, color = GlowSilver.copy(alpha = 0.56f))
            }
            Text(verdictLabel(record.verdict), Modifier.padding(12.dp), fontSize = 10.sp, color = accent, fontWeight = FontWeight.Black)
        }
    }
}

@Composable
private fun EnterpriseSettings(modifier: Modifier, themeMode: EnterpriseTheme, onThemeMode: (EnterpriseTheme) -> Unit) {
    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { ScreenHeader(stringResource(R.string.settings), "NEXVARY / FOODGUARD") }
        item { SectionHeader(localized("Appearance", "المظهر", "Görünüm", "Apparence", "Apariencia", "Darstellung", "Aspetto"), "DISPLAY") }
        item {
            Surface(Modifier.fillMaxWidth(), RoundedCornerShape(6.dp), MaterialTheme.colorScheme.surface, border = BorderStroke(1.dp, GlowSilver.copy(alpha = 0.14f))) {
                Column {
                    ThemeRow(Icons.Outlined.LightMode, localized("Light", "فاتح", "Açık", "Clair", "Claro", "Hell", "Chiaro"), themeMode == EnterpriseTheme.LIGHT) { onThemeMode(EnterpriseTheme.LIGHT) }
                    ThemeRow(Icons.Outlined.DarkMode, localized("Dark", "داكن", "Koyu", "Sombre", "Oscuro", "Dunkel", "Scuro"), themeMode == EnterpriseTheme.DARK) { onThemeMode(EnterpriseTheme.DARK) }
                    ThemeRow(Icons.Outlined.Settings, localized("System", "النظام", "Sistem", "Système", "Sistema", "System", "Sistema"), themeMode == EnterpriseTheme.SYSTEM) { onThemeMode(EnterpriseTheme.SYSTEM) }
                }
            }
        }
        item { SectionHeader(localized("Product integrity", "نزاهة المنتج", "Ürün bütünlüğü", "Intégrité du produit", "Integridad del producto", "Produktintegrität", "Integrità prodotto"), "ENGINEERING") }
        item {
            Surface(Modifier.fillMaxWidth(), RoundedCornerShape(6.dp), MaterialTheme.colorScheme.surface, border = BorderStroke(1.dp, ElectricCyan.copy(alpha = 0.20f))) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SettingsLine(Icons.Outlined.HealthAndSafety, localized("Offline-first inspection", "فحص محلي أولاً", "Çevrimdışı öncelikli", "Inspection hors ligne", "Inspección local", "Offline-Prüfung", "Ispezione offline"), Fresh)
                    SettingsLine(Icons.Outlined.Info, localized("Open-source benchmark documented", "مرجع المصادر المفتوحة موثق", "Açık kaynak kıyaslaması belgeli", "Benchmark open source documenté", "Benchmark open source documentado", "Open-Source-Benchmark dokumentiert", "Benchmark open source documentato"), ElectricCyan)
                    SettingsLine(Icons.Outlined.WarningAmber, localized("No image-only safety claims", "لا توجد ادعاءات سلامة من الصورة فقط", "Yalnız görselle güvenlik iddiası yok", "Aucune preuve de sécurité par image seule", "Sin afirmaciones de seguridad solo por imagen", "Keine Sicherheitsbehauptung nur per Bild", "Nessuna certezza solo da immagine"), Amber)
                }
            }
        }
        item { SafetyNotice() }
    }
}

@Composable
private fun EnterpriseAbout(modifier: Modifier, onBack: () -> Unit) {
    val context = LocalContext.current
    LazyColumn(
        modifier.fillMaxSize(),
        contentPadding = PaddingValues(18.dp),
        verticalArrangement = Arrangement.spacedBy(11.dp)
    ) {
        item { BackHeader(localized("About", "عنا", "Hakkında", "À propos", "Acerca de", "Über", "Informazioni"), onBack) }
        item {
            Surface(
                Modifier.fillMaxWidth(),
                RoundedCornerShape(8.dp),
                MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, RoyalGold.copy(alpha = 0.28f))
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                    Text("NEXVARY", color = RoyalGold, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                    Text("NEXVARY FoodGuard", fontSize = 22.sp, fontWeight = FontWeight.Black)
                    Text(
                        localized(
                            "Local-first food intelligence and visible-spoilage decision support.",
                            "منصة محلية أولاً لمعلومات الغذاء ودعم تقييم علامات التلف الظاهرة.",
                            "Yerel öncelikli gıda bilgisi ve görünür bozulma karar desteği.",
                            "Plateforme locale d’aide à l’évaluation des signes visibles d’altération.",
                            "Plataforma local de información alimentaria y apoyo ante deterioro visible.",
                            "Lokale Lebensmittelinformation und Entscheidungshilfe bei sichtbarem Verderb.",
                            "Piattaforma locale per informazioni alimentari e supporto sul deterioramento visibile."
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        item { SectionHeader(localized("Official links", "الروابط الرسمية", "Resmî bağlantılar", "Liens officiels", "Enlaces oficiales", "Offizielle Links", "Link ufficiali"), "NEXVARY") }
        item { AboutLinkRow(Icons.Outlined.Language, "Website", "https://nexvary.com/", ElectricCyan) { openExternal(context, "https://nexvary.com/") } }
        item { AboutLinkRow(Icons.Outlined.Share, "Facebook", "facebook.com/share/14p9krEn5ij/", ElectricBlue) { openExternal(context, "https://www.facebook.com/share/14p9krEn5ij/") } }
        item { AboutLinkRow(Icons.Outlined.Email, "Email", "info@nexvary.com", RoyalGold) { openExternal(context, "mailto:info@nexvary.com") } }
        item { AboutLinkRow(Icons.Outlined.PhotoLibrary, "YouTube", "youtube.com/@NexvaryInc", Danger) { openExternal(context, "https://www.youtube.com/@NexvaryInc") } }
        item { AboutLinkRow(Icons.Outlined.Share, "X", "x.com/Nexvary", GlowSilver) { openExternal(context, "https://x.com/Nexvary") } }
        item { SafetyNotice() }
    }
}

@Composable
private fun AboutLinkRow(icon: ImageVector, title: String, value: String, accent: Color, onClick: () -> Unit) {
    Surface(
        Modifier.fillMaxWidth().clickable(onClick = onClick),
        RoundedCornerShape(7.dp),
        MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, GlowSilver.copy(alpha = 0.14f))
    ) {
        Row(Modifier.padding(13.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(38.dp).border(1.dp, accent.copy(alpha = 0.40f), RoundedCornerShape(5.dp)), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = accent, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.width(11.dp))
            Column(Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold)
                Text(value, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Icon(Icons.AutoMirrored.Outlined.ChevronRight, null, tint = GlowSilver.copy(alpha = 0.50f), modifier = Modifier.size(18.dp))
        }
    }
}

private fun openExternal(context: Context, uri: String) {
    runCatching {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
    }
}

@Composable
private fun ThemeRow(icon: ImageVector, label: String, selected: Boolean, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().clickable(onClick = onClick).padding(13.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = if (selected) RoyalGold else GlowSilver.copy(alpha = 0.64f), modifier = Modifier.size(20.dp)); Spacer(Modifier.width(10.dp)); Text(label, Modifier.weight(1f), fontWeight = FontWeight.SemiBold); if (selected) Text("ACTIVE", fontSize = 9.sp, color = RoyalGold, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun SettingsLine(icon: ImageVector, text: String, accent: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) { Icon(icon, null, tint = accent, modifier = Modifier.size(19.dp)); Spacer(Modifier.width(9.dp)); Text(text, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f)) }
}

@Composable
private fun SafetyNotice() {
    Surface(Modifier.fillMaxWidth(), RoundedCornerShape(6.dp), MaterialTheme.colorScheme.surface, border = BorderStroke(1.dp, Amber.copy(alpha = 0.28f))) {
        Row(Modifier.padding(13.dp), verticalAlignment = Alignment.Top) {
            Icon(Icons.Outlined.WarningAmber, null, tint = Amber, modifier = Modifier.size(19.dp)); Spacer(Modifier.width(9.dp)); Column { Text(localized("SAFETY LIMIT", "حدود السلامة", "GÜVENLİK SINIRI", "LIMITE DE SÉCURITÉ", "LÍMITE DE SEGURIDAD", "SICHERHEITSGRENZE", "LIMITE DI SICUREZZA"), fontSize = 9.sp, color = Amber, fontWeight = FontWeight.Black); Text(stringResource(R.string.safety_note), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        }
    }
}

@Composable
private fun ScreenHeader(title: String, code: String, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(3.dp)) { Text(code, fontSize = 9.sp, color = ElectricCyan, fontWeight = FontWeight.Black, letterSpacing = 1.1.sp); Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black) }
}

@Composable
private fun BackHeader(title: String, onBack: () -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Outlined.ArrowBack, stringResource(R.string.back), tint = RoyalGold) }
        Spacer(Modifier.width(4.dp)); Column(Modifier.weight(1f)) { Text(localized("FOOD INTELLIGENCE RECORD", "سجل معلومات الغذاء", "GIDA BİLGİ KAYDI", "FICHE INTELLIGENCE ALIMENTAIRE", "REGISTRO DE INFORMACIÓN ALIMENTARIA", "LEBENSMITTEL-INFORMATIONSAKTE", "SCHEDA INFORMAZIONI ALIMENTARI"), fontSize = 9.sp, color = ElectricCyan, fontWeight = FontWeight.Black); Text(title, fontWeight = FontWeight.Black, fontSize = 20.sp, maxLines = 1, overflow = TextOverflow.Ellipsis) }
    }
}

@Composable
private fun EmptyPanel(text: String) {
    Surface(Modifier.fillMaxWidth(), RoundedCornerShape(6.dp), MaterialTheme.colorScheme.surface, border = BorderStroke(1.dp, GlowSilver.copy(alpha = 0.12f))) { Text(text, Modifier.padding(18.dp), color = MaterialTheme.colorScheme.onSurfaceVariant) }
}

@Composable
private fun ThinRule(color: Color) { Box(Modifier.fillMaxWidth().height(1.dp).background(color)) }

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

private fun categoryIcon(category: FoodCategory): ImageVector = when (category) {
    FoodCategory.FRUIT -> Icons.Outlined.Eco
    FoodCategory.VEGETABLE -> Icons.Outlined.Spa
    FoodCategory.MEAT -> Icons.Outlined.LunchDining
    FoodCategory.POULTRY -> Icons.Outlined.EggAlt
    FoodCategory.SEAFOOD -> Icons.Outlined.SetMeal
    FoodCategory.DAIRY -> Icons.Outlined.LocalDrink
    FoodCategory.BAKERY -> Icons.Outlined.BakeryDining
    FoodCategory.PREPARED -> Icons.Outlined.Restaurant
    FoodCategory.DRINK -> Icons.Outlined.LocalCafe
    FoodCategory.PACKAGED -> Icons.Outlined.Inventory2
}

private fun categoryAccent(category: FoodCategory): Color = when (category) {
    FoodCategory.FRUIT -> Fresh
    FoodCategory.VEGETABLE -> Color(0xFF77FFB0)
    FoodCategory.MEAT -> Color(0xFFFF6C7D)
    FoodCategory.POULTRY -> RoyalGold
    FoodCategory.SEAFOOD -> ElectricCyan
    FoodCategory.DAIRY -> GlowSilver
    FoodCategory.BAKERY -> Amber
    FoodCategory.PREPARED -> ElectricBlue
    FoodCategory.DRINK -> Color(0xFF74D7FF)
    FoodCategory.PACKAGED -> Color(0xFFB8C8D1)
}

private fun riskColor(risk: RiskTier): Color = when (risk) { RiskTier.LOW -> Fresh; RiskTier.MEDIUM -> Amber; RiskTier.HIGH -> Danger }
private fun riskLabel(risk: RiskTier): String = when (risk) {
    RiskTier.LOW -> localized("Low", "منخفض", "Düşük", "Faible", "Bajo", "Niedrig", "Basso")
    RiskTier.MEDIUM -> localized("Medium", "متوسط", "Orta", "Moyen", "Medio", "Mittel", "Medio")
    RiskTier.HIGH -> localized("High", "مرتفع", "Yüksek", "Élevé", "Alto", "Hoch", "Alto")
}
private fun verdictColor(verdict: SafetyVerdict): Color = when (verdict) { SafetyVerdict.NO_VISIBLE_RED_FLAGS -> Fresh; SafetyVerdict.CAUTION -> Amber; SafetyVerdict.DISCARD -> Danger; SafetyVerdict.INSUFFICIENT_INFORMATION -> ElectricBlue }
private fun verdictLabel(verdict: SafetyVerdict): String = when (verdict) {
    SafetyVerdict.NO_VISIBLE_RED_FLAGS -> localized("No visible red flags", "لا توجد علامات خطر ظاهرة", "Görünür risk yok", "Aucun signal visible", "Sin señales visibles", "Keine sichtbaren Warnzeichen", "Nessun segnale visibile")
    SafetyVerdict.CAUTION -> localized("Caution", "حذر", "Dikkat", "Prudence", "Precaución", "Vorsicht", "Attenzione")
    SafetyVerdict.DISCARD -> localized("Discard", "تخلص منه", "At", "Jeter", "Desechar", "Entsorgen", "Scartare")
    SafetyVerdict.INSUFFICIENT_INFORMATION -> localized("Insufficient information", "معلومات غير كافية", "Yetersiz bilgi", "Informations insuffisantes", "Información insuficiente", "Unzureichende Informationen", "Informazioni insufficienti")
}
private fun referenceStateLabel(state: FoodReferenceState): String = when (state) {
    FoodReferenceState.HEALTHY -> localized("Healthy", "سليم", "Sağlıklı", "Sain", "Sano", "Normal", "Sano")
    FoodReferenceState.RIPE -> localized("Ripe", "ناضج", "Olgun", "Mûr", "Maduro", "Reif", "Maturo")
    FoodReferenceState.OVERRIPE -> localized("Overripe", "مفرط النضج", "Aşırı olgun", "Trop mûr", "Muy maduro", "Überreif", "Troppo maturo")
    FoodReferenceState.SPOILAGE -> localized("Spoilage", "تلف", "Bozulma", "Altération", "Deterioro", "Verderb", "Deterioramento")
}

private fun shareAssessment(context: Context, food: FoodItem?, assessment: SafetyAssessment) {
    val body = buildString {
        append("NEXVARY FoodGuard\n")
        append(food?.localizedName() ?: "Food")
        append("\n")
        append(verdictLabel(assessment.verdict))
        append("\nRed flags: ${assessment.redFlagCount}\nUncertainty: ${assessment.uncertaintyCount}\n")
        assessment.reasons.forEach { append("• $it\n") }
        append("\nImages cannot prove microbiological safety.")
    }
    val intent = Intent(Intent.ACTION_SEND).apply { type = "text/plain"; putExtra(Intent.EXTRA_TEXT, body) }
    context.startActivity(Intent.createChooser(intent, "NEXVARY FoodGuard"))
}

private fun localized(en: String, ar: String, tr: String, fr: String, es: String, de: String, it: String): String = when (Locale.getDefault().language.lowercase(Locale.ROOT)) {
    "ar" -> ar; "tr" -> tr; "fr" -> fr; "es" -> es; "de" -> de; "it" -> it; else -> en
}
