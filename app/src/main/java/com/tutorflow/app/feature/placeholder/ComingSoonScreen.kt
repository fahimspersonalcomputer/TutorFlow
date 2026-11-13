package com.tutorflow.app.feature.placeholder

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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

/** Honest placeholder for modules that are planned but not built yet. It saves nothing. */
@Composable
fun ComingSoonScreen(
    title: String,
    icon: ImageVector,
    phase: String,
    onBack: (() -> Unit)?,
) {
    val isTab = onBack == null
    Column(Modifier.fillMaxSize()) {
        ScreenTitleBar(title = title, onBack = onBack)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .then(if (isTab) Modifier.padding(bottom = TabBarSpace) else Modifier.navigationBarsPadding()),
            contentAlignment = Alignment.Center,
        ) {
            GlassSurface(Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Brand.Gold),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(imageVector = icon, contentDescription = null, tint = Brand.Navy, modifier = Modifier.size(32.dp))
                    }
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.coming_title),
                        color = TF.colors.textPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.coming_body, title, phase),
                        color = TF.colors.textSecondary,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}
