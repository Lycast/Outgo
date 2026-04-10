package fr.abknative.outgo.android.components.onbaording

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.toColor

@Composable
fun WelcomeStep(
    onLoginClicked: () -> Unit,
    onStartClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(AppTheme.spacing.extraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .size(120.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(R.drawable.outgo_logo),
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "Reprenez le contrôle\nde votre budget.",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = AppTheme.colors.textPrimary.toColor(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Sans friction. Sans connexion obligatoire. Vos données restent sur votre appareil.",
            style = MaterialTheme.typography.bodyLarge,
            color = AppTheme.colors.textSecondary.toColor(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onStartClicked,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Commencer",
                style = AppTheme.typo.label
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        OutlinedButton (
            onClick = onLoginClicked,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Vous avez déjà un compte? Connectez vous",
                style = AppTheme.typo.label
            )
        }

        Spacer(modifier = Modifier.height(48.dp))
    }
}