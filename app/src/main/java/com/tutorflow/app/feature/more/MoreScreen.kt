package com.tutorflow.app.feature.more

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tutorflow.app.R
import com.tutorflow.app.core.designsystem.Brand
import com.tutorflow.app.core.designsystem.GlassSurface
import com.tutorflow.app.core.designsystem.ScreenTitleBar
import com.tutorflow.app.core.designsystem.TF
import com.tutorflow.app.core.designsystem.TabBarSpace
import com.tutorflow.app.core.navigation.Feature

private class MoreEntry(
    @StringRes val label: Int,
    val icon: ImageVector,
    val onClick: () -> Unit,
)

@Composable
fun MoreScreen(
    onOpenFeature: (Feature) -> Unit,
    onSettings: () -> Unit,
    onAbout: () -> Unit,
) {
    val entries: List<MoreEntry> =
        Feature.moreHub.map { f -> MoreEntry(f.label, f.icon) { onOpenFeature(f) } } +
            MoreEntry(R.string.more_settings, Icons.Rounded.Settings, onSettings) +
            MoreEntry(R.string.more_about, Icons.Rounded.Info, onAbout)

    Column(Modifier.fillMaxSize()) {
        ScreenTitleBar(title = stringResource(R.string.more_title))
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = TabBarSpace),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(entries.chunked(2)) { pair ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    pair.forEach { entry -> MoreTile(entry, Modifier.weight(1f)) }
                    if (pair.size == 1) Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun MoreTile(entry: MoreEntry, modifier: Modifier) {
    GlassSurface(modifier = modifier.height(116.dp), onClick = entry.onClick) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Brand.Gold),
                contentAlignment = Alignment.Center,
            ) {
                Icon(imageVector = entry.icon, contentDescription = null, tint = Brand.Navy, modifier = Modifier.size(26.dp))
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = stringResource(entry.label),
                color = TF.colors.textPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                maxLines = 2,
            )
        }
    }
}
