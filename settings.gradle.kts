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

include(":shared:feature:subscription")
include(":shared:feature:auth:api")
include(":shared:feature:auth:impl")
include(":shared:feature:sync:api")
include(":shared:feature:sync:impl")
include(":shared:feature:wallet:api")
include(":shared:feature:wallet:impl")
include(":shared:feature:wallet:network")

include(":shared:presentation:login:api")
include(":shared:presentation:login:impl")
include(":shared:presentation:dashboard:api")
include(":shared:presentation:dashboard:impl")
include(":shared:presentation:settings:api")
include(":shared:presentation:settings:impl")

include(":androidApp")
include(":webApp")