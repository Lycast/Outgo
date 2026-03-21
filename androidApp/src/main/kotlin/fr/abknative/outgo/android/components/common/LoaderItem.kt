package fr.abknative.outgo.android.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.ui.AccessibilityLabels

@Composable
fun LoaderItem(
    modifier: Modifier = Modifier
) {

    val contentDesc = AccessibilityLabels.LOADING

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp)
            .semantics { contentDescription = contentDesc },
        contentAlignment = Alignment.Center
    ) {
        LinearProgressIndicator()
    }
}