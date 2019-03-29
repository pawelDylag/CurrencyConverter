package com.paweldylag.currencyconverter.di

import com.paweldylag.currencyconverter.features.converter.view.CurrencyConverterActivity
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector

/**
 * Created by Pawel Dylag
 */
@Suppress("unused")
@Module(includes = [ViewModelModule::class])
interface ActivityModule : AndroidInjector<CurrencyConverterActivity> {

    @ContributesAndroidInjector
    fun contributeCurrencyConverterActivity(): CurrencyConverterActivity

}