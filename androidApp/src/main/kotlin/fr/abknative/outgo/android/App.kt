package fr.abknative.outgo.android

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.abknative.outgo.android.components.shell.AppGlobalHeader
import fr.abknative.outgo.android.components.shell.AppGlobalModals
import fr.abknative.outgo.android.designsystem.foundation.AppBackground
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.OutgoTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.screens.DashboardScreen
import fr.abknative.outgo.android.screens.LoginScreen
import fr.abknative.outgo.android.screens.OnboardingScreen
import fr.abknative.outgo.android.screens.SettingsScreen
import fr.abknative.outgo.core.api.KeyValueStorage
import fr.abknative.outgo.core.api.nav.AppStep
import fr.abknative.outgo.core.api.nav.NavCoordinator
import fr.abknative.outgo.login.api.LoginPresenter
import fr.abknative.outgo.shell.api.ShellPresenter
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun App() {
    val coordinator: NavCoordinator = koinInject()
    val storage: KeyValueStorage = koinInject()
    val shellPresenter: ShellPresenter = koinViewModel()

    val shellState by shellPresenter.state.collectAsStateWithLifecycle()
    val navState by coordinator.state.collectAsState()

    // --- Gestion du Thème ---
    val systemTheme = isSystemInDarkTheme()
    val themeKey = "app_is_dark_mode"
    var isDarkMode by remember {
        mutableStateOf(storage.getBoolean(themeKey, systemTheme))
    }

    // --- Gestion des Modales Globales ---
    var showPremiumTeasingModal by remember { mutableStateOf(false) }
    var showSyncModal by remember { mutableStateOf(false) }

    // --- Configuration Écran ---
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val currentStep = navState.currentStep
    val shouldShowHeader = currentStep != AppStep.Login && currentStep != AppStep.Splash && currentStep != AppStep.Onboarding

    BackHandler(enabled = navState.canGoBack) {
        coordinator.handleBack()
    }

    OutgoTheme(darkTheme = isDarkMode) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = AppTheme.colors.background.toColor()
        ) {
            AppBackground {
                Row(modifier = Modifier.fillMaxSize()) {

                    // --- Header en mode Paysage (Menu latéral) ---
                    if (isLandscape && shouldShowHeader) {
                        AppGlobalHeader(
                            isVertical = true,
                            currentStep = currentStep,
                            shellPresenter = shellPresenter,
                            shellState = shellState, // Assure-toi de passer l'état complet ou juste les champs nécessaires
                            coordinator = coordinator,
                            onShowSyncModal = { showSyncModal = true },
                            onShowPremiumTeasing = { showPremiumTeasingModal = true }
                        )
                        VerticalDivider(
                            thickness = 1.dp,
                            color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.1f)
                        )
                    }

                    // --- Contenu Principal ---
                    Scaffold(
                        modifier = Modifier.weight(1f),
                        containerColor = Color.Transparent,
                        topBar = {
                            // --- Header en mode Portrait (TopBar) ---
                            if (!isLandscape && shouldShowHeader) {
                                AppGlobalHeader(
                                    isVertical = false,
                                    currentStep = currentStep,
                                    shellPresenter = shellPresenter,
                                    shellState = shellState,
                                    coordinator = coordinator,
                                    onShowSyncModal = { showSyncModal = true },
                                    onShowPremiumTeasing = { showPremiumTeasingModal = true }
                                )
                            }
                        }
                    ) { innerPadding ->

                        LaunchedEffect(currentStep) {
                            println("Compose dessine l'écran : $currentStep")
                        }

                        AnimatedContent(
                            targetState = currentStep,
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                            label = "AppNav",
                            modifier = Modifier.padding(innerPadding)
                        ) { step ->
                            when (step) {
                                AppStep.Splash -> Box(modifier = Modifier.fillMaxSize())

                                AppStep.Onboarding -> OnboardingScreen(
                                    presenter = koinViewModel(),
                                    onLoginClicked = { coordinator.navigateTo(AppStep.Login) },
                                    onOnboardingComplete = { coordinator.replaceRoot(AppStep.Dashboard) }
                                )

                                AppStep.Dashboard -> DashboardScreen(
                                    presenter = koinViewModel(),
                                    isPremium = shellState.isPremium
                                )

                                AppStep.Settings -> SettingsScreen(
                                    presenter = koinViewModel(),
                                    onNavigateToLogin = { coordinator.navigateTo(AppStep.Login) },
                                    isDarkMode = isDarkMode,
                                    onToggleDarkMode = { newThemeValue ->
                                        isDarkMode = newThemeValue
                                        storage.putBoolean(themeKey, newThemeValue)
                                    }
                                )

                                AppStep.Login -> LoginScreen(
                                    presenter = koinViewModel<LoginPresenter>(),
                                    onNavigateBack = { coordinator.handleBack() },
                                    onLoginSuccess = { coordinator.handleBack() }
                                )

                                else -> {}
                            }
                        }
                    }
                }
            }

            // --- Modales Globales Extraites ---
            AppGlobalModals(
                showSyncModal = showSyncModal,
                showPremiumTeasingModal = showPremiumTeasingModal,
                onDismissSync = { showSyncModal = false },
                onDismissPremium = { showPremiumTeasingModal = false },
                onNavigateToLogin = {
                    showSyncModal = false
                    coordinator.navigateTo(AppStep.Login)
                }
            )
        }
    }
}