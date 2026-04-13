package fr.abknative.outgo.android.components.sheet

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
import fr.abknative.outgo.android.ui.states.OperationFormEvent
import fr.abknative.outgo.android.ui.states.OperationFormState
import fr.abknative.outgo.list.api.ListIntent
import kotlinx.coroutines.launch

/**
 * A modal bottom sheet container for the operation form.
 * It provides a glassmorphism container and a specialized header for destructive/utility actions.
 *
 * @param formState Current state of the form.
 * @param sheetState State of the underlying [ModalBottomSheet].
 * @param isPremium Whether premium features should be enabled in the content.
 * @param onEvent Callback for form interactions.
 * @param onDismiss Invoked when the sheet is fully dismissed.
 * @param onSave Invoked with the prepared intent when the user clicks save.
 * @param onDeleteRequest Invoked when the trash icon is clicked.
 * @param onDuplicateRequest Invoked when the copy icon is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperationFormSheet(
    formState: OperationFormState,
    sheetState: SheetState,
    isPremium: Boolean,
    onEvent: (OperationFormEvent) -> Unit,
    onDismiss: () -> Unit,
    onSave: (ListIntent.SaveOperation) -> Unit,
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
            // Centralized Drag Handle
            BottomSheetDefaults.DragHandle(
                color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.5f)
            )

            // Conditional Header Actions (Edit Mode only)
            if (!formState.operationId.isNullOrBlank()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Delete Action
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

                    // Duplicate Action
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
                    state = formState,
                    onEvent = onEvent,
                    isPremium = isPremium,
                    onCancel = { closeSheet() },
                    onSave = {
                        val intent = ListIntent.SaveOperation(
                            id = formState.operationId,
                            walletId = formState.walletId,
                            name = formState.nameBuffer,
                            amountInCents = formState.amountInCents,
                            type = formState.typeSelection,
                            recurrence = formState.recurrenceSelection,
                            startDate = formState.startDate,
                            endDate = null
                        )
                        onSave(intent)
                        closeSheet()
                    }
                )
            }
        }
    }
}