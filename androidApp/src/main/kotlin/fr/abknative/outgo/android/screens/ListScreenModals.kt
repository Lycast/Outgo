package fr.abknative.outgo.android.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.abknative.outgo.android.designsystem.components.buttons.AppButton
import fr.abknative.outgo.android.designsystem.components.buttons.AppOutlinedButton
import fr.abknative.outgo.android.designsystem.components.feedback.ConfirmationDialog
import fr.abknative.outgo.android.designsystem.foundation.AppText
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.CommonLabels
import fr.abknative.outgo.android.ui.DialogLabels
import fr.abknative.outgo.list.api.ListIntent
import fr.abknative.outgo.list.api.ListPresenter
import fr.abknative.outgo.wallet.api.model.presenter.ProjectedOperation

@Composable
fun ListScreenModals(
    operationToDelete: ProjectedOperation?,
    onDismissDelete: () -> Unit,
    presenter: ListPresenter
) {
    if (operationToDelete != null) {
        ConfirmationDialog(
            title = DialogLabels.DELETE_OPERATION_TITLE,
            description = DialogLabels.DELETE_OPERATION_DESC,
            onDismiss = onDismissDelete,

            confirmButton = {
                AppButton(
                    onClick = {
                        presenter.onIntent(ListIntent.Delete(operationToDelete.operation.id))
                        onDismissDelete()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AppText(
                        text = CommonLabels.ACTION_DELETE,
                        color = AppTheme.colors.textOnBrand.toColor()
                    )
                }
            },

            dismissButton = {
                AppOutlinedButton (
                    onClick = onDismissDelete,
                    modifier = Modifier.padding(end = AppTheme.dimens.medium).fillMaxWidth()
                ) {
                    AppText(text = CommonLabels.ACTION_CANCEL)
                }
            }
        )
    }
}