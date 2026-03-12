package fr.abknative.outgo.android.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.core.ui.DesignSpacing

/**
 * Cette classe fait le pont entre tes tokens "Shared" et les "dp" Android.
 */
data class OutgoSpacing(
    val none: Dp = DesignSpacing.None.dp,
    val extraSmall: Dp = DesignSpacing.ExtraSmall.dp,
    val small: Dp = DesignSpacing.Small.dp,
    val medium: Dp = DesignSpacing.Medium.dp,
    val large: Dp = DesignSpacing.Large.dp,
    val extraLarge: Dp = DesignSpacing.ExtraLarge.dp,
    val big: Dp = DesignSpacing.Big.dp
)

/**
 * "Local" pour stocker l'instance.
 */
val LocalSpacing = staticCompositionLocalOf { OutgoSpacing() }