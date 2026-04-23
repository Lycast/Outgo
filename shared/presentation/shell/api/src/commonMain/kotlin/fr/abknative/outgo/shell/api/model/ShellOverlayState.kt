package fr.abknative.outgo.shell.api.model

/**
 * Defines which global component (Overlay) should take control of the screen.
 */
enum class ShellOverlayState {
    NONE,
    LOADING,
    CONFLICT,
    ERROR
}