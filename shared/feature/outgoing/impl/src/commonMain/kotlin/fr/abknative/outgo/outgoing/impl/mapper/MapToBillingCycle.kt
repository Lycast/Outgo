package fr.abknative.outgo.outgoing.impl.mapper

import fr.abknative.outgo.outgoing.api.BillingCycle
import fr.abknative.outgo.outgoing.api.BillingCycle.UNKNOWN

/**
 * Safely parses a string into a [BillingCycle].
 * Returns [BillingCycle.UNKNOWN] if the value is unrecognized.
 */
internal fun mapToBillingCycle(value: String): BillingCycle {
    return BillingCycle.entries.find {
        it.name.equals(value, ignoreCase = true)
    } ?: UNKNOWN
}