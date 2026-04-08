package fr.abknative.outgo.android

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
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
import fr.abknative.outgo.android.components.common.Header
import fr.abknative.outgo.android.components.common.SyncPromotionModal
import fr.abknative.outgo.android.components.shell.AppBackground
import fr.abknative.outgo.android.screens.DashboardScreen
import fr.abknative.outgo.android.screens.LoginScreen
import fr.abknative.outgo.android.screens.SettingsScreen
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.OutgoTheme
import fr.abknative.outgo.android.ui.theme.toColor
import fr.abknative.outgo.app.nav.AppCoordinator
import fr.abknative.outgo.app.nav.AppStep
import fr.abknative.outgo.core.api.KeyValueStorage
import fr.abknative.outgo.login.api.LoginPresenter
import fr.abknative.outgo.shell.api.ShellIntent
import fr.abknative.outgo.shell.api.ShellPresenter
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun App() {
    val coordinator: AppCoordinator = koinInject()
    val storage: KeyValueStorage = koinInject()

    // 🌟 1. On injecte notre nouveau ShellPresenter
    val shellPresenter: ShellPresenter = koinViewModel()
    val shellState by shellPresenter.state.collectAsState()

    val navState by coordinator.state.collectAsState()

    BackHandler(enabled = navState.canGoBack) {
        coordinator.handleBack()
    }

    val systemTheme = isSystemInDarkTheme()
    val themeKey = "app_is_dark_mode"
    var isDarkMode by remember {
        mutableStateOf(storage.getBoolean(themeKey, systemTheme))
    }

    // Gestion des modales globales
    var showPremiumTeasingModal by remember { mutableStateOf(false) }
    var showSyncModal by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val currentStep = navState.currentStep

    // 🌟 2. On extrait la logique du Header pour ne pas la dupliquer
    val globalHeader = @Composable { isVertical: Boolean ->
        Header(
            syncState = shellState.syncState,
            isVertical = isVertical,
            currentStep = currentStep,
            isPremium = shellState.isPremium,
            onSyncIconClick = {
                if (shellState.syncState.isUnauthenticated) {
                    showSyncModal = true
                } else {
                    shellPresenter.onIntent(ShellIntent.RefreshSync)
                }
            },
            onNavigate = { step -> coordinator.navigateTo(step) },
            onTeasingClick = { showPremiumTeasingModal = true }
        )
    }


    OutgoTheme(darkTheme = isDarkMode) {

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = AppTheme.colors.background.toColor()
        ) {

            AppBackground {
                Row(modifier = Modifier.fillMaxSize()) {

                    if (isLandscape && currentStep != AppStep.Login) {
                        globalHeader(true)
                        VerticalDivider(
                            thickness = 1.dp,
                            color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.1f)
                        )
                    }

                    // Le Scaffold prend l'espace restant
                    Scaffold(
                        modifier = Modifier.weight(1f),
                        containerColor = Color.Transparent,
                        topBar = {
                            // Si on est en portrait, le Header est en haut
                            if (!isLandscape && currentStep != AppStep.Login) {
                                globalHeader(false)
                            }
                        }
                    ) { innerPadding ->

                        AnimatedContent(
                            targetState = currentStep,
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                            label = "AppNav",
                            modifier = Modifier.padding(innerPadding) // On applique le padding du Scaffold !
                        ) { step ->
                            when (step) {
                                AppStep.Dashboard -> {
                                    DashboardScreen(
                                        presenter = koinViewModel(),
                                        isPremium = shellState.isPremium, // On passe l'info si Dashboard en a besoin
                                        onNavigateToLogin = { coordinator.navigateTo(AppStep.Login) }
                                    )
                                }

                                AppStep.Settings -> {
                                    SettingsScreen(
                                        presenter = koinViewModel(),
                                        onNavigateToLogin = { coordinator.navigateTo(AppStep.Login) },
                                        isDarkMode = isDarkMode,
                                        onToggleDarkMode = { newThemeValue ->
                                            isDarkMode = newThemeValue
                                            storage.putBoolean(themeKey, newThemeValue)
                                        }
                                    )
                                }

                                AppStep.Login -> {
                                    LoginScreen(
                                        presenter = koinViewModel<LoginPresenter>(),
                                        onNavigateBack = { coordinator.handleBack() },
                                        onLoginSuccess = { coordinator.handleBack() }
                                    )
                                }

                                else -> {} // Pour Analyse plus tard
                            }
                        }
                    }
                }
            }

            // --- Modales Globales ---
            if (showSyncModal) {
                SyncPromotionModal(
                    onDismiss = { showSyncModal = false },
                    onNavigateToLogin = {
                        showSyncModal = false
                        coordinator.navigateTo(AppStep.Login)
                    }
                )
            }

            if (showPremiumTeasingModal) {
                // PremiumTeasingModal(onDismiss = { showPremiumTeasingModal = false })
            }
        }
    }
}
