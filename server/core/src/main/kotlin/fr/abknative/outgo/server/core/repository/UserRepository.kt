package fr.abknative.outgo.server.core.repository

interface UserRepository {
    fun ensureUserExists(userId: String, email: String = "debug@outgo.app")
}