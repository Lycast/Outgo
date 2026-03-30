package fr.abknative.outgo.wallet.impl.usecase

import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.wallet.api.model.Operation
import fr.abknative.outgo.wallet.api.model.dashboard.DashboardData
import fr.abknative.outgo.wallet.api.model.operation.OperationType
import fr.abknative.outgo.wallet.api.usecase.CalculateDashboardDataUseCase

internal class CalculateDashboardDataUseCaseImpl(
    private val timeProvider: TimeProvider
) : CalculateDashboardDataUseCase {

    override fun invoke(operations: List<Operation>, currentMonth: Int, currentYear: Int): DashboardData {

        // --- Détermination du contexte temporel réel ---
        val realCurrentMonth = timeProvider.monthValue()
        val realCurrentYear = timeProvider.yearValue()

        val isViewingPast = currentYear < realCurrentYear || (currentYear == realCurrentYear && currentMonth < realCurrentMonth)
        val isViewingFuture = currentYear > realCurrentYear || (currentYear == realCurrentYear && currentMonth > realCurrentMonth)

        val today = timeProvider.dayOfMonth()

        // On récupère le dernier jour du mois visionné (ex: 28 pour Février) pour sécuriser les échéances
        val viewStartTimestamp = timeProvider.startOfMonth(currentMonth, currentYear)
        val lastDayOfViewedMonth = timeProvider.lastDayOfMonth(viewStartTimestamp)

        // --- Initialisation des accumulateurs ---
        var totalIncome = 0L
        var totalExpenses = 0L
        var remainingToPay = 0L

        // --- Moteur de calcul unifié ---
        operations.forEach { operation ->
            when (operation.type) {
                OperationType.INCOME -> {
                    totalIncome += operation.amountInCents
                }
                OperationType.EXPENSE -> {
                    totalExpenses += operation.amountInCents

                    // Logique dynamique du "Reste à payer"
                    when {
                        isViewingPast -> {
                            // Le mois est terminé, toutes les factures sont considérées comme payées
                        }
                        isViewingFuture -> {
                            // Le mois n'a pas commencé, toutes les factures sont à payer
                            remainingToPay += operation.amountInCents
                        }
                        else -> {
                            // Nous sommes dans le mois en cours.
                            // On extrait le jour d'ancrage (le jour du startDate).
                            val anchorDay = timeProvider.dayOfMonth(operation.startDate)

                            // On sécurise pour les fins de mois (ex: un abonnement le 31, mais on est en Février)
                            val effectiveBillingDay = anchorDay.coerceAtMost(lastDayOfViewedMonth)

                            if (effectiveBillingDay >= today) {
                                remainingToPay += operation.amountInCents
                            }
                        }
                    }
                }
            }
        }

        // --- Synthèse finale ---
        // Le "Reste à vivre" (Disposable Income) est mathématiquement le revenu total moins les dépenses totales.
        val disposableIncome = totalIncome - totalExpenses

        return DashboardData(
            currentBalanceInCents = disposableIncome,
            totalExpensesInCents = totalExpenses,
            remainingToPayInCents = remainingToPay,
            disposableIncomeInCents = disposableIncome
        )
    }
}