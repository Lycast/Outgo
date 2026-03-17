package fr.abknative.outgo.core.ui

import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

@OptIn(ExperimentalResourceApi::class)
object IosResourceHelper {

    fun getStringSync(resource: Any): String {
        val res = resource as? StringResource ?: return ""
        return runBlocking { getString(res) }
    }
}