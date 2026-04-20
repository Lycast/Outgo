package fr.abknative.outgo.android.ui.operation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import fr.abknative.outgo.android.core.components.cards.GlassCard
import fr.abknative.outgo.android.core.components.feedback.AppSnackbar
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor
import fr.abknative.outgo.android.ui.operation.components.OperationFormContent
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
    onDismiss: () -> Unit
) {
    val scope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }
    val currentError = state.error
    val errorMessage = currentError?.toUIString()

    LaunchedEffect(currentError) {
        if (currentError != null && errorMessage != null) {
            snackbarHostState.showSnackbar(message = errorMessage, withDismissAction = true)
            onIntent(OperationIntent.DismissError)
        }
    }

    val closeSheet = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) onDismiss()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.Transparent,
        dragHandle = null,
        scrimColor = Color.Black.copy(alpha = 0.32f)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.dimens.small)
                    .padding(vertical = AppTheme.dimens.medium)
            ) {
                Column(
                    modifier = Modifier
                        .padding(bottom = AppTheme.dimens.extraLarge)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        BottomSheetDefaults.DragHandle(
                            color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.8f)
                        )
                    }
                    OperationFormContent(
                        state = state,
                        onIntent = onIntent,
                        isPremium = isPremium,
                        onCancel = { closeSheet() },
                        onSave = { onIntent(OperationIntent.Save) }
                    )
                }
            }
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) { data ->
                AppSnackbar(data)
            }
        }
    }
}