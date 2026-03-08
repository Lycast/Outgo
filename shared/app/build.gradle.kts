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
            // Koin Core
            implementation(libs.koin.core)

            // Importation des réalisations (qui contiennent les Koin Modules locaux)
            implementation(projects.shared.core.impl)
            implementation(projects.shared.database)
            implementation(projects.shared.feature.outgoing.impl)

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "fr.abknative.outgo.shared.app"
}