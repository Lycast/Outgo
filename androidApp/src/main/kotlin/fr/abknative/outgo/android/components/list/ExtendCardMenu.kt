package fr.abknative.outgo.android.components.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor

@Composable
fun ExtendCardMenu(
    modifier: Modifier = Modifier,
    onEditClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onDuplicateClicked: () -> Unit,
    onUnsubscribeClicked: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = AppTheme.dimens.medium, start = AppTheme.dimens.large, end = AppTheme.dimens.large),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(onClick = onEditClicked) {
            Icon(painterResource(R.drawable.pencil_simple), contentDescription = "Éditer", tint = AppTheme.colors.textPrimary.toColor())
        }

        IconButton(onClick = onDuplicateClicked) {
            Icon(painterResource(R.drawable.copy), contentDescription = "Dupliquer", tint = AppTheme.colors.primary.toColor())
        }

        if (onUnsubscribeClicked != null) {
            IconButton(onClick = onUnsubscribeClicked) {
                Icon(painterResource(R.drawable.sign_out_duotone), contentDescription = "Résilier", tint = AppTheme.colors.error.toColor())
            }
        }

        IconButton(onClick = onDeleteClicked) {
            Icon(painterResource(R.drawable.trash), contentDescription = "Supprimer", tint = AppTheme.colors.error.toColor().copy(alpha = 0.5f))
        }
    }
}