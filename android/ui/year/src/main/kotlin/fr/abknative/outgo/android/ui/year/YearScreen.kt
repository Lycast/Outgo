package fr.abknative.outgo.android.ui.year

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import fr.abknative.outgo.android.core.designsystem.AppText
import fr.abknative.outgo.android.core.designsystem.AppTheme

/**
 * Placeholder screen for the Year analyze section to test navigation.
 */
@Composable
fun YearScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AppText(
            text = YearLabels.PLACEHOLDER_TITLE,
            style = AppTheme.typo.title,
            textAlign = TextAlign.Center
        )
    }
}