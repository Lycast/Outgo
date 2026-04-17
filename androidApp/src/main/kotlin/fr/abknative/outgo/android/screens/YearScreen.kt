package fr.abknative.outgo.android.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import fr.abknative.outgo.android.designsystem.foundation.AppText
import fr.abknative.outgo.android.designsystem.foundation.AppTheme

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
            text = "Year analyze Screen\n(Coming Soon)",
            style = AppTheme.typo.title,
            textAlign = TextAlign.Center
        )
    }
}