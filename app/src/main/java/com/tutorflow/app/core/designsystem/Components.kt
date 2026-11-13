package com.tutorflow.app.core.designsystem

import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tutorflow.app.R

/** Vertical space screens must leave at the bottom so content is not hidden behind the floating tab bar. */
val TabBarSpace: Dp = 112.dp

data class TabItem(val route: String, @StringRes val label: Int, val icon: ImageVector)

/**
 * Floating glass capsule with the five main destinations plus a separate round Search button.
 * Original TutorFlow design inspired by the layered floating-bar idea of the supplied reference.
 */
@Composable
fun GlassTabBar(
    items: List<TabItem>,
    selectedRoute: String?,
    onSelect: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val c = TF.colors
    Row(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        GlassSurface(
            modifier = Modifier
                .weight(1f)
                .height(68.dp),
            shape = RoundedCornerShape(34.dp),
            level = GlassLevel.Bar,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                items.forEach { item ->
                    TabButton(
                        item = item,
                        selected = item.route == selectedRoute,
                        onClick = { onSelect(item.route) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
        Spacer(Modifier.width(10.dp))
        GlassSurface(
            modifier = Modifier.size(68.dp),
            shape = CircleShape,
            level = GlassLevel.Bar,
            onClick = onSearch,
        ) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = stringResource(R.string.nav_search),
                tint = c.textPrimary,
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}

@Composable
private fun TabButton(
    item: TabItem,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier,
) {
    val c = TF.colors
    val pill by animateColorAsState(
        targetValue = if (selected) c.accent else Color.Transparent,
        label = "tabPill",
    )
    val content = if (selected) c.onAccent else c.textSecondary
    Column(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(28.dp))
            .background(pill)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(imageVector = item.icon, contentDescription = null, tint = content, modifier = Modifier.size(24.dp))
        Text(
            text = stringResource(item.label),
            color = content,
            fontSize = 10.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun StatTile(label: String, value: String, modifier: Modifier = Modifier) {
    GlassSurface(modifier = modifier) {
        Column(Modifier.padding(16.dp)) {
            Text(text = value, color = TF.colors.textPrimary, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(text = label, color = TF.colors.textSecondary, fontSize = 13.sp)
        }
    }
}

/** Title row for full-screen pages. Shows a back button when onBack is given. */
@Composable
fun ScreenTitleBar(title: String, onBack: (() -> Unit)? = null, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (onBack != null) {
            GlassSurface(
                modifier = Modifier.size(44.dp),
                shape = CircleShape,
                level = GlassLevel.Bar,
                onClick = onBack,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = TF.colors.textPrimary,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
            Spacer(Modifier.width(12.dp))
        }
        Text(
            text = title,
            color = TF.colors.textPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}
