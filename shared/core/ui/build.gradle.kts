plugins {
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    id("outgo.kmp.library")
}

kotlin {

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.components.resources)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "fr.abknative.outgo.shared.core.ui"
}

compose.resources {
    publicResClass = true
    packageOfResClass = "fr.abknative.outgo.shared.core.ui.resources"
    generateResClass = always
}

/**
 * Custom Gradle task to parse Android/Compose XML strings and generate
 * a native iOS `Localizable.strings` file.
 * This guarantees zero runtime overhead on iOS and complete independence
 * from fragile third-party resource plugins.
 */
tasks.register("generateIosStrings") {
    val inputFile = file("src/commonMain/composeResources/values/strings.xml")
    val outputFile = rootProject.file("iosApp/iosApp/Localizable.strings")

    inputs.file(inputFile)
    outputs.file(outputFile)

    doLast {
        if (!inputFile.exists()) {
            println("XML strings not found, skipping iOS generation.")
            return@doLast
        }

        val regex = """<string\s+name="([^"]+)">([^<]+)</string>""".toRegex()
        val text = inputFile.readText()
        val builder = StringBuilder()

        regex.findAll(text).forEach { matchResult ->
            val key = matchResult.groupValues[1]
            var value = matchResult.groupValues[2]

            // Format conversions for native iOS
            value = value.replace("%s", "%@") // Apple format specifier
                .replace("\"", "\\\"") // Escape double quotes
                .replace("\\'", "'") // Unescape Android single quotes

            builder.append("\"$key\" = \"$value\";\n")
        }

        outputFile.parentFile.mkdirs()
        outputFile.writeText(builder.toString())
        println("Successfully generated Localizable.strings for iOS!")
    }
}

/**
 * Binds the string generation task to the Kotlin/Native compilation phase.
 * Ensures the strings are always up-to-date before iOS builds.
 */
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinNativeCompile>().configureEach {
    if (name.contains("Ios")) {
        dependsOn("generateIosStrings")
    }
}