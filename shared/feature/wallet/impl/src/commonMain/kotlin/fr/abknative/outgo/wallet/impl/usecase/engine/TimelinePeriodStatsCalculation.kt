package fr.abknative.outgo.wallet.impl.usecase.engine

/**
 * Moteur de calcul Premium (Timeline).
 * TODO V2: Implémenter le vrai algorithme de prédiction de trésorerie.
 * En attendant, ce moteur délègue temporairement son comportement au moteur standard
 * pour garantir un fonctionnement sans duplication de code.
 */
internal class TimelinePeriodStatsCalculation(
    private val fallbackEngine: SimplePeriodStatsCalculation
) : PeriodStatsCalculation by fallbackEngine