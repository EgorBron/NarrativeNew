plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlinx.serialization)
}

group = "net.blusutils"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

kotlin {
    js {
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
        }
        binaries.executable()
    }
    sourceSets {
        jsMain {
            dependencies {
                implementation(libs.kotlinx.serialization)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.narrative)
                implementation(libs.narrative.js)
//                implementation(kotlin("stdlib-js"))
//                implementation(libs.kotlinx.serialization)
//                implementation(libs.narrative)
//                implementation(libs.kotlinx.browser)
                implementation(libs.jetbrains.kotlinx.html.js)
            }
        }
    }
}