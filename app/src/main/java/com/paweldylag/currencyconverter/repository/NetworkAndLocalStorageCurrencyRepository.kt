package com.paweldylag.currencyconverter.repository

import com.paweldylag.currencyconverter.repository.models.CurrencyModel
import com.paweldylag.currencyconverter.repository.models.ExchangeRatesModel
import com.paweldylag.currencyconverter.repository.models.ModifiedCurrenciesModel
import com.wafel.skald.api.createLogger
import io.reactivex.Flowable
import io.reactivex.Single
import java.math.BigDecimal
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Pawel Dylag
 */

class NetworkAndLocalStorageCurrencyRepository @Inject constructor(
    private val networkService: CurrencyNetworkService,
    private val localStorageService: CurrencyLocalStorageService
) : CurrencyRepository {

    private val logger = createLogger(this::class.java)

    override fun getCurrencyExchangeRates(base: CurrencyModel): Single<ExchangeRatesModel> =
        networkService.getCurrencyExchangeRates(base)
            .doOnSuccess {
                logger.debug("Got new exchange rates from network.")
                localStorageService.updateCurrencyExchangeRates(it)
            }
            .onErrorResumeNext {
                logger.debug("Unable to get exchange rates from network: ${it.message}")
                localStorageService.getCurrencyExchangeRates(base)
            }
            .doOnError {
                logger.debug("Unable to get exchange rates from local storage: ${it.message}")
            }
            .map { ExchangeRatesModel(base, it) }

    override fun observeCurrenciesListModifications(): Flowable<ModifiedCurrenciesModel> =
        localStorageService.observeCurrencyListModifications().map {
            logger.debug("Got new currency list modifications update.")
            ModifiedCurrenciesModel(it)
        }

    override fun observeCurrencyExchangeRates(
        base: CurrencyModel,
        period: Long,
        unit: TimeUnit
    ): Flowable<ExchangeRatesModel> =
        Flowable.interval(period, unit).flatMap {
            getCurrencyExchangeRates(base).toFlowable()
        }


    override fun observeUserDefinedCurrencyAmount(): Flowable<BigDecimal> =
        localStorageService.observeUserDefinedCurrencyAmount()
            .doOnNext {
                logger.debug("Got new user currency amount: $it")
            }

    override fun setUserDefinedCurrency(currency: CurrencyModel) =
        localStorageService.setUserDefinedCurrency(currency)


    override fun setUserDefinedCurrencyAmount(amount: BigDecimal) =
        localStorageService.setUserDefinedCurrencyAmount(amount)

}