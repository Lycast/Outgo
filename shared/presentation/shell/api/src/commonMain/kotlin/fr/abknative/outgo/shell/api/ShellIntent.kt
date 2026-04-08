package fr.abknative.outgo.shell.api

sealed interface ShellIntent {
    /** Intent to manually trigger a cloud synchronization from the global header. */
    object RefreshSync : ShellIntent

    /** Intent to clear any global error message. */
    object DismissError : ShellIntent
}