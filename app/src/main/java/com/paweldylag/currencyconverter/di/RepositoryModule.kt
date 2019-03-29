package com.paweldylag.currencyconverter.di

import android.app.Application
import com.paweldylag.currencyconverter.repository.CurrencyLocalStorageService
import com.paweldylag.currencyconverter.repository.CurrencyNetworkService
import com.paweldylag.currencyconverter.repository.CurrencyRepository
import com.paweldylag.currencyconverter.repository.NetworkAndLocalStorageCurrencyRepository
import com.paweldylag.currencyconverter.repository.local.AppDatabase
import com.paweldylag.currencyconverter.repository.local.NaiveTimestampProvider
import com.paweldylag.currencyconverter.repository.local.RoomCurrencyLocalStorageService
import com.paweldylag.currencyconverter.repository.local.TimestampProvider
import com.paweldylag.currencyconverter.repository.network.RevolutCurrencyNetworkService
import com.paweldylag.currencyconverter.repository.network.RevolutCurrencyRestApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Pawel Dylag
 */
@Module
class RepositoryModule {

    @Provides
    fun provideCurrencyRepository(
        networkService: CurrencyNetworkService,
        localStorageService: CurrencyLocalStorageService
    ): CurrencyRepository =
        NetworkAndLocalStorageCurrencyRepository(networkService, localStorageService)

    @Provides
    fun providesCurrencyNetworkService(revolutApi: RevolutCurrencyRestApi): CurrencyNetworkService =
        RevolutCurrencyNetworkService(revolutApi)

    @Provides
    fun providesCurrencyLocalStorageService(
        appDatabase: AppDatabase,
        timestampProvider: TimestampProvider
    ): CurrencyLocalStorageService =
        RoomCurrencyLocalStorageService(appDatabase, timestampProvider)

    @Provides
    fun providesRevolutCurrencyRestApi() =
        RevolutCurrencyRestApi.create()

    @Singleton
    @Provides
    fun providesRoomDatabase(application: Application): AppDatabase =
        AppDatabase.create(application)

    @Provides
    fun providesTimestampProvider(): TimestampProvider =
        NaiveTimestampProvider()

}