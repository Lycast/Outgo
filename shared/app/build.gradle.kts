import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
}

kotlin {

    androidTarget { compilerOptions { jvmTarget.set(JvmTarget.JVM_11) } }
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