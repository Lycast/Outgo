package fr.abknative.outgo.android.screens

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.abknative.outgo.android.components.sheet.OperationFormSheet
import fr.abknative.outgo.android.components.shell.AppGlobalHeader
import fr.abknative.outgo.android.components.shell.AppGlobalModals
import fr.abknative.outgo.android.components.shell.BottomNavBar
import fr.abknative.outgo.android.designsystem.foundation.AppBackground
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.core.api.nav.AppStep
import fr.abknative.outgo.core.api.nav.NavCoordinator
import fr.abknative.outgo.login.api.LoginPresenter
import fr.abknative.outgo.operation.api.OperationIntent
import fr.abknative.outgo.operation.api.OperationPresenter
import fr.abknative.outgo.shell.api.ShellIntent
import fr.abknative.outgo.shell.api.ShellPresenter
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShellScreen(
    shellPresenter: ShellPresenter,
    coordinator: NavCoordinator,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit
) {
    val shellState by shellPresenter.state.collectAsStateWithLifecycle()
    val navState by coordinator.state.collectAsState()

    // --- Modales ---
    var showPremiumTeasingModal by remember { mutableStateOf(false) }
    var showSyncModal by remember { mutableStateOf(false) }

    // --- Configuration ---
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val currentStep = navState.currentStep
    val shouldShowHeader = currentStep != AppStep.Login && currentStep != AppStep.Splash && currentStep != AppStep.Onboarding

    BackHandler(enabled = navState.canGoBack) {
        coordinator.handleBack()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AppTheme.colors.background.toColor()
    ) {
        AppBackground {
            Row(modifier = Modifier.fillMaxSize()) {

                // --- Header en mode Paysage ---
                if (isLandscape && shouldShowHeader) {
                    AppGlobalHeader(
                        isVertical = true,
                        shellPresenter = shellPresenter,
                        shellState = shellState,
                        onShowSyncModal = { showSyncModal = true }
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
                        // --- Header en mode Portrait ---
                        if (!isLandscape && shouldShowHeader) {
                            AppGlobalHeader(
                                isVertical = false,
                                shellPresenter = shellPresenter,
                                shellState = shellState,
                                onShowSyncModal = { showSyncModal = true }
                            )
                        }
                    }
                ) { innerPadding ->

                    Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {

                        // --- Router Central ---
                        AnimatedContent(
                            targetState = currentStep,
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                            label = "AppNav"
                        ) { step ->
                            when (step) {
                                AppStep.Splash -> Box(modifier = Modifier.fillMaxSize())

                                AppStep.Onboarding -> OnboardingScreen(
                                    presenter = koinViewModel(),
                                    onLoginClicked = { coordinator.navigateTo(AppStep.Login) },
                                    onOnboardingComplete = { coordinator.replaceRoot(AppStep.Month) }
                                )

                                AppStep.Month -> MonthScreen(presenter = koinViewModel())

                                AppStep.Year -> {
                                    if (shellState.isPremium) YearScreen()
                                    else PremiumShowcaseScreen(onNotifyMeClick = {})
                                }

                                AppStep.List -> ListScreen(
                                    presenter = koinViewModel(),
                                    shellPresenter = shellPresenter
                                )

                                AppStep.Settings -> SettingsScreen(
                                    presenter = koinViewModel(),
                                    onNavigateToLogin = { coordinator.navigateTo(AppStep.Login) },
                                    isDarkMode = isDarkMode,
                                    onToggleDarkMode = onToggleDarkMode
                                )

                                AppStep.Login -> LoginScreen(
                                    presenter = koinViewModel<LoginPresenter>(),
                                    onNavigateBack = { coordinator.handleBack() },
                                    onLoginSuccess = { coordinator.handleBack() }
                                )
                            }
                        }

                        // --- Bottom Navigation Bar ---
                        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                            BottomNavBar(
                                currentStep = currentStep,
                                isPremium = shellState.isPremium,
                                onNavigate = { step -> coordinator.navigateTo(step) },
                                onTeasingClick = { showPremiumTeasingModal = true },
                                onAddClick = {
                                    shellPresenter.onIntent(ShellIntent.OpenOperationForm(operationId = null))
                                }
                            )
                        }
                    }
                }
            }
        }

        // --- Modales Globales (Sync, Premium) ---
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

        // --- LE FORMULAIRE GLOBAL D'OPÉRATION ---
        if (shellState.isOperationFormVisible) {
            val operationPresenter = koinViewModel<OperationPresenter>()
            val operationState by operationPresenter.state.collectAsStateWithLifecycle()
            val timeProvider = koinInject<TimeProvider>()
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

            LaunchedEffect(shellState.operationIdToEdit) {
                operationPresenter.onIntent(
                    OperationIntent.Init(
                        walletId = shellState.activeWalletId ?: "",
                        operationId = shellState.operationIdToEdit,
                        initialName = shellState.initialName,
                        initialAmount = shellState.initialAmount,
                        initialType = shellState.initialType,
                        initialRecurrence = shellState.initialRecurrence,
                        initialDate = shellState.initialStartDate
                    )
                )
            }

            LaunchedEffect(operationState.isSavedSuccessfully) {
                if (operationState.isSavedSuccessfully) {
                    shellPresenter.onIntent(ShellIntent.CloseOperationForm)
                }
            }

            OperationFormSheet(
                state = operationState,
                onIntent = { operationPresenter.onIntent(it) },
                sheetState = sheetState,
                isPremium = shellState.isPremium,
                onDismiss = { shellPresenter.onIntent(ShellIntent.CloseOperationForm) },
                onDeleteRequest = { /* À gérer si besoin depuis le formulaire */ },
                onDuplicateRequest = { /* À gérer si besoin */ }
            )
        }
    }
}