package fr.abknative.outgo.core.ui

import fr.abknative.outgo.shared.core.ui.resources.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.getString

@OptIn(ExperimentalResourceApi::class)
object IosStringsResourcesLoader {

    /**
     * Charge l'intégralité des chaînes de caractères depuis les ressources partagées.
     * Cette fonction est suspendue et s'exécute en parallèle pour chaque groupe.
     * @throws Exception pour permettre la gestion d'erreur côté Swift (try await).
     */
    @Throws(Exception::class)
    suspend fun loadAll(): IosStringsResources = coroutineScope {
        val errorDeferred = async { loadError() }
        val commonDeferred = async { loadCommon() }
        val dashboardDeferred = async { loadDashboard() }
        val formDeferred = async { loadForm() }
        val headerDeferred = async { loadHeader() }
        val budgetDeferred = async { loadBudgetDialog() }
        val a11yDeferred = async { loadA11y() }
        val settingsDeferred = async { loadSettings() }

        // Assemblage final
        IosStringsResources(
            error = errorDeferred.await(),
            common = commonDeferred.await(),
            dashboard = dashboardDeferred.await(),
            form = formDeferred.await(),
            header = headerDeferred.await(),
            budgetDialog = budgetDeferred.await(),
            a11y = a11yDeferred.await(),
            settings = settingsDeferred.await()
        )
    }

    private suspend fun loadError() = ErrorStrings(
        errorEmptyName = getString(Res.string.error_outgoing_empty_name),
        errorInvalidAmount = getString(Res.string.error_outgoing_invalid_amount),
        errorNetwork = getString(Res.string.error_global_network),
        errorUnknown = getString(Res.string.error_global_unknown)
    )

    private suspend fun loadCommon() = CommonStrings(
        appName = getString(Res.string.app_name),
        actionSave = getString(Res.string.common_action_save),
        actionCancel = getString(Res.string.common_action_cancel),
        actionDelete = getString(Res.string.common_action_delete),
        actionEdit = getString(Res.string.common_action_edit),
        actionDuplicate = getString(Res.string.common_action_duplicate),
        actionClose = getString(Res.string.common_action_close)
    )

    private suspend fun loadDashboard() = DashboardStrings(
        tooltipBalanceTitle = getString(Res.string.dashboard_tooltip_balance_title),
        tooltipBalanceDesc = getString(Res.string.dashboard_tooltip_balance_desc),
        tooltipBalanceDueTitle = getString(Res.string.dashboard_tooltip_balance_due_title),
        tooltipBalanceDueDesc = getString(Res.string.dashboard_tooltip_balance_due_desc),
        heroTotalIncomeLabel = getString(Res.string.dashboard_hero_total_income),
        heroDisposableIncomeLabel = getString(Res.string.dashboard_hero_disposable_income),
        heroMissingIncomeLabel = getString(Res.string.dashboard_hero_missing_income),
        heroTotalChargesLabel = getString(Res.string.dashboard_hero_total_charges),
        heroRemainingToPayLabel = getString(Res.string.dashboard_hero_remaining_to_pay),
        tabAll = getString(Res.string.dashboard_tab_all),
        tabPaid = getString(Res.string.dashboard_tab_paid),
        tabRemaining = getString(Res.string.dashboard_tab_remaining),
        emptyAll = getString(Res.string.dashboard_empty_all),
        emptyPaid = getString(Res.string.dashboard_empty_paid),
        emptyRemaining = getString(Res.string.dashboard_empty_remaining),
        emptyStateDesc = getString(Res.string.dashboard_empty_state_desc),
        defaultName = getString(Res.string.dashboard_default_name),
        duePrefix = getString(Res.string.dashboard_due_prefix),
        monthAll = getString(Res.string.dashboard_month_all),
        month1 = getString(Res.string.dashboard_month_1),
        month2 = getString(Res.string.dashboard_month_2),
        month3 = getString(Res.string.dashboard_month_3),
        month4 = getString(Res.string.dashboard_month_4),
        month5 = getString(Res.string.dashboard_month_5),
        month6 = getString(Res.string.dashboard_month_6),
        month7 = getString(Res.string.dashboard_month_7),
        month8 = getString(Res.string.dashboard_month_8),
        month9 = getString(Res.string.dashboard_month_9),
        month10 = getString(Res.string.dashboard_month_10),
        month11 = getString(Res.string.dashboard_month_11),
        month12 = getString(Res.string.dashboard_month_12)
    )

