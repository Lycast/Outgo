package fr.abknative.outgo.android.components.operation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.designsystem.components.buttons.AppHeaderButton
import fr.abknative.outgo.android.designsystem.components.cards.GlassCard
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.AccessibilityLabels
import fr.abknative.outgo.operation.api.OperationIntent
import fr.abknative.outgo.operation.api.OperationState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperationFormSheet(
    state: OperationState,
    sheetState: SheetState,
    isPremium: Boolean,
    onIntent: (OperationIntent) -> Unit,
    onDismiss: () -> Unit,
    onDeleteRequest: () -> Unit,
    onDuplicateRequest: () -> Unit
) {
    val scope = rememberCoroutineScope()

    val closeSheet = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) onDismiss()
        }
    }

    val sheetHeader = @Composable {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.dimens.medium),
            contentAlignment = Alignment.Center
        ) {
            BottomSheetDefaults.DragHandle(
                color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.5f)
            )

            if (!state.operationId.isNullOrBlank()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppHeaderButton(
                        onClick = onDeleteRequest,
                        elevation = 1.dp
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.trash),
                            contentDescription = AccessibilityLabels.DELETE_EXPENSE,
                            tint = AppTheme.colors.error.toColor().copy(alpha = 0.5f)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    AppHeaderButton(
                        onClick = onDuplicateRequest,
                        elevation = 1.dp
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.copy),
                            contentDescription = AccessibilityLabels.DUPLICATE_EXPENSE,
                            tint = AppTheme.colors.primary.toColor()
                        )
                    }
                }
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.Transparent,
        dragHandle = null,
        scrimColor = Color.Black.copy(alpha = 0.32f)
    ) {
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.dimens.small)
                .padding(vertical = AppTheme.dimens.medium)
        ) {
            Column(modifier = Modifier.padding(top = AppTheme.dimens.small, bottom = AppTheme.dimens.big)) {
                sheetHeader()

                OperationFormContent(
                    state = state,
                    onIntent = onIntent,
                    isPremium = isPremium,
                    onCancel = { closeSheet() },
                    onSave = { onIntent(OperationIntent.Save) }
                )
            }
        }
    }
}