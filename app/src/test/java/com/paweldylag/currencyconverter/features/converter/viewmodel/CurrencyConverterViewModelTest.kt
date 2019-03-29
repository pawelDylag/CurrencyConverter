package com.paweldylag.currencyconverter.features.converter.viewmodel

import com.paweldylag.currencyconverter.features.converter.model.*
import com.paweldylag.currencyconverter.repository.CurrencyRepository
import io.mockk.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Test
import java.math.BigDecimal
import java.util.*

/**
 * Created by Pawel Dylag
 */
class CurrencyConverterViewModelTest {


    private val EUR = CurrencyModel(Currency.getInstance("EUR"))
    private val PLN = CurrencyModel(Currency.getInstance("PLN"))
    private val USD = CurrencyModel(Currency.getInstance("USD"))
    private val NOK = CurrencyModel(Currency.getInstance("NOK"))

    @Test
    fun `changes base currency in repository when new item is selected`() {
        // given
        val repository = mockk<CurrencyRepository>()
        every { repository.setUserDefinedCurrency(EUR)} returns Completable.complete()
        every { repository.setUserDefinedCurrencyAmount(BigDecimal("1.0"))} returns Completable.complete()
        val viewModel = CurrencyConverterViewModel(repository)
        val item = newItem(EUR, "1.0")

        // when
        viewModel.onItemSelected(item)

        // then
        verify { repository.setUserDefinedCurrency(item.currencyModel) }
    }

    @Test
    fun `changes base amount in repository when new item is selected`() {
        // given
        val repository = spyk<CurrencyRepository>()
        every { repository.setUserDefinedCurrency(any())} returns Completable.complete()
        every { repository.setUserDefinedCurrencyAmount(any())} returns Completable.complete()
        val viewModel = CurrencyConverterViewModel(repository)
        val item = newItem(EUR, "1.0")


        // when
        viewModel.onItemSelected(item)

        // then
        verify { repository.setUserDefinedCurrencyAmount(BigDecimal(item.currencyAmount)) }
    }

    @Test
    fun `changes base amount in repository when new amount is set`() {
        // given
        val repository = spyk<CurrencyRepository>()
        every { repository.setUserDefinedCurrency(any())} returns Completable.complete()
        every { repository.setUserDefinedCurrencyAmount(any())} returns Completable.complete()
        val viewModel = CurrencyConverterViewModel(repository)

        // when
        viewModel.onAmountChanged(BigDecimal.TEN)

        // then
        verify { repository.setUserDefinedCurrencyAmount(BigDecimal.TEN) }
        confirmVerified(repository)
    }

    @Test
    fun `emitted item list is sorted by modification rate`() {
        // given
        val repository = mockk<CurrencyRepository>()
        every { repository.observeCurrencyExchangeRates(any(), any(), any()) } returns Flowable.just(
            ExchangeRatesModel(
                EUR,
                mapOf(
                    EUR to BigDecimal("1"),
                    USD to BigDecimal("1"),
                    PLN to BigDecimal("1")
                )
            )
        )
        every { repository.observeCurrenciesListModifications()} returns Flowable.just(ModifiedCurrenciesModel(
            mapOf(
                EUR to 0L,
                USD to 2L,
                PLN to 1L
            )
        ))
        every { repository.observeUserDefinedCurrencyAmount()} returns Flowable.just(BigDecimal.ONE )

        val viewModel = CurrencyConverterViewModel(repository)

        // when
        val observable = viewModel.observeEvents().test()

        // then
        observable.assertValues(CurrencyConverterViewModel.Event.ItemsUpdate(
            listOf(
                newItem(USD, "1"),
                newItem(PLN, "1"),
                newItem(EUR, "1")
            )
        ))
    }

    ////////////////////
    // TESTING UTILS //
    //////////////////

    fun newItem(
        currencyModel: CurrencyModel,
        currencyAmount: String
    ) =
        CurrencyViewItemModel(
            currencyModel,
            currencyAmount
        )

}