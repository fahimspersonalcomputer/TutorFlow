package com.tutorflow.app.core.designsystem

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Opacity floors per glass level. They are chosen so text keeps the contrast measured in
 * docs/04-design-system.md. Real backdrop blur is deliberately not used in Phase 2 (decision D11).
 */
enum class GlassLevel(val lightAlpha: Float, val darkAlpha: Float) {
    Card(0.80f, 0.62f),
    Bar(0.88f, 0.82f),
    Sheet(0.95f, 0.93f),
}

@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20.dp),
    level: GlassLevel = GlassLevel.Card,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    val c = TF.colors
    val fill = c.card.copy(alpha = if (c.isDark) level.darkAlpha else level.lightAlpha)
    val specular = Brush.linearGradient(
        listOf(
            Color.White.copy(alpha = if (c.isDark) 0.30f else 0.95f),
            c.border,
            Color.White.copy(alpha = if (c.isDark) 0.08f else 0.50f),
        ),
    )
    var m = modifier
        .shadow(
            elevation = if (c.isDark) 0.dp else 8.dp,
            shape = shape,
            ambientColor = c.brand.copy(alpha = 0.10f),
            spotColor = c.brand.copy(alpha = 0.14f),
        )
        .clip(shape)
        .background(fill)
        .border(1.dp, specular, shape)
    if (onClick != null) m = m.clickable(onClick = onClick)
    Box(modifier = m, content = content)
}

/** Static screen background: theme colour plus two soft gold/navy glows. No animated shaders. */
@Composable
fun GlassBackground(modifier: Modifier = Modifier) {
    val c = TF.colors
    Box(
        modifier
            .fillMaxSize()
            .background(c.background)
            .drawBehind {
                drawRect(
                    Brush.radialGradient(
                        colors = listOf(Brand.Gold.copy(alpha = if (c.isDark) 0.14f else 0.20f), Color.Transparent),
                        center = Offset(size.width * 0.9f, size.height * 0.05f),
                        radius = size.width * 0.9f,
                    ),
                )
                drawRect(
                    Brush.radialGradient(
                        colors = listOf(Brand.Navy.copy(alpha = if (c.isDark) 0.0f else 0.08f), Color.Transparent),
                        center = Offset(size.width * 0.05f, size.height * 0.7f),
                        radius = size.width,
                    ),
                )
            },
    )
}

@Composable
fun GlassButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    primary: Boolean = true,
) {
    val c = TF.colors
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (pressed) 0.97f else 1f, label = "buttonPress")
    val shape = RoundedCornerShape(999.dp)
    val look: Modifier = if (primary) {
        Modifier.background(GoldShineGradient, shape)
    } else {
        Modifier
            .background(c.card.copy(alpha = 0.80f), shape)
            .border(1.dp, c.border, shape)
    }
    Box(
        modifier = modifier
            .scale(scale)
            .heightIn(min = 52.dp)
            .clip(shape)
            .then(look)
            .clickable(interactionSource = interaction, indication = null, onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = if (primary) Brand.Navy else c.textPrimary,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
        )
    }
}
