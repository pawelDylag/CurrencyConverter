/**
 * Created by Pawel Dylag
 */

object Config {

    const val kotlinVersion = "1.3.21"
    const val androidGradleVersion = "3.3.2"

    object App {
        const val appId = "com.paweldylag.currencyconverter"
        const val versionCode = 1
        const val versionName = "0.1"
        const val minSdkVersion = 19
        const val targetSdkVersion = 28
        const val compileSdkVersion = 28
        const val testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    object BuildTools {
        const val buildToolsVersion = "27.0.3"
        const val androidGradle = "com.android.tools.build:gradle:$androidGradleVersion"
        const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    }

    object Dependencies {
        // KOTLIN
        const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion"

        // ANDROID
        const val recyclerView = "androidx.recyclerview:recyclerview:1.0.0"
        const val appCompat = "androidx.appcompat:appcompat:1.0.2"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:1.1.3"

        // RX
        const val rxAndroid = "io.reactivex.rxjava2:rxandroid:2.0.1"
        const val rxJava = "io.reactivex.rxjava2:rxjava:2.2.1"

        // DAGGER
        const val dagger = "com.google.dagger:dagger:${Versions.daggerVersion}"
        const val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.daggerVersion}"
        const val daggerAndroid = "com.google.dagger:dagger-android:${Versions.daggerVersion}"
        const val daggerSupport = "com.google.dagger:dagger-android-support:${Versions.daggerVersion}"
        const val daggerAnnotationProcessor = "com.google.dagger:dagger-android-processor:${Versions.daggerVersion}"
        // LIFECYCLE
        const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:2.0.0"
        const val lifecycleJava8AnnotationProcessor = "androidx.lifecycle:lifecycle-common-java8:2.0.0"
        const val lifecycleExtensionsReactiveStreams = "androidx.lifecycle:lifecycle-reactivestreams-ktx:2.0.0"
    }

    object TestDependencies {
        const val jUnit = "junit:junit:4.12"
        const val androidTestRunner = "com.android.support.test:runner:1.0.2"
    }

    object Versions {
        const val daggerVersion = "2.21"
    }

}