package fr.abknative.outgo.android.components.sheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
        AppTheme.colors.primary.toColor()
    } else {
        AppTheme.colors.tertiary.toColor()
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Sélectionnez le type d'opération", // TODO: Extract to FormLabels
            style = AppTheme.typo.caption,
            color = AppTheme.colors.textSecondary.toColor(),
            modifier = Modifier.padding(horizontal = AppTheme.spacing.small)
        )

        Spacer(modifier = Modifier.height(AppTheme.spacing.small))

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
}