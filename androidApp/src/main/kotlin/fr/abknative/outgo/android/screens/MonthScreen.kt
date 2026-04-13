package fr.abknative.outgo.android.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor

/**
 * Placeholder screen for the Month analyze section to test navigation.
 */
@Composable
fun MonthScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Month Analyze Screen\n(Coming Soon)",
            style = AppTheme.typo.title,
            textAlign = TextAlign.Center,
            color = AppTheme.colors.textPrimary.toColor()
        )
    }
}