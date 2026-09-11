package com.nexvary.foodguard.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Navy = Color(0xFF07131A)
val Gunmetal = Color(0xFF15242C)
val Platinum = Color(0xFFE9F0F2)
val Fresh = Color(0xFF50D890)
val FreshDeep = Color(0xFF0C7C59)
val Gold = Color(0xFFD4AF37)
val Amber = Color(0xFFF6B73C)
val Danger = Color(0xFFFF6B6B)
val ElectricBlue = Color(0xFF6BA3C8)

private val DarkColors = darkColorScheme(
    primary = Fresh,
    onPrimary = Color(0xFF04130C),
    secondary = Gold,
    onSecondary = Color(0xFF1D1600),
    tertiary = ElectricBlue,
    background = Navy,
    onBackground = Platinum,
    surface = Color(0xFF0D1D25),
    onSurface = Platinum,
    surfaceVariant = Gunmetal,
    onSurfaceVariant = Color(0xFFC6D3D8),
    error = Danger,
    onError = Color(0xFF2A0000)
)

private val LightColors = lightColorScheme(
    primary = FreshDeep,
    onPrimary = Color.White,
    secondary = Color(0xFF8B6A00),
    onSecondary = Color.White,
    tertiary = Color(0xFF315F7B),
    background = Color(0xFFF5F8F6),
    onBackground = Color(0xFF13211A),
    surface = Color.White,
    onSurface = Color(0xFF13211A),
    surfaceVariant = Color(0xFFE7EFEB),
    onSurfaceVariant = Color(0xFF40514A),
    error = Color(0xFFB3261E),
    onError = Color.White
)

@Composable
fun FoodGuardTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography(),
        content = content
    )
}
