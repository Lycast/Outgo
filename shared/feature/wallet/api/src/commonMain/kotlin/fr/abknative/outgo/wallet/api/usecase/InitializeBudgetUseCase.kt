package fr.abknative.outgo.wallet.api.usecase

import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result

/**
 * Chef d'orchestre pour le premier lancement de l'application.
 * Crée simultanément un Wallet et son Revenu Principal associé de manière sécurisée.
 */
interface InitializeBudgetUseCase {
    suspend operator fun invoke(walletName: String, incomeInCents: Long): Result<Unit, AppException>
}