package com.tutorflow.app.core.designsystem

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/** Brand colours sampled from the official logo pixels (see docs/04-design-system.md). */
object Brand {
    val Navy = Color(0xFF0B1B3F)
    val Gold = Color(0xFFF5B942)
    val White = Color(0xFFFFFFFF)
    val AntiqueGold = Color(0xFF5D5340)
    val NavyLift = Color(0xFF13295C)
    val GoldLight = Color(0xFFFFD27A)
}

@Immutable
class TutorFlowColors(
    val isDark: Boolean,
    val background: Color,
    val surface: Color,
    val card: Color,
    val brand: Color,
    val onBrand: Color,
    val accent: Color,
    val onAccent: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val border: Color,
    val success: Color,
    val error: Color,
    val warning: Color,
)

val LightTutorFlowColors = TutorFlowColors(
    isDark = false,
    background = Color(0xFFF5F7FC),
    surface = Color(0xFFFFFFFF),
    card = Color(0xFFFFFFFF),
    brand = Brand.Navy,
    onBrand = Color(0xFFFFFFFF),
    accent = Brand.Gold,
    onAccent = Brand.Navy,
    textPrimary = Brand.Navy,
    textSecondary = Color(0xFF5B6B83), // small-text safe (5.06:1); spec #64748B is 4.44:1
    border = Color(0xFFE3E7EF),
    success = Color(0xFF1B7F4B),
    error = Color(0xFFC62828),
    warning = Color(0xFF8A5A00),
)

val DarkTutorFlowColors = TutorFlowColors(
    isDark = true,
    background = Color(0xFF08152E),
    surface = Color(0xFF101F3D),
    card = Color(0xFF142442),
    brand = Brand.Gold,
    onBrand = Brand.Navy,
    accent = Brand.Gold,
    onAccent = Brand.Navy,
    textPrimary = Color(0xFFFFFFFF),
    textSecondary = Color(0xFFB8C4D8),
    border = Color(0xFF34435D),
    success = Color(0xFF6FDC9B),
    error = Color(0xFFFF8A80),
    warning = Brand.Gold,
)

val LocalTutorFlowColors = staticCompositionLocalOf { LightTutorFlowColors }

val HeroGradient: Brush
    get() = Brush.linearGradient(listOf(Brand.Navy, Brand.NavyLift))

val GoldShineGradient: Brush
    get() = Brush.linearGradient(listOf(Brand.Gold, Brand.GoldLight))
