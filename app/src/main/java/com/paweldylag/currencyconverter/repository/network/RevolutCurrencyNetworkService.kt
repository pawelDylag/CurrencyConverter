package com.paweldylag.currencyconverter.repository.network

import com.paweldylag.currencyconverter.repository.CurrencyNetworkService
import com.paweldylag.currencyconverter.repository.models.CurrencyModel
import io.reactivex.Single
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject

/**
 * Created by Pawel Dylag
 */
class RevolutCurrencyNetworkService @Inject constructor(
    private val api: RevolutCurrencyRestApi): CurrencyNetworkService {

    override fun getCurrencyExchangeRates(baseCurrency: CurrencyModel): Single<Map<CurrencyModel, BigDecimal>> =
        api.getCurrencyRates(baseCurrency.currency.currencyCode).map {
            it.toCurrencyRatesModel()
        }

    fun CurrencyRatesResponse.toCurrencyRatesModel(): Map<CurrencyModel, BigDecimal> {
        val baseCurrency = Currency.getInstance(this.base) ?: return emptyMap()
        return this
            .rates
            .mapNotNull {
                val currency: Currency = Currency.getInstance(it.key) ?: return@mapNotNull null
                val currencyModel = CurrencyModel(currency)
                try {
                    return@mapNotNull currencyModel to BigDecimal(it.value)
                } catch (e: NumberFormatException){
                    return@mapNotNull null
                }
            }
            .plus(CurrencyModel(baseCurrency) to BigDecimal.ONE)
            .toMap()
    }

}
