package fr.abknative.outgo.android.components.month

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.designsystem.atoms.CircleIcon
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.ListLabels
import fr.abknative.outgo.android.ui.extensions.getUiColor
import fr.abknative.outgo.android.ui.extensions.uiAmount
import fr.abknative.outgo.android.ui.extensions.uiLabel
import fr.abknative.outgo.android.ui.extensions.uiTitle
import fr.abknative.outgo.wallet.api.model.Operation
import fr.abknative.outgo.wallet.api.model.operation.OperationType

@Composable
fun OperationCardSummary(
    operation: Operation,
    formattedDate: String
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.dimens.extraLarge),
        verticalAlignment = Alignment.CenterVertically
    ) {

        val iconColor = operation.recurrence.getUiColor().copy(alpha = 0.5f)

        CircleIcon(
            iconRes = R.drawable.credit_card_duotone,
            tint = iconColor
        )
        Spacer(modifier = Modifier.width(AppTheme.dimens.large))

        Column(verticalArrangement = Arrangement.Center) {

            // --- Textes label ---
            Row( modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = operation.uiTitle,
                    style = AppTheme.typo.body,
                    color = AppTheme.colors.textPrimary.toColor(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // --- Date et récurrence + Montant ---
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {

                // Date et récurrence
                Row(horizontalArrangement = Arrangement.End) {
                    Text(
                        text = "${ListLabels.DUE_PREFIX} $formattedDate • ",
                        style = AppTheme.typo.caption,
                        color = AppTheme.colors.textSecondary.toColor()
                    )
                    Text(
                        text = operation.recurrence.uiLabel,
                        style = AppTheme.typo.caption,
                        color = operation.recurrence.getUiColor().copy(alpha = 0.8f)
                    )
                }

                Spacer(modifier = Modifier.width(AppTheme.dimens.medium))

                Text(
                    text = if(operation.type == OperationType.INCOME) "+ ${operation.amountInCents.uiAmount}" else operation.amountInCents.uiAmount,
                    style = AppTheme.typo.body,
                    color = if(operation.type == OperationType.INCOME) AppTheme.colors.primary.toColor() else AppTheme.colors.secondary.toColor(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}