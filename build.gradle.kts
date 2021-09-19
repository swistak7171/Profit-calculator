plugins {
    id(Plugins.DOKKA) version (Versions.DOKKA)
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(Dependencies.ANDROID_GRADLE_BUILD_TOOLS)
        classpath(Dependencies.Kotlin.KOTLIN_GRADLE_PLUGIN)
        classpath(Dependencies.AndroidX.Navigation.NAVIGATION_SAFE_ARGS_GRADLE_PLUGIN)
        classpath(Dependencies.KTLINT)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(Repositories.JITPACK)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}