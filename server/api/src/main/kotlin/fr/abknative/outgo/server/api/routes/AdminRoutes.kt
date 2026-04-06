package fr.abknative.outgo.server.api.routes

import fr.abknative.outgo.server.core.repository.GarbageCollectorRepository
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory

fun Route.adminRoutes() {
    val gcRepository by inject<GarbageCollectorRepository>()
    val logger = LoggerFactory.getLogger("GarbageCollector")

    get("/internal/cron/gc") {
        val cronSecret = System.getenv("CRON_SECRET")
        val headerSecret = call.request.headers["X-Cron-Secret"]

        if (cronSecret == null || headerSecret != cronSecret) {
            call.respond(HttpStatusCode.Unauthorized)
            return@get
        }

        val (deletedWallets, deletedOps) = gcRepository.purgeOldDeletedData(daysOld = 30L)
        logger.info("CRON GC Run: Purged $deletedWallets wallets and $deletedOps operations.")

        call.respond(HttpStatusCode.OK, "GC Executed")
    }
}