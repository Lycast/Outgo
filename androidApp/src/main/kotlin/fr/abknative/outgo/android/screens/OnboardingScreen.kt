package fr.abknative.outgo.android.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import fr.abknative.outgo.android.components.onbaording.ConfigurationStep
import fr.abknative.outgo.android.components.onbaording.WelcomeStep
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
    val state by presenter.state.collectAsState()

    // Gère l'étape locale (1 = Accueil, 2 = Configuration)
    var currentStep by remember { mutableIntStateOf(1) }

    // 🌟 Le point de sortie magique
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
                // Animation de glissement de droite à gauche
                slideInHorizontally(animationSpec = tween(400)) { fullWidth -> fullWidth } + fadeIn() togetherWith
                        slideOutHorizontally(animationSpec = tween(400)) { fullWidth -> -fullWidth } + fadeOut()
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
                    errorMessage = state.error?.message,
                    onNameChange = { presenter.onIntent(OnboardingIntent.UpdateWalletName(it)) },
                    onAmountChange = { presenter.onIntent(OnboardingIntent.UpdateIncomeAmount(it)) },
                    onSubmit = { presenter.onIntent(OnboardingIntent.Submit) }
                )
            }
        }
    }
}