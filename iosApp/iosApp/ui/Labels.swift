import Foundation
import SharedApp


class StringsCache {
    static let shared = StringsCache()
    var resources: IosStringsResources? = nil
}

struct CommonLabels {
    static let shared = CommonLabels()
    private var data: CommonStrings? { StringsCache.shared.resources?.common }

    var APP_NAME: String { data?.appName ?? "" }
    let CURRENCY_SYMBOL = "€"

    var ACTION_SAVE: String { data?.actionSave ?? "" }
    var ACTION_CANCEL: String { data?.actionCancel ?? "" }
    var ACTION_DELETE: String { data?.actionDelete ?? "" }
    var ACTION_EDIT: String { data?.actionEdit ?? "" }
    var ACTION_DUPLICATE: String { data?.actionDuplicate ?? "" }
    var ACTION_CLOSE: String { data?.actionClose ?? "" }
}

struct DashboardLabels {
    static let shared = DashboardLabels()
    private var data: DashboardStrings? { StringsCache.shared.resources?.dashboard }

    // Hero Section
    var TOOLTIP_BALANCE_TITLE: String { data?.tooltipBalanceTitle ?? "" }
    var TOOLTIP_BALANCE_DESC: String { data?.tooltipBalanceDesc ?? "" }
    var TOOLTIP_BALANCE_DUE_TITLE: String { data?.tooltipBalanceDueTitle ?? "" }
    var TOOLTIP_BALANCE_DUE_DESC: String { data?.tooltipBalanceDueDesc ?? "" }
    var HERO_TOTAL_INCOME_LABEL: String { data?.heroTotalIncomeLabel ?? "" }
    var HERO_DISPOSABLE_INCOME_LABEL: String { data?.heroDisposableIncomeLabel ?? "" }
    var HERO_MISSING_INCOME_LABEL: String { data?.heroMissingIncomeLabel ?? "" }
    var HERO_TOTAL_CHARGES_LABEL: String { data?.heroTotalChargesLabel ?? "" }
    var HERO_REMAINING_TO_PAY_LABEL: String { data?.heroRemainingToPayLabel ?? "" }

    // Liste et Filtres
    var TAB_ALL: String { data?.tabAll ?? "" }
    var TAB_PAID: String { data?.tabPaid ?? "" }
    var TAB_REMAINING: String { data?.tabRemaining ?? "" }

    // États de la liste
    var EMPTY_ALL: String { data?.emptyAll ?? "" }
    var EMPTY_PAID: String { data?.emptyPaid ?? "" }
    var EMPTY_REMAINING: String { data?.emptyRemaining ?? "" }
    var EMPTY_STATE_DESC: String { data?.emptyStateDesc ?? "" }

    var DEFAULT_NAME: String { data?.defaultName ?? "" }
    var DUE_PREFIX: String { data?.duePrefix ?? "" }
    var MONTH_ALL: String { data?.monthAll ?? "" }

    // Noms des mois
    var MONTH_1: String { data?.month1 ?? "" }
    var MONTH_2: String { data?.month2 ?? "" }
    var MONTH_3: String { data?.month3 ?? "" }
    var MONTH_4: String { data?.month4 ?? "" }
    var MONTH_5: String { data?.month5 ?? "" }
    var MONTH_6: String { data?.month6 ?? "" }
    var MONTH_7: String { data?.month7 ?? "" }
    var MONTH_8: String { data?.month8 ?? "" }
    var MONTH_9: String { data?.month9 ?? "" }
    var MONTH_10: String { data?.month10 ?? "" }
    var MONTH_11: String { data?.month11 ?? "" }
    var MONTH_12: String { data?.month12 ?? "" }
}

struct FormLabels {
    static let shared = FormLabels()
    private var data: FormStrings? { StringsCache.shared.resources?.form }

    var SHEET_TITLE_ADD: String { data?.sheetTitleAdd ?? "" }
    var SHEET_TITLE_EDIT: String { data?.sheetTitleEdit ?? "" }
    var FIELD_NAME: String { data?.fieldName ?? "" }
    var FIELD_PLACE_HOLDER_NAME: String { data?.fieldPlaceHolderName ?? "" }
    var FIELD_AMOUNT: String { data?.fieldAmount ?? "" }
    var FIELD_PLACE_HOLDER_AMOUNT: String { data?.fieldPlaceHolderAmount ?? "" }
    var FIELD_DATE_DESC: String { data?.fieldDateDesc ?? "" }
    var CYCLE_MONTHLY: String { data?.cycleMonthly ?? "" }
    var CYCLE_YEARLY: String { data?.cycleYearly ?? "" }
}

