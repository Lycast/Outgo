package fr.abknative.outgo.android.components.shell

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.core.api.nav.AppStep

@Composable
fun BottomNavBar(
    currentStep: AppStep,
    isPremium: Boolean,
    onNavigate: (AppStep) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val backgroundColor = AppTheme.colors.surface100.toColor()

    val navItems = remember {
        listOf(AppStep.Month, AppStep.Year, null, AppStep.List, AppStep.Settings)
    }
    val selectedIndex = remember(currentStep) {
        when (currentStep) {
            AppStep.Month -> 0
            AppStep.Year -> 1
            AppStep.List -> 3
            AppStep.Settings -> 4
            else -> -1
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.dimens.extraLarge)
            .padding(bottom = AppTheme.dimens.large),
        contentAlignment = Alignment.Center
    ) {

        Surface(
            modifier = Modifier
                .height(42.dp)
                .fillMaxWidth(),
            shape = CircleShape,
            color = backgroundColor,
            shadowElevation = 12.dp
        ) {
            BoxWithConstraints(modifier = Modifier.fillMaxSize().padding(AppTheme.dimens.extraSmall)) {
                val segmentWidth = maxWidth / navItems.size

                if (selectedIndex != -1) {
                    val indicatorOffset by animateDpAsState(
                        targetValue = segmentWidth * selectedIndex,
                        animationSpec = spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioLowBouncy),
                        label = "navIndicatorOffset"
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(segmentWidth)
                            .offset(x = indicatorOffset)
                            .padding(AppTheme.dimens.extraSmall)
                            .clip(CircleShape)
                            .background(AppTheme.colors.primary.toColor().copy(alpha = 0.2f))
                    )
                }

                Row(modifier = Modifier.fillMaxSize()) {
                    navItems.forEachIndexed { index, step ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(CircleShape)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = {
                                        if (step == null) {
                                            onAddClick()
                                        } else {
                                            onNavigate(step)
                                        }
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (step != null) {
                                BottomNavIconContent(
                                    step = step,
                                    isSelected = index == selectedIndex,
                                    isLocked = step == AppStep.Year && !isPremium
                                )
                            }
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .size(62.dp)
                .clip(CircleShape)
                .background(backgroundColor)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onAddClick
                ),
            contentAlignment = Alignment.Center
        ) {
            BottomCentralAction()
        }
    }
}