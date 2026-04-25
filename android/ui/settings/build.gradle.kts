plugins {
    id("outgo.android.compose")
}

android {
    namespace = "fr.abknative.outgo.android.ui.settings"
}

dependencies {

    implementation(projects.android.core)

    implementation(projects.shared.presentation.settings.api)
    implementation(projects.shared.feature.auth.api)

    implementation(libs.compose.runtime)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel)
}