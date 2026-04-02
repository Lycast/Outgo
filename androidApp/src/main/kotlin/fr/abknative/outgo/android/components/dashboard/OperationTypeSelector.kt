package fr.abknative.outgo.android.components.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.abknative.outgo.android.components.common.SegmentedControl
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.toColor
import fr.abknative.outgo.wallet.api.model.operation.OperationType

/**
 * A stateless component that allows the user to select the financial direction
 * of an operation (Expense or Income) using mutually exclusive chips.
 *
 * @param selectedType The currently active [OperationType].
 * @param onTypeChanged Callback triggered when a new type is selected.
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun OperationTypeSelector(
    selectedType: OperationType,
    onTypeChanged: (OperationType) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = listOf("Dépense", "Revenu") // TODO: FormLabels
    val selectedIndex = if (selectedType == OperationType.EXPENSE) 0 else 1

    val activeColor = if (selectedIndex == 0) {
        AppTheme.colors.secondary.toColor()
    } else {
        AppTheme.colors.primary.toColor()
    }

    SegmentedControl(
        modifier = modifier,
        items = options,
        selectedIndex = selectedIndex,
        activeColor = activeColor,
        onItemSelected = { index ->
            val newType = if (index == 0) OperationType.EXPENSE else OperationType.INCOME
            onTypeChanged(newType)
        }
    )
}