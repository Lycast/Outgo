plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    id("outgo.android.library")
    id("outgo.jvm")
}

kotlin {

    androidTarget()
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "CoreUI"
            isStatic = true
            export(libs.compose.components.resources)
        }
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.components.resources)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "fr.abknative.outgo.shared.core.ui"
}

compose.resources {
    publicResClass = true
    packageOfResClass = "fr.abknative.outgo.shared.core.ui.resources"
    generateResClass = always
}