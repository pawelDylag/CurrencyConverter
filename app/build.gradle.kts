
plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    compileSdkVersion(Config.App.compileSdkVersion)
    defaultConfig {
        applicationId = Config.App.appId
        minSdkVersion(Config.App.minSdkVersion)
        targetSdkVersion(Config.App.targetSdkVersion)
        versionCode = Config.App.versionCode
        versionName = Config.App.versionName
        testInstrumentationRunner = Config.App.testInstrumentationRunner
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    packagingOptions.exclude("META-INF/main.kotlin_module")
}

dependencies {
    implementation(Config.Dependencies.kotlinStdLib)
    implementation(Config.Dependencies.constraintLayout)
    implementation(Config.Dependencies.appCompat)
    implementation(Config.Dependencies.recyclerView)
    implementation(Config.Dependencies.rxAndroid)
    implementation(Config.Dependencies.rxJava)
    implementation(Config.Dependencies.dagger)
    implementation(Config.Dependencies.daggerSupport)
    annotationProcessor(Config.Dependencies.daggerAnnotationProcessor)
}
