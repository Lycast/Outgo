package fr.abknative.outgo.wallet.impl.usecase.engine

import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.wallet.api.model.Operation
import fr.abknative.outgo.wallet.api.model.dashboard.DashboardData

internal class TimelineEngine(
    private val timeProvider: TimeProvider
) : DashboardCalculationEngine {

    override fun calculate(operations: List<Operation>, currentMonth: Int, currentYear: Int): DashboardData {
        // Logique Premium (Sprint ultérieur) :
        // Construction de la ligne de temps journalière, gestion fine des dates de fin, etc.
        // En attendant l'implémentation complète de l'algorithme Premium, on peut instancier
        // un SimpleSumEngine en interne ou retourner un mock, selon la stratégie de déploiement.

        return DashboardData(0L, 0L, 0L, 0L)
    }
}