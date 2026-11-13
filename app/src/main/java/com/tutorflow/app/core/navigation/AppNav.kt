package com.tutorflow.app.core.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.FactCheck
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tutorflow.app.AppViewModel
import com.tutorflow.app.R
import com.tutorflow.app.core.designsystem.GlassBackground
import com.tutorflow.app.core.designsystem.GlassTabBar
import com.tutorflow.app.core.designsystem.TabItem
import com.tutorflow.app.data.prefs.UserPrefs
import com.tutorflow.app.feature.about.AboutScreen
import com.tutorflow.app.feature.home.HomeScreen
import com.tutorflow.app.feature.more.MoreScreen
import com.tutorflow.app.feature.onboarding.OnboardingScreen
import com.tutorflow.app.feature.placeholder.ComingSoonScreen
import com.tutorflow.app.feature.settings.SettingsScreen
import com.tutorflow.app.feature.splash.SplashScreen

object Routes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val HOME = "home"
    const val STUDENTS = "students"
    const val ATTENDANCE = "attendance"
    const val PAYMENTS = "payments"
    const val MORE = "more"
    const val SEARCH = "search"
    const val SETTINGS = "settings"
    const val ABOUT = "about"
    const val FEATURE = "feature/{key}"

    fun feature(key: String) = "feature/$key"
}

private val Tabs = listOf(
    TabItem(Routes.HOME, R.string.nav_home, Icons.Rounded.Home),
    TabItem(Routes.STUDENTS, R.string.nav_students, Icons.Rounded.Groups),
    TabItem(Routes.ATTENDANCE, R.string.nav_attendance, Icons.Rounded.FactCheck),
    TabItem(Routes.PAYMENTS, R.string.nav_payments, Icons.Rounded.Payments),
    TabItem(Routes.MORE, R.string.nav_more, Icons.Rounded.Apps),
)

@Composable
fun AppNav(viewModel: AppViewModel, prefs: UserPrefs?) {
    val nav = rememberNavController()
    val entry by nav.currentBackStackEntryAsState()
    val route = entry?.destination?.route
    val showBar = Tabs.any { it.route == route }

    Box(Modifier.fillMaxSize()) {
        GlassBackground()

        NavHost(
            navController = nav,
            startDestination = Routes.SPLASH,
            modifier = Modifier.fillMaxSize(),
            enterTransition = { fadeIn(tween(240)) + slideInHorizontally(tween(240)) { it / 12 } },
            exitTransition = { fadeOut(tween(160)) },
            popEnterTransition = { fadeIn(tween(240)) },
            popExitTransition = { fadeOut(tween(160)) + slideOutHorizontally(tween(200)) { it / 12 } },
        ) {
            composable(Routes.SPLASH) {
                SplashScreen(
                    ready = prefs != null,
                    onFinished = {
                        val next = if (prefs?.onboardingDone == true) Routes.HOME else Routes.ONBOARDING
                        nav.navigate(next) { popUpTo(Routes.SPLASH) { inclusive = true } }
                    },
                )
            }

            composable(Routes.ONBOARDING) {
                OnboardingScreen(
                    currentTheme = prefs?.themeMode ?: com.tutorflow.app.data.prefs.ThemeMode.SYSTEM,
                    onLanguage = viewModel::setLanguage,
                    onTheme = viewModel::setTheme,
                    onFinish = { profile ->
                        viewModel.completeOnboarding(profile)
                        nav.navigate(Routes.HOME) { popUpTo(Routes.ONBOARDING) { inclusive = true } }
                    },
                )
            }

            composable(Routes.HOME) {
                HomeScreen(
                    teacherName = prefs?.teacherName.orEmpty(),
                    onOpenFeature = { nav.navigate(Routes.feature(it.key)) },
                )
            }
            composable(Routes.STUDENTS) {
                ComingSoonScreen(
                    title = stringResource(R.string.nav_students),
                    icon = Icons.Rounded.Groups,
                    phase = stringResource(R.string.phase_3),
                    onBack = null,
                )
            }
            composable(Routes.ATTENDANCE) {
                ComingSoonScreen(
                    title = stringResource(R.string.nav_attendance),
                    icon = Icons.Rounded.FactCheck,
                    phase = stringResource(R.string.phase_3),
                    onBack = null,
                )
            }
            composable(Routes.PAYMENTS) {
                ComingSoonScreen(
                    title = stringResource(R.string.nav_payments),
                    icon = Icons.Rounded.Payments,
                    phase = stringResource(R.string.phase_3),
                    onBack = null,
                )
            }
            composable(Routes.MORE) {
                MoreScreen(
                    onOpenFeature = { nav.navigate(Routes.feature(it.key)) },
                    onSettings = { nav.navigate(Routes.SETTINGS) },
                    onAbout = { nav.navigate(Routes.ABOUT) },
                )
            }

            composable(Routes.SEARCH) {
                ComingSoonScreen(
                    title = stringResource(R.string.search_title),
                    icon = Icons.Rounded.Apps,
                    phase = stringResource(R.string.phase_3),
                    onBack = { nav.popBackStack() },
                )
            }
            composable(Routes.SETTINGS) {
                SettingsScreen(
                    currentTheme = prefs?.themeMode ?: com.tutorflow.app.data.prefs.ThemeMode.SYSTEM,
                    onLanguage = viewModel::setLanguage,
                    onTheme = viewModel::setTheme,
                    onBack = { nav.popBackStack() },
                )
            }
            composable(Routes.ABOUT) {
                AboutScreen(onBack = { nav.popBackStack() })
            }
            composable(
                route = Routes.FEATURE,
                arguments = listOf(navArgument("key") { type = NavType.StringType }),
            ) { backStackEntry ->
                val feature = Feature.fromKey(backStackEntry.arguments?.getString("key"))
                if (feature != null) {
                    ComingSoonScreen(
                        title = stringResource(feature.label),
                        icon = feature.icon,
                        phase = stringResource(feature.phase),
                        onBack = { nav.popBackStack() },
                    )
                }
            }
        }

        if (showBar) {
            GlassTabBar(
                items = Tabs,
                selectedRoute = route,
                onSelect = { target ->
                    nav.navigate(target) {
                        popUpTo(Routes.HOME) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onSearch = { nav.navigate(Routes.SEARCH) { launchSingleTop = true } },
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}
