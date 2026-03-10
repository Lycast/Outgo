package fr.abknative.outgo.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import fr.abknative.outgo.core.api.theme.DesignColors

// --- Mapping Mode Clair ---
private val LightColorScheme = lightColorScheme(
    primary = Color(DesignColors.Light.Primary),
    secondary = Color(DesignColors.Light.Secondary),
    tertiary = Color(DesignColors.Light.Tertiary),
    background = Color(DesignColors.Light.Background),
    surface = Color(DesignColors.Light.Surface),
    onSurface = Color(DesignColors.Light.OnSurface),
    onSurfaceVariant = Color(DesignColors.Light.onSurfaceVariant),
    error = Color(DesignColors.Light.Error)
)

// --- Mapping Mode Sombre ---
private val DarkColorScheme = darkColorScheme(
    primary = Color(DesignColors.Dark.Primary),
    secondary = Color(DesignColors.Dark.Secondary),
    tertiary = Color(DesignColors.Dark.Tertiary),
    background = Color(DesignColors.Dark.Background),
    surface = Color(DesignColors.Dark.Surface),
    onSurface = Color(DesignColors.Dark.OnSurface),
    onSurfaceVariant = Color(DesignColors.Dark.onSurfaceVariant),
    error = Color(DesignColors.Dark.Error)
)


data class DashboardColors(
    val budget: Color,
    val charges: Color,
    val remainingPay: Color,
    val remainingLive: Color,
    val warning: Color

)
val LocalDashboardColors = staticCompositionLocalOf {
    DashboardColors(Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified)
}

@Composable
fun OutgoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val dashColors = if (darkTheme) {
        DashboardColors(
            budget = Color(DesignColors.Dark.BarBudget),
            charges = Color(DesignColors.Dark.BarCharges),
            remainingPay = Color(DesignColors.Dark.BarRemainingPay),
            remainingLive = Color(DesignColors.Dark.BarRemainingLive),
            warning = Color(DesignColors.Dark.BarWarning)
        )
    } else {
        DashboardColors(
            budget = Color(DesignColors.Light.BarBudget),
            charges = Color(DesignColors.Light.BarCharges),
            remainingPay = Color(DesignColors.Light.BarRemainingPay),
            remainingLive = Color(DesignColors.Light.BarRemainingLive),
            warning = Color(DesignColors.Light.BarWarning)
        )
    }

    CompositionLocalProvider(
        LocalSpacing provides OutgoSpacing(),
        LocalDashboardColors provides dashColors
    ) {
        MaterialTheme(colorScheme = colorScheme, content = content)
    }
}

/**
 * Raccourci magique pour accéder aux espacements
 * Utilisation : AppTheme.spacing.medium
 */
object AppTheme {
    val spacing @Composable get() = LocalSpacing.current
    val dashboardColors @Composable get() = LocalDashboardColors.current
}