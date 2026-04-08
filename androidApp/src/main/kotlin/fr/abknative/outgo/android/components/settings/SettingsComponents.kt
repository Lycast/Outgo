package fr.abknative.outgo.android.components.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontWeight
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.components.common.CircleIcon
import fr.abknative.outgo.android.components.common.GlassCard
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.toColor

@Composable
fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.small)) {
        Text(
            text = title.uppercase(),
            style = AppTheme.typo.label,
            color = AppTheme.colors.primary.toColor(),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = AppTheme.spacing.medium)
        )
        GlassCard {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppTheme.spacing.medium)
            ) {
                content()
            }
        }
    }
}

@Composable
fun SettingsRowClickable(
    icon: Int,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick, role = Role.Button)
            .padding(AppTheme.spacing.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SettingsRowContent(icon, title, subtitle)
        Icon(
            painter = painterResource(R.drawable.caret_right),
            contentDescription = null,
            tint = AppTheme.colors.textSecondary.toColor()
        )
    }
}

@Composable
fun SettingsRowToggle(
    icon: Int,
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .toggleable(
                value = isChecked,
                onValueChange = onCheckedChange,
                role = Role.Switch
            )
            .padding(AppTheme.spacing.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SettingsRowContent(icon, title, subtitle)
        Switch(
            checked = isChecked,
            onCheckedChange = null,
            modifier = Modifier.clearAndSetSemantics {},
            colors = SwitchDefaults.colors(
                checkedThumbColor = AppTheme.colors.surface50.toColor(),
                checkedTrackColor = AppTheme.colors.primary.toColor(),
                checkedBorderColor = Color.Transparent,

                uncheckedThumbColor = AppTheme.colors.textSecondary.toColor(),
                uncheckedTrackColor = AppTheme.colors.surface100.toColor(),
                uncheckedBorderColor = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.3f),
            )
        )
    }
}

@Composable
fun SettingsRowContent(
    icon: Int,
    title: String,
    subtitle: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        CircleIcon(icon, 24, AppTheme.colors.primary.toColor(), AppTheme.colors.surface100.toColor())
        Spacer(modifier = Modifier.width(AppTheme.spacing.medium))
        Column {
            Text(
                text = title,
                style = AppTheme.typo.body,
                fontWeight = FontWeight.Medium,
                color = AppTheme.colors.textPrimary.toColor()
            )
            Text(
                text = subtitle,
                style = AppTheme.typo.label,
                color = AppTheme.colors.textSecondary.toColor()
            )
        }
    }
}