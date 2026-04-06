plugins {
    alias(libs.plugins.kotlin.serialization)
    id("outgo.kmp.library")
}

kotlin {

    
    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.feature.auth.api)
            implementation(projects.shared.core.api)
            implementation(projects.shared.feature.sync.api)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.androidx.lifecycle.viewmodel)

            implementation(libs.firebase.auth)

            implementation(libs.ktor.client.core)

            implementation(libs.koin.core)
            implementation(libs.koin.core.viewmodel)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "fr.abknative.outgo.shared.feature.auth.impl"
}