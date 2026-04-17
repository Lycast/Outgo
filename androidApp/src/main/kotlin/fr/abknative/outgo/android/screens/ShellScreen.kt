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
import fr.abknative.outgo.android.components.operation.OperationFormSheet
import fr.abknative.outgo.android.components.shell.AppGlobalHeader
import fr.abknative.outgo.android.components.shell.BottomNavBar
import fr.abknative.outgo.android.designsystem.components.feedback.AppSnackbar
import fr.abknative.outgo.android.designsystem.foundation.AppBackground
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.CommonLabels
import fr.abknative.outgo.android.ui.ShellLabels
import fr.abknative.outgo.android.ui.toUIString
import fr.abknative.outgo.core.api.nav.AppStep
import fr.abknative.outgo.core.api.nav.NavCoordinator
import fr.abknative.outgo.login.api.LoginPresenter
import fr.abknative.outgo.operation.api.OperationIntent
import fr.abknative.outgo.operation.api.OperationPresenter
import fr.abknative.outgo.shell.api.ShellIntent
import fr.abknative.outgo.shell.api.ShellPresenter
import fr.abknative.outgo.shell.api.payload.OperationPayload
import org.koin.androidx.compose.koinViewModel

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
    var showPremiumTeasingModal by remember { mutableStateOf(false) }
    var showSyncModal by remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val currentStep = navState.currentStep
    val shouldShowScaffoldComponents = currentStep != AppStep.Login && currentStep != AppStep.Splash && currentStep != AppStep.Onboarding
    val copySubname = ShellLabels.COPY_SUBNAME
    val textRetry = CommonLabels.ACTION_RETRY
    val isInitialSyncInProgress = shellState.syncState.isInProgress && currentStep == AppStep.Onboarding

    val shellSnackbarHostState = remember { SnackbarHostState() }
    val currentError = shellState.error
    val errorMessage = currentError?.toUIString()

    BackHandler(enabled = navState.canGoBack) {
        coordinator.handleBack()
    }

    LaunchedEffect(currentError) {
        if (currentError != null && errorMessage != null) {
            val snackbarResult = shellSnackbarHostState.showSnackbar(
                message = errorMessage,
                actionLabel = textRetry,
                withDismissAction = true,
                duration = SnackbarDuration.Long
            )
            shellPresenter.onIntent(ShellIntent.DismissError)
            if (snackbarResult == SnackbarResult.ActionPerformed) {
                shellPresenter.onIntent(ShellIntent.RefreshSync)
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AppTheme.colors.background.toColor()
    ) {
        AppBackground {
            Row(modifier = Modifier.fillMaxSize()) {

                // --- Header en mode Paysage ---
                if (isLandscape && shouldShowScaffoldComponents) {
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
                        if (!isLandscape && shouldShowScaffoldComponents) {
                            AppGlobalHeader(
                                isVertical = false,
                                shellPresenter = shellPresenter,
                                shellState = shellState,
                                onShowSyncModal = { showSyncModal = true }
                            )
                        }
                    }
                ) { innerPadding ->

                    Box(modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()) {

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
                        if (shouldShowScaffoldComponents) {
                            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                                BottomNavBar(
                                    currentStep = currentStep,
                                    isPremium = shellState.isPremium,
                                    onNavigate = { step -> coordinator.navigateTo(step) },
                                    onAddClick = {
                                        shellPresenter.onIntent(ShellIntent.OpenOperationForm(payload = OperationPayload()))
                                    }
                                )
                            }
                        }

                        SnackbarHost(
                            hostState = shellSnackbarHostState,
                            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = AppTheme.dimens.big)
                        ) { data -> AppSnackbar(data) }
                    }
                }
            }
        }

        // --- Modales Globales (Sync, Premium) ---
        ShellModals(
            showSyncModal = showSyncModal,
            showPremiumTeasingModal = showPremiumTeasingModal,
            showLoadingOverlay = isInitialSyncInProgress,
            onDismissSync = { showSyncModal = false },
            onNavigateToLogin = {
                showSyncModal = false
                coordinator.navigateTo(AppStep.Login)
            }
        )

        // --- LE FORMULAIRE GLOBAL D'OPÉRATION ---
        shellState.operationPayload?.let { payload ->
            val operationPresenter = koinViewModel<OperationPresenter>()
            val operationState by operationPresenter.state.collectAsStateWithLifecycle()
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

            LaunchedEffect(payload) {
                operationPresenter.onIntent(
                    OperationIntent.Init(
                        walletId = shellState.activeWalletId ?: "",
                        operationId = payload.id,
                        initialName = payload.name,
                        initialAmount = payload.amount,
                        initialType = payload.type,
                        initialRecurrence = payload.recurrence,
                        initialDate = payload.startDate
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
                onDeleteRequest = { operationPresenter.onIntent(OperationIntent.Delete) },
                onDuplicateRequest = {operationPresenter.onIntent(OperationIntent.Duplicate(copySubname)) }
            )
        }
    }
}