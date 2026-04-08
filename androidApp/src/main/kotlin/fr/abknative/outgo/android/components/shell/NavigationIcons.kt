package fr.abknative.outgo.android.components.shell

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.components.common.GlassCard
import fr.abknative.outgo.android.ui.AccessibilityLabels
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.toColor
import fr.abknative.outgo.app.nav.AppStep

@Composable
fun NavigationIcons(
    currentStep: AppStep,
    isPremium: Boolean,
    onNavigate: (AppStep) -> Unit,
    onTeasingClick: () -> Unit
) {
    // 1. On définit toutes les destinations de navigation
    val navDestinations = listOf(
        AppStep.Analyse,
        AppStep.Dashboard,
        AppStep.Settings
    )

    // 2. On boucle sur les destinations
    navDestinations.forEach { step ->
        // On n'affiche pas l'icône si c'est l'étape actuelle
        if (step == currentStep) return@forEach

        HeaderNavIcon {
            // 3. Le "when" sert à définir le contenu spécifique de chaque bouton
            when (step) {
                AppStep.Dashboard -> {
                    IconButton(onClick = { onNavigate(AppStep.Dashboard) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.house_line),
                            contentDescription = AccessibilityLabels.NAVIGATE_HOME,
                            tint = AppTheme.colors.primary.toColor()
                        )
                    }
                }

                AppStep.Analyse -> {
                    AnalyseIconButton(
                        isPremium = isPremium,
                        onClick = { if (isPremium) onNavigate(AppStep.Analyse) else onTeasingClick() }
                    )
                }

                AppStep.Settings -> {
                    IconButton(onClick = { onNavigate(AppStep.Settings) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.gear_six),
                            contentDescription = AccessibilityLabels.NAVIGATE_SETTINGS,
                            tint = AppTheme.colors.primary.toColor()
                        )
                    }
                }
                else -> {} // Sécurité pour les autres steps (Login, etc.)
            }
        }
    }
}

/**
 * On extrait la logique complexe de l'icône Analyse pour garder le "when" lisible
 */
@Composable
private fun AnalyseIconButton(
    isPremium: Boolean,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        if (!isPremium) {
            BadgedBox(
                badge = {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(AppTheme.colors.surface200.toColor()),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Premium",
                            tint = AppTheme.colors.primary.toColor(),
                            modifier = Modifier.size(8.dp)
                        )
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Insights,
                    contentDescription = "Analyse",
                    tint = AppTheme.colors.primary.toColor()
                )
            }
        } else {
            Icon(
                imageVector = Icons.Default.Insights,
                contentDescription = "Analyse",
                tint = AppTheme.colors.primary.toColor()
            )
        }
    }
}

/**
 * Reusable wrapper for header navigation buttons to maintain consistent glass styling.
 */
@Composable
fun HeaderNavIcon(content: @Composable () -> Unit) {
    GlassCard(modifier = Modifier.size(42.dp)) {
        content()
    }
}

