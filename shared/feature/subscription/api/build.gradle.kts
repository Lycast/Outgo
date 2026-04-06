plugins { id("outgo.kmp.library") }

kotlin {
    
    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.core.api)
            implementation(libs.kotlinx.coroutines.core)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "fr.abknative.outgo.shared.feature.subscription.api"
}