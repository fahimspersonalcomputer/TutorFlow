package com.tutorflow.app.feature.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tutorflow.app.R
import com.tutorflow.app.core.designsystem.GlassButton
import com.tutorflow.app.core.designsystem.GlassSurface
import com.tutorflow.app.core.designsystem.TF
import com.tutorflow.app.data.prefs.AppLanguage
import com.tutorflow.app.data.prefs.OnboardingProfile
import com.tutorflow.app.data.prefs.ThemeMode
import com.tutorflow.app.feature.settings.LanguageChoice
import com.tutorflow.app.feature.settings.ThemeChoice

/** First-run setup. Only the teacher name is mandatory; no account and no network are used. */
@Composable
fun OnboardingScreen(
    currentTheme: ThemeMode,
    onLanguage: (AppLanguage) -> Unit,
    onTheme: (ThemeMode) -> Unit,
    onFinish: (OnboardingProfile) -> Unit,
) {
    var teacherName by rememberSaveable { mutableStateOf("") }
    var institution by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var session by rememberSaveable { mutableStateOf("") }
    var showNameError by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Image(
            painter = painterResource(R.drawable.tutorflow_logo),
            contentDescription = stringResource(R.string.logo_description),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(120.dp)
                .align(Alignment.CenterHorizontally),
        )
        Text(
            text = stringResource(R.string.onb_title),
            color = TF.colors.textPrimary,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = stringResource(R.string.onb_subtitle),
            color = TF.colors.textSecondary,
            fontSize = 14.sp,
        )

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

        OutlinedTextField(
            value = teacherName,
            onValueChange = {
                teacherName = it
                if (it.isNotBlank()) showNameError = false
            },
            label = { Text(stringResource(R.string.onb_teacher_name)) },
            singleLine = true,
            isError = showNameError,
            supportingText = if (showNameError) {
                { Text(stringResource(R.string.onb_name_required)) }
            } else {
                null
            },
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = institution,
            onValueChange = { institution = it },
            label = { Text(stringResource(R.string.onb_institution)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text(stringResource(R.string.onb_address)) },
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text(stringResource(R.string.onb_phone)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.onb_email)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = session,
            onValueChange = { session = it },
            label = { Text(stringResource(R.string.onb_session)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        GlassButton(
            text = stringResource(R.string.onb_start),
            onClick = {
                if (teacherName.isBlank()) {
                    showNameError = true
                } else {
                    onFinish(
                        OnboardingProfile(
                            teacherName = teacherName,
                            institutionName = institution,
                            institutionAddress = address,
                            phone = phone,
                            email = email,
                            session = session,
                        ),
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
