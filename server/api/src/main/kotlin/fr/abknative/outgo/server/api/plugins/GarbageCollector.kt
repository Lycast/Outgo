package fr.abknative.outgo.server.api.plugins

import fr.abknative.outgo.server.core.repository.GarbageCollectorRepository
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory
import kotlin.time.Duration.Companion.milliseconds

fun Application.configureGarbageCollector() {
    val gcRepository by inject<GarbageCollectorRepository>()
    val logger = LoggerFactory.getLogger("GarbageCollector")

    // Lancement en tâche de fond attachée au cycle de vie de l'application
    launch(Dispatchers.IO) {
        while (isActive) {
            try {
                // Purge les données dont le deleted_at est > 30 jours
                val (deletedWallets, deletedOps) = gcRepository.purgeOldDeletedData(daysOld = 30L)

                if (deletedWallets > 0 || deletedOps > 0) {
                    logger.info("GC Run: Purged $deletedWallets wallets and $deletedOps operations.")
                }
            } catch (e: Exception) {
                logger.error("Error during Garbage Collection run", e)
            }

            // Attente de 24 heures avant le prochain cycle
            delay((24 * 60 * 60 * 1000L).milliseconds)
        }
    }
}