package com.paweldylag.currencyconverter.features.converter.view

import android.text.Editable
import android.text.TextWatcher
import java.math.BigDecimal

/**
 * Created by Pawel Dylag
 */
class CurrencyAmountTextWatcher(private val onBaseAmountChanged: (BigDecimal) -> Unit ): TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        // do nothing
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // do nothing
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        s?.let {
            if (it.isNotEmpty() && it.isNotBlank()) {
                onBaseAmountChanged(BigDecimal(s.toString()))
            } else if (it.isBlank()){
                onBaseAmountChanged(BigDecimal.ZERO)
            }
        }
    }
}