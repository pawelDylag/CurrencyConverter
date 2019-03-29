package com.paweldylag.currencyconverter.features.converter.model

import com.paweldylag.currencyconverter.repository.models.CurrencyModel

/**
 * Created by Pawel Dylag
 */
data class CurrencyViewItemModel(
    val currencyModel: CurrencyModel,
    val currencyAmount: String
)