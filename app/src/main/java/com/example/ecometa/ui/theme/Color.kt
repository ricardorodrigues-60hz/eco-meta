package com.example.ecometa.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Paleta de Cores Extraída (Fidelidade ao .tsx)
val EcoGreenPrimary = Color(0xFF34A853)
val EcoGreenDark = Color(0xFF2E7D32)
val EcoGreenLight = Color(0xFFE8F5E9)

val EcoSurface = Color(0xFFF8F9FA)
val EcoBackground = Color(0xFFFFFFFF)

val EcoTextPrimary = Color(0xFF2D3436)
val EcoTextSecondary = Color(0xFF636E72)

val EcoPointsGold = Color(0xFFFFD700)
val EcoCO2Blue = Color(0xFF00B8D9)

// Gradientes Estilizados
val EcoGradientPrimary = Brush.verticalGradient(
    colors = listOf(EcoGreenPrimary, EcoGreenDark)
)

val EcoGradientSurface = Brush.verticalGradient(
    colors = listOf(EcoBackground, EcoSurface)
)

// Material 3 Color Scheme
val LightColorScheme = androidx.compose.material3.lightColorScheme(
    primary = EcoGreenPrimary,
    onPrimary = Color.White,
    primaryContainer = EcoGreenLight,
    onPrimaryContainer = EcoGreenDark,
    secondary = EcoGreenDark,
    onSecondary = Color.White,
    surface = EcoSurface,
    onSurface = EcoTextPrimary,
    background = EcoBackground,
    onBackground = EcoTextPrimary,
    outline = EcoTextSecondary.copy(alpha = 0.5f)
)
