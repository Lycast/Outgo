package fr.abknative.outgo.list.impl

import fr.abknative.outgo.list.api.OperationFilter
import fr.abknative.outgo.wallet.api.model.Wallet

internal data class PipelineInput(
    val wallet: Wallet,
    val month: Int,
    val year: Int,
    val filter: OperationFilter,
    val isPremium: Boolean
)