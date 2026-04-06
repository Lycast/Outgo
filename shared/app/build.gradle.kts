plugins {
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    kotlin("native.cocoapods")
    id("outgo.kmp.library")
    alias(libs.plugins.skie)
}

kotlin {

    cocoapods {
        version = "1.0.0"
        summary = "Shared App Module"
        homepage = "https://outgo.app"
        ios.deploymentTarget = "17.0"

        framework {
            baseName = "SharedApp"

            linkerOpts("-lsqlite3")
            freeCompilerArgs += "-Xbinary=bundleId=fr.abknative.outgo.shared"
            freeCompilerArgs += listOf("-Xoverride-konan-properties=min_os_version_ios_simulator_arm64=17.0")

            export(projects.shared.core.ui)
            export(projects.shared.core.api)
            export(projects.shared.presentation.dashboard.api)
            export(projects.shared.presentation.login.api)
            export(projects.shared.presentation.settings.api)
            export(projects.shared.feature.wallet.api)
            export(projects.shared.feature.auth.api)
        }

        pod("FirebaseCore") {
            version = libs.versions.firebase.ios.get()
            linkOnly = true
        }
        pod("FirebaseAuth") {
            version = libs.versions.firebase.ios.get()
            linkOnly = true
        }
    }
    
    sourceSets {
        commonMain.dependencies {
            // --- Couches Data & Core ---
            implementation(projects.shared.database)
            implementation(projects.shared.core.impl)
            implementation(projects.shared.feature.auth.impl)
            implementation(projects.shared.feature.subscription.impl)
            implementation(projects.shared.feature.wallet.impl)
            implementation(projects.shared.feature.sync.impl)

            // --- Couches Présentation (pour que Koin puisse les instancier) ---
            implementation(projects.shared.presentation.dashboard.impl)
            implementation(projects.shared.presentation.login.impl)
            implementation(projects.shared.presentation.settings.impl)

            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.compose.runtime)

            // --- APIs exposées aux applications natives (Android) ---
            api(projects.shared.core.ui)

            // Exposer les contrats UI
            api(projects.shared.presentation.dashboard.api)
            api(projects.shared.presentation.login.api)
            api(projects.shared.presentation.settings.api)

            // Exposer les contrats métier
            api(projects.shared.feature.wallet.api)
            api(projects.shared.feature.auth.api)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "fr.abknative.outgo.shared.app"
}