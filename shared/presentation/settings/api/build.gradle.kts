plugins { id("outgo.kmp.library") }

kotlin {
    
    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.core.api)
            implementation(projects.shared.feature.auth.api)
            implementation(projects.shared.feature.sync.api)


            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.androidx.lifecycle.viewmodel)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "fr.abknative.outgo.shared.presentation.settings.api"
}