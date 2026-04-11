package fr.abknative.outgo.android.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.abknative.outgo.android.components.dashboard.WalletEditDialog
import fr.abknative.outgo.android.components.sheet.OperationFormSheet
import fr.abknative.outgo.android.designsystem.components.buttons.AppButton
import fr.abknative.outgo.android.designsystem.components.buttons.AppOutlinedButton
import fr.abknative.outgo.android.designsystem.components.feedback.ConfirmationDialog
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.ui.CommonLabels
import fr.abknative.outgo.android.ui.DialogLabels
import fr.abknative.outgo.android.ui.states.OperationFormState
import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.dashboard.api.DashboardIntent
import fr.abknative.outgo.dashboard.api.DashboardPresenter
import fr.abknative.outgo.dashboard.api.DashboardState
import fr.abknative.outgo.wallet.api.model.dashboard.ProjectedOperation

/**
 * Handles the display logic for all dashboard-related modals and dialogs.
 * This component is stateless regarding visibility and delegates actions back to the caller.
 *
 * @param onDismissBudget Callback to hide the budget edit dialog.
 * @param onDismissForm Callback to hide the operation form sheet.
 * @param onDismissDelete Callback to hide the confirmation dialog.
 * @param onOperationToDeleteChange Updates the reference of the operation marked for deletion.
 * @param onSelectedOperationChange Updates the reference of the operation currently being edited.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardModals(
    state: DashboardState,
    showBudgetDialog: Boolean,
    showFormSheet: Boolean,
    operationToDelete: ProjectedOperation?,
    selectedOperation: ProjectedOperation?,
    formState: OperationFormState,
    sheetState: SheetState,
    onDismissBudget: () -> Unit,
    onDismissForm: () -> Unit,
    onDismissDelete: () -> Unit,
    onOperationToDeleteChange: (ProjectedOperation?) -> Unit,
    onSelectedOperationChange: (ProjectedOperation?) -> Unit,
    presenter: DashboardPresenter,
    timeProvider: TimeProvider,
    isPremium: Boolean
) {
    if (showBudgetDialog) {
        WalletEditDialog(
            initialWalletName = state.activeWalletName,
            currentIncomeInCents = state.monthlyIncomeInCents,
            onDismiss = onDismissBudget, // CORRECT: Calling the callback
            onConfirm = { newName, newIncomeInCents ->
                val currentYear = timeProvider.yearValue(timeProvider.now())
                val startOfSelectedMonth = timeProvider.startOfMonth(state.selectedMonth, currentYear)

                presenter.onIntent(
                    DashboardIntent.SaveWalletAndIncome(
                        walletId = state.activeWalletId ?: "",
                        walletName = newName,
                        incomeAmountInCents = newIncomeInCents,
                        startDate = startOfSelectedMonth
                    )
                )
                onDismissBudget()
            }
        )
    }

    if (showFormSheet) {
        OperationFormSheet(
            formState = formState,
            sheetState = sheetState,
            isPremium = isPremium,
            onEvent = { event -> formState.onEvent(event) },
            onDismiss = onDismissForm,
            onSave = { intent -> presenter.onIntent(intent) },
            onDeleteRequest = {
                onDismissForm()
                onOperationToDeleteChange(selectedOperation)
            },
            onDuplicateRequest = {
                selectedOperation?.let { current ->
                    onSelectedOperationChange(
                        current.copy(operation = current.operation.copy(id = ""))
                    )
                }
            }
        )
    }

    if (operationToDelete != null) {
        ConfirmationDialog(
            title = DialogLabels.DELETE_OPERATION_TITLE,
            description = DialogLabels.DELETE_OPERATION_DESC,
            onDismiss = onDismissDelete,

            confirmButton = {
                AppButton(
                    onClick = {
                        presenter.onIntent(DashboardIntent.Delete(operationToDelete.operation.id))
                        onDismissDelete()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = CommonLabels.ACTION_DELETE)
                }
            },

            dismissButton = {
                AppOutlinedButton (
                    onClick = onDismissDelete,
                    modifier = Modifier.padding(end = AppTheme.dimens.medium).fillMaxWidth()
                ) {
                    Text(text = CommonLabels.ACTION_CANCEL)
                }
            }
        )
    }
}