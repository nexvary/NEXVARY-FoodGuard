package com.nexvary.foodguard.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Navy = Color(0xFF050C14)
val Gunmetal = Color(0xFF15242C)
val Platinum = Color(0xFFF2F7FA)
val GlowSilver = Color(0xFFDDE9F0)
val Fresh = Color(0xFF3CFF9E)
val FreshDeep = Color(0xFF087B56)
val Gold = Color(0xFFFFC94A)
val RoyalGold = Color(0xFFFFD76A)
val Amber = Color(0xFFFFAE35)
val Danger = Color(0xFFFF4F67)
val ElectricBlue = Color(0xFF42B9FF)
val ElectricCyan = Color(0xFF40F4FF)
val ElectricViolet = Color(0xFFAE7CFF)

private val DarkColors = darkColorScheme(
    primary = RoyalGold,
    onPrimary = Color(0xFF1D1300),
    primaryContainer = Color(0xFF3B2A00),
    onPrimaryContainer = RoyalGold,
    secondary = ElectricCyan,
    onSecondary = Color(0xFF001417),
    secondaryContainer = Color(0xFF2A2105),
    onSecondaryContainer = RoyalGold,
    tertiary = GlowSilver,
    onTertiary = Color(0xFF0A1218),
    tertiaryContainer = Color(0xFF102A36),
    onTertiaryContainer = ElectricCyan,
    background = Navy,
    onBackground = Platinum,
    surface = Color(0xFF0B1721),
    onSurface = Platinum,
    surfaceVariant = Gunmetal,
    onSurfaceVariant = GlowSilver,
    outline = Color(0xFF8EA5B1),
    outlineVariant = Color(0xFF405763),
    error = Danger,
    onError = Color(0xFF2A0000)
)

private val LightColors = lightColorScheme(
    primary = Color(0xFF8A6500),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFE9A8),
    onPrimaryContainer = Color(0xFF2D2100),
    secondary = Color(0xFF006D77),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFE8A6),
    onSecondaryContainer = Color(0xFF5A4100),
    tertiary = Color(0xFF455A64),
    background = Color(0xFFF4F7F9),
    onBackground = Color(0xFF111B22),
    surface = Color.White,
    onSurface = Color(0xFF111B22),
    surfaceVariant = Color(0xFFE7EDF1),
    onSurfaceVariant = Color(0xFF3F5059),
    error = Color(0xFFB3261E),
    onError = Color.White
)

private val FoodGuardTypography = Typography().run {
    copy(
        headlineMedium = headlineMedium.copy(fontSize = 30.sp, lineHeight = 36.sp, fontWeight = FontWeight.Black),
        headlineSmall = headlineSmall.copy(fontSize = 25.sp, lineHeight = 31.sp, fontWeight = FontWeight.ExtraBold),
        titleLarge = titleLarge.copy(fontSize = 22.sp, lineHeight = 28.sp, fontWeight = FontWeight.ExtraBold),
        titleMedium = titleMedium.copy(fontSize = 18.sp, lineHeight = 24.sp, fontWeight = FontWeight.Bold),
        bodyLarge = bodyLarge.copy(fontSize = 17.sp, lineHeight = 25.sp),
        bodyMedium = bodyMedium.copy(fontSize = 15.5.sp, lineHeight = 23.sp),
        bodySmall = bodySmall.copy(fontSize = 13.5.sp, lineHeight = 19.sp),
        labelLarge = labelLarge.copy(fontWeight = FontWeight.Bold)
    )
}

@Composable
fun FoodGuardTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = FoodGuardTypography,
        content = content
    )
}
