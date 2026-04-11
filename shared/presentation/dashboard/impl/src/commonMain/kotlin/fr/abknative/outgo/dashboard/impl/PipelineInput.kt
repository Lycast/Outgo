package fr.abknative.outgo.dashboard.impl

import fr.abknative.outgo.dashboard.api.OperationFilter

internal data class PipelineInput(
    val wallet: fr.abknative.outgo.wallet.api.model.Wallet,
    val month: Int,
    val year: Int,
    val filter: OperationFilter,
    val isPremium: Boolean
)