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
            implementation(libs.kotlinx.coroutines.core)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "fr.abknative.outgo.shared.core.api"
}