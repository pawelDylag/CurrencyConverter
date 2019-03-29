package com.paweldylag.currencyconverter.repository

import com.paweldylag.currencyconverter.repository.models.CurrencyModel
import io.reactivex.Single
import java.math.BigDecimal

/**
 * Created by Pawel Dylag
 */
interface CurrencyNetworkService {

    fun getCurrencyExchangeRates(baseCurrency: CurrencyModel): Single<Map<CurrencyModel, BigDecimal>>

}
