package com.tutorflow.app

import android.graphics.Color as AndroidColor
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tutorflow.app.core.designsystem.TutorFlowTheme
import com.tutorflow.app.core.navigation.AppNav
import com.tutorflow.app.data.prefs.ThemeMode
import dagger.hilt.android.AndroidEntryPoint

// AppCompatActivity (not plain ComponentActivity) is required for per-app language switching.
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val appViewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { TutorFlowRoot(appViewModel) }
    }
}

@Composable
private fun TutorFlowRoot(viewModel: AppViewModel) {
    val prefs by viewModel.prefs.collectAsStateWithLifecycle()
    val systemDark = isSystemInDarkTheme()
    val dark = when (prefs?.themeMode ?: ThemeMode.SYSTEM) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> systemDark
    }

    // Keep status/navigation bar icon colours readable for the theme the user chose,
    // which may differ from the phone's system theme.
    val activity = LocalContext.current as ComponentActivity
    DisposableEffect(dark) {
        val style = if (dark) {
            SystemBarStyle.dark(AndroidColor.TRANSPARENT)
        } else {
            SystemBarStyle.light(AndroidColor.TRANSPARENT, AndroidColor.TRANSPARENT)
        }
        activity.enableEdgeToEdge(statusBarStyle = style, navigationBarStyle = style)
        onDispose { }
    }

    TutorFlowTheme(darkTheme = dark) {
        AppNav(viewModel = viewModel, prefs = prefs)
    }
}
