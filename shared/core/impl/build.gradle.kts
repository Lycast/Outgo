import com.codingfeline.buildkonfig.compiler.FieldSpec
import java.io.FileInputStream
import java.util.*

plugins {
    alias(libs.plugins.kotlin.serialization)
    id("outgo.kmp.library")
    alias(libs.plugins.buildkonfig)
}

val envProperties = Properties().apply {
    val envFile = rootProject.file(".env")
    if (envFile.exists()) {
        load(FileInputStream(envFile))
    }
}

buildkonfig {
    packageName = "fr.abknative.outgo.core.impl.secret"
    objectName = "OutgoConfig"

    defaultConfigs {
        val baseUrl = System.getenv("BASE_URL")
            ?: envProperties.getProperty("BASE_URL")
            ?: "https://api.outgo.app"
        buildConfigField(FieldSpec.Type.STRING, "BASE_URL", baseUrl)

        val webClientId = System.getenv("WEB_CLIENT_ID")
            ?: envProperties.getProperty("DEFAULT_WEB_CLIENT_ID")
            ?: ""
        buildConfigField(FieldSpec.Type.STRING, "WEB_CLIENT_ID", webClientId)
    }
}

kotlin {
    
    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.core.api)
            implementation(projects.shared.feature.auth.api)


            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.json)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.client.logging)

            implementation(libs.koin.core)

            implementation(libs.multiplatform.settings)

            implementation(libs.androidx.lifecycle.viewmodel)
        }

        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            implementation(libs.androidx.security.crypto)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "fr.abknative.outgo.shared.core.impl"
}