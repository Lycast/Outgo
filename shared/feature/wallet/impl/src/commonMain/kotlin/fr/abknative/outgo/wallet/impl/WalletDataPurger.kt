package fr.abknative.outgo.wallet.impl

import fr.abknative.outgo.core.api.DataPurger
import fr.abknative.outgo.wallet.api.repository.OperationRepository
import fr.abknative.outgo.wallet.api.repository.WalletRepository

internal class WalletDataPurger(
    private val walletRepository: WalletRepository,
    private val operationRepository: OperationRepository
) : DataPurger {
    override suspend fun purgeData() {
        walletRepository.deleteAll()
        operationRepository.deleteAll()
    }
}