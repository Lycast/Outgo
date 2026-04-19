package fr.abknative.outgo.android.ui

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
    val TAB_MONTH_INITIAL @Composable get() = stringResource(Res.string.nav_tab_month_initial)
    val TAB_YEAR_INITIAL @Composable get() = stringResource(Res.string.nav_tab_year_initial)
    val TAB_LIST @Composable get() = stringResource(Res.string.nav_tab_list)
    val TAB_SETTINGS @Composable get() = stringResource(Res.string.nav_tab_settings)

    val SYNC_OFFLINE_TITLE @Composable get() = stringResource(Res.string.common_sync_offline_title)
    val SYNC_OFFLINE_DESC @Composable get() = stringResource(Res.string.common_sync_offline_desc)
    val GLOBAL_UNKNOWN_ERROR @Composable get() = stringResource(Res.string.error_global_unknown)
}

object DialogLabels {
    val DELETE_OPERATION_TITLE @Composable get() = stringResource(Res.string.dialog_delete_operation_title)
    val DELETE_OPERATION_DESC @Composable get() = stringResource(Res.string.dialog_delete_operation_desc)
    val LOGOUT_TITLE @Composable get() = stringResource(Res.string.dialog_logout_title)
    val PURGE_TITLE @Composable get() = stringResource(Res.string.dialog_purge_title)
    val PURGE_DESC @Composable get() = stringResource(Res.string.dialog_purge_desc)
    val PURGE_CONFIRM @Composable get() = stringResource(Res.string.dialog_purge_confirm)
    val DELETE_ACCOUNT_TITLE @Composable get() = stringResource(Res.string.dialog_delete_account_title)

    val DIALOG_BUDGET_TITLE @Composable get() = stringResource(Res.string.budget_dialog_title)
    val DIALOG_BUDGET_DESC @Composable get() = stringResource(Res.string.budget_dialog_desc)
    val DIALOG_BUDGET_INFO @Composable get() = stringResource(Res.string.budget_dialog_info)
    val DIALOG_BUDGET_FIELD @Composable get() = stringResource(Res.string.budget_dialog_field)

    val DELETE_ACCOUNT_CHOICE_DESC @Composable get() = stringResource(Res.string.dialog_delete_account_choice_desc)
    val DELETE_ACCOUNT_LOCAL_TITLE @Composable get() = stringResource(Res.string.dialog_delete_account_local_title)
    val DELETE_ACCOUNT_LOCAL_DESC @Composable get() = stringResource(Res.string.dialog_delete_account_local_desc)
    val DELETE_ACCOUNT_SERVER_TITLE @Composable get() = stringResource(Res.string.dialog_delete_account_server_title)
    val DELETE_ACCOUNT_SERVER_DESC @Composable get() = stringResource(Res.string.dialog_delete_account_server_desc)
    val DELETE_ACCOUNT_AUTH_TITLE @Composable get() = stringResource(Res.string.dialog_delete_account_auth_title)
    val DELETE_ACCOUNT_AUTH_DESC @Composable get() = stringResource(Res.string.dialog_delete_account_auth_desc)

    val LOGOUT_DATA_QUESTION @Composable get() = stringResource(Res.string.dialog_logout_data_question)
    val LOGOUT_ACTION_KEEP_BUDGET @Composable get() = stringResource(Res.string.dialog_logout_action_keep_budget)
    val LOGOUT_ACTION_RETURN_LOCAL @Composable get() = stringResource(Res.string.dialog_logout_action_return_local)
}

