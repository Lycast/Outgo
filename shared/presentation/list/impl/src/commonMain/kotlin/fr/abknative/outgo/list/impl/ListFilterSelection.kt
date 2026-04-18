package fr.abknative.outgo.list.impl

import fr.abknative.outgo.list.api.ListViewMode
import fr.abknative.outgo.list.api.ProjectedFilter
import fr.abknative.outgo.list.api.StandardFilter

/**
 * Encapsulates the user's active selection for filtering and navigating the list.
 * Modifying this object triggers a single, atomic update of the data pipeline,
 * preventing multiple database queries (glitches) during simultaneous state changes.
 */
internal data class ListFilterSelection(
    val month: Int,
    val year: Int,
    val viewMode: ListViewMode,
    val projectedFilter: ProjectedFilter,
    val standardFilter: StandardFilter
)