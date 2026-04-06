package fr.abknative.outgo.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Convention plugin for Kotlin Multiplatform library modules.
 * Centralizes target configurations (Android, iOS) to maintain a DRY build architecture.
 * Apply "outgo.kmp.library" in your feature and core shared modules.
 */
class OutgoKmpLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("outgo.android.library")
            }

            extensions.configure<KotlinMultiplatformExtension> {
                androidTarget()

                iosArm64()
                iosSimulatorArm64()

                // Add web/desktop targets here later
            }
        }
    }
}