package fr.abknative.outgo.list.api

/**
 * Defines the two main universes of the List screen.
 */
enum class ListViewMode {
    /** Displays generated occurrences for a specific month (The "Envelope" view). */
    PROJECTED,
    /** Displays the raw rules and subscriptions independently of time (The "Engine" view). */
    STANDARD
}

/**
 * Filters applied when the user is in [ListViewMode.PROJECTED].
 */
enum class ProjectedFilter {
    /** Operations scheduled for today or later in the selected month. */
    REMAINING,
    /** Operations that have already occurred in the selected month. */
    PAST,
    /** All operations for the selected month. */
    ALL
}

/**
 * Filters applied when the user is in [ListViewMode.STANDARD].
 */
enum class StandardFilter {
    ALL,
    UNIQUE,
    WEEKLY,
    MONTHLY,
    YEARLY
}