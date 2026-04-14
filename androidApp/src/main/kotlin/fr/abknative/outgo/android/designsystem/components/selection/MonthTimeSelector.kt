package fr.abknative.outgo.android.designsystem.components.selection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.AccessibilityLabels

@Composable
fun MonthTimeSelector(
    formattedMonth: String,
    canGoBack: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPrevious, enabled = canGoBack) {
            Icon(
                painter = painterResource(R.drawable.caret_left),
                contentDescription = AccessibilityLabels.PREVIOUS_MONTH,
                tint = if (canGoBack) AppTheme.colors.primary.toColor() else AppTheme.colors.textSecondary.toColor().copy(alpha = 0.2f)
            )
        }
        Text(
            text = formattedMonth,
            style = AppTheme.typo.title.copy(fontWeight = FontWeight.Bold),
            color = AppTheme.colors.textPrimary.toColor()
        )
        IconButton(onClick = onNext) {
            Icon(
                painter = painterResource(R.drawable.caret_right),
                contentDescription = AccessibilityLabels.NEXT_MONTH,
                tint = AppTheme.colors.primary.toColor()
            )
        }
    }
}