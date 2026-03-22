plugins {
    id("outgo.jvm")
}

dependencies {
    implementation(project(":server:core"))

    // SQL & Persistence
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.hikaricp)
    implementation(libs.postgresql)
}