package fr.abknative.outgo.android.components.list

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.designsystem.atoms.CircleIcon
import fr.abknative.outgo.android.designsystem.foundation.AppText
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.ListLabels
import fr.abknative.outgo.android.ui.extensions.getUiColor
import fr.abknative.outgo.android.ui.extensions.uiAmount
import fr.abknative.outgo.android.ui.extensions.uiLabel
import fr.abknative.outgo.android.ui.extensions.uiTitle
import fr.abknative.outgo.wallet.api.model.Operation
import fr.abknative.outgo.wallet.api.model.operation.OperationType

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OperationCard(
    operation: Operation,
    formattedDate: String,
    onEdit: () -> Unit,
    onDeleteRequest: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(AppTheme.shapes.large)
            .combinedClickable(
                onClick = onEdit,
                onLongClick = onDeleteRequest
            )
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.dimens.large, vertical = AppTheme.dimens.large),
            verticalAlignment = Alignment.CenterVertically
        ) {

            val iconColor = operation.recurrence.getUiColor()

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
                    AppText(
                        text = operation.uiTitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(AppTheme.dimens.small))

                // --- Date et récurrence + Montant ---
                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom
                ) {

                    // Date et récurrence
                    Row(horizontalArrangement = Arrangement.End) {
                        AppText(
                            text = "${ListLabels.DUE_PREFIX} $formattedDate • ",
                            style = AppTheme.typo.caption,
                            color = AppTheme.colors.textSecondary.toColor()
                        )
                        AppText(
                            text = operation.recurrence.uiLabel,
                            style = AppTheme.typo.caption,
                            color = operation.recurrence.getUiColor()
                        )
                    }

                    Spacer(modifier = Modifier.width(AppTheme.dimens.medium))

                    // Montant de la dépense
                    AppText(
                        text = if(operation.type == OperationType.INCOME) "+ ${operation.amountInCents.uiAmount}" else operation.amountInCents.uiAmount,
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
}