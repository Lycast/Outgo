plugins {
    // Android natif
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.google.android.secrets) apply false

    // IOS
    alias(libs.plugins.skie) apply false

    // Multiplatform & UI
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false

    alias(libs.plugins.google.services) apply false

    // Backend & Logique
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.ktor) apply false
    alias(libs.plugins.sqldelight) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

/**
 * Ensures unique artifact names for all modules to prevent naming collisions
 * during the packaging phase (installDist/shadowJar).
 */
subprojects {
    pluginManager.withPlugin("org.gradle.base") {
        configure<BasePluginExtension> {
            archivesName.set("outgo" + project.path.replace(":", "-"))
        }
    }
}