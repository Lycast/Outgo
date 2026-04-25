plugins {
    id("outgo.android.compose")
}

android {
    namespace = "fr.abknative.outgo.android.core"
}

dependencies {
    api(projects.shared.core.api)
    api(projects.shared.core.ui)

    api(libs.compose.runtime)
    api(libs.compose.foundation)
    api(libs.compose.ui)
    api(libs.compose.material3)
    api(libs.compose.components.resources)

    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.tooling.preview)
}