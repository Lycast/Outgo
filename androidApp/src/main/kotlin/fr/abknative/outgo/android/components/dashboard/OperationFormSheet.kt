package fr.abknative.outgo.android.components.dashboard

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
    onEvent: (OperationFormEvent) -> Unit,
    onDismiss: () -> Unit,
    onSave: (DashboardIntent.SaveOperation) -> Unit
) {
    val scope = rememberCoroutineScope()

    val closeSheet = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) onDismiss()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = AppTheme.colors.surface200.toColor()
    ) {
        OperationFormContent(
            state = formState,
            onEvent = onEvent,
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