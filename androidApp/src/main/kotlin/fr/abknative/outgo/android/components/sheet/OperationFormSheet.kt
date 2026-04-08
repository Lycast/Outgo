package fr.abknative.outgo.android.components.sheet

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.components.common.GlassCard
import fr.abknative.outgo.android.components.dashboard.OperationFormContent
import fr.abknative.outgo.android.ui.AccessibilityLabels
import fr.abknative.outgo.android.ui.states.OperationFormEvent
import fr.abknative.outgo.android.ui.states.OperationFormState
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.toColor
import fr.abknative.outgo.dashboard.api.DashboardIntent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperationFormSheet(
    formState: OperationFormState,
    sheetState: SheetState,
    isPremium: Boolean,
    onEvent: (OperationFormEvent) -> Unit,
    onDismiss: () -> Unit,
    onSave: (DashboardIntent.SaveOperation) -> Unit,
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
                .padding(horizontal = AppTheme.spacing.medium),
            contentAlignment = Alignment.Center
        ) {
            BottomSheetDefaults.DragHandle(
                color = AppTheme.colors.textSecondary.toColor()
            )

            if (!formState.operationId.isNullOrBlank()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDeleteRequest) {
                        Icon(
                            painter = painterResource(R.drawable.trash),
                            contentDescription = AccessibilityLabels.DELETE_EXPENSE,
                            tint = AppTheme.colors.error.toColor().copy(alpha = 0.75f)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(onClick = onDuplicateRequest) {
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
        dragHandle = null
    ) {

        GlassCard(
            modifier = Modifier.padding(top = AppTheme.spacing.medium)
        ) {
            Column(modifier = Modifier.padding(bottom = AppTheme.spacing.big)) {
                sheetHeader()
                OperationFormContent(
                    state = formState,
                    onEvent = onEvent,
                    isPremium = isPremium,
                    onCancel = { closeSheet() },
                    onSave = {
                        val intent = DashboardIntent.SaveOperation(
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