object FormLabels {
    val SHEET_TITLE_ADD @Composable get() = stringResource(Res.string.form_sheet_title_add)
    val SHEET_TITLE_EDIT @Composable get() = stringResource(Res.string.form_sheet_title_edit)
    val FIELD_NAME @Composable get() = stringResource(Res.string.form_field_name)
    val FIELD_PLACE_HOLDER_NAME @Composable get() = stringResource(Res.string.form_field_place_holder_name)
    val FIELD_AMOUNT @Composable get() = stringResource(Res.string.form_field_amount)
    val FIELD_PLACE_HOLDER_AMOUNT @Composable get() = stringResource(Res.string.form_field_place_holder_amount)
    val CYCLE_UNIQUE @Composable get() = stringResource(Res.string.form_cycle_unique)
    val CYCLE_WEEKLY @Composable get() = stringResource(Res.string.form_cycle_weekly)
    val CYCLE_MONTHLY @Composable get() = stringResource(Res.string.form_cycle_monthly)
    val CYCLE_YEARLY @Composable get() = stringResource(Res.string.form_cycle_yearly)

    val FIELD_RECURRENCE_DESC @Composable get() = stringResource(Res.string.form_field_recurrence_desc)
    val FIELD_TYPE_DESC @Composable get() = stringResource(Res.string.form_field_type_desc)
    val TYPE_EXPENSE @Composable get() = stringResource(Res.string.form_type_expense)
    val TYPE_INCOME @Composable get() = stringResource(Res.string.form_type_income)
    val FIELD_START_DATE_LABEL @Composable get() = stringResource(Res.string.form_field_start_date_label)
    val FIELD_END_DATE_LABEL @Composable get() = stringResource(Res.string.form_field_end_date_label)
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
}

object ShellLabels {
    val COPY_SUBNAME @Composable get() = stringResource(Res.string.shell_copy_subname)
}

object HeaderLabels {
    val SYNC_PROMO_TITLE @Composable get() = stringResource(Res.string.header_sync_promo_title)
    val SYNC_PROMO_DESC @Composable get() = stringResource(Res.string.header_sync_promo_desc)
    val SYNC_PROMO_ACTION_LOGIN @Composable get() = stringResource(Res.string.header_sync_promo_action_login)
    val SYNC_PROMO_ACTION_LATER @Composable get() = stringResource(Res.string.header_sync_promo_action_later)
}

object OperationLabels {
    val PLACEHOLDER_DATE_FORMAT @Composable get() = stringResource(Res.string.operation_placeholder_date_format)
}

object OnboardingLabels {
    val CONFIG_TITLE @Composable get() = stringResource(Res.string.onboarding_config_title)
    val WALLET_NAME_LABEL @Composable get() = stringResource(Res.string.onboarding_wallet_name_label)
    val WALLET_NAME_PLACEHOLDER @Composable get() = stringResource(Res.string.onboarding_wallet_name_placeholder)
    val INCOME_AMOUNT_LABEL @Composable get() = stringResource(Res.string.onboarding_income_amount_label)
    val INCOME_AMOUNT_PLACEHOLDER @Composable get() = stringResource(Res.string.onboarding_income_amount_placeholder)
    val SUBMIT_ONBOARDING @Composable get() = stringResource(Res.string.onboarding_submit)
    val WELCOME_TITLE @Composable get() = stringResource(Res.string.onboarding_welcome_title)
    val WELCOME_SUBTITLE @Composable get() = stringResource(Res.string.onboarding_welcome_subtitle)
    val WELCOME_ACTION_START @Composable get() = stringResource(Res.string.onboarding_welcome_action_start)
    val WELCOME_ACTION_LOGIN @Composable get() = stringResource(Res.string.onboarding_welcome_action_login)
}

object MonthLabels {
    val DEFAULT_ERROR @Composable get() = stringResource(Res.string.month_default_error)
    val SECTION_BUDGET @Composable get() = stringResource(Res.string.month_section_budget)
    val SECTION_EXPENSES @Composable get() = stringResource(Res.string.month_section_expenses)
    val SECTION_UPCOMING @Composable get() = stringResource(Res.string.month_section_upcoming)
    val SECTION_RECURRENCE @Composable get() = stringResource(Res.string.month_section_recurrence)
}

