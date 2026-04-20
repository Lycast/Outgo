package fr.abknative.outgo.android.core.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import fr.abknative.outgo.core.ui.DesignColor
import fr.abknative.outgo.core.ui.OutgoTheme

val LocalOutgoColors = staticCompositionLocalOf<OutgoTheme> { error("No colors") }
val LocalOutgoDimens = staticCompositionLocalOf { OutgoDimens() }
val LocalOutgoTypography = staticCompositionLocalOf<OutgoTypography> { error("No typography provided") }
val LocalOutgoShapes = staticCompositionLocalOf { OutgoShapes() }

@Composable
fun OutgoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val theme = if (darkTheme) DesignColor.Dark else DesignColor.Light

    CompositionLocalProvider(
        LocalOutgoColors provides theme,
        LocalOutgoTypography provides OutgoTypographyInstance,
        LocalOutgoDimens provides OutgoDimens(),
        LocalOutgoShapes provides OutgoShapes()
    ) {
        content()
    }
}

object AppTheme {
    val colors: OutgoTheme @Composable get() = LocalOutgoColors.current
    val typo: OutgoTypography @Composable get() = LocalOutgoTypography.current
    val dimens @Composable get()= LocalOutgoDimens.current
    val shapes @Composable get() = LocalOutgoShapes.current
}

fun Long.toColor(): Color = Color(this)