struct HeaderLabels {
    static let shared = HeaderLabels()
    private var data: HeaderStrings? { StringsCache.shared.resources?.header }

    var SYNC_PROMO_TITLE: String { data?.syncPromoTitle ?? "" }
    var SYNC_PROMO_DESC: String { data?.syncPromoDesc ?? "" }
    var SYNC_PROMO_ACTION_LOGIN: String { data?.syncPromoActionLogin ?? "" }
    var SYNC_PROMO_ACTION_LATER: String { data?.syncPromoActionLater ?? "" }
}

struct BudgetEditDialogLabels {
    static let shared = BudgetEditDialogLabels()
    private var data: BudgetDialogStrings? { StringsCache.shared.resources?.budgetDialog }

    var DIALOG_BUDGET_TITLE: String { data?.title ?? "" }
    var DIALOG_BUDGET_DESC: String { data?.desc ?? "" }
    var DIALOG_BUDGET_INFO: String { data?.info ?? "" }
    var DIALOG_BUDGET_FIELD: String { data?.field ?? "" }
}

struct AccessibilityLabels {
    static let shared = AccessibilityLabels()
    private var data: A11yStrings? { StringsCache.shared.resources?.a11y }

    var LOADING: String { data?.loading ?? "" }
    var SYNCED: String { data?.synced ?? "" }
    var NOT_SYNCED: String { data?.notSynced ?? "" }
    var DELETE_EXPENSE: String { data?.deleteExpense ?? "" }
    var EDIT_EXPENSE: String { data?.editExpense ?? "" }
    var DUPLICATE_EXPENSE: String { data?.duplicateExpense ?? "" }
    var EDIT_BUDGET: String { data?.editBudget ?? "" }
    var NAVIGATE_HOME: String { data?.navigateHome ?? "" }
    var NAVIGATE_SETTINGS: String { data?.navigateSettings ?? "" }
    var PREVIOUS_MONTH: String { data?.previousMonth ?? "" }
    var NEXT_MONTH: String { data?.nextMonth ?? "" }
    var EXPAND_HERO: String { data?.expandHero ?? "" }
    var COLLAPSE_HERO: String { data?.collapseHero ?? "" }
    var EXPAND_DESC: String { data?.expandDesc ?? "" }
    var COLLAPSE_DESC: String { data?.collapseDesc ?? "" }
    var ADD_EXPENSE: String { data?.addExpense ?? "" }
    var INFO_TOOLTIP: String { data?.infoTooltip ?? "" }
    var INFO_EMPTY_STATE: String { data?.infoEmptyState ?? "" }
    var DAY_SELECTOR: String { data?.daySelector ?? "" }
    var MONTH_SELECTOR: String { data?.monthSelector ?? "" }
}

struct SettingsLabels {
    static let shared = SettingsLabels()
    private var data: SettingsStrings? { StringsCache.shared.resources?.settings }

    var SECTION_APPEARANCE: String { data?.sectionAppearance ?? "" }
    var SECTION_SUPPORT: String { data?.sectionSupport ?? "" }
    var SECTION_DATA: String { data?.sectionData ?? "" }
    var DARK_MODE_TITLE: String { data?.darkModeTitle ?? "" }
    var DARK_MODE_SUBTITLE: String { data?.darkModeSubtitle ?? "" }
    var COFFEE_TITLE: String { data?.coffeeTitle ?? "" }
    var COFFEE_SUBTITLE: String { data?.coffeeSubtitle ?? "" }
    var TIPS_TITLE: String { data?.tipsTitle ?? "" }
    var TIPS_SUBTITLE: String { data?.tipsSubtitle ?? "" }
    var CONTACT_TITLE: String { data?.contactTitle ?? "" }
    var CONTACT_SUBTITLE: String { data?.contactSubtitle ?? "" }
    var SYNC_TITLE: String { data?.syncTitle ?? "" }
    var SYNC_SUBTITLE: String { data?.syncSubtitle ?? "" }
    var APP_VERSION_PREFIX: String { data?.appVersionPrefix ?? "" }

    let URL_COFFEE = "https://buymeacoffee.com/lycast"
    let URL_SITE = "https://abknative.fr"
    let URL_CONTACT = "https://abknative.fr/contact"
}
