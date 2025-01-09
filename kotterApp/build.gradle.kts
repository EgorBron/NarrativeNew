plugins {
    application
    kotlin("jvm")
    kotlin("plugin.serialization")
}

group = "net.blusutils"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.varabyte.kotter:kotter-jvm:1.1.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation(project(":"))
    testImplementation("com.varabyte.kotterx:kotter-test-support-jvm:1.1.2")
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