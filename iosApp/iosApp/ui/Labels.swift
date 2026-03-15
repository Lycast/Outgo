import Foundation
import SharedApp

private func getString(_ resource: Any) -> String {
    return IosResourceHelper.shared.getStringSync(resource: resource)
}


struct CommonLabels {
    static let shared = CommonLabels()
    
    var APP_NAME: String { getString(Res.string().app_name) }
    let CURRENCY_SYMBOL = "€" // Constante pure
    
    var ACTION_SAVE: String { getString(Res.string().common_action_save) }
    var ACTION_CANCEL: String { getString(Res.string().common_action_cancel) }
    var ACTION_DELETE: String { getString(Res.string().common_action_delete) }
    var ACTION_EDIT: String { getString(Res.string().common_action_edit) }
    var ACTION_DUPLICATE: String { getString(Res.string().common_action_duplicate) }
    var ACTION_CLOSE: String { getString(Res.string().common_action_close) }
}


struct DashboardLabels {
    static let shared = DashboardLabels()
    
    // Hero Section
    var TOOLTIP_BALANCE_TITLE: String { getString(Res.string().dashboard_tooltip_balance_title) }
    var TOOLTIP_BALANCE_DESC: String { getString(Res.string().dashboard_tooltip_balance_desc) }
    var TOOLTIP_BALANCE_DUE_TITLE: String { getString(Res.string().dashboard_tooltip_balance_due_title) }
    var TOOLTIP_BALANCE_DUE_DESC: String { getString(Res.string().dashboard_tooltip_balance_due_desc) }
    var HERO_TOTAL_CHARGES_LABEL: String { getString(Res.string().dashboard_hero_total_charges) }
    var HERO_REMAINING_TO_PAY_LABEL: String { getString(Res.string().dashboard_hero_remaining_to_pay) }

    // Liste et Filtres
    var TAB_ALL: String { getString(Res.string().dashboard_tab_all) }
    var TAB_PAID: String { getString(Res.string().dashboard_tab_paid) }
    var TAB_REMAINING: String { getString(Res.string().dashboard_tab_remaining) }

    // États de la liste
    var EMPTY_ALL: String { getString(Res.string().dashboard_empty_all) }
    var EMPTY_PAID: String { getString(Res.string().dashboard_empty_paid) }
    var EMPTY_REMAINING: String { getString(Res.string().dashboard_empty_remaining) }
    var EMPTY_STATE_DESC: String { getString(Res.string().dashboard_empty_state_desc) }

    var DEFAULT_NAME: String { getString(Res.string().dashboard_default_name) }
    var DUE_PREFIX: String { getString(Res.string().dashboard_due_prefix) }
    var DUE_MONTHLY_SUFFIX: String { getString(Res.string().dashboard_due_monthly_suffix) }

    // Noms des mois
    var MONTH_1: String { getString(Res.string().dashboard_month_1) }
    var MONTH_2: String { getString(Res.string().dashboard_month_2) }
    var MONTH_3: String { getString(Res.string().dashboard_month_3) }
    var MONTH_4: String { getString(Res.string().dashboard_month_4) }
    var MONTH_5: String { getString(Res.string().dashboard_month_5) }
    var MONTH_6: String { getString(Res.string().dashboard_month_6) }
    var MONTH_7: String { getString(Res.string().dashboard_month_7) }
    var MONTH_8: String { getString(Res.string().dashboard_month_8) }
    var MONTH_9: String { getString(Res.string().dashboard_month_9) }
    var MONTH_10: String { getString(Res.string().dashboard_month_10) }
    var MONTH_11: String { getString(Res.string().dashboard_month_11) }
    var MONTH_12: String { getString(Res.string().dashboard_month_12) }
}


struct FormLabels {
    static let shared = FormLabels()
    
    var SHEET_TITLE_ADD: String { getString(Res.string().form_sheet_title_add) }
    var SHEET_TITLE_EDIT: String { getString(Res.string().form_sheet_title_edit) }
    var FIELD_NAME: String { getString(Res.string().form_field_name) }
    var FIELD_AMOUNT: String { getString(Res.string().form_field_amount) }
    var FIELD_DATE_DESC: String { getString(Res.string().form_field_date_desc) }
    var FIELD_DAY: String { getString(Res.string().form_field_day) }
    var FIELD_MONTH: String { getString(Res.string().form_field_month) }
    var CYCLE_MONTHLY: String { getString(Res.string().form_cycle_monthly) }
    var CYCLE_YEARLY: String { getString(Res.string().form_cycle_yearly) }
}


