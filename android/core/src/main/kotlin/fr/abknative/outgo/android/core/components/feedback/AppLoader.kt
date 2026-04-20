package fr.abknative.outgo.android.core.components.feedback

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import fr.abknative.outgo.android.core.AccessibilityLabels
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor

@Composable
fun AppLoader(
    modifier: Modifier = Modifier,
    strokeCap: StrokeCap = StrokeCap.Round
) {

    val contentDesc = AccessibilityLabels.LOADING
    val primaryColor = AppTheme.colors.primary.toColor()

    Box(
        modifier = modifier
            .fillMaxWidth().sizeIn(maxWidth = AppTheme.dimens.big, maxHeight = AppTheme.dimens.big)
            .semantics { contentDescription = contentDesc },
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = primaryColor,
            trackColor = primaryColor.copy(alpha = 0.15f),
            strokeCap = strokeCap,
            modifier = Modifier.fillMaxWidth()
        )
    }
}