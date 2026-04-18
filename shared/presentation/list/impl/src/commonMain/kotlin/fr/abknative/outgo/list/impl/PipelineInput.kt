package fr.abknative.outgo.list.impl

import fr.abknative.outgo.list.api.ListViewMode
import fr.abknative.outgo.list.api.ProjectedFilter
import fr.abknative.outgo.list.api.StandardFilter
import fr.abknative.outgo.wallet.api.model.Wallet

/**
 * Internal data class transporting all necessary parameters down the reactive pipeline
 * to fetch and map the correct list state.
 */
internal data class PipelineInput(
    val wallet: Wallet,
    val month: Int,
    val year: Int,
    val viewMode: ListViewMode,
    val projectedFilter: ProjectedFilter,
    val standardFilter: StandardFilter,
    val isPremium: Boolean
)