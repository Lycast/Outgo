package fr.abknative.outgo.shell.api

sealed interface ShellIntent {

    /**
     * Intent to signal that the user wants to add a new operation from a global component.
     */
    object TriggerAddOperation : ShellIntent

    /**
     * Intent to reset the add operation trigger once it has been handled by the destination screen.
     * This prevents the modal from re-opening on configuration changes or back navigation.
     */
    object ConsumeAddOperationTrigger : ShellIntent

    /** Intent to manually trigger a cloud synchronization from the global header. */
    object RefreshSync : ShellIntent

    /** Intent to clear any global error message. */
    object DismissError : ShellIntent
}