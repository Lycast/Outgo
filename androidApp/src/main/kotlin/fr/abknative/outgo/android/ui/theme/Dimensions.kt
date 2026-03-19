package fr.abknative.outgo.android.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.core.ui.DesignSpacing

/**
 * Cette classe fait le pont entre tes tokens "Shared" et les "dp" Android.
 */
data class OutgoSpacing(
    val none: Dp = DesignSpacing.NONE.dp,
    val extraSmall: Dp = DesignSpacing.EXTRA_SMALL.dp,
    val small: Dp = DesignSpacing.SMALL.dp,
    val medium: Dp = DesignSpacing.MEDIUM.dp,
    val large: Dp = DesignSpacing.LARGE.dp,
    val extraLarge: Dp = DesignSpacing.EXTRA_LARGE.dp,
    val big: Dp = DesignSpacing.BIG.dp
)