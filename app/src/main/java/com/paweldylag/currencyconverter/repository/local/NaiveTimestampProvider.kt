package com.paweldylag.currencyconverter.repository.local

/**
 * Created by Pawel Dylag
 * This is a very naive implementation that doesn't care about timezones and precision.
 * For demo purposes only.
 */
class NaiveTimestampProvider : TimestampProvider {

    override fun getTimestamp() = System.currentTimeMillis()
}