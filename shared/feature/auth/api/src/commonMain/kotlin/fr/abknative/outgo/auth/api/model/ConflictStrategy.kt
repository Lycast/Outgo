package fr.abknative.outgo.auth.api.model

/**
 * Defines the user's choice when resolving a data conflict during authentication.
 * Occurs when local offline data exists and the target remote account already contains data.
 */
enum class ConflictStrategy {
    /** Merges local offline data into the remote account. */
    MERGE,

    /** Deletes local offline data and downloads the remote account's truth. */
    DISCARD_LOCAL
}