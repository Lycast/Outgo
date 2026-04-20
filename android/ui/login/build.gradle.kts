plugins {
    id("outgo.android.compose")
}

android {
    namespace = "fr.abknative.outgo.android.ui.login"
}

dependencies {
    implementation(projects.android.core)
    implementation(projects.shared.presentation.login.api)
    implementation(projects.shared.feature.auth.api)

    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.runtime)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel)

    implementation(libs.koin.compose)

    // Spécifique à ce module uniquement
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.auth)
    implementation(libs.googleid)
}