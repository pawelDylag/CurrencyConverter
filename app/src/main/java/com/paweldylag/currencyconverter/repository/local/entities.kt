package com.paweldylag.currencyconverter.repository.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Created by Pawel Dylag
 */
@Entity(tableName = "currency_exchange_rates")
data class CurrencyExchangeRateEntity(
    @PrimaryKey
    @ColumnInfo(name = "currency_id")
    var currencyId: Long,
    @ColumnInfo(name = "currency_code")
    var currencyCode: String,
    @ColumnInfo(name = "exchange_rate")
    var exchangeRate: String
)

@Entity(tableName = "currency_converter_data")
data class CurrencyConverterDataEntity(
    @PrimaryKey
    var id: Int,
    @ColumnInfo(name = "user_currency_code")
    var userCurrencyCode: String,
    @ColumnInfo(name = "user_currency_amount")
    var userCurrencyAmount: String
)

data class UserCurrency(
    @ColumnInfo(name = "user_currency_code")
    var userCurrencyCode: String)

data class UserAmount(
    @ColumnInfo(name = "user_currency_amount")
    var userCurrencyAmount: String)

@Entity(tableName = "currency_order")
data class CurrencyOrderEntity(
    @PrimaryKey
    @ColumnInfo(name = "currency_id")
    var currencyId: Long,
    @ColumnInfo(name = "modified_at")
    var modifiedAt: Long,
    @ColumnInfo(name = "currency_code")
    var currencyCode: String
)