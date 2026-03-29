plugins {
    id("outgo.jvm")
}

dependencies {
    implementation(project(":server:core"))
    implementation(project(":shared:feature:wallet:network"))

    // SQL & Persistence
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.kotlin.datetime)
    implementation(libs.hikaricp)
    implementation(libs.flyway.core)
    implementation(libs.flyway.database.postgresql)
    implementation(libs.postgresql)
}