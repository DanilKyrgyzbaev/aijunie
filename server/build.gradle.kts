plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    kotlin("plugin.serialization") version "1.9.21"
    application
}

group = "com.example.aijunie"
version = "1.0.0"
application {
    mainClass.set("com.example.aijunie.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}
sourceSets {
    main {
        resources {
            srcDir("src/main/resources")
        }
    }
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}


dependencies {
//    implementation(projects.shared)

    // Logging
    implementation(libs.logback)

    // Ktor Server
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.default.headers)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.compression)
    implementation(libs.ktor.server.host.common)
    implementation(libs.ktor.server.status.pages)

    // Kotlin Coroutines and Flow
    implementation(libs.kotlinx.coroutines.core)

    // Koin Dependency Injection
    implementation(libs.koin.core)
    implementation(libs.koin.ktor)
    implementation(libs.koin.logger.slf4j)

    // Database
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation("org.jetbrains.exposed:exposed-dao:${libs.versions.exposedCore.get()}")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:${libs.versions.exposedCore.get()}")
    implementation(libs.h2)
    implementation(libs.postgresql)

    // Kotlin DateTime
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")

    // Kotlin Crypto for password hashing
    implementation("com.soywiz.korlibs.krypto:krypto:3.4.0")

    // Testing
    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)
}
