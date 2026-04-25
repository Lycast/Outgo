plugins {
    id("outgo.android.compose")
}

android {
    namespace = "fr.abknative.outgo.android.ui.list"
}

dependencies {

    implementation(projects.android.core)

    implementation(projects.shared.presentation.list.api)
    implementation(projects.shared.feature.wallet.api)

    implementation(libs.compose.runtime)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel)
}