package fr.abknative.outgo.month.impl

import fr.abknative.outgo.wallet.api.model.Wallet

internal data class MonthPipelineInput(
    val wallet: Wallet,
    val month: Int,
    val year: Int,
    val isPremium: Boolean
)