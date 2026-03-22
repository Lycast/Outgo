rootProject.name = "Outgo"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
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

include(":server:api")
include(":server:core")
include(":server:data")
include(":shared:app")
include(":shared:database")
include(":shared:core:api")
include(":shared:core:impl")
include(":shared:core:ui")
include(":shared:feature:auth:api")
include(":shared:feature:auth:impl")
include(":shared:feature:outgoing:api")
include(":shared:feature:outgoing:impl")

include(":androidApp")
include(":webApp")