plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("outgo.android.library")
    id("outgo.jvm")
    alias(libs.plugins.skie)
}

kotlin {

    androidTarget()
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "SharedApp"
            isStatic = true

            export(projects.shared.feature.outgoing.api)
        }
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.core.impl)
            implementation(projects.shared.database)
            implementation(projects.shared.feature.outgoing.impl)

            implementation(libs.koin.core)

            implementation(libs.kotlinx.coroutines.core)

            api(projects.shared.feature.outgoing.api)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "fr.abknative.outgo.shared.app"
}