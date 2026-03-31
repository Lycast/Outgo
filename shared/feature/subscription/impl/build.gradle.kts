plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("outgo.android.library")
}

kotlin {

    androidTarget()
    iosArm64()
    iosSimulatorArm64()
    
    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.core.api)
            implementation(projects.shared.feature.subscription.api)

            // Coroutines & Koin
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "fr.abknative.outgo.shared.feature.subscription.impl"
}