    private suspend fun loadForm() = FormStrings(
        sheetTitleAdd = getString(Res.string.form_sheet_title_add),
        sheetTitleEdit = getString(Res.string.form_sheet_title_edit),
        fieldName = getString(Res.string.form_field_name),
        fieldPlaceHolderName = getString(Res.string.form_field_place_holder_name),
        fieldAmount = getString(Res.string.form_field_amount),
        fieldPlaceHolderAmount = getString(Res.string.form_field_place_holder_amount),
        fieldDateDesc = getString(Res.string.form_field_date_desc),
        cycleMonthly = getString(Res.string.form_cycle_monthly),
        cycleYearly = getString(Res.string.form_cycle_yearly)
    )

    private suspend fun loadHeader() = HeaderStrings(
        syncPromoTitle = getString(Res.string.header_sync_promo_title),
        syncPromoDesc = getString(Res.string.header_sync_promo_desc),
        syncPromoActionLogin = getString(Res.string.header_sync_promo_action_login),
        syncPromoActionLater = getString(Res.string.header_sync_promo_action_later)
    )

    private suspend fun loadBudgetDialog() = BudgetDialogStrings(
        title = getString(Res.string.budget_dialog_title),
        desc = getString(Res.string.budget_dialog_desc),
        info = getString(Res.string.budget_dialog_info),
        field = getString(Res.string.budget_dialog_field)
    )

    private suspend fun loadA11y() = A11yStrings(
        synced = getString(Res.string.a11y_synced),
        notSynced = getString(Res.string.a11y_not_synced),
        deleteExpense = getString(Res.string.a11y_delete_expense),
        editExpense = getString(Res.string.a11y_edit_expense),
        duplicateExpense = getString(Res.string.a11y_duplicate_expense),
        editBudget = getString(Res.string.a11y_edit_budget),
        navigateHome = getString(Res.string.a11y_navigate_home),
        navigateSettings = getString(Res.string.a11y_navigate_settings),
        previousMonth = getString(Res.string.a11y_previous_month),
        nextMonth = getString(Res.string.a11y_next_month),
        expandHero = getString(Res.string.a11y_expand_hero),
        collapseHero = getString(Res.string.a11y_collapse_hero),
        addExpense = getString(Res.string.a11y_add_expense),
        closeDialog = getString(Res.string.a11y_close_dialog),
        infoEmptyState = getString(Res.string.a11y_info_empty_state),
        iconBalance = getString(Res.string.a11y_balance_desc)
    )

    private suspend fun loadSettings() = SettingsStrings(
        chevronDesc = getString(Res.string.a11y_chevron_desc),
        sectionAppearance = getString(Res.string.settings_section_appearance),
        sectionSupport = getString(Res.string.settings_section_support),
        sectionData = getString(Res.string.settings_section_data),
        darkModeTitle = getString(Res.string.settings_dark_mode_title),
        darkModeSubtitle = getString(Res.string.settings_dark_mode_subtitle),
        coffeeTitle = getString(Res.string.settings_coffee_title),
        coffeeSubtitle = getString(Res.string.settings_coffee_subtitle),
        tipsTitle = getString(Res.string.settings_tips_title),
        tipsSubtitle = getString(Res.string.settings_tips_subtitle),
        contactTitle = getString(Res.string.settings_contact_title),
        contactSubtitle = getString(Res.string.settings_contact_subtitle),
        syncTitle = getString(Res.string.settings_sync_title),
        syncSubtitle = getString(Res.string.settings_sync_subtitle),
        appVersionPrefix = getString(Res.string.settings_app_version_prefix)
    )
}