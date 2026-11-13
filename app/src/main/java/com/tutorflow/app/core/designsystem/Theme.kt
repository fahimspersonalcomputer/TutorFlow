package com.tutorflow.app.core.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun TutorFlowTheme(darkTheme: Boolean, content: @Composable () -> Unit) {
    val colors = if (darkTheme) DarkTutorFlowColors else LightTutorFlowColors
    val scheme = if (darkTheme) {
        darkColorScheme(
            primary = colors.brand,
            onPrimary = colors.onBrand,
            secondary = colors.accent,
            onSecondary = colors.onAccent,
            background = colors.background,
            onBackground = colors.textPrimary,
            surface = colors.surface,
            onSurface = colors.textPrimary,
            surfaceVariant = colors.card,
            onSurfaceVariant = colors.textSecondary,
            outline = colors.border,
            error = colors.error,
        )
    } else {
        lightColorScheme(
            primary = colors.brand,
            onPrimary = colors.onBrand,
            secondary = colors.accent,
            onSecondary = colors.onAccent,
            background = colors.background,
            onBackground = colors.textPrimary,
            surface = colors.surface,
            onSurface = colors.textPrimary,
            surfaceVariant = colors.card,
            onSurfaceVariant = colors.textSecondary,
            outline = colors.border,
            error = colors.error,
        )
    }
    CompositionLocalProvider(LocalTutorFlowColors provides colors) {
        MaterialTheme(colorScheme = scheme, typography = Typography(), content = content)
    }
}

/** Screens read colours only through TF.colors, never through hex literals. */
object TF {
    val colors: TutorFlowColors
        @Composable
        @ReadOnlyComposable
        get() = LocalTutorFlowColors.current
}
