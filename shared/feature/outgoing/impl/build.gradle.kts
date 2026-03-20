plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    id("outgo.android.library")
    id("outgo.jvm")
}

kotlin {

    androidTarget()
    iosArm64()
    iosSimulatorArm64()
    
    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.feature.outgoing.api)
            implementation(projects.shared.core.api)
            implementation(projects.shared.database)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.ktor.client.core)

            implementation(libs.koin.core)
            implementation(libs.koin.core.viewmodel)

            implementation(libs.androidx.lifecycle.viewmodel)

            implementation(libs.sqldelight.coroutine)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.kotest.assertions)
        }
    }
}

android {
    namespace = "fr.abknative.outgo.shared.feature.outgoing.impl"
}