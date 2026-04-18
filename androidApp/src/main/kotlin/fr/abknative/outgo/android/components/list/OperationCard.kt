package fr.abknative.outgo.android.components.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.designsystem.atoms.CircleIcon
import fr.abknative.outgo.android.designsystem.foundation.AppText
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OperationCard(
    title: String,
    subtitle: String,
    amountText: String,
    amountColor: Color,
    iconColor: Color,
    modifier: Modifier = Modifier,
    onEdit: (() -> Unit)? = null,
    onDeleteRequest: (() -> Unit)? = null
) {

    val clickModifier = if (onEdit != null || onDeleteRequest != null) {
        Modifier.combinedClickable(
            onClick = onEdit ?: {},
            onLongClick = onDeleteRequest
        )
    } else {
        Modifier
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(AppTheme.shapes.large)
            .then(clickModifier)
            .padding(horizontal = AppTheme.dimens.large, vertical = AppTheme.dimens.large),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleIcon(
            iconRes = R.drawable.credit_card_duotone,
            tint = iconColor
        )

        Spacer(modifier = Modifier.width(AppTheme.dimens.large))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            // --- Titre ---
            AppText(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(AppTheme.dimens.small))

            // --- Sous-titre et Montant ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                // Sous-titre (Date + Récurrence)
                AppText(
                    text = subtitle,
                    style = AppTheme.typo.caption,
                    color = AppTheme.colors.textSecondary.toColor(),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(AppTheme.dimens.medium))

                // Montant
                AppText(
                    text = amountText,
                    color = amountColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}