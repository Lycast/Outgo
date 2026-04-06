plugins {
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    id("outgo.kmp.library")
}

kotlin {

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