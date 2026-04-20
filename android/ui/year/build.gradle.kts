plugins {
    id("outgo.android.compose")
}

android {
    namespace = "fr.abknative.outgo.android.ui.year"
}

dependencies {
    // 1. Dépendance UI Interne
    implementation(projects.android.core)

    // 2. Contrat KMP
    implementation(projects.shared.presentation.year.api)

    // 3. Outils Compose
    implementation(libs.compose.runtime)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel)
}