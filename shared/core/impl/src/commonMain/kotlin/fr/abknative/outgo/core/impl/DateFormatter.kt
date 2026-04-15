package fr.abknative.outgo.core.impl

import fr.abknative.outgo.core.api.EpochMillis

/**
 * Platform-specific date formatting delegator.
 */
internal expect fun formatToShortDate(millis: EpochMillis): String