import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android)
    `maven-publish`
}

val rootgroup: String by project
val libversion: String by project

group = rootgroup
version = libversion

android {
    namespace = rootgroup
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
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
        val commonMain by getting {
            dependencies {
                implementation(project(":narrative-foundation"))
//                implementation(project(":extension-signals"))
            }
        }
    }
}

publishing {
    repositories {
        mavenLocal()
    }
}