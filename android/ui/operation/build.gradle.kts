plugins {
    id("outgo.android.compose")
}

android {
    namespace = "fr.abknative.outgo.android.ui.operation"
}

dependencies {

    implementation(projects.android.core)

    implementation(projects.shared.presentation.operation.api)
    implementation(projects.shared.feature.wallet.api)

    implementation(libs.compose.runtime)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel)
}