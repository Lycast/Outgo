package fr.abknative.outgo.android.ui.month

import androidx.compose.runtime.Composable
import fr.abknative.outgo.shared.core.ui.resources.*
import org.jetbrains.compose.resources.stringResource

internal object MonthLabels {

    val SECTION_BUDGET @Composable get() = stringResource(Res.string.month_section_budget)
    val SECTION_EXPENSES @Composable get() = stringResource(Res.string.month_section_expenses)
    val SECTION_UPCOMING @Composable get() = stringResource(Res.string.month_section_upcoming)
    val SECTION_RECURRENCE @Composable get() = stringResource(Res.string.month_section_recurrence)

    val EMPTY_STATE_TITLE @Composable get() = stringResource(Res.string.month_empty_state_title)
    val EMPTY_STATE_DESC @Composable get() = stringResource(Res.string.month_empty_state_desc)

    val HERO_TOTAL_CHARGES_LABEL @Composable get() = stringResource(Res.string.month_hero_total_charges)
    val HERO_REMAINING_TO_PAY_LABEL @Composable get() = stringResource(Res.string.month_hero_remaining_to_pay)

    val BUDGET_TITLE @Composable get() = stringResource(Res.string.month_dialog_title)
    val BUDGET_DESC @Composable get() = stringResource(Res.string.month_dialog_desc)
    val BUDGET_INFO @Composable get() = stringResource(Res.string.month_dialog_info)
    val BUDGET_FIELD @Composable get() = stringResource(Res.string.month_dialog_field)

    val WALLET_NAME_LABEL @Composable get() = stringResource(Res.string.form_field_name)
    val WALLET_NAME_PLACEHOLDER @Composable get() = stringResource(Res.string.onboarding_wallet_name_placeholder)
    val AMOUNT_PLACEHOLDER @Composable get() = stringResource(Res.string.form_field_place_holder_amount)

    val DUE_PREFIX @Composable get() = stringResource(Res.string.list_due_prefix)
}