plugins {
    alias(libs.plugins.kotlin.serialization)
    id("outgo.kmp.library")
}

kotlin {

    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.core.api)
            implementation(projects.shared.feature.wallet.api)

            implementation(libs.kotlinx.serialization.json)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "fr.abknative.outgo.shared.feature.wallet.network"
}