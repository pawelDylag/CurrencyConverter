package com.paweldylag.currencyconverter

import android.app.Activity
import android.app.Application
import com.paweldylag.currencyconverter.di.DaggerAppComponent
import com.wafel.skald.api.LogLevel
import com.wafel.skald.api.skald
import com.wafel.skald.plugins.logcat.toLogcat
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

/**
 * Created by Pawel Dylag
 */
class App : Application(), HasActivityInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)

        skald {
            writeSaga {
                toLogcat {
                    withLevel { LogLevel.TRACE }
                    withPath { "com.paweldylag" }
                    withPattern { "${it.simplePath}[${it.threadName}] -> ${it.message}" }
                }
            }
        }
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

}