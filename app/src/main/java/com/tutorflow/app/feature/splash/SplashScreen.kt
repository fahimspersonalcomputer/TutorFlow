package com.tutorflow.app.feature.splash

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tutorflow.app.R
import com.tutorflow.app.core.designsystem.Brand
import com.tutorflow.app.core.designsystem.GoldShineGradient
import com.tutorflow.app.core.designsystem.HeroGradient
import kotlinx.coroutines.delay

private const val LOGO_ASPECT = 0.836f // width / height of the trimmed original logo

/**
 * Navy hero splash. The original logo is shown untouched on a white panel (its navy wordmark
 * would be invisible on navy). Waits until preferences are loaded, then hands over.
 */
@Composable
fun SplashScreen(ready: Boolean, onFinished: () -> Unit) {
    val finish by rememberUpdatedState(onFinished)
    var started by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { started = true }
    LaunchedEffect(ready) {
        if (ready) {
            delay(1900)
            finish()
        }
    }

    val logoAlpha by animateFloatAsState(if (started) 1f else 0f, tween(700), label = "logoAlpha")
    val logoScale by animateFloatAsState(
        targetValue = if (started) 1f else 0.85f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "logoScale",
    )
    val textAlpha by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(durationMillis = 900, delayMillis = 500),
        label = "textAlpha",
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HeroGradient),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .alpha(logoAlpha)
                    .scale(logoScale)
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color.White)
                    .padding(24.dp),
            ) {
                Image(
                    painter = painterResource(R.drawable.tutorflow_logo),
                    contentDescription = stringResource(R.string.logo_description),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(190.dp)
                        .aspectRatio(LOGO_ASPECT),
                )
            }
            Spacer(Modifier.height(28.dp))
            Box(
                modifier = Modifier
                    .alpha(textAlpha)
                    .width(120.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(GoldShineGradient),
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = stringResource(R.string.app_subtitle),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(textAlpha),
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.app_tagline),
                color = Brand.Gold,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(textAlpha),
            )
        }
    }
}
