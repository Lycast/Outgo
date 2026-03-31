package fr.abknative.outgo.wallet.impl.usecase.engine

import fr.abknative.outgo.wallet.api.model.Operation
import fr.abknative.outgo.wallet.api.model.dashboard.DashboardData

internal interface DashboardCalculationEngine {
    fun calculate(operations: List<Operation>, currentMonth: Int, currentYear: Int): DashboardData
}