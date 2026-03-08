plugins {
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.google.android.secrets)
    id("outgo.android.application")
    id("outgo.jvm")
}

android {
    namespace = "fr.abknative.outgo.android"

    buildFeatures {
        buildConfig = true
        compose = true
    }

    defaultConfig {
        applicationId = "fr.abknative.outgo.android"
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(projects.shared.app)
    implementation(projects.shared.core.api)
    implementation(projects.shared.feature.outgoing.api)
    implementation(projects.shared.database)

    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.runtime)
    implementation(libs.compose.foundation)
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.components.resources)

    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.tooling.preview)

    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.runtime)

    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.androidx.compose)

    implementation(libs.sqldelight.driver.android)

    // 5. TESTS
    testImplementation(libs.kotlin.test)
}