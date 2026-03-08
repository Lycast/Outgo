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
            implementation(projects.shared.core.api)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.androidx.lifecycle.viewmodel)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "fr.abknative.outgo.shared.feature.outgoing.api"
}