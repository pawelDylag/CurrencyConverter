package com.paweldylag.currencyconverter.repository

import com.paweldylag.currencyconverter.repository.models.CurrencyModel
import com.paweldylag.currencyconverter.repository.models.ExchangeRatesModel
import com.paweldylag.currencyconverter.repository.models.ModifiedCurrenciesModel
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.math.BigDecimal
import java.util.concurrent.TimeUnit

/**
 * Created by Pawel Dylag
 */
interface CurrencyRepository {

    fun getCurrencyExchangeRates(base: CurrencyModel) : Single<ExchangeRatesModel>

    fun observeCurrencyExchangeRates(base: CurrencyModel, period: Long, unit: TimeUnit) : Flowable<ExchangeRatesModel>

    fun observeCurrenciesListModifications() : Flowable<ModifiedCurrenciesModel>

    fun observeUserDefinedCurrencyAmount() : Flowable<BigDecimal>

    fun setUserDefinedCurrency(currency: CurrencyModel): Completable

    fun setUserDefinedCurrencyAmount(amount: BigDecimal): Completable

}