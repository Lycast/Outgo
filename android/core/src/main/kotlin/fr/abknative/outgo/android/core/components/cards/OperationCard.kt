package fr.abknative.outgo.android.core.components.cards

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.em
import fr.abknative.outgo.android.core.R
import fr.abknative.outgo.android.core.components.atoms.CircleIcon
import fr.abknative.outgo.android.core.designsystem.AppText
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OperationCard(
    modifier: Modifier = Modifier,
    topLeftText: String,
    topRightText: String? = null,
    bottomLeftText: String,
    bottomRightText: String,
    amountColor: Color = AppTheme.colors.textPrimary.toColor(),
    iconColor: Color,
    isExpanded: Boolean = false,
    onToggleExpand: (() -> Unit)? = null,
    onDeleteRequest: (() -> Unit)? = null,
    onUnsubscribeRequest: (() -> Unit)? = null,
    onEditRequest: (() -> Unit)? = null,
    onDuplicateRequest: (() -> Unit)? = null
) {

    val isInteractive = onToggleExpand != null
    val interaction = remember { MutableInteractionSource() }

    val baseModifier = modifier
        .fillMaxWidth()
        .clip(AppTheme.shapes.large)
        .animateContentSize()

    Column(modifier = baseModifier) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(AppTheme.shapes.large)
                .then(
                    if (isInteractive) {
                        Modifier.clickable(
                            interactionSource = interaction,
                            indication = LocalIndication.current,
                            onClick = { onToggleExpand.invoke() }
                        )
                    } else Modifier
                )
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

                // --- TOP LINE ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    AppText(
                        text = topLeftText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(AppTheme.dimens.medium))

                    if (topRightText != null) {
                        Spacer(modifier = Modifier.width(AppTheme.dimens.small))
                        AppText(
                            text = topRightText,
                            style = AppTheme.typo.label.copy(fontWeight = FontWeight.Normal),
                            color = AppTheme.colors.textSecondary.toColor(),
                            textAlign = TextAlign.End
                        )
                    }
                }

                Spacer(modifier = Modifier.height(AppTheme.dimens.small))

                // --- BOTTOM LINE ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppText(
                        text = bottomLeftText,
                        style = AppTheme.typo.label.copy(fontWeight = FontWeight.Normal, letterSpacing = (-0.02).em),
                        color = AppTheme.colors.textSecondary.toColor(),
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(AppTheme.dimens.medium))

                    AppText(
                        text = bottomRightText,
                        color = amountColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
        if (isInteractive) {

            HorizontalDivider(
                color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.1f),
                modifier = Modifier.padding(horizontal = AppTheme.dimens.large)
            )

            AnimatedVisibility(visible = isExpanded) {
                ExtendCardMenu(
                    onEditClicked = { onEditRequest?.invoke() },
                    onDuplicateClicked = { onDuplicateRequest?.invoke() },
                    onDeleteClicked = { onDeleteRequest?.invoke() },
                    onUnsubscribeClicked = onUnsubscribeRequest?.let { action -> { action.invoke() } }
                )
            }
        }
    }
}