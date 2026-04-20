plugins {
    id("outgo.android.compose")
}

android {
    namespace = "fr.abknative.outgo.android.ui.onboarding"
}

dependencies {
    // 1. Dépendance UI Interne
    implementation(projects.android.core)

    // 2. Contrat KMP
    implementation(projects.shared.presentation.onboarding.api)

    // 3. Outils Compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.runtime)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel)
}