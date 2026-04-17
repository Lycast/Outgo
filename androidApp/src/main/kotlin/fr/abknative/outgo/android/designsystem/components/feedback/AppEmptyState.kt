package fr.abknative.outgo.android.designsystem.components.feedback

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import fr.abknative.outgo.android.designsystem.foundation.AppText
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor

@Composable
fun AppEmptyState(
    icon: ImageVector,
    title: String,
    description: String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(AppTheme.dimens.extraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(AppTheme.dimens.big),
            tint = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.4f)
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.large))

        AppText(
            text = title,
            style = AppTheme.typo.subtitle,
            textAlign = TextAlign.Center
        )

        if (description != null) {
            Spacer(modifier = Modifier.height(AppTheme.dimens.medium))
            AppText(
                text = description,
                color = AppTheme.colors.textSecondary.toColor(),
                textAlign = TextAlign.Center
            )
        }
    }
}