package com.tutorflow.app.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tutorflow.app.R
import com.tutorflow.app.core.designsystem.GlassChip
import com.tutorflow.app.core.designsystem.GlassSurface
import com.tutorflow.app.core.designsystem.ScreenTitleBar
import com.tutorflow.app.core.designsystem.TF
import com.tutorflow.app.data.prefs.AppLanguage
import com.tutorflow.app.data.prefs.ThemeMode

@Composable
fun SettingsScreen(
    currentTheme: ThemeMode,
    onLanguage: (AppLanguage) -> Unit,
    onTheme: (ThemeMode) -> Unit,
    onBack: () -> Unit,
) {
    Column(Modifier.fillMaxSize()) {
        ScreenTitleBar(title = stringResource(R.string.settings_title), onBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            GlassSurface(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    LanguageChoice(onLanguage)
                }
            }
            GlassSurface(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ThemeChoice(currentTheme, onTheme)
                }
            }
            Text(
                text = stringResource(R.string.settings_note),
                color = TF.colors.textSecondary,
                fontSize = 13.sp,
            )
        }
    }
}

/** Language selector shared by Settings and Onboarding. Reflects the language actually in use. */
@Composable
fun LanguageChoice(onLanguage: (AppLanguage) -> Unit) {
    val isBangla = LocalConfiguration.current.locales[0].language == "bn"
    Text(
        text = stringResource(R.string.settings_language),
        color = TF.colors.textPrimary,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
    )
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        GlassChip(
            text = stringResource(R.string.lang_bn),
            selected = isBangla,
            onClick = { onLanguage(AppLanguage.BANGLA) },
            modifier = Modifier.weight(1f),
        )
        GlassChip(
            text = stringResource(R.string.lang_en),
            selected = !isBangla,
            onClick = { onLanguage(AppLanguage.ENGLISH) },
            modifier = Modifier.weight(1f),
        )
    }
}

/** Theme selector shared by Settings and Onboarding. */
@Composable
fun ThemeChoice(currentTheme: ThemeMode, onTheme: (ThemeMode) -> Unit) {
    Text(
        text = stringResource(R.string.settings_theme),
        color = TF.colors.textPrimary,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
    )
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        GlassChip(
            text = stringResource(R.string.theme_system),
            selected = currentTheme == ThemeMode.SYSTEM,
            onClick = { onTheme(ThemeMode.SYSTEM) },
            modifier = Modifier.weight(1f),
        )
        GlassChip(
            text = stringResource(R.string.theme_light),
            selected = currentTheme == ThemeMode.LIGHT,
            onClick = { onTheme(ThemeMode.LIGHT) },
            modifier = Modifier.weight(1f),
        )
        GlassChip(
            text = stringResource(R.string.theme_dark),
            selected = currentTheme == ThemeMode.DARK,
            onClick = { onTheme(ThemeMode.DARK) },
            modifier = Modifier.weight(1f),
        )
    }
}
