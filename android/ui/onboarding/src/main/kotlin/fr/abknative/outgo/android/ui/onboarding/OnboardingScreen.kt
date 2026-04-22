package fr.abknative.outgo.android.ui.onboarding

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.abknative.outgo.android.core.toCommonUIString
import fr.abknative.outgo.android.ui.onboarding.components.ConfigurationStep
import fr.abknative.outgo.android.ui.onboarding.components.WelcomeStep
import fr.abknative.outgo.core.ui.DesignAnimations
import fr.abknative.outgo.onboarding.api.OnboardingIntent
import fr.abknative.outgo.onboarding.api.OnboardingPresenter

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OnboardingScreen(
    presenter: OnboardingPresenter,
    onLoginClicked: () -> Unit,
    onOnboardingComplete: () -> Unit,
    modifier: Modifier = Modifier
) {

    val state by presenter.state.collectAsStateWithLifecycle()
    val translatedError = state.error?.toCommonUIString()
    var currentStep by remember { mutableIntStateOf(1) }

    BackHandler(enabled = currentStep == 2) {
        currentStep = 1
    }

    LaunchedEffect(state.isCompleted) {
        if (state.isCompleted) {
            onOnboardingComplete()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.Transparent
    ) { innerPadding ->

        AnimatedContent(
            targetState = currentStep,
            transitionSpec = {
                if (targetState > initialState) {
                    slideInHorizontally(animationSpec = tween(DesignAnimations.NORMAL)) { fullWidth -> fullWidth } + fadeIn() togetherWith
                            slideOutHorizontally(animationSpec = tween(DesignAnimations.NORMAL)) { fullWidth -> -fullWidth } + fadeOut()
                } else {
                    slideInHorizontally(animationSpec = tween(DesignAnimations.NORMAL)) { fullWidth -> -fullWidth } + fadeIn() togetherWith
                            slideOutHorizontally(animationSpec = tween(DesignAnimations.NORMAL)) { fullWidth -> fullWidth } + fadeOut()
                }
            },
            label = "OnboardingTransition",
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { targetStep ->
            when (targetStep) {
                1 -> WelcomeStep(
                    onLoginClicked = onLoginClicked,
                    onStartClicked = { currentStep = 2 }
                )
                2 -> ConfigurationStep(
                    walletName = state.walletName,
                    incomeAmountText = state.incomeAmountText,
                    isLoading = state.isLoading,
                    errorMessage = translatedError,
                    onNameChange = { presenter.onIntent(OnboardingIntent.UpdateWalletName(it)) },
                    onAmountChange = { presenter.onIntent(OnboardingIntent.UpdateIncomeAmount(it)) },
                    onSubmit = { presenter.onIntent(OnboardingIntent.Submit) },
                    onBackClicked = { currentStep = 1 }
                )
            }
        }
    }
}