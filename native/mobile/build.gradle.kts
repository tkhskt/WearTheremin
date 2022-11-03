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

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    buildFeatures {
        prefab = true
    }
}

apply(from = "$rootDir/gradle/ktlint.gradle")

dependencies {
    implementation(project(":feature-theremin"))
    implementation(project(":feature-tutorial"))
    implementation(project(":feature-license"))
    implementation(project(":core-ui"))
    implementation(project(":redux"))

    implementation(libs.play.services.wearable)

    // AndroidX
    implementation(libs.core)
    implementation(libs.appcompat)
    implementation(libs.activity)
    implementation(libs.jankstats)

    // hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Jetpack Compose
    implementation(libs.activity.compose)
    implementation(libs.bundles.compose)
    debugImplementation(libs.compose.tooling)

    implementation(libs.timber)
    ktlint(libs.ktlint)

    testImplementation(libs.test.junit)
    androidTestImplementation(libs.test.junit.ext)
    androidTestImplementation(libs.test.espresso)
}
