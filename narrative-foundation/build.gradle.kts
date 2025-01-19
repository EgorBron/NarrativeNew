import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.android)
    `maven-publish`
    signing
    alias(libs.plugins.dokka)
}

val rootgroup: String by project
val libversion: String by project

group = rootgroup
version = libversion

repositories {
    google()
    mavenCentral()
}

android {
    namespace = rootgroup
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

kotlin {
    jvm()
    js {
        browser()
        nodejs()
        binaries.executable()
    }
    iosArm64()
    iosX64()
    macosX64()
    macosArm64()
    linuxX64()
    mingwX64()

    androidTarget {
        publishLibraryVariants("release")
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.serialization)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

signing {
    // TODO https://mikewacker.github.io/gradle-publish.html#step-7-sign-files
}

publishing {
    repositories {
        mavenLocal()
//        maven {
//            name = "ossrh"
//            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
//            credentials {
//                val ossrhUsername: String? by project
//                val ossrhPassword: String? by project
//                require(ossrhUsername != null && ossrhPassword != null) {
//                    "Sonatype OSSRH credentials must be provided (`ossrhUsername` and `ossrhPassword` in `local.properties`)"
//                }
//                username = ossrhUsername
//                password = ossrhPassword
//            }
//        }
    }
    publications {
        create<MavenPublication>("mavenCentral") {
            pom {
                name = "Narrative (Foundation API)"
                description = "Kotlin APIs for base of Narrative format"
                url = "https://github.com/EgorBron/Narrative"
                developers {
                    developer {
                        id = "EgorBron"
                        name = "Yegor Bron"
                        email = "egorbron@blusutils.net"
                    }
                }
                licenses {
                    license {
                        name = "The Apache License, Version 2.0"
                        url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }
                scm {
                    connection = "scm:git:https://github.com/EgorBron/Narrative.git"
                    developerConnection = "scm:git:https://github.com/EgorBron/Narrative.git"
                    url = "https://github.com/EgorBron/Narrative"
                }
            }
        }
    }
}

tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets {
        named("commonMain") {
            moduleName.set("Narrative Foundation")
            moduleVersion.set(libversion)
            noAndroidSdkLink.set(true)

            includes.from("module-root-doc.md")

            sourceLink {
                localDirectory.set(file("src/commonMain/kotlin"))
                remoteUrl.set(uri("https://github.com/EgorBron/NarrativeNew/tree/master/" +
                        "narrative-foundation/src/commonMain/kotlin"
                ).toURL())
                remoteLineSuffix.set("#L")
            }
        }
    }
}