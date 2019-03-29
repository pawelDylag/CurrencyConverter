package com.paweldylag.currencyconverter.repository.local

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by Pawel Dylag
 */
@Dao
interface CurrencyExchangeRateDao {

    @Query("SELECT * FROM currency_exchange_rates")
    fun getAll(): Single<List<CurrencyExchangeRateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(entities: List<CurrencyExchangeRateEntity>): Completable

}


@Dao
interface CurrencyConverterDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: CurrencyConverterDataEntity): Completable

    @Query("UPDATE currency_converter_data SET user_currency_code = :userBaseCurrency")
    fun updateUserCurrency(userBaseCurrency: String): Completable

    @Query("UPDATE currency_converter_data SET user_currency_amount = :userAmount")
    fun updateUserAmount(userAmount: String): Completable

    @Query("SELECT user_currency_amount FROM currency_converter_data")
    fun observeUserAmount(): Flowable<UserAmount>

}

@Dao
interface CurrencyOrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(entity: CurrencyOrderEntity): Completable

    @Query("SELECT * FROM currency_order ORDER BY modified_at DESC")
    fun observeCurrenciesSortedDescByModificationDate(): Flowable<List<CurrencyOrderEntity>>

}