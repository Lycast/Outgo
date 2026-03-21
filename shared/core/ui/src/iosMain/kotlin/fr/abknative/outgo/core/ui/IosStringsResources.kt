package fr.abknative.outgo.core.ui

/**
 * Ce fichier regroupe toutes les chaînes de caractères résolues pour l'UI native.
 */
data class IosStringsResources(
    val error: ErrorStrings,
    val common: CommonStrings,
    val dashboard: DashboardStrings,
    val form: FormStrings,
    val header: HeaderStrings,
    val budgetDialog: BudgetDialogStrings,
    val a11y: A11yStrings,
    val settings: SettingsStrings
)

data class ErrorStrings(
    val errorEmptyName: String,
    val errorInvalidAmount: String,
    val errorNetwork: String,
    val errorUnknown: String
)

data class CommonStrings(
    val appName: String,
    val actionSave: String,
    val actionCancel: String,
    val actionDelete: String,
    val actionEdit: String,
    val actionDuplicate: String,
    val actionClose: String
)

data class DashboardStrings(
    val tooltipBalanceTitle: String,
    val tooltipBalanceDesc: String,
    val tooltipBalanceDueTitle: String,
    val tooltipBalanceDueDesc: String,
    val heroTotalIncomeLabel: String,
    val heroDisposableIncomeLabel: String,
    val heroMissingIncomeLabel: String,
    val heroTotalChargesLabel: String,
    val heroRemainingToPayLabel: String,
    val tabAll: String,
    val tabPaid: String,
    val tabRemaining: String,
    val emptyAll: String,
    val emptyPaid: String,
    val emptyRemaining: String,
    val emptyStateDesc: String,
    val defaultName: String,
    val duePrefix: String,
    val monthAll: String,
    val month1: String,
    val month2: String,
    val month3: String,
    val month4: String,
    val month5: String,
    val month6: String,
    val month7: String,
    val month8: String,
    val month9: String,
    val month10: String,
    val month11: String,
    val month12: String
)

data class FormStrings(
    val sheetTitleAdd: String,
    val sheetTitleEdit: String,
    val fieldName: String,
    val fieldPlaceHolderName: String,
    val fieldAmount: String,
    val fieldPlaceHolderAmount: String,
    val fieldDateDesc: String,
    val cycleMonthly: String,
    val cycleYearly: String
)

data class HeaderStrings(
    val syncPromoTitle: String,
    val syncPromoDesc: String,
    val syncPromoActionLogin: String,
    val syncPromoActionLater: String
)

data class BudgetDialogStrings(
    val title: String,
    val desc: String,
    val info: String,
    val field: String
)

data class A11yStrings(
    val loading: String,
    val synced: String,
    val notSynced: String,
    val deleteExpense: String,
    val editExpense: String,
    val duplicateExpense: String,
    val editBudget: String,
    val navigateHome: String,
    val navigateSettings: String,
    val previousMonth: String,
    val nextMonth: String,
    val expandHero: String,
    val collapseHero: String,
    val expandDesc:String,
    val collapseDesc: String,
    val addExpense: String,
    val infoTooltip: String,
    val infoEmptyState: String,
    val daySelector: String,
    val monthSelector: String,
)

data class SettingsStrings(
    val sectionAppearance: String,
    val sectionSupport: String,
    val sectionData: String,
    val darkModeTitle: String,
    val darkModeSubtitle: String,
    val coffeeTitle: String,
    val coffeeSubtitle: String,
    val tipsTitle: String,
    val tipsSubtitle: String,
    val contactTitle: String,
    val contactSubtitle: String,
    val syncTitle: String,
    val syncSubtitle: String,
    val appVersionPrefix: String
)