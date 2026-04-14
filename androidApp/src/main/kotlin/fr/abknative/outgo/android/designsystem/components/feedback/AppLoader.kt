package fr.abknative.outgo.android.designsystem.components.feedback

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.AccessibilityLabels

@Composable
fun AppLoader(
    modifier: Modifier = Modifier,
    strokeCap: StrokeCap = StrokeCap.Round
) {

    val contentDesc = AccessibilityLabels.LOADING
    val primaryColor = AppTheme.colors.primary.toColor()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(AppTheme.dimens.big)
            .semantics { contentDescription = contentDesc },
        contentAlignment = Alignment.Center
    ) {
        LinearProgressIndicator(
            color = primaryColor,
            trackColor = primaryColor.copy(alpha = 0.15f),
            strokeCap = strokeCap,
            modifier = Modifier.fillMaxWidth()
        )
    }
}