// Versions verified against official release notes / release pages on 2026-11-13:
// AGP 9.1.1, Kotlin 2.3.10, KSP 2.3.11, Hilt 2.60 (Hilt plugin >= 2.59 requires AGP 9 + Gradle 9.1+).
buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:9.1.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.3.10")
        classpath("org.jetbrains.kotlin:compose-compiler-gradle-plugin:2.3.10")
        classpath("com.google.devtools.ksp:symbol-processing-gradle-plugin:2.3.11")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.60")
    }
}
