plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("outgo.android.library")
}

kotlin {

    androidTarget()
    jvm()
    iosArm64()
    iosSimulatorArm64()
    
    sourceSets {
        commonMain.dependencies {

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "fr.abknative.outgo.shared.feature.subscription"
}