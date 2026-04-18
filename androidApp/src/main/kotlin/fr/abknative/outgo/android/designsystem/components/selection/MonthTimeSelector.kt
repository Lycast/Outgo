package fr.abknative.outgo.android.designsystem.components.selection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.designsystem.foundation.AppText
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.AccessibilityLabels

@Composable
fun MonthTimeSelector(
    formattedMonth: String,
    canGoBack: Boolean,
    textStyle: TextStyle = AppTheme.typo.title.copy(fontWeight = FontWeight.Medium),
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .height(48.dp)
            .clip(CircleShape)
            .background(AppTheme.colors.surface50.toColor().copy(alpha = 0.3f))
            .padding(horizontal = AppTheme.dimens.small),
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
        AppText(
            text = formattedMonth,
            style = textStyle,
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