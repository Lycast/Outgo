package fr.abknative.outgo.android.ui

import androidx.compose.runtime.Composable
import fr.abknative.outgo.shared.core.ui.resources.*
import org.jetbrains.compose.resources.stringResource

object CommonLabels {
    val APP_NAME @Composable get() = stringResource(Res.string.app_name)
    const val CURRENCY_SYMBOL = "€"

    val ACTION_SAVE @Composable get() = stringResource(Res.string.common_action_save)
    val ACTION_CANCEL @Composable get() = stringResource(Res.string.common_action_cancel)
    val ACTION_DELETE @Composable get() = stringResource(Res.string.common_action_delete)
    val ACTION_EDIT @Composable get() = stringResource(Res.string.common_action_edit)
    val ACTION_DUPLICATE @Composable get() = stringResource(Res.string.common_action_duplicate)
    val ACTION_CLOSE @Composable get() = stringResource(Res.string.common_action_close)
}

object DashboardLabels {
    // Hero Section
    val TOOLTIP_BALANCE_TITLE @Composable get()  = stringResource(Res.string.dashboard_tooltip_balance_title)
    val TOOLTIP_BALANCE_DESC @Composable get()  = stringResource(Res.string.dashboard_tooltip_balance_desc)
    val TOOLTIP_BALANCE_DUE_TITLE @Composable get()  = stringResource(Res.string.dashboard_tooltip_balance_due_title)
    val TOOLTIP_BALANCE_DUE_DESC @Composable get()  = stringResource(Res.string.dashboard_tooltip_balance_due_desc)
    val HERO_TOTAL_INCOME_LABEL @Composable get() = stringResource(Res.string.dashboard_hero_total_income)
    val HERO_DISPOSABLE_INCOME_LABEL @Composable get() = stringResource(Res.string.dashboard_hero_disposable_income)
    val HERO_MISSING_INCOME_LABEL @Composable get() = stringResource(Res.string.dashboard_hero_missing_income)
    val HERO_TOTAL_CHARGES_LABEL @Composable get() = stringResource(Res.string.dashboard_hero_total_charges)
    val HERO_REMAINING_TO_PAY_LABEL @Composable get() = stringResource(Res.string.dashboard_hero_remaining_to_pay)

    // Liste et Filtres
    val TAB_ALL @Composable get() = stringResource(Res.string.dashboard_tab_all)
    val TAB_PAID @Composable get() = stringResource(Res.string.dashboard_tab_paid)
    val TAB_REMAINING @Composable get() = stringResource(Res.string.dashboard_tab_remaining)

    // États de la liste
    val EMPTY_ALL @Composable get() = stringResource(Res.string.dashboard_empty_all)
    val EMPTY_PAID @Composable get() = stringResource(Res.string.dashboard_empty_paid)
    val EMPTY_REMAINING @Composable get() = stringResource(Res.string.dashboard_empty_remaining)
    val EMPTY_STATE_DESC @Composable get() = stringResource(Res.string.dashboard_empty_state_desc)

    val DEFAULT_NAME @Composable get() = stringResource(Res.string.dashboard_default_name)
    val DUE_PREFIX @Composable get() = stringResource(Res.string.dashboard_due_prefix)
    val MONTH_ALL @Composable get() = stringResource(Res.string.dashboard_month_all)

    // Noms des mois
    val MONTH_1 @Composable get() = stringResource(Res.string.dashboard_month_1)
    val MONTH_2 @Composable get() = stringResource(Res.string.dashboard_month_2)
    val MONTH_3 @Composable get() = stringResource(Res.string.dashboard_month_3)
    val MONTH_4 @Composable get() = stringResource(Res.string.dashboard_month_4)
    val MONTH_5 @Composable get() = stringResource(Res.string.dashboard_month_5)
    val MONTH_6 @Composable get() = stringResource(Res.string.dashboard_month_6)
    val MONTH_7 @Composable get() = stringResource(Res.string.dashboard_month_7)
    val MONTH_8 @Composable get() = stringResource(Res.string.dashboard_month_8)
    val MONTH_9 @Composable get() = stringResource(Res.string.dashboard_month_9)
    val MONTH_10 @Composable get() = stringResource(Res.string.dashboard_month_10)
    val MONTH_11 @Composable get() = stringResource(Res.string.dashboard_month_11)
    val MONTH_12 @Composable get() = stringResource(Res.string.dashboard_month_12)
}

object FormLabels {
    val SHEET_TITLE_ADD @Composable get() = stringResource(Res.string.form_sheet_title_add)
    val SHEET_TITLE_EDIT @Composable get() = stringResource(Res.string.form_sheet_title_edit)
    val FIELD_NAME @Composable get() = stringResource(Res.string.form_field_name)
    val FIELD_PLACE_HOLDER_NAME @Composable get() = stringResource(Res.string.form_field_place_holder_name)
    val FIELD_AMOUNT @Composable get() = stringResource(Res.string.form_field_amount)
    val FIELD_PLACE_HOLDER_AMOUNT @Composable get() = stringResource(Res.string.form_field_place_holder_amount)
    val FIELD_DATE_DESC @Composable get() = stringResource(Res.string.form_field_date_desc)
    val CYCLE_MONTHLY @Composable get() = stringResource(Res.string.form_cycle_monthly)
    val CYCLE_YEARLY @Composable get() = stringResource(Res.string.form_cycle_yearly)
}

