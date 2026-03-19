package fr.abknative.outgo.android.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


@Composable
fun CircleIcon(
    icon: Int,
    tintColor: Color,
    containerColor: Color
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(color = containerColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(painter = painterResource(icon), contentDescription = null, tint = tintColor)
    }
}