plugins {
    alias(libs.plugins.kotlin.serialization)
    id("outgo.kmp.library")
}

kotlin {
    
    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.core.api)
            implementation(projects.shared.feature.sync.api)
            implementation(projects.shared.feature.wallet.api)
            implementation(projects.shared.feature.wallet.network)

            // Coroutines & Koin
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)

            // client HTTP
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.json)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.kotest.assertions)
        }
    }
}

android {
    namespace = "fr.abknative.outgo.shared.feature.sync.impl"
}