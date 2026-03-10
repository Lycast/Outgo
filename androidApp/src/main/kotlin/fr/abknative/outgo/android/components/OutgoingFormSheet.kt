package fr.abknative.outgo.android.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import fr.abknative.outgo.android.ui.states.OutgoingFormEvent
import fr.abknative.outgo.android.ui.states.OutgoingFormState
import fr.abknative.outgo.outgoing.api.presenter.OutgoingIntent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutgoingFormSheet(
    formState: OutgoingFormState,
    sheetState: SheetState,
    onEvent: (OutgoingFormEvent) -> Unit,
    onDismiss: () -> Unit,
    onSave: (OutgoingIntent.Save) -> Unit,
    onDuplicate: ((OutgoingIntent.Save) -> Unit)?,
    onDelete: ((String) -> Unit)?
) {
    val scope = rememberCoroutineScope()

    val closeSheet = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) onDismiss() // CORRECTION ICI
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss, // CORRECTION ICI
        sheetState = sheetState
    ) {
        OutgoingFormContent(
            state = formState,
            onEvent = onEvent,
            onCancel = { closeSheet() },
            onSave = {
                onSave( // CORRECTION ICI
                    OutgoingIntent.Save(
                        id = formState.outgoingId,
                        name = formState.nameBuffer,
                        amountInCents = formState.amountInCents,
                        recurrence = formState.recurrenceSelection,
                        dueDay = formState.dueDayBuffer.toIntOrNull() ?: 1,
                        dueMonth = formState.dueMonthBuffer.toIntOrNull()
                    )
                )
                closeSheet()
            },
            onDuplicate = if (onDuplicate != null) {
                {
                    onDuplicate( // CORRECTION ICI
                        OutgoingIntent.Save(
                            id = null,
                            name = formState.nameBuffer,
                            amountInCents = formState.amountInCents,
                            recurrence = formState.recurrenceSelection,
                            dueDay = formState.dueDayBuffer.toIntOrNull() ?: 1,
                            dueMonth = formState.dueMonthBuffer.toIntOrNull()
                        )
                    )
                    closeSheet()
                }
            } else null,
            onDelete = if (onDelete != null && formState.outgoingId != null) {
                {
                    onDelete(formState.outgoingId)
                    closeSheet()
                }
            } else null
        )
    }
}