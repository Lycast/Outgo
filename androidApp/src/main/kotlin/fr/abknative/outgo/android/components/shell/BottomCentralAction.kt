package fr.abknative.outgo.android.components.shell

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor

/**
 * The central action button for the bottom navigation bar.
 * Designed to stand out as the primary "Add" action.
 */
@Composable
fun BottomCentralAction() {
    Surface(
        modifier = Modifier.size(48.dp),
        shape = CircleShape,
        color = AppTheme.colors.secondary.toColor().copy(alpha = 0.3f)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.plus_bold),
            contentDescription = null,
            tint = AppTheme.colors.textOnBrand.toColor(),
            modifier = Modifier.padding(AppTheme.dimens.small)
        )
    }
}