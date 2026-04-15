package fr.abknative.outgo.core.ui

/**
 * Shared animation durations (in milliseconds) to ensure
 * consistent pacing and feel across Android and iOS platforms.
 */
object DesignAnimations {

    /** Fast animations for quick UI feedback (e.g., toggles, quick transitions) */
    const val FAST: Int = 300

    /** Standard duration for filling bars or progress elements */
    const val NORMAL: Int = 800

    /** Slower duration for complex chart drawings (e.g., Donut charts) */
    const val SLOW: Int = 1000

    /** Specific duration required to trigger the long-press confirmation */
    const val HOLD_TO_CONFIRM: Int = 1500
}