package fr.abknative.outgo.android.components.shell

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.AccessibilityLabels
import fr.abknative.outgo.android.ui.CommonLabels
import fr.abknative.outgo.core.api.nav.AppStep

@Composable
fun BottomNavIconContent(
    step: AppStep,
    isSelected: Boolean,
    isLocked: Boolean
) {
    val tint = if (isSelected) AppTheme.colors.textOnBrand.toColor() else AppTheme.colors.textSecondary.toColor()

    val iconContent = @Composable {
        when (step) {

            AppStep.Month -> Text(
                text = CommonLabels.TAB_MONTH_INITIAL,
                style = AppTheme.typo.title.copy(fontWeight = FontWeight.SemiBold),
                color = tint
            )
            AppStep.Year -> Text(
                text = CommonLabels.TAB_YEAR_INITIAL,
                style = AppTheme.typo.title.copy(fontWeight = FontWeight.SemiBold),
                color = tint
            )

            AppStep.List -> Icon(
                painter = painterResource(id = R.drawable.list_bullets),
                contentDescription = AccessibilityLabels.NAVIGATE_LIST,
                tint = tint,
                modifier = Modifier.size(24.dp)
            )
            AppStep.Settings -> Icon(
                painter = painterResource(id = R.drawable.gear_six),
                contentDescription = AccessibilityLabels.NAVIGATE_SETTINGS,
                tint = tint,
                modifier = Modifier.size(24.dp)
            )
            else -> {}
        }
    }

    if (isLocked) {
        BadgedBox(
            badge = {
                Box(
                    modifier = Modifier
                        .offset(x = 8.dp)
                        .size(14.dp)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.crown_duotone),
                        contentDescription = null,
                        tint = AppTheme.colors.tertiary.toColor(),
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        ) {
            iconContent()
        }
    } else {
        iconContent()
    }
}