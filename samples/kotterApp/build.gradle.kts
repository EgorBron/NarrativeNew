plugins {
    application
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlinx.serialization)
}

group = "net.blusutils"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(libs.kotter.jvm)
    implementation(libs.kotlinx.serialization)
    implementation(libs.narrative)
    testImplementation(libs.kotter.test.support.jvm)
    testImplementation(kotlin("test"))
}

application {
    mainClass = "net.blusutils.MainKt"
}
tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(20)
}
tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}