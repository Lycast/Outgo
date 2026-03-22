plugins {
    id("outgo.jvm")
}

dependencies {
    implementation(project(":shared:core:api"))
    implementation(project(":shared:feature:outgoing:api"))
}