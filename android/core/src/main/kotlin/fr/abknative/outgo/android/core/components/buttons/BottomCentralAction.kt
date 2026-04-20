package fr.abknative.outgo.android.core.components.buttons

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.core.R
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor

@Composable
fun BottomCentralAction(
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.size(48.dp),
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