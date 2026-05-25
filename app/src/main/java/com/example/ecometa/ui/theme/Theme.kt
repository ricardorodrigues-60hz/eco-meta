package com.example.ecometa.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

/**
 * Tema Principal do EcoMeta (Material Design 3).
 * Centraliza as cores, tipografia e formas para garantir consistência visual.
 */
@Composable
fun EcoMetaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
