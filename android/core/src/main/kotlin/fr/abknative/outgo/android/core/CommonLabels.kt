package fr.abknative.outgo.android.core

import androidx.compose.runtime.Composable
import fr.abknative.outgo.shared.core.ui.resources.*
import org.jetbrains.compose.resources.stringResource

object CommonLabels {
    val APP_NAME @Composable get() = stringResource(Res.string.app_name)
    val CURRENCY_SYMBOL @Composable get() = stringResource(Res.string.common_currency_symbol)

    val ACTION_OK @Composable get() = stringResource(Res.string.common_action_ok)
    val ACTION_BACK @Composable get() = stringResource(Res.string.common_action_back)
    val ACTION_SAVE @Composable get() = stringResource(Res.string.common_action_save)
    val ACTION_CANCEL @Composable get() = stringResource(Res.string.common_action_cancel)
    val ACTION_DELETE @Composable get() = stringResource(Res.string.common_action_delete)
    val ACTION_CLEAR @Composable get() = stringResource(Res.string.common_action_clear)
    val ACTION_EDIT @Composable get() = stringResource(Res.string.common_action_edit)
    val ACTION_DUPLICATE @Composable get() = stringResource(Res.string.common_action_duplicate)
    val ACTION_CLOSE @Composable get() = stringResource(Res.string.common_action_close)
    val ACTION_RETRY @Composable get() = stringResource(Res.string.common_action_retry)
    val ACTION_CLOSURE @Composable get() = stringResource(Res.string.common_action_closure)

    val TAB_MONTH_INITIAL @Composable get() = stringResource(Res.string.common_nav_tab_month_initial)
    val TAB_YEAR_INITIAL @Composable get() = stringResource(Res.string.common_nav_tab_year_initial)
    val TAB_LIST @Composable get() = stringResource(Res.string.common_nav_tab_list)
    val TAB_SETTINGS @Composable get() = stringResource(Res.string.common_nav_tab_settings)

    val MONTH_1 @Composable get() = stringResource(Res.string.common_list_month_1)
    val MONTH_2 @Composable get() = stringResource(Res.string.common_list_month_2)
    val MONTH_3 @Composable get() = stringResource(Res.string.common_list_month_3)
    val MONTH_4 @Composable get() = stringResource(Res.string.common_list_month_4)
    val MONTH_5 @Composable get() = stringResource(Res.string.common_list_month_5)
    val MONTH_6 @Composable get() = stringResource(Res.string.common_list_month_6)
    val MONTH_7 @Composable get() = stringResource(Res.string.common_list_month_7)
    val MONTH_8 @Composable get() = stringResource(Res.string.common_list_month_8)
    val MONTH_9 @Composable get() = stringResource(Res.string.common_list_month_9)
    val MONTH_10 @Composable get() = stringResource(Res.string.common_list_month_10)
    val MONTH_11 @Composable get() = stringResource(Res.string.common_list_month_11)
    val MONTH_12 @Composable get() = stringResource(Res.string.common_list_month_12)

    val CYCLE_UNIQUE @Composable get() = stringResource(Res.string.common_form_cycle_unique)
    val CYCLE_WEEKLY @Composable get() = stringResource(Res.string.common_form_cycle_weekly)
    val CYCLE_MONTHLY @Composable get() = stringResource(Res.string.common_form_cycle_monthly)
    val CYCLE_YEARLY @Composable get() = stringResource(Res.string.common_form_cycle_yearly)
    val PLACEHOLDER_DATE_FORMAT @Composable get() = stringResource(Res.string.common_placeholder_date_format)
}

object AccessibilityLabels {
    val LOADING @Composable get() = stringResource(Res.string.a11y_loading)
    val SYNCED @Composable get() = stringResource(Res.string.a11y_synced)
    val NOT_SYNCED @Composable get() = stringResource(Res.string.a11y_not_synced)
    val DELETE_EXPENSE @Composable get() = stringResource(Res.string.a11y_delete_expense)
    val DUPLICATE_EXPENSE @Composable get() = stringResource(Res.string.a11y_duplicate_expense)
    val NAVIGATE_SETTINGS @Composable get() = stringResource(Res.string.a11y_navigate_settings)
    val PREVIOUS_MONTH @Composable get() = stringResource(Res.string.a11y_previous_month)
    val NEXT_MONTH @Composable get() = stringResource(Res.string.a11y_next_month)
    val INFO_TOOLTIP @Composable get() = stringResource(Res.string.a11y_info_tooltip)
    val DAY_SELECTOR @Composable get() = stringResource(Res.string.a11y_day_selector)
    val SYNC_ERROR @Composable get() = stringResource(Res.string.a11y_sync_error)
    val NAVIGATE_LIST @Composable get() = stringResource(Res.string.a11y_navigate_list)
    val VIEW_MODE @Composable get() = stringResource(Res.string.a11y_view_mode_selector)
    val ADD_EXPENSE @Composable get() = stringResource(Res.string.a11y_add_expense)

    val DISPLAY @Composable get() = stringResource(Res.string.a11y_display)
    val HIDE @Composable get() = stringResource(Res.string.a11y_hide)
}