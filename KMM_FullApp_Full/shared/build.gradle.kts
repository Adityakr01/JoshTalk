plugins {
    kotlin("multiplatform")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.20"
}

kotlin {
    jvm() // simplified target for this exercise
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
            }
        }
        val jvmMain by getting
    }
}

kotlin.sourceSets["commonMain"].kotlin.srcDirs("src/commonMain/kotlin")
kotlin.sourceSets["jvmMain"].kotlin.srcDirs("src/androidMain/kotlin")
