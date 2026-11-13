package com.tutorflow.app

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tutorflow.app.data.prefs.AppLanguage
import com.tutorflow.app.data.prefs.OnboardingProfile
import com.tutorflow.app.data.prefs.ThemeMode
import com.tutorflow.app.data.prefs.UserPrefs
import com.tutorflow.app.data.prefs.UserPrefsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class AppViewModel @Inject constructor(
    private val repository: UserPrefsRepository,
) : ViewModel() {

    /** null until DataStore has delivered its first value (splash waits for this). */
    val prefs: StateFlow<UserPrefs?> = repository.prefs
        .map<UserPrefs, UserPrefs?> { it }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun setTheme(mode: ThemeMode) {
        viewModelScope.launch { repository.setTheme(mode) }
    }

    fun setLanguage(language: AppLanguage) {
        viewModelScope.launch { repository.setLanguage(language) }
        // Applies immediately and is remembered by AndroidX across restarts.
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language.tag))
    }

    fun completeOnboarding(profile: OnboardingProfile) {
        viewModelScope.launch { repository.completeOnboarding(profile) }
    }
}
