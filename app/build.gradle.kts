plugins {
    id(Plugins.ANDROID_APPLICATION)
    id(Plugins.KOTLIN_ANDROID)
    id(Plugins.KOTLIN_KAPT)
    id(Plugins.DOKKA)
}

android {
    compileSdk = Configuration.COMPILE_SDK_VERSION
    buildToolsVersion = Configuration.BUILD_TOOLS_VERSION

    defaultConfig {
        applicationId = Configuration.APPLICATION_ID
        minSdk = Configuration.MIN_SDK_VERSION
        targetSdk = Configuration.TARGET_SDK_VERSION
        versionCode = Configuration.VERSION_CODE
        versionName = Configuration.VERSION_NAME
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        getByName("debug") {
            storeFile = rootProject.file("debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }

        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        freeCompilerArgs = listOf(
            "-Xallow-result-return-type",
            "-XXLanguage:+InlineClasses"
        )
    }

    testOptions {
        tasks.withType<Test>().configureEach {
            useJUnitPlatform()
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // implementation(project(Modules.COMMON))
    // implementation(project(Modules.DATA))
    // implementation(project(Modules.DATA_ACCESS))
    // implementation(project(Modules.DOMAIN))
    // implementation(project(Modules.DOMAIN_ACCESS))
    // implementation(project(Modules.NOTIFICATION))
    // implementation(project(Modules.MODEL_ENTITY))
    // implementation(project(Modules.MODEL_DOMAIN))
    // implementation(project(Modules.MODEL_MAPPER))
    // implementation(project(Modules.UI_BASE))
    // implementation(project(Modules.UI_SPLASHSCREEN))
    // implementation(project(Modules.UI_MAIN))

    // Material Components
    implementation(Dependencies.Google.Material.MATERIAL_COMPONENTS)

    // Dagger
    kapt(Dependencies.Google.Dagger.DAGGER_ANDROID_PROCESSOR)
    kapt(Dependencies.Google.Dagger.DAGGER_COMPILER)

    // LeakCanary
    debugImplementation(Dependencies.LEAK_CANARY_ANDROID)
}