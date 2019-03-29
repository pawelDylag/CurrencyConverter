
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
    implementation(Config.Dependencies.lifecycleExtensions)
    implementation(Config.Dependencies.lifecycleExtensionsReactiveStreams)
    implementation(Config.Dependencies.rxAndroid)
    implementation(Config.Dependencies.rxJava)
    implementation(Config.Dependencies.dagger)
    implementation(Config.Dependencies.daggerAndroid)
    implementation(Config.Dependencies.daggerSupport)
    implementation(Config.Dependencies.skald)
    implementation(Config.Dependencies.skaldLogcat)
    implementation(Config.Dependencies.roomRuntime)
    implementation(Config.Dependencies.roomKtx)
    implementation(Config.Dependencies.roomRx)
    implementation(Config.Dependencies.retrofit)
    implementation(Config.Dependencies.retrofitRx)
    implementation(Config.Dependencies.retrofitGson)
    implementation(Config.Dependencies.picasso)
    implementation(Config.Dependencies.picassoTransformations)
    kapt(Config.Dependencies.daggerAnnotationProcessor)
    kapt(Config.Dependencies.daggerCompiler)
    kapt(Config.Dependencies.lifecycleJava8AnnotationProcessor)
    kapt(Config.Dependencies.roomCompiler)
}
