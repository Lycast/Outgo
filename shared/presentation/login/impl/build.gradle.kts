plugins {
    alias(libs.plugins.kotlin.serialization)
    id("outgo.kmp.library")
}

kotlin {
    
    sourceSets {
        commonMain.dependencies {
            // Dépendance vers son propre contrat (API)
            implementation(projects.shared.presentation.login.api)

            // Dépendances vers les couches inférieures
            implementation(projects.shared.core.api)
            implementation(projects.shared.feature.auth.api)
            implementation(projects.shared.feature.sync.api)

            // Outils de présentation et d'injection
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.koin.core)
            implementation(libs.koin.core.viewmodel)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.kotest.assertions)
        }
    }
}

android {
    namespace = "fr.abknative.outgo.shared.presentation.login.impl"
}