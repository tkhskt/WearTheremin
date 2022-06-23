import app.cash.licensee.LicenseeExtension

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:${libs.versions.hilt.get()}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
        classpath("app.cash.licensee:licensee-gradle-plugin:${libs.versions.licensee.get()}")
    }
}

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
}

subprojects {
    afterEvaluate {
        plugins.withId("com.android.application") {
            apply(plugin = libs.plugins.licensee.get().pluginId)
            extensions.findByType(LicenseeExtension::class.java)?.apply {
                allow("Apache-2.0")
                allow("MIT")
                allow("BSD-3-Clause")
                allowUrl("https://developer.android.com/studio/terms.html")
                allowUrl("https://github.com/google/oboe/blob/master/LICENSE")
                ignoreDependencies("com.tkhskt.theremin")
            }
        }
    }
}

tasks.register("delete", Delete::class) {
    delete(rootProject.buildDir)
}
