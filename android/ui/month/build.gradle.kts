plugins {
    id("outgo.android.compose")
}

android {
    namespace = "fr.abknative.outgo.android.ui.month"
}

dependencies {
    implementation(projects.android.core)

    implementation(projects.shared.presentation.month.api)
    implementation(projects.shared.feature.wallet.api)

    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel)
}