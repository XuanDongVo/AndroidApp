plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.navigation.safeargs)
}

//buildscript {
//    repositories {
//        google()
//        mavenCentral()
//    }
//    dependencies {
//        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.8.9")
//    }
//}

android {
    namespace = "com.example.myapplication"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    val composeBom = platform("androidx.compose:compose-bom:2024.10.00") // Sử dụng phiên bản hợp lệ
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Material Design 3 (không chỉ định phiên bản, để BOM quản lý)
    implementation("androidx.compose.material3:material3")

    // Foundation & UI components
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.ui:ui")

    // Hỗ trợ Preview trong Android Studio
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Optional - Icon Material
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")

    // Optional - Adaptive Layout cho Material 3
    implementation("androidx.compose.material3.adaptive:adaptive")

    // Optional - Tích hợp với Activity
    implementation("androidx.activity:activity-compose:1.8.2") // Phiên bản mới nhất tương thích

    // Optional - Tích hợp với ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.5")

    // Optional - LiveData hỗ trợ Jetpack Compose
    implementation("androidx.compose.runtime:runtime-livedata")

    // Optional - RxJava hỗ trợ Jetpack Compose
    implementation("androidx.compose.runtime:runtime-rxjava2")

    // Navigation for Jetpack Compose
    implementation("androidx.navigation:navigation-compose:2.8.9")
    androidTestImplementation("androidx.navigation:navigation-testing:2.8.9")

    //database
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
}