object ListLabels {
    val HERO_TOTAL_CHARGES_LABEL @Composable get() = stringResource(Res.string.list_hero_total_charges)
    val HERO_REMAINING_TO_PAY_LABEL @Composable get() = stringResource(Res.string.list_hero_remaining_to_pay)
    val EMPTY_ALL @Composable get() = stringResource(Res.string.list_empty_all)
    val EMPTY_STATE_DESC @Composable get() = stringResource(Res.string.list_empty_state_desc)
    val DEFAULT_NAME @Composable get() = stringResource(Res.string.list_default_name)

    val DUE_PREFIX @Composable get() = stringResource(Res.string.list_due_prefix)
    val TAB_PROJECTED @Composable get() = stringResource(Res.string.list_tab_projected)
    val TAB_STANDARD @Composable get() = stringResource(Res.string.list_tab_standard)
    val TAB_REMAINING @Composable get() = stringResource(Res.string.list_tab_remaining)
    val TAB_PAID @Composable get() = stringResource(Res.string.list_tab_paid)
    val TAB_ALL @Composable get() = stringResource(Res.string.list_tab_all)

    val MONTH_1 @Composable get() = stringResource(Res.string.list_month_1)
    val MONTH_2 @Composable get() = stringResource(Res.string.list_month_2)
    val MONTH_3 @Composable get() = stringResource(Res.string.list_month_3)
    val MONTH_4 @Composable get() = stringResource(Res.string.list_month_4)
    val MONTH_5 @Composable get() = stringResource(Res.string.list_month_5)
    val MONTH_6 @Composable get() = stringResource(Res.string.list_month_6)
    val MONTH_7 @Composable get() = stringResource(Res.string.list_month_7)
    val MONTH_8 @Composable get() = stringResource(Res.string.list_month_8)
    val MONTH_9 @Composable get() = stringResource(Res.string.list_month_9)
    val MONTH_10 @Composable get() = stringResource(Res.string.list_month_10)
    val MONTH_11 @Composable get() = stringResource(Res.string.list_month_11)
    val MONTH_12 @Composable get() = stringResource(Res.string.list_month_12)
    val VIEW_MODE_TOOLTIP_TITLE @Composable get() = stringResource(Res.string.list_tooltip_view_mode_title)
    val VIEW_MODE_TOOLTIP_DESC @Composable get() = stringResource(Res.string.list_tooltip_view_mode_desc)
}

object SettingsLabels {
    val SECTION_APPEARANCE @Composable get() = stringResource(Res.string.settings_section_appearance)
    val SECTION_SUPPORT @Composable get() = stringResource(Res.string.settings_section_support)
    val SECTION_ACCOUNT @Composable get() = stringResource(Res.string.settings_section_account)
    val DARK_MODE_TITLE @Composable get() = stringResource(Res.string.settings_dark_mode_title)
    val DARK_MODE_SUBTITLE @Composable get() = stringResource(Res.string.settings_dark_mode_subtitle)
    val TIPS_TITLE @Composable get() = stringResource(Res.string.settings_tips_title)
    val TIPS_SUBTITLE @Composable get() = stringResource(Res.string.settings_tips_subtitle)
    val CONTACT_TITLE @Composable get() = stringResource(Res.string.settings_contact_title)
    val CONTACT_SUBTITLE @Composable get() = stringResource(Res.string.settings_contact_subtitle)
    val SYNC_TITLE @Composable get() = stringResource(Res.string.settings_sync_title)
    val SYNC_SUBTITLE @Composable get() = stringResource(Res.string.settings_sync_subtitle)
    val LOGOUT_TITLE @Composable get() = stringResource(Res.string.settings_logout_title)
    val LOGOUT_SUBTITLE @Composable get() = stringResource(Res.string.settings_logout_subtitle)
    val DELETE_ACCOUNT_TITLE @Composable get() = stringResource(Res.string.settings_delete_account)
    val DELETE_ACCOUNT_SUBTITLE @Composable get() = stringResource(Res.string.settings_delete_account_subtitle)
    val PURGE_TITLE @Composable get() = stringResource(Res.string.settings_local_purge_title)
    val PURGE_SUBTITLE @Composable get() = stringResource(Res.string.settings_local_purge_subtitle)
    val APP_VERSION_PREFIX @Composable get() = stringResource(Res.string.settings_app_version_prefix)

