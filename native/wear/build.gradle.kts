val ktlint: Configuration by configurations.creating

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 33

    defaultConfig {
        applicationId = "com.tkhskt.theremin"
        minSdk = 30
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro")
        }
    }
    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }
}

apply(from = "$rootDir/gradle/ktlint.gradle")

dependencies {
    implementation(project(":redux"))

    implementation(libs.play.services.wearable)

    // AndroidX
    implementation(libs.core)
    implementation(libs.legacy.support)
    implementation(libs.wear)
    implementation(libs.appcompat)
    implementation(libs.activity)
    implementation(libs.work)

    // hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Jetpack Compose
    implementation(libs.activity.compose)
    implementation(libs.bundles.wear.compose)

    // Coroutines
    implementation(libs.bundles.coroutines)

    implementation(libs.timber)
    ktlint(libs.ktlint)
    implementation(libs.bundles.lifecycle)

    wearApp(project(":wear"))
}
