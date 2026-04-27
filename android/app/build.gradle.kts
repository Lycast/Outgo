plugins {
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.google.android.secrets)
    alias(libs.plugins.google.services)
    id("outgo.android.application")
}

android {
    namespace = "fr.abknative.outgo.android.app"

    defaultConfig {
        applicationId = "fr.abknative.outgo.android"
        versionCode = 3
        versionName = "1.0.2"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    signingConfigs {
        create("release") {
            val keystoreFile = file("../../outgo-upload-key.jks")

            if (keystoreFile.exists()) {
                storeFile = keystoreFile
                storePassword = System.getenv("ANDROID_KEYSTORE_PASSWORD")
                keyAlias = System.getenv("ANDROID_KEY_ALIAS")
                keyPassword = System.getenv("ANDROID_KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(projects.shared.app)
    implementation(projects.shared.database)

    implementation(projects.android.core)
    implementation(projects.android.ui.list)
    implementation(projects.android.ui.login)
    implementation(projects.android.ui.month)
    implementation(projects.android.ui.onboarding)
    implementation(projects.android.ui.operation)
    implementation(projects.android.ui.settings)
    implementation(projects.android.ui.year)

    implementation(libs.androidx.activity.compose)

    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    implementation(libs.sqldelight.driver.android)
}