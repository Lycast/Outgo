plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("outgo.android.library")
    id("outgo.jvm")
}

kotlin {

    androidTarget()
    iosArm64()
    iosSimulatorArm64()
    
    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.core.impl)
            implementation(projects.shared.database)
            implementation(projects.shared.feature.outgoing.impl)

            implementation(libs.koin.core)

            implementation(libs.kotlinx.coroutines.core)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "fr.abknative.outgo.shared.app"
}