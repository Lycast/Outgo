package fr.abknative.outgo.outgoing.impl

object OutgoContract {
    const val PATH_SYNC_PUSH = "/sync/push"
    const val PATH_SYNC_PULL = "/sync/pull"

    object Headers {
        const val AUTH = "Authorization"
        const val BEARER_PREFIX = "Bearer "
    }
}