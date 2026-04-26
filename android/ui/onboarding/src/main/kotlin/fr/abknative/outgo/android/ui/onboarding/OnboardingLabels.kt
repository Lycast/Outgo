package fr.abknative.outgo.android.ui.onboarding

import androidx.compose.runtime.Composable
import fr.abknative.outgo.shared.core.ui.resources.*
import org.jetbrains.compose.resources.stringResource

internal object OnboardingLabels {

    val CONFIG_TITLE @Composable get() = stringResource(Res.string.onboarding_config_title)
    val WALLET_NAME_LABEL @Composable get() = stringResource(Res.string.onboarding_wallet_name_label)
    val WALLET_NAME_PLACEHOLDER @Composable get() = stringResource(Res.string.onboarding_wallet_name_placeholder)
    val INCOME_AMOUNT_LABEL @Composable get() = stringResource(Res.string.onboarding_income_amount_label)
    val INCOME_AMOUNT_PLACEHOLDER @Composable get() = stringResource(Res.string.onboarding_income_amount_placeholder)
    val SUBMIT_ONBOARDING @Composable get() = stringResource(Res.string.onboarding_submit)

    val WELCOME_TITLE @Composable get() = stringResource(Res.string.onboarding_welcome_title)
    val WELCOME_SUBTITLE @Composable get() = stringResource(Res.string.onboarding_welcome_subtitle)
    val WELCOME_ACTION_START @Composable get() = stringResource(Res.string.onboarding_welcome_action_start)
    val WELCOME_ACTION_LOGIN @Composable get() = stringResource(Res.string.onboarding_welcome_action_login)
}