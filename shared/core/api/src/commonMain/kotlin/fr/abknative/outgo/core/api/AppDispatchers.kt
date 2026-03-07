package fr.abknative.outgo.core.api

import kotlinx.coroutines.CoroutineDispatcher

interface AppDispatchers {
    val main: CoroutineDispatcher     // UI et interactions légères
    val io: CoroutineDispatcher       // Réseau et Base de données
    val default: CoroutineDispatcher  // Calculs lourds (tri, calculs mathématiques)
    val unconfined: CoroutineDispatcher
}