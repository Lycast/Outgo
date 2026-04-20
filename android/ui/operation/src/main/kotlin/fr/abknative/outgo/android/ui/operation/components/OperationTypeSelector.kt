package fr.abknative.outgo.android.ui.operation.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import fr.abknative.outgo.android.core.FormLabels
import fr.abknative.outgo.android.core.components.selection.AppSegmentedControl
import fr.abknative.outgo.android.core.designsystem.AppText
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor
import fr.abknative.outgo.wallet.api.model.operation.OperationType

@Composable
fun OperationTypeSelector(
    selectedType: OperationType,
    onTypeChanged: (OperationType) -> Unit,
    modifier: Modifier = Modifier
) {

    val options = listOf(FormLabels.TYPE_EXPENSE, FormLabels.TYPE_INCOME)
    val selectedIndex = if (selectedType == OperationType.EXPENSE) 0 else 1

    val activeColor = if (selectedIndex == 0) {
        AppTheme.colors.primary.toColor() } else { AppTheme.colors.tertiary.toColor() }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppText(
            text = FormLabels.FIELD_TYPE_DESC,
            style = AppTheme.typo.caption,
            color = AppTheme.colors.textSecondary.toColor(),
            modifier = Modifier.padding(horizontal = AppTheme.dimens.small)
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.small))

        AppSegmentedControl(
            items = options,
            selectedIndex = selectedIndex,
            activeColor = activeColor,
            onItemSelected = { index ->
                val newType = if (index == 0) OperationType.EXPENSE else OperationType.INCOME
                onTypeChanged(newType)
            }
        )
    }
}