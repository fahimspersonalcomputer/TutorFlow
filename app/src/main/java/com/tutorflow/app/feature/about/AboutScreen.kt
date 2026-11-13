package com.tutorflow.app.feature.about

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tutorflow.app.BuildConfig
import com.tutorflow.app.R
import com.tutorflow.app.core.designsystem.GlassButton
import com.tutorflow.app.core.designsystem.GlassSurface
import com.tutorflow.app.core.designsystem.ScreenTitleBar
import com.tutorflow.app.core.designsystem.TF

private const val LOGO_ASPECT = 0.836f

private const val FACEBOOK_URL = "https://www.facebook.com/abdullah.al.fahim247"
private const val INSTAGRAM_URL = "https://www.instagram.com/a.a.fahim.me"
private const val WHATSAPP_URL = "https://wa.me/8801764454377"
private const val EMAIL_URI = "mailto:abdullahalfahim247@gmail.com"

private fun openLink(context: Context, uri: String) {
    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
    } catch (_: ActivityNotFoundException) {
        // No app can open this link on this phone; nothing else to do.
    }
}

@Composable
fun AboutScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    Column(Modifier.fillMaxSize()) {
        ScreenTitleBar(title = stringResource(R.string.about_title), onBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Original logo, unmodified, on a white panel so its navy wordmark stays readable in dark mode.
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color.White)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(R.drawable.tutorflow_logo),
                    contentDescription = stringResource(R.string.logo_description),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(150.dp)
                        .aspectRatio(LOGO_ASPECT),
                )
            }
            Text(
                text = stringResource(R.string.app_subtitle),
                color = TF.colors.textPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
            )
            Text(
                text = stringResource(R.string.about_version, BuildConfig.VERSION_NAME),
                color = TF.colors.textSecondary,
                fontSize = 13.sp,
            )

            GlassSurface(Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(R.string.about_developer),
                        color = TF.colors.textSecondary,
                        fontSize = 13.sp,
                    )
                    Spacer(Modifier.height(12.dp))
                    Image(
                        painter = painterResource(R.drawable.developer_photo),
                        contentDescription = stringResource(R.string.about_developer_name),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = stringResource(R.string.about_developer_name),
                        color = TF.colors.textPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.about_qualification),
                        color = TF.colors.textSecondary,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = stringResource(R.string.about_institution),
                        color = TF.colors.textSecondary,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = stringResource(R.string.about_session),
                        color = TF.colors.textSecondary,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            GlassSurface(Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Text(
                        text = stringResource(R.string.about_contact),
                        color = TF.colors.textPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                    GlassButton(text = "Facebook", onClick = { openLink(context, FACEBOOK_URL) }, modifier = Modifier.fillMaxWidth(), primary = false)
                    GlassButton(text = "Instagram", onClick = { openLink(context, INSTAGRAM_URL) }, modifier = Modifier.fillMaxWidth(), primary = false)
                    GlassButton(text = "WhatsApp", onClick = { openLink(context, WHATSAPP_URL) }, modifier = Modifier.fillMaxWidth(), primary = false)
                    GlassButton(text = "Gmail", onClick = { openLink(context, EMAIL_URI) }, modifier = Modifier.fillMaxWidth(), primary = false)
                }
            }

            Text(
                text = stringResource(R.string.about_copyright),
                color = TF.colors.textSecondary,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(8.dp))
        }
    }
}
