plugins {
    id("outgo.android.compose")
}

android {
    namespace = "fr.abknative.outgo.android.ui.year"
}

dependencies {

    implementation(projects.android.core)

    implementation(projects.shared.presentation.year.api)

    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel)
}