struct HeaderLabels {
    static let shared = HeaderLabels()
    
    var SYNC_PROMO_TITLE: String { getString(Res.string().header_sync_promo_title) }
    var SYNC_PROMO_DESC: String { getString(Res.string().header_sync_promo_desc) }
    var SYNC_PROMO_ACTION_LOGIN: String { getString(Res.string().header_sync_promo_action_login) }
    var SYNC_PROMO_ACTION_LATER: String { getString(Res.string().header_sync_promo_action_later) }
}


struct BudgetEditDialogLabels {
    static let shared = BudgetEditDialogLabels()
    
    var DIALOG_BUDGET_TITLE: String { getString(Res.string().budget_dialog_title) }
    var DIALOG_BUDGET_DESC: String { getString(Res.string().budget_dialog_desc) }
    var DIALOG_BUDGET_INFO: String { getString(Res.string().budget_dialog_info) }
    var DIALOG_BUDGET_FIELD: String { getString(Res.string().budget_dialog_field) }
}


struct AccessibilityLabels {
    static let shared = AccessibilityLabels()
    
    var SYNCED: String { getString(Res.string().a11y_synced) }
    var NOT_SYNCED: String { getString(Res.string().a11y_not_synced) }
    var DELETE_EXPENSE: String { getString(Res.string().a11y_delete_expense) }
    var EDIT_EXPENSE: String { getString(Res.string().a11y_edit_expense) }
    var DUPLICATE_EXPENSE: String { getString(Res.string().a11y_duplicate_expense) }
    var EDIT_BUDGET: String { getString(Res.string().a11y_edit_budget) }
    var NAVIGATE_HOME: String { getString(Res.string().a11y_navigate_home) }
    var NAVIGATE_SETTINGS: String { getString(Res.string().a11y_navigate_settings) }
    var PREVIOUS_MONTH: String { getString(Res.string().a11y_previous_month) }
    var NEXT_MONTH: String { getString(Res.string().a11y_next_month) }
    var EXPAND_HERO: String { getString(Res.string().a11y_expand_hero) }
    var COLLAPSE_HERO: String { getString(Res.string().a11y_collapse_hero) }
    var ADD_EXPENSE: String { getString(Res.string().a11y_add_expense) }
    var CLOSE_DIALOG: String { getString(Res.string().a11y_close_dialog) }
    var INFO_EMPTY_STATE: String { getString(Res.string().a11y_info_empty_state) }
    var ICON_BALANCE: String { getString(Res.string().a11y_balance_desc) }
}


struct SettingsLabels {
    static let shared = SettingsLabels()
    
    var CHEVRON_DESC: String { getString(Res.string().a11y_chevron_desc) }

    var SECTION_APPEARANCE: String { getString(Res.string().settings_section_appearance) }
    var SECTION_SUPPORT: String { getString(Res.string().settings_section_support) }
    var SECTION_DATA: String { getString(Res.string().settings_section_data) }

    var DARK_MODE_TITLE: String { getString(Res.string().settings_dark_mode_title) }
    var DARK_MODE_SUBTITLE: String { getString(Res.string().settings_dark_mode_subtitle) }

    var COFFEE_TITLE: String { getString(Res.string().settings_coffee_title) }
    var COFFEE_SUBTITLE: String { getString(Res.string().settings_coffee_subtitle) }
    var TIPS_TITLE: String { getString(Res.string().settings_tips_title) }
    var TIPS_SUBTITLE: String { getString(Res.string().settings_tips_subtitle) }
    var CONTACT_TITLE: String { getString(Res.string().settings_contact_title) }
    var CONTACT_SUBTITLE: String { getString(Res.string().settings_contact_subtitle) }

    var SYNC_TITLE: String { getString(Res.string().settings_sync_title) }
    var SYNC_SUBTITLE: String { getString(Res.string().settings_sync_subtitle) }
    
    var APP_VERSION_PREFIX: String { getString(Res.string().settings_app_version_prefix) }

    // Constantes pures
    let URL_COFFEE = "https://buymeacoffee.com/lycast"
    let URL_SITE = "https://abknative.fr"
    let URL_CONTACT = "https://abknative.fr/contact"
}