object HeaderLabels {
    val SYNC_PROMO_TITLE @Composable get() = stringResource(Res.string.header_sync_promo_title)
    val SYNC_PROMO_DESC @Composable get() = stringResource(Res.string.header_sync_promo_desc)
    val SYNC_PROMO_ACTION_LOGIN @Composable get() = stringResource(Res.string.header_sync_promo_action_login)
    val SYNC_PROMO_ACTION_LATER @Composable get() = stringResource(Res.string.header_sync_promo_action_later)
}

object BudgetEditDialogLabels {
    val DIALOG_BUDGET_TITLE @Composable get() = stringResource(Res.string.budget_dialog_title)
    val DIALOG_BUDGET_DESC @Composable get() = stringResource(Res.string.budget_dialog_desc)
    val DIALOG_BUDGET_INFO @Composable get() = stringResource(Res.string.budget_dialog_info)
    val DIALOG_BUDGET_FIELD @Composable get() = stringResource(Res.string.budget_dialog_field)
}

object AccessibilityLabels {
    val LOADING @Composable get() = stringResource(Res.string.a11y_loading)
    val SYNCED @Composable get() = stringResource(Res.string.a11y_synced)
    val NOT_SYNCED @Composable get() = stringResource(Res.string.a11y_not_synced)
    val DELETE_EXPENSE @Composable get() = stringResource(Res.string.a11y_delete_expense)
    val EDIT_EXPENSE @Composable get() = stringResource(Res.string.a11y_edit_expense)
    val DUPLICATE_EXPENSE @Composable get() = stringResource(Res.string.a11y_duplicate_expense)
    val EDIT_BUDGET @Composable get() = stringResource(Res.string.a11y_edit_budget)
    val NAVIGATE_HOME @Composable get() = stringResource(Res.string.a11y_navigate_home)
    val NAVIGATE_SETTINGS @Composable get() = stringResource(Res.string.a11y_navigate_settings)
    val PREVIOUS_MONTH @Composable get() = stringResource(Res.string.a11y_previous_month)
    val NEXT_MONTH @Composable get() = stringResource(Res.string.a11y_next_month)
    val EXPAND_HERO @Composable get() = stringResource(Res.string.a11y_expand_hero)
    val COLLAPSE_HERO @Composable get() = stringResource(Res.string.a11y_collapse_hero)
    val EXPAND_DESC @Composable get() = stringResource(Res.string.a11y_expand_hero_desc)
    val COLLAPSE_DESC @Composable get() = stringResource(Res.string.a11y_collapse_hero_desc)
    val ADD_EXPENSE @Composable get() = stringResource(Res.string.a11y_add_expense)
    val INFO_TOOLTIP @Composable get() = stringResource(Res.string.a11y_info_tooltip)
    val INFO_EMPTY_STATE @Composable get() = stringResource(Res.string.a11y_info_empty_state)
    val DAY_SELECTOR @Composable get() = stringResource(Res.string.a11y_day_selector)
    val MONTH_SELECTOR @Composable get() = stringResource(Res.string.a11y_month_selector)
}

object SettingsLabels {

    val SECTION_APPEARANCE @Composable get() = stringResource(Res.string.settings_section_appearance)
    val SECTION_SUPPORT @Composable get() = stringResource(Res.string.settings_section_support)
    val SECTION_DATA @Composable get() = stringResource(Res.string.settings_section_data)

    val DARK_MODE_TITLE @Composable get() = stringResource(Res.string.settings_dark_mode_title)
    val DARK_MODE_SUBTITLE @Composable get() = stringResource(Res.string.settings_dark_mode_subtitle)

    val COFFEE_TITLE @Composable get() = stringResource(Res.string.settings_coffee_title)
    val COFFEE_SUBTITLE @Composable get() = stringResource(Res.string.settings_coffee_subtitle)
    val TIPS_TITLE @Composable get() = stringResource(Res.string.settings_tips_title)
    val TIPS_SUBTITLE @Composable get() = stringResource(Res.string.settings_tips_subtitle)
    val CONTACT_TITLE @Composable get() = stringResource(Res.string.settings_contact_title)
    val CONTACT_SUBTITLE @Composable get() = stringResource(Res.string.settings_contact_subtitle)

    val SYNC_TITLE @Composable get() = stringResource(Res.string.settings_sync_title)
    val SYNC_SUBTITLE @Composable get() = stringResource(Res.string.settings_sync_subtitle)

    const val URL_COFFEE = "https://buymeacoffee.com/lycast"
    const val URL_SITE = "https://abknative.fr"
    const val URL_CONTACT = "https://abknative.fr/contact"

    val APP_VERSION_PREFIX @Composable get() = stringResource(Res.string.settings_app_version_prefix)
}