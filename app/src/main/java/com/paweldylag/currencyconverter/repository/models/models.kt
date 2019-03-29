package com.paweldylag.currencyconverter.repository.models

import java.math.BigDecimal
import java.util.*

/**
 * Created by Pawel Dylag
 */
data class CurrencyModel(
    val currency: Currency
)

data class ExchangeRatesModel(
    val baseCurrency: CurrencyModel,
    val rates: Map<CurrencyModel, BigDecimal>
)

data class ModifiedCurrenciesModel(
    val currenciesByTimestamp: Map<CurrencyModel, Long>
)
