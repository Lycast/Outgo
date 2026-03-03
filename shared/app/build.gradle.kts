import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
}

kotlin {

    androidTarget { compilerOptions { jvmTarget.set(JvmTarget.JVM_11) } }
    iosArm64()
    iosSimulatorArm64()
    jvm()
    js(IR) { browser() }
    
    sourceSets {
        commonMain.dependencies {
            // put your Multiplatform dependencies here
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "fr.abknative.outgo.shared.app"
}