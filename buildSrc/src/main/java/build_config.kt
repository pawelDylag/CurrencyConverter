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
        const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion"
        const val supportAppCompat = "com.android.support:appcompat-v7:28.0.0"
        const val constraintLayout = "com.android.support.constraint:constraint-layout:1.1.3"
        const val rxAndroid = "io.reactivex.rxjava2:rxandroid:2.0.1"
        const val rxJava = "io.reactivex.rxjava2:rxjava:2.2.1"
        const val dagger = "com.google.dagger:dagger-android:2.15"
        const val daggerSupport = "com.google.dagger:dagger-android-support:2.15"
        const val daggerAnnotationProcessor = "com.google.dagger:dagger-android-processor:2.15"
    }

    object TestDependencies {
        const val jUnit = "junit:junit:4.12"
        const val androidTestRunner = "com.android.support.test:runner:1.0.2"
    }

}