plugins {
    alias(libs.plugins.ktor)
    id("outgo.jvm")
    application
}

application {
    mainClass.set("fr.abknative.outgo.server.api.ApplicationKt")
}

dependencies {
    implementation(project(":server:core"))
    implementation(project(":server:data"))
    implementation(project(":shared:core:api"))
    implementation(project(":shared:feature:auth:api"))
    implementation(project(":shared:feature:wallet:api"))
    implementation(project(":shared:feature:wallet:network"))

    implementation(libs.ktor.server.auth)

    // Sentry
    implementation(libs.sentry.otel)
    implementation(libs.ktor.otel)

    implementation(libs.logback)

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.server.request.validation)

    implementation(libs.firebase.admin)

    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.rate.limit)

    implementation(libs.koin.ktor)

    testImplementation(libs.ktor.server.test.host)
}

tasks.named<Sync>("installDist") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
