plugins {
    alias(libs.plugins.sqldelight)
    id("outgo.kmp.library")
}

kotlin {

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutine)

            implementation(libs.koin.core)
        }

        androidMain.dependencies {
            implementation(libs.sqldelight.driver.android)
        }

        iosMain.dependencies {
            implementation(libs.sqldelight.driver.native)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "fr.abknative.outgo.shared.database"
}

sqldelight {
    databases {
        create("OutgoDatabase") {
            dialect(libs.sqldelight.dialect)
            packageName.set("fr.abknative.outgo.database")
        }
    }
}