package fr.abknative.outgo.android.components.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.ui.AccessibilityLabels
import fr.abknative.outgo.android.ui.theme.AppTheme


@Composable
fun MonthBudgetSelector(
    formattedMonthDate: String,
    onPreviousMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.spacing.large),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButton(onClick = onPreviousMonthClick) {
            Icon(
                painter = painterResource(R.drawable.caret_left),
                contentDescription = AccessibilityLabels.PREVIOUS_MONTH,
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Text(
            text = formattedMonthDate.uppercase(),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(AppTheme.spacing.large)
        )

        IconButton(onClick = onNextMonthClick) {
            Icon(
                painter = painterResource(R.drawable.caret_right),
                contentDescription = AccessibilityLabels.NEXT_MONTH,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}