plugins {
    id("outgo.android.compose")
}

android {
    namespace = "fr.abknative.outgo.android.ui.operation"
}

dependencies {
    // 1. Dépendance UI Interne
    implementation(projects.android.core)

    // 2. Contrat KMP
    implementation(projects.shared.presentation.operation.api)
    implementation(projects.shared.feature.wallet.api)

    // 3. Outils Compose
    implementation(libs.compose.runtime)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel)
}