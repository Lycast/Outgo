package fr.abknative.outgo.android.app.shell

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.abknative.outgo.android.app.shell.components.AppGlobalHeader
import fr.abknative.outgo.android.app.shell.components.BottomNavBar
import fr.abknative.outgo.android.app.shell.components.EmailVerificationBanner
import fr.abknative.outgo.android.core.CommonLabels
import fr.abknative.outgo.android.core.components.feedback.AppSnackbar
import fr.abknative.outgo.android.core.designsystem.AppBackground
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor
import fr.abknative.outgo.android.core.toCommonUIString
import fr.abknative.outgo.android.ui.list.ListScreen
import fr.abknative.outgo.android.ui.login.DeleteAccountScreen
import fr.abknative.outgo.android.ui.login.LoginScreen
import fr.abknative.outgo.android.ui.month.MonthScreen
import fr.abknative.outgo.android.ui.onboarding.OnboardingScreen
import fr.abknative.outgo.android.ui.operation.OperationFormSheet
import fr.abknative.outgo.android.ui.settings.SettingsScreen
import fr.abknative.outgo.android.ui.year.PremiumShowcaseScreen
import fr.abknative.outgo.android.ui.year.YearScreen
import fr.abknative.outgo.auth.api.AuthError
import fr.abknative.outgo.core.api.formatters.formatForInput
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
    val shouldShowScaffoldComponents =
        currentStep !is AppStep.Login && currentStep != AppStep.Splash && currentStep != AppStep.Onboarding
    val textRetry = CommonLabels.ACTION_RETRY

    val shellSnackbarHostState = remember { SnackbarHostState() }
    val shellInternalError = when (val error = shellState.error) {
        null -> null
        is AuthError.EmailNotVerified -> ShellLabels.EMAIL_NOT_VERIFIED_MESSAGE
        else -> error.toCommonUIString()
    }
    val childScreenError = shellState.globalErrorMessage
    val messageToShow = childScreenError ?: shellInternalError
    val onShowGlobalError: (String) -> Unit = { message ->
        shellPresenter.onIntent(ShellIntent.ShowGlobalError(message))
    }

    BackHandler(enabled = navState.canGoBack) {
        coordinator.handleBack()
    }

    LaunchedEffect(messageToShow) {
        if (messageToShow != null) {
            val actionLabelToShow = if (
                shellState.error != null &&
                shellState.error !is AuthError.EmailNotVerified &&
                childScreenError == null
            ) textRetry else null

            val snackbarResult = shellSnackbarHostState.showSnackbar(
                message = messageToShow,
                actionLabel = actionLabelToShow,
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
        AppBackground(isDarkMode = isDarkMode) {
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
                        Column {
                            if (!isLandscape && shouldShowScaffoldComponents) {
                                AppGlobalHeader(
                                    isVertical = false,
                                    shellPresenter = shellPresenter,
                                    shellState = shellState,
                                    onShowSyncModal = { showSyncModal = true }
                                )
                            }

                            if (shellState.isEmailVerificationPending && shouldShowScaffoldComponents) {
                                EmailVerificationBanner(
                                    onCheckVerificationClick = {
                                        shellPresenter.onIntent(ShellIntent.CheckEmailVerification)
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->

                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {

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
                                    onSettingsClicked = { coordinator.navigateTo(AppStep.Settings) },
                                    onOnboardingComplete = { coordinator.replaceRoot(AppStep.Month) }
                                )

                                AppStep.Month -> MonthScreen(
                                    presenter = koinViewModel(),
                                    onError = onShowGlobalError,
                                    onNavigateToList = { coordinator.navigateTo(AppStep.List) },
                                    onAddOperationClick = {
                                        shellPresenter.onIntent(ShellIntent.OpenOperationForm(payload = OperationPayload()))
                                    }
                                )

                                AppStep.Year -> {
                                    if (shellState.isPremium) YearScreen()
                                    else PremiumShowcaseScreen()
                                }

                                AppStep.List -> ListScreen(
                                    presenter = koinViewModel(),
                                    onError = onShowGlobalError,
                                    onEditOperation = { projectedOp ->
                                        val op = projectedOp.operation
                                        val formattedAmount = op.amountInCents.formatForInput()

                                        shellPresenter.onIntent(
                                            ShellIntent.OpenOperationForm(
                                                payload = OperationPayload(
                                                    id = op.id,
                                                    name = op.name,
                                                    amount = formattedAmount,
                                                    type = op.type,
                                                    recurrence = op.recurrence,
                                                    startDate = op.startDate,
                                                    endDate = op.endDate
                                                )
                                            )
                                        )
                                    },
                                    onDuplicateOperation = { projectedOp ->
                                        val op = projectedOp.operation
                                        val formattedAmount = op.amountInCents.formatForInput()

                                        shellPresenter.onIntent(
                                            ShellIntent.OpenOperationForm(
                                                payload = OperationPayload(
                                                    id = null,
                                                    name = op.name,
                                                    amount = formattedAmount,
                                                    type = op.type,
                                                    recurrence = op.recurrence,
                                                    startDate = op.startDate,
                                                    endDate = op.endDate
                                                )
                                            )
                                        )
                                    }
                                )

                                AppStep.Settings -> SettingsScreen(
                                    presenter = koinViewModel(),
                                    onError = onShowGlobalError,
                                    onNavigateToLogin = { coordinator.navigateTo(AppStep.Login) },
                                    onNavigateToDeleteAccount = { coordinator.navigateTo(AppStep.DeleteAccount) },
                                    isDarkMode = isDarkMode,
                                    onToggleDarkMode = onToggleDarkMode
                                )

                                is AppStep.Login -> LoginScreen(
                                    presenter = koinViewModel<LoginPresenter>(),
                                    onError = onShowGlobalError,
                                    onNavigateBack = { coordinator.handleBack() },
                                    onLoginSuccess = { coordinator.handleBack() }
                                )

                                AppStep.DeleteAccount -> DeleteAccountScreen(
                                    presenter = koinViewModel<LoginPresenter>(),
                                    onError = onShowGlobalError,
                                    onNavigateBack = { coordinator.handleBack() },
                                    onDeleteSuccess = { coordinator.handleBack() }
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
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = AppTheme.dimens.big)
                        ) { data -> AppSnackbar(data) }
                    }
                }
            }
        }

        // --- Modales Globales (Sync, Premium) ---
        ShellModals(
            showSyncModal = showSyncModal,
            showPremiumTeasingModal = showPremiumTeasingModal,
            overlayState = shellState.overlayState,
            onDismissSync = { showSyncModal = false },
            onNavigateToLogin = {
                showSyncModal = false
                coordinator.navigateTo(AppStep.Login)
            },
            onResolveConflictDownloadCloud = { shellPresenter.onIntent(ShellIntent.ResolveConflictDownloadCloud) },
            onResolveConflictCancel = { shellPresenter.onIntent(ShellIntent.CancelSyncAndLogout) },
            onRetry = { shellPresenter.onIntent(ShellIntent.RefreshSync) }
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
                        initialStartDate = payload.startDate,
                        initialEndDate = payload.endDate
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
                onError = onShowGlobalError,
                onIntent = { operationPresenter.onIntent(it) },
                sheetState = sheetState,
                isPremium = shellState.isPremium,
                onDismiss = { shellPresenter.onIntent(ShellIntent.CloseOperationForm) }
            )
        }
    }
}