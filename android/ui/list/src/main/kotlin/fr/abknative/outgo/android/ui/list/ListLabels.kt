package fr.abknative.outgo.android.ui.list

import androidx.compose.runtime.Composable
import fr.abknative.outgo.shared.core.ui.resources.*
import org.jetbrains.compose.resources.stringResource

internal object ListLabels {

    val EMPTY_ALL @Composable get() = stringResource(Res.string.list_empty_all)
    val EMPTY_STATE_DESC @Composable get() = stringResource(Res.string.list_empty_state_desc)
    val DEFAULT_NAME @Composable get() = stringResource(Res.string.list_default_name)

    val CYCLE_UNIQUE_FORMATTED @Composable get() = stringResource(Res.string.list_cycle_unique_formatted)
    val CYCLE_WEEKLY_FORMATTED @Composable get() = stringResource(Res.string.list_cycle_weekly_formatted)
    val CYCLE_MONTHLY_FORMATTED @Composable get() = stringResource(Res.string.list_cycle_monthly_formatted)
    val CYCLE_YEARLY_FORMATTED @Composable get() = stringResource(Res.string.list_cycle_yearly_formatted)

    val DUE_PREFIX @Composable get() = stringResource(Res.string.list_due_prefix)
    val FROM_PREFIX @Composable get() = stringResource(Res.string.list_from_prefix)
    val TO_PREFIX  @Composable get() = stringResource(Res.string.list_to_prefix)
    val SINCE_PREFIX  @Composable get() = stringResource(Res.string.list_since_prefix)

    val TAB_PROJECTED @Composable get() = stringResource(Res.string.list_tab_projected)
    val TAB_STANDARD @Composable get() = stringResource(Res.string.list_tab_standard)
    val TAB_REMAINING @Composable get() = stringResource(Res.string.list_tab_remaining)
    val TAB_PAID @Composable get() = stringResource(Res.string.list_tab_paid)
    val TAB_ALL @Composable get() = stringResource(Res.string.list_tab_all)

    val VIEW_MODE_TOOLTIP_TITLE @Composable get() = stringResource(Res.string.list_tooltip_view_mode_title)
    val VIEW_MODE_TOOLTIP_DESC @Composable get() = stringResource(Res.string.list_tooltip_view_mode_desc)
}