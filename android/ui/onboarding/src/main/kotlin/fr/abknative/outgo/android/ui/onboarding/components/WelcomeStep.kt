package fr.abknative.outgo.android.ui.onboarding.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.core.CommonLabels
import fr.abknative.outgo.android.core.R
import fr.abknative.outgo.android.core.components.buttons.AppButton
import fr.abknative.outgo.android.core.components.buttons.AppIconTextButton
import fr.abknative.outgo.android.core.components.buttons.AppOutlinedButton
import fr.abknative.outgo.android.core.designsystem.AppText
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor
import fr.abknative.outgo.android.ui.onboarding.OnboardingLabels

@Composable
fun WelcomeStep(
    onLoginClicked: () -> Unit,
    onStartClicked: () -> Unit,
    onSettingsClicked: () -> Unit
) {

    Box(modifier = Modifier.fillMaxSize()) {

        AppIconTextButton(
            text = CommonLabels.TAB_SETTINGS,
            iconRes = R.drawable.gear_six,
            onClick = onSettingsClicked,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(AppTheme.dimens.medium)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(AppTheme.dimens.extraLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // --- Logo ---
            Box(
                modifier = Modifier.size(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(R.drawable.outgo_logo),
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.height(AppTheme.dimens.big))

            // --- Brand Message ---
            AppText(
                text = OnboardingLabels.WELCOME_TITLE,
                style = AppTheme.typo.title,
                color = AppTheme.colors.primary.toColor(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(AppTheme.dimens.medium))

            AppText(
                text = OnboardingLabels.WELCOME_SUBTITLE,
                color = AppTheme.colors.textSecondary.toColor(),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = AppTheme.dimens.medium)
            )

            Spacer(modifier = Modifier.height(AppTheme.dimens.big))

            // --- Primary Action: Create Local Wallet ---
            AppButton(
                onClick = onStartClicked,
                modifier = Modifier.fillMaxWidth()
            ) {
                AppText(
                    text = OnboardingLabels.WELCOME_ACTION_START,
                    color = AppTheme.colors.textOnBrand.toColor()
                )
            }

            Spacer(modifier = Modifier.height(AppTheme.dimens.medium))

            // --- Secondary Action: Login for existing users ---
            AppOutlinedButton(
                onClick = onLoginClicked,
                modifier = Modifier.fillMaxWidth()
            ) {
                AppText(
                    text = OnboardingLabels.WELCOME_ACTION_LOGIN,
                    style = AppTheme.typo.caption
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}