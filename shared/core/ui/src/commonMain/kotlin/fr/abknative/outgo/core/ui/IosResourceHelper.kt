package fr.abknative.outgo.core.ui

import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

/**
 * Ce pont est exclusivement réservé à iOS pour extraire
 * les strings générées par Compose Multiplatform de manière synchrone.
 */
object IosResourceHelper {
    fun getStringSync(resource: Any): String {
        val res = resource as? StringResource ?: return ""
        return runBlocking { getString(res) }
    }
}