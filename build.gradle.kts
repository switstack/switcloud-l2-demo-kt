// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "9.2.0" apply false
    id("org.jetbrains.kotlin.android") version "2.3.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.3.0" apply false

    id("org.jlleitschuh.gradle.ktlint") version "14.2.0" apply true
    id("io.gitlab.arturbosch.detekt") version "1.23.8" apply true
    id("org.jetbrains.dokka") version "1.9.10" apply false
}
