package fr.abknative.outgo.android.components.dashboard

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import fr.abknative.outgo.android.ui.states.OutgoingFormEvent
import fr.abknative.outgo.android.ui.states.OutgoingFormState
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.toColor
import fr.abknative.outgo.outgoing.api.presenter.OutgoingIntent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutgoingFormSheet(
    formState: OutgoingFormState,
    sheetState: SheetState,
    onEvent: (OutgoingFormEvent) -> Unit,
    onDismiss: () -> Unit,
    onSave: (OutgoingIntent.Save) -> Unit
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
        OutgoingFormContent(
            state = formState,
            onEvent = onEvent,
            onCancel = { closeSheet() },
            onSave = {
                val monthValue = formState.dueMonthBuffer.toIntOrNull() ?: 0
                val intent = OutgoingIntent.Save(
                    id = formState.outgoingId,
                    name = formState.nameBuffer,
                    amountInCents = formState.amountInCents,
                    recurrence = formState.recurrenceSelection,
                    dueDay = formState.dueDayBuffer.toIntOrNull() ?: 1,
                    dueMonth = if (monthValue == 0) null else monthValue
                )
                onSave(intent)
                closeSheet()
            }
        )
    }
}