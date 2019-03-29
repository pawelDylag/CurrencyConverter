package com.paweldylag.currencyconverter.repository.local

import com.paweldylag.currencyconverter.repository.CurrencyLocalStorageService
import com.paweldylag.currencyconverter.repository.models.CurrencyModel
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject

/**
 * Created by Pawel Dylag
 */
class RoomCurrencyLocalStorageService @Inject constructor(
    private val db: AppDatabase,
    private val timestampProvider: TimestampProvider
) : CurrencyLocalStorageService {

    override fun updateCurrencyExchangeRates(exchangeRatesModel: Map<CurrencyModel, BigDecimal>) =
        db.currencyExchangeRatesDao()
            .insertAll(
                exchangeRatesModel.map {
                    CurrencyExchangeRateEntity(
                        it.key.currency.currencyCode.hashCode().toLong(),
                        it.key.currency.currencyCode,
                        it.value.toString()
                    )
                })


    override fun getCurrencyExchangeRates(base: CurrencyModel): Single<Map<CurrencyModel, BigDecimal>> =
        db.currencyExchangeRatesDao().getAll().map { dbModel -> dbModel.transformToMap() }

    override fun observeCurrencyListModifications(): Flowable<Map<CurrencyModel, Long>> =
        db.currencyOrderDao().observeCurrenciesSortedDescByModificationDate().map { dbModel ->
            dbModel.associateBy({ CurrencyModel(Currency.getInstance(it.currencyCode)) }, { it.modifiedAt })
        }

    override fun observeUserDefinedCurrencyAmount(): Flowable<BigDecimal> =
        db.currencyConverterDataDao().observeUserAmount().map { BigDecimal(it.userCurrencyAmount) }

    override fun setUserDefinedCurrency(currency: CurrencyModel): Completable =
        with(currency.currency) {
            db.currencyConverterDataDao().updateUserCurrency(currencyCode).andThen(
                db.currencyOrderDao().update(
                    CurrencyOrderEntity(
                        currencyCode.hashCode().toLong(),
                        timestampProvider.getTimestamp(),
                        currencyCode
                    )
                )
            )
        }

    override fun setUserDefinedCurrencyAmount(amount: BigDecimal) =
        db.currencyConverterDataDao().updateUserAmount(amount.toString())

    private fun List<CurrencyExchangeRateEntity>.transformToMap() =
        associateBy(
            { CurrencyModel(Currency.getInstance(it.currencyCode)) },
            { BigDecimal(it.exchangeRate) })

}