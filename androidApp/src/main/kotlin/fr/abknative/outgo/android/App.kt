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
import fr.abknative.outgo.android.components.common.Header
import fr.abknative.outgo.android.components.common.SyncPromotionModal
import fr.abknative.outgo.android.components.shell.AppBackground
import fr.abknative.outgo.android.screens.DashboardScreen
import fr.abknative.outgo.android.screens.LoginScreen
import fr.abknative.outgo.android.screens.OnboardingScreen
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
import fr.abknative.outgo.wallet.api.usecase.ObserveWalletsUseCase
import kotlinx.coroutines.flow.firstOrNull
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun App() {
    val coordinator: AppCoordinator = koinInject()
    val storage: KeyValueStorage = koinInject()
    val observeWalletsUseCase: ObserveWalletsUseCase = koinInject() // todo appeler un usecase dans l'ui est ce valable ? ce n'est pas le travail du presenter ?

    val shellPresenter: ShellPresenter = koinViewModel()
    val shellState by shellPresenter.state.collectAsState()

    val navState by coordinator.state.collectAsState()

    LaunchedEffect(Unit) {
        val wallets = observeWalletsUseCase().firstOrNull()

        if (wallets.isNullOrEmpty()) {
            coordinator.replaceRoot(AppStep.Onboarding)
        } else {
            coordinator.replaceRoot(AppStep.Dashboard)
        }
    }

    BackHandler(enabled = navState.canGoBack) {
        coordinator.handleBack()
    }

    val systemTheme = isSystemInDarkTheme()
    val themeKey = "app_is_dark_mode"
    var isDarkMode by remember {
        mutableStateOf(storage.getBoolean(themeKey, systemTheme))
    }

    var showPremiumTeasingModal by remember { mutableStateOf(false) }
    var showSyncModal by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val currentStep = navState.currentStep

    // Règle d'affichage du Header : On le cache sur Login, Splash et Onboarding
    val shouldShowHeader = currentStep != AppStep.Login && currentStep != AppStep.Splash && currentStep != AppStep.Onboarding

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

                    if (isLandscape && shouldShowHeader) {
                        globalHeader(true)
                        VerticalDivider(
                            thickness = 1.dp,
                            color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.1f)
                        )
                    }

                    Scaffold(
                        modifier = Modifier.weight(1f),
                        containerColor = Color.Transparent,
                        topBar = {
                            if (!isLandscape && shouldShowHeader) {
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

                                AppStep.Splash -> {
                                    Box(modifier = Modifier.fillMaxSize())
                                }

                                AppStep.Onboarding -> {
                                    OnboardingScreen(
                                        presenter = koinViewModel(),
                                        onLoginClicked = { coordinator.navigateTo(AppStep.Login) },
                                        onOnboardingComplete = { coordinator.replaceRoot(AppStep.Dashboard) }
                                    )
                                }

                                AppStep.Dashboard -> {
                                    DashboardScreen(
                                        presenter = koinViewModel(),
                                        isPremium = shellState.isPremium,
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

                                else -> {}
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
