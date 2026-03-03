rootProject.name = "Outgo"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

include(":server")

include(":shared:app")

include(":shared:database")
include(":shared:core:api")
include(":shared:core:impl")
include(":shared_feature:outgoing:api")
include(":shared_feature:outgoing:impl")

include(":androidApp")
include(":webApp")