package com.tutorflow.app.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

enum class ThemeMode { SYSTEM, LIGHT, DARK }

enum class AppLanguage(val tag: String) {
    BANGLA("bn"),
    ENGLISH("en"),
}

data class UserPrefs(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val language: AppLanguage? = null,
    val onboardingDone: Boolean = false,
    val teacherName: String = "",
    val institutionName: String = "",
    val institutionAddress: String = "",
    val phone: String = "",
    val email: String = "",
    val session: String = "",
)

data class OnboardingProfile(
    val teacherName: String,
    val institutionName: String,
    val institutionAddress: String,
    val phone: String,
    val email: String,
    val session: String,
)

private val Context.tutorFlowDataStore: DataStore<Preferences> by preferencesDataStore(name = "tutorflow_prefs")

/**
 * Phase 2 keeps the teacher/institution profile here. In Phase 3 (task 3.3) it moves into the
 * Room `app_settings` row so that it travels inside backups.
 */
@Singleton
class UserPrefsRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private object Keys {
        val THEME = stringPreferencesKey("theme_mode")
        val LANGUAGE = stringPreferencesKey("language")
        val ONBOARDING_DONE = booleanPreferencesKey("onboarding_done")
        val TEACHER_NAME = stringPreferencesKey("teacher_name")
        val INSTITUTION_NAME = stringPreferencesKey("institution_name")
        val INSTITUTION_ADDRESS = stringPreferencesKey("institution_address")
        val PHONE = stringPreferencesKey("phone")
        val EMAIL = stringPreferencesKey("email")
        val SESSION = stringPreferencesKey("session")
    }

    val prefs: Flow<UserPrefs> = context.tutorFlowDataStore.data
        .catch { error ->
            if (error is IOException) emit(emptyPreferences()) else throw error
        }
        .map { p ->
            UserPrefs(
                themeMode = p[Keys.THEME]
                    ?.let { name -> ThemeMode.entries.firstOrNull { it.name == name } }
                    ?: ThemeMode.SYSTEM,
                language = p[Keys.LANGUAGE]
                    ?.let { name -> AppLanguage.entries.firstOrNull { it.name == name } },
                onboardingDone = p[Keys.ONBOARDING_DONE] ?: false,
                teacherName = p[Keys.TEACHER_NAME].orEmpty(),
                institutionName = p[Keys.INSTITUTION_NAME].orEmpty(),
                institutionAddress = p[Keys.INSTITUTION_ADDRESS].orEmpty(),
                phone = p[Keys.PHONE].orEmpty(),
                email = p[Keys.EMAIL].orEmpty(),
                session = p[Keys.SESSION].orEmpty(),
            )
        }

    suspend fun setTheme(mode: ThemeMode) {
        context.tutorFlowDataStore.edit { it[Keys.THEME] = mode.name }
    }

    suspend fun setLanguage(language: AppLanguage) {
        context.tutorFlowDataStore.edit { it[Keys.LANGUAGE] = language.name }
    }

    suspend fun completeOnboarding(profile: OnboardingProfile) {
        context.tutorFlowDataStore.edit {
            it[Keys.TEACHER_NAME] = profile.teacherName.trim()
            it[Keys.INSTITUTION_NAME] = profile.institutionName.trim()
            it[Keys.INSTITUTION_ADDRESS] = profile.institutionAddress.trim()
            it[Keys.PHONE] = profile.phone.trim()
            it[Keys.EMAIL] = profile.email.trim()
            it[Keys.SESSION] = profile.session.trim()
            it[Keys.ONBOARDING_DONE] = true
        }
    }
}
