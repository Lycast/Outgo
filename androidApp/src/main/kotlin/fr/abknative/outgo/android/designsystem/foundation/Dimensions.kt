package fr.abknative.outgo.android.designsystem.foundation

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.core.ui.DesignDimens

/**
 * Cette classe fait le pont entre tes tokens "Shared" et les "dp" Android.
 */
data class OutgoDimens(
    val none: Dp = DesignDimens.NONE.dp,
    val extraSmall: Dp = DesignDimens.EXTRA_SMALL.dp,
    val small: Dp = DesignDimens.SMALL.dp,
    val medium: Dp = DesignDimens.MEDIUM.dp,
    val large: Dp = DesignDimens.LARGE.dp,
    val extraLarge: Dp = DesignDimens.EXTRA_LARGE.dp,
    val big: Dp = DesignDimens.BIG.dp
)


/**
 * Defines the corner shapes (roundness) for the application's components.
 */
data class OutgoShapes(
    val none: Shape = RoundedCornerShape(DesignDimens.NONE.dp),
    val extraSmall: Shape = RoundedCornerShape(DesignDimens.EXTRA_SMALL.dp),
    val small: Shape = RoundedCornerShape(DesignDimens.SMALL.dp),
    val medium: Shape = RoundedCornerShape(DesignDimens.MEDIUM.dp),
    val large: Shape = RoundedCornerShape(DesignDimens.LARGE.dp),
    val extraLarge: Shape = RoundedCornerShape(DesignDimens.EXTRA_LARGE.dp),
    val full: Shape = RoundedCornerShape(100)
)