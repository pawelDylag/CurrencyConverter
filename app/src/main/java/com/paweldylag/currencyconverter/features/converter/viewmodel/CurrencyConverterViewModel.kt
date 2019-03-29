package com.paweldylag.currencyconverter.features.converter.viewmodel

import android.widget.ImageView
import androidx.lifecycle.ViewModel
import com.paweldylag.currencyconverter.R
import com.paweldylag.currencyconverter.features.converter.model.CurrencyViewItemModel
import com.paweldylag.currencyconverter.repository.CurrencyRepository
import com.paweldylag.currencyconverter.repository.models.CurrencyModel
import com.paweldylag.currencyconverter.repository.models.ExchangeRatesModel
import com.paweldylag.currencyconverter.repository.models.ModifiedCurrenciesModel
import com.squareup.picasso.Picasso
import com.wafel.skald.api.createLogger
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import jp.wasabeef.picasso.transformations.CropTransformation
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Pawel Dylag
 *
 */

class CurrencyConverterViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    private val EXCHANGE_RATES_POLLING_PERIOD = 1L
    private val EXCHANGE_RATES_POLLING_PERIOD_UNIT = TimeUnit.SECONDS
    private val EXCHANGE_RATES_BASE_CURRENCY = CurrencyModel(Currency.getInstance("EUR"))

    private val logger = createLogger(this::class.java)
    private val mathContext = MathContext(4, RoundingMode.HALF_EVEN)
    private var repositoryItemUpdateDisposable: Disposable? = null
    private var repositoryItemAmountUpdateDisposable: Disposable? = null

    sealed class Event {
        data class ItemsUpdate(val items: List<CurrencyViewItemModel>) : Event()
    }

    private data class CurrencyRepositoryState(
        val exchangeRates: ExchangeRatesModel,
        val modifiedCurrenciesModel: ModifiedCurrenciesModel,
        val userBaseCurrency: CurrencyModel,
        val userBaseCurrencyExchangeRate: BigDecimal,
        val userBaseAmount: BigDecimal
    )

    private data class CurrencyItemModel(
        val currencyModel: CurrencyModel,
        val amount: BigDecimal,
        val lastModifiedAt: Long
    )

    /**
     * Observes for repository events, processes them, and emits an [Event] updates
     */
    fun observeEvents(): Flowable<Event> =
        observeCurrencyRepositoryChanges()
            .map { processRepositoryChange(it) }
            .wrapInEvent()

    /**
     * To be invoked on item selection
     */
    fun onItemSelected(item: CurrencyViewItemModel) {
        repositoryItemUpdateDisposable?.dispose()
        repositoryItemUpdateDisposable =
            currencyRepository.setUserDefinedCurrency(item.currencyModel)
                .andThen(currencyRepository.setUserDefinedCurrencyAmount(BigDecimal(item.currencyAmount)))
                .subscribeOn(Schedulers.io())
                .subscribe({}, { logger.error("Unable to update selected item in repo: ${it.message}") })
    }

    /**
     * To be invoked whenever base amount has changed
     */
    fun onAmountChanged(newValue: BigDecimal) {
        repositoryItemAmountUpdateDisposable?.dispose()
        repositoryItemAmountUpdateDisposable =
            currencyRepository.setUserDefinedCurrencyAmount(newValue)
                .subscribeOn(Schedulers.io())
                .subscribe({}, { logger.error("Unable to update user amount in repo: ${it.message}") })
    }

    /**
     * Asynchronously loads image for an item into given [ImageView].
     */
    fun loadItemImage(item: CurrencyViewItemModel, imageView: ImageView) {
        Picasso.get()
            .load("https://www.countryflags.io/${getCountryCode(item.currencyModel)}/flat/64.png")
            .placeholder(R.drawable.image_placeholder)
            .transform(CropTransformation(
                0.5f,
                0.5f,
                CropTransformation.GravityHorizontal.CENTER,
                CropTransformation.GravityVertical.CENTER))
            .transform(CropCircleTransformation())
            .into(imageView)
    }

    override fun onCleared() {
        super.onCleared()
        repositoryItemUpdateDisposable?.dispose()
        repositoryItemAmountUpdateDisposable?.dispose()
        logger.debug("Clearing view model")
    }

    private fun processRepositoryChange(repositoryState: CurrencyRepositoryState) =
        with(repositoryState) {
            sortCurrenciesByLastModificationTimestamp()
                .asSequence()
                .divideAllCurrenciesRatesBySelectedCurrencyRate(userBaseCurrencyExchangeRate)
                .multiplyAllCurrenciesRatesBySelectedAmount(userBaseAmount)
                .mapToCurrencyConverterViewItems()
                .toList()
                .logDebug()
        }

    private fun Sequence<CurrencyItemModel>.multiplyAllCurrenciesRatesBySelectedAmount(baseAmount: BigDecimal) =
        map { it.copy(amount = it.amount.multiply(baseAmount, mathContext)) }

    private fun Sequence<CurrencyItemModel>.divideAllCurrenciesRatesBySelectedCurrencyRate(baseRate: BigDecimal) =
        map { it.copy(amount = it.amount.divide(baseRate, mathContext)) }

    private fun Sequence<CurrencyItemModel>.mapToCurrencyConverterViewItems() =
        map { it.toCurrencyViewItemModel() }

    private fun Flowable<List<CurrencyViewItemModel>>.wrapInEvent(): Flowable<Event> =
        map { Event.ItemsUpdate(it) }

    private fun observeCurrencyRepositoryChanges() =
        Flowable.combineLatest<ExchangeRatesModel, ModifiedCurrenciesModel, BigDecimal, CurrencyRepositoryState>(
            currencyRepository.observeCurrencyExchangeRates(
                EXCHANGE_RATES_BASE_CURRENCY,
                EXCHANGE_RATES_POLLING_PERIOD,
                EXCHANGE_RATES_POLLING_PERIOD_UNIT),
            currencyRepository.observeCurrenciesListModifications(),
            currencyRepository.observeUserDefinedCurrencyAmount(),
            Function3 { exchangeRates, userModifiedCurrencies, userAmount ->
                val userDefinedBaseCurrency =
                    userModifiedCurrencies.getMostRecentlyModifiedOrDefault(EXCHANGE_RATES_BASE_CURRENCY)
                CurrencyRepositoryState(
                    exchangeRates,
                    userModifiedCurrencies,
                    userDefinedBaseCurrency,
                    exchangeRates.rates[userDefinedBaseCurrency] ?: BigDecimal.ONE,
                    userAmount
                )
            }
        ).doOnNext{
            logger.debug("New repo state: ${it.userBaseCurrency}, ${it.userBaseAmount}, ${it.userBaseCurrencyExchangeRate}")
        }

    private fun CurrencyRepositoryState.sortCurrenciesByLastModificationTimestamp() =
        this.exchangeRates.rates.map { (currency, exchangeRate) ->
            CurrencyItemModel(
                currency,
                exchangeRate,
                this.modifiedCurrenciesModel.currenciesByTimestamp[currency] ?: 0
            )
        }
            .sortedByDescending { it.lastModifiedAt }

    private fun ModifiedCurrenciesModel.getMostRecentlyModifiedOrDefault(default: CurrencyModel) =
        currenciesByTimestamp.maxBy { it.value }?.key ?: default

    private fun CurrencyItemModel.toCurrencyViewItemModel() =
        CurrencyViewItemModel(
            currencyModel,
            amount.toString()
        )

    private fun List<CurrencyViewItemModel>.logDebug() =
        also {
            logger.debug(it.joinToString{"${it.currencyModel.currency.currencyCode}:${it.currencyAmount}"})
        }


    /**
     * Demo purposes:
     * Ugly hack to get proper flags for currencies.
     * Typically we would use dedicated backend for that :)
     */
    private fun getCountryCode(currencyModel: CurrencyModel) =
        if (currencyModel.currency.currencyCode == "EUR")
            "EU"
        else Locale.getAvailableLocales().find {
            try {
                Currency.getInstance(it) == currencyModel.currency
            } catch (e: Exception) {
                return@find false
            }
        }?.country ?: ""

}