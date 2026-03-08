plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("jvmConvention") {
            id = "outgo.jvm"
            implementationClass = "fr.abknative.outgo.convention.OutgoJvmConventionPlugin"
        }
        register("androidLibraryConvention") {
            id = "outgo.android.library"
            implementationClass = "fr.abknative.outgo.convention.OutgoAndroidLibraryConventionPlugin"
        }
        register("androidApplicationConvention") {
            id = "outgo.android.application"
            implementationClass = "fr.abknative.outgo.convention.OutgoAndroidApplicationConventionPlugin"
        }
    }
}