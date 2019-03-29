package com.paweldylag.currencyconverter.features.converter.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paweldylag.currencyconverter.R
import com.paweldylag.currencyconverter.features.converter.viewmodel.CurrencyConverterViewModel
import com.wafel.skald.api.createLogger
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_currency_converter.*
import javax.inject.Inject


class CurrencyConverterActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val logger = createLogger(this::class.java)

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: CurrencyConverterRecyclerAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewModel: CurrencyConverterViewModel
    private var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_converter)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[CurrencyConverterViewModel::class.java]
        viewManager = LinearLayoutManager(this)
        viewAdapter = CurrencyConverterRecyclerAdapter(
            emptyList(),
            viewModel::onItemSelected,
            viewModel::onAmountChanged,
            viewManager::scrollToPosition,
            viewModel::loadItemImage
        ).apply {
            setHasStableIds(true)
        }
        recyclerView = currency_converter_recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.observeEvents()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { logger.debug("New currencyModel list item update.") }
            .subscribe(
                {
                    when (it) {
                        is CurrencyConverterViewModel.Event.ItemsUpdate -> {
                            currency_converter_loader_view.visibility = View.GONE
                            viewAdapter.updateItems(it.items)
                        }
                    }
                },
                {
                    logger.error("Error while observing currencies. ${it.message}")
                    it.printStackTrace()
                })
            .addToCompositeDisposable()

    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    private fun Disposable.addToCompositeDisposable() {
        compositeDisposable.add(this)
    }

}