    val URL_SITE @Composable get() = stringResource(Res.string.settings_url_site)
    val URL_CONTACT @Composable get() = stringResource(Res.string.settings_url_contact)
    val SECTION_DATA_AND_ACCOUNT @Composable get() = stringResource(Res.string.settings_section_data_account)
}

object LoginLabels {
    val TITLE @Composable get() = stringResource(Res.string.login_title)
    val EMAIL_LABEL @Composable get() = stringResource(Res.string.login_email_label)
    val PASSWORD_LABEL @Composable get() = stringResource(Res.string.login_password_label)

    val GOOGLE_BUTTON @Composable get() = stringResource(Res.string.login_google_button)
    val APPLE_BUTTON @Composable get() = stringResource(Res.string.login_apple_button)
    val SUBMIT_BUTTON @Composable get() = stringResource(Res.string.login_submit_button)
    val REGISTER_BUTTON @Composable get() = stringResource(Res.string.login_register_button)
    val SUBMIT_ACTION @Composable get() = stringResource(Res.string.login_submit_action)
    val REGISTER_ACTION @Composable get() = stringResource(Res.string.login_register_action)
    val OR_LABEL @Composable get() = stringResource(Res.string.login_or_label)

    val BACK_TITLE @Composable get() = stringResource(Res.string.login_back_title)

    val CONFLICT_TITLE @Composable get() = stringResource(Res.string.login_conflict_title)
    val CONFLICT_DESC @Composable get() = stringResource(Res.string.login_conflict_desc)
    val CONFLICT_QUESTION @Composable get() = stringResource(Res.string.login_conflict_question)
    val CONFLICT_CONFIRM @Composable get() = stringResource(Res.string.login_conflict_confirm)
    val CONFLICT_CANCEL @Composable get() = stringResource(Res.string.login_conflict_cancel)

    val POST_LOGIN_SYNC_TITLE @Composable get() = stringResource(Res.string.login_dialog_sync_title)
    val POST_LOGIN_SYNC_MESSAGE @Composable get() = stringResource(Res.string.login_dialog_sync_message)
    val POST_LOGIN_ERROR_TITLE @Composable get() = stringResource(Res.string.login_dialog_error_title)
}

object PremiumLabels {
    val SHOWCASE_TITLE @Composable get() = stringResource(Res.string.premium_showcase_title)
    val SHOWCASE_SUBTITLE @Composable get() = stringResource(Res.string.premium_showcase_subtitle)
    val ESSENTIAL_TITLE @Composable get() = stringResource(Res.string.premium_essential_title)
    val ESSENTIAL_BADGE @Composable get() = stringResource(Res.string.premium_essential_badge)
    val ESSENTIAL_DESC @Composable get() = stringResource(Res.string.premium_essential_desc)

    val ESSENTIAL_FEATURES @Composable get() = listOf(
        stringResource(Res.string.premium_essential_feature_1),
        stringResource(Res.string.premium_essential_feature_2),
        stringResource(Res.string.premium_essential_feature_3)
    )

    val PREMIUM_TITLE @Composable get() = stringResource(Res.string.premium_premium_title)
    val PREMIUM_BADGE @Composable get() = stringResource(Res.string.premium_premium_badge)
    val PREMIUM_DESC @Composable get() = stringResource(Res.string.premium_premium_desc)

    val PREMIUM_FEATURES @Composable get() = listOf(
        stringResource(Res.string.premium_premium_feature_1),
        stringResource(Res.string.premium_premium_feature_2),
        stringResource(Res.string.premium_premium_feature_3),
        stringResource(Res.string.premium_premium_feature_4)
    )

    val NOTIFY_ME_ACTION @Composable get() = stringResource(Res.string.premium_notify_me_action)
}