import org.gradle.internal.deployment.RunApplication
import org.jetbrains.kotlin.gradle.targets.jvm.tasks.KotlinJvmRun

plugins {
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.serialization") version "2.0.20"
    application
}

group = "net.blusutils"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
//    // kotlinx-serialization-cbor
//    implementation("org.jetbrains.kotlinx:kotlinx-serialization-cbor:1.6.3")
//    // kotlinx-serialization-properties
//    implementation("org.jetbrains.kotlinx:kotlinx-serialization-properties:1.6.3")
//    // kotlinx-serialization-protobuf
//    implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:1.6.3")
    implementation(kotlin("reflect"))
    testImplementation(kotlin("test"))
}

application {
    mainClass = "net.blusutils.narrative.MainKt"
}

//tasks.register<JavaExec>("runAgnostic") {
//    classpath = sourceSets["main"].runtimeClasspath
//    mainClass.set("net.blusutils.narrative.agnostic.AgnosticKt")
//}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(20)
}