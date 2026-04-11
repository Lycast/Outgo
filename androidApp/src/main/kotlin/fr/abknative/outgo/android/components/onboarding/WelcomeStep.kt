package fr.abknative.outgo.android.components.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.designsystem.components.buttons.AppButton
import fr.abknative.outgo.android.designsystem.components.buttons.AppOutlinedButton
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.OnboardingLabels

/**
 * The first screen of the onboarding flow.
 * Introduces the brand and provides entry points for new and existing users.
 *
 * @param onLoginClicked Callback to navigate to the authentication screen.
 * @param onStartClicked Callback to start the local wallet configuration.
 */
@Composable
fun WelcomeStep(
    onLoginClicked: () -> Unit,
    onStartClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(AppTheme.dimens.extraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Flexible space to push content to the center
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
        Text(
            text = OnboardingLabels.WELCOME_TITLE,
            style = AppTheme.typo.title,
            color = AppTheme.colors.primary.toColor(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.medium))

        Text(
            text = OnboardingLabels.WELCOME_SUBTITLE,
            style = AppTheme.typo.body,
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
            Text(
                text = OnboardingLabels.WELCOME_ACTION_START,
                fontWeight = FontWeight.Bold
            )
        }

        // Pushes the secondary action towards the bottom
        Spacer(modifier = Modifier.weight(1f))

        // --- Secondary Action: Login for existing users ---
        AppOutlinedButton(
            onClick = onLoginClicked,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = OnboardingLabels.WELCOME_ACTION_LOGIN,
                fontWeight = FontWeight.Medium
            )
        }

        // Margin at the very bottom
        Spacer(modifier = Modifier.height(AppTheme.dimens.big))
    }
}