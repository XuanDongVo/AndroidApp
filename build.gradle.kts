// Top-level build file where you can add configuration options common to all sub-projects/modules.

// Khai báo buildscript để thêm các dependency Gradle
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.8.2") // Phải khớp với agp trong libs.versions.toml
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.0") // Cập nhật để khớp với kotlin trong libs.versions.toml
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false // Giữ lại khai báo này, sử dụng alias từ libs.versions.toml
    alias(libs.plugins.navigation.safeargs) apply false
}
