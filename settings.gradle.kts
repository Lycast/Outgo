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
include(":shared:feature:subscription:api")
include(":shared:feature:subscription:impl")
include(":shared:feature:sync:api")
include(":shared:feature:sync:impl")
include(":shared:feature:wallet:api")
include(":shared:feature:wallet:impl")
include(":shared:feature:wallet:network")


include(":shared:presentation:list:api")
include(":shared:presentation:list:impl")
include(":shared:presentation:month:api")
include(":shared:presentation:month:impl")
include(":shared:presentation:login:api")
include(":shared:presentation:login:impl")
include(":shared:presentation:onboarding:api")
include(":shared:presentation:onboarding:impl")
include(":shared:presentation:operation:api")
include(":shared:presentation:operation:impl")
include(":shared:presentation:settings:api")
include(":shared:presentation:settings:impl")
include(":shared:presentation:shell:api")
include(":shared:presentation:shell:impl")
include(":shared:presentation:year:api")
include(":shared:presentation:year:impl")

include(":androidApp")
include(":webApp")