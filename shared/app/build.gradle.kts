plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    id("outgo.android.library")
    alias(libs.plugins.skie)
}

kotlin {

    androidTarget()
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { target ->
        target.binaries.framework {
            baseName = "SharedApp"

            linkerOpts("-lsqlite3")

            freeCompilerArgs += "-Xbinary=bundleId=fr.abknative.outgo.shared"

            freeCompilerArgs += listOf("-Xoverride-konan-properties=min_os_version_ios_simulator_arm64=17.0")

            export(projects.shared.core.ui)
            export(projects.shared.core.api)
            export(projects.shared.feature.outgoing.api)
            export(projects.shared.feature.auth.api)
        }
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.core.impl)
            implementation(projects.shared.database)
            implementation(projects.shared.feature.auth.impl)
            implementation(projects.shared.feature.outgoing.impl)

            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.compose.runtime)

            api(projects.shared.feature.outgoing.api)
            api(projects.shared.core.ui)
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