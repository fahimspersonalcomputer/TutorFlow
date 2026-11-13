package com.tutorflow.app.feature.home

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tutorflow.app.R
import com.tutorflow.app.core.designsystem.Brand
import com.tutorflow.app.core.designsystem.GlassSurface
import com.tutorflow.app.core.designsystem.HeroGradient
import com.tutorflow.app.core.designsystem.StatTile
import com.tutorflow.app.core.designsystem.TF
import com.tutorflow.app.core.designsystem.TabBarSpace
import com.tutorflow.app.core.navigation.Feature

private val OverviewStats: List<Int> = listOf(
    R.string.dash_total_students,
    R.string.dash_active_students,
    R.string.dash_inactive_students,
    R.string.dash_classes,
    R.string.dash_batches,
    R.string.dash_today_attendance,
    R.string.dash_monthly_collection,
    R.string.dash_outstanding,
)

/**
 * Phase 2 dashboard skeleton: layout, theme, language and navigation are real; the figures show
 * an em dash because no student database exists yet. No number here is made up.
 */
@Composable
fun HomeScreen(teacherName: String, onOpenFeature: (Feature) -> Unit) {
    val firstName = teacherName.trim().substringBefore(' ')

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = TabBarSpace),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item { HeroHeader(firstName) }
        item { SectionTitle(R.string.dash_overview) }
        items(OverviewStats.chunked(2)) { pair ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                pair.forEach { label ->
                    StatTile(label = stringResource(label), value = "—", modifier = Modifier.weight(1f))
                }
                if (pair.size == 1) Spacer(Modifier.weight(1f))
            }
        }
        item {
            Text(
                text = stringResource(R.string.dash_not_connected),
                color = TF.colors.textSecondary,
                fontSize = 13.sp,
            )
        }
        item { SectionTitle(R.string.dash_quick_actions) }
        items(Feature.quickActions.chunked(3)) { row ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                row.forEach { feature ->
                    QuickActionCard(feature, onClick = { onOpenFeature(feature) }, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun HeroHeader(firstName: String) {
    Box(
        modifier = Modifier
            .statusBarsPadding()
            .padding(top = 8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(HeroGradient)
            .padding(20.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = if (firstName.isEmpty()) {
                        stringResource(R.string.greeting_welcome_no_name)
                    } else {
                        stringResource(R.string.greeting_welcome, firstName)
                    },
                    color = Brand.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.app_tagline),
                    color = Brand.Gold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
            Image(
                painter = painterResource(R.drawable.tutorflow_symbol),
                contentDescription = stringResource(R.string.logo_description),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(72.dp),
            )
        }
    }
}

@Composable
private fun SectionTitle(@StringRes title: Int) {
    Text(
        text = stringResource(title),
        color = TF.colors.textPrimary,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 6.dp),
    )
}

@Composable
private fun QuickActionCard(feature: Feature, onClick: () -> Unit, modifier: Modifier) {
    GlassSurface(modifier = modifier.height(112.dp), onClick = onClick) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Brand.Gold),
                contentAlignment = Alignment.Center,
            ) {
                Icon(imageVector = feature.icon, contentDescription = null, tint = Brand.Navy, modifier = Modifier.size(24.dp))
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(feature.label),
                color = TF.colors.textPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                maxLines = 2,
            )
        }
    }
}
