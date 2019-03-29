package com.paweldylag.currencyconverter.repository

import com.paweldylag.currencyconverter.repository.models.CurrencyModel
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.math.BigDecimal

/**
 * Created by Pawel Dylag
 */
interface CurrencyLocalStorageService {

    fun observeCurrencyListModifications() : Flowable<Map<CurrencyModel, Long>>

    fun observeUserDefinedCurrencyAmount() : Flowable<BigDecimal>

    fun setUserDefinedCurrency(currency: CurrencyModel): Completable

    fun setUserDefinedCurrencyAmount(amount: BigDecimal): Completable

    fun updateCurrencyExchangeRates(exchangeRatesModel: Map<CurrencyModel, BigDecimal>): Completable

    fun getCurrencyExchangeRates(base: CurrencyModel): Single<Map<CurrencyModel, BigDecimal>>

}


