val ktlint: Configuration by configurations.creating

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "com.tkhskt.theremin"
        minSdk = 30
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        externalNativeBuild.cmake {
            arguments += "-DANDROID_STL=c++_shared"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
        }
    }

    buildFeatures {
        prefab = true
    }
}

apply(from = "$rootDir/gradle/ktlint.gradle")

dependencies {
    implementation(project(":redux"))

    implementation(libs.play.services.wearable)
    implementation(libs.android.material)

    // AndroidX
    implementation(libs.core)
    implementation(libs.appcompat)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.activity)
    implementation(libs.jankstats)

    // hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Jetpack Compose
    implementation(libs.activity.compose)
    implementation(libs.bundles.compose)
    debugImplementation(libs.compose.tooling)

    // Coroutines
    implementation(libs.bundles.coroutines)

    // MediaPipe
    implementation(libs.bundles.mediapipe)

    implementation(libs.timber)
    implementation(libs.oboe)
    ktlint(libs.ktlint)

    // theremin-core(local maven)
    implementation(libs.theremin.core)

    testImplementation(libs.test.junit)
    androidTestImplementation(libs.test.junit.ext)
    androidTestImplementation(libs.test.espresso)

    wearApp(project(":wear"))
}