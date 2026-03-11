package fr.abknative.outgo.android.components.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import fr.abknative.outgo.android.ui.DashboardLabels
import fr.abknative.outgo.android.ui.theme.AppTheme

@Composable
fun MonthBudgetSelector(
    formattedTodayDate: String,
    onEditIncomeClick: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onEditIncomeClick),
        horizontalArrangement = Arrangement.Center
    ){
        Text(
            text = "${DashboardLabels.HERO_DATE_PREFIX} ${formattedTodayDate.uppercase()}",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(AppTheme.spacing.large)
        )
    }
}