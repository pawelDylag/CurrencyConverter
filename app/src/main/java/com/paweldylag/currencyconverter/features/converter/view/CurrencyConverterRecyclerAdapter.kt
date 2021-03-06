package com.paweldylag.currencyconverter.features.converter.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.paweldylag.currencyconverter.R
import com.paweldylag.currencyconverter.features.converter.model.CurrencyViewItemModel
import com.paweldylag.currencyconverter.features.converter.view.CurrencyItemDiffUtil.CurrencyViewItemDifference
import com.wafel.skald.api.createLogger
import java.math.BigDecimal

/**
 * Created by Pawel Dylag
 */
class CurrencyConverterRecyclerAdapter(
    private var items: List<CurrencyViewItemModel>,
    private val onItemSelected: (CurrencyViewItemModel) -> Unit,
    private val onBaseAmountChanged: (BigDecimal) -> Unit,
    private val onRequestScrollToPosition: (Int) -> Unit,
    private val loadItemImage: (item: CurrencyViewItemModel, imageView: ImageView) -> Unit

) : RecyclerView.Adapter<CurrencyViewHolder>() {

    private val logger = createLogger(this::class.java)
    private var itemBeingEdited: CurrencyViewItemModel? = null
    private val textWatcher = CurrencyAmountTextWatcher(onBaseAmountChanged)

    fun updateItems(newItems: List<CurrencyViewItemModel>) {
        DiffUtil
            .calculateDiff(CurrencyItemDiffUtil(this.items, newItems))
            .dispatchUpdatesTo(this)
        scrollToTopIfBaseCurrencyHasChanged(items, newItems)
        this.items = newItems
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CurrencyViewHolder(parent.inflate(R.layout.currency_converter_recycler_item, false))

    override fun getItemCount() =
        items.size

    override fun getItemId(position: Int) =
        items[position].currencyModel.hashCode().toLong()

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        with(items[position]) {
            holder.setCurrencyAmount(currencyAmount)
            holder.setCurrencyCode(currencyModel.currency.currencyCode)
            holder.setCurrencyName(currencyModel.currency.displayName)
            loadItemImage(this, holder.currencyImage)
            holder.amountEditText.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    itemBeingEdited = this
                    holder.amountEditText.setSelection(holder.amountEditText.length())
                    holder.amountEditText.addTextChangedListener(textWatcher)
                    onItemSelected(this)
                } else {
                    holder.amountEditText.removeTextChangedListener(textWatcher)
                }
            }
            holder.itemView.setOnClickListener {
                holder.amountEditText.requestFocus()
            }
        }
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) super.onBindViewHolder(holder, position, payloads)
        else {
            @Suppress("UNCHECKED_CAST")
            with(payloads as MutableList<CurrencyViewItemDifference>) {
                with(squashToMostRecentDifference()) {
                    if (itemBeingEdited?.currencyModel != new.currencyModel) {
                        if (old.currencyModel != new.currencyModel) {
                            holder.setCurrencyCode(new.currencyModel.currency.currencyCode)
                            holder.setCurrencyName(new.currencyModel.currency.displayName)
                        }
                        if (old.currencyAmount != new.currencyAmount)
                            holder.setCurrencyAmount(new.currencyAmount)
                    }
                }
            }
        }
    }

    private fun scrollToTopIfBaseCurrencyHasChanged(
        items: List<CurrencyViewItemModel>,
        newItems: List<CurrencyViewItemModel>
    ) {
        if (newItems.isNotEmpty() && items.isNotEmpty()) {
            val newListBaseItem = newItems.first()
            val oldListBaseItem = items.first()
            if (newListBaseItem.currencyModel != oldListBaseItem.currencyModel) {
                with(items.indexOfFirst { it.currencyModel == newListBaseItem.currencyModel }) {
                    if (this > 0) {
                        onRequestScrollToPosition(0)
                    }
                }
            }
        }
    }

    private fun MutableList<CurrencyViewItemDifference>.squashToMostRecentDifference(): CurrencyViewItemDifference =
        CurrencyViewItemDifference(this.first().old, this.last().new)

    private fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
    }
}


class CurrencyViewHolder(
    view: View,
    private val currencyCodeTextView: TextView = view.findViewById(R.id.currency_converter_recycler_item_text),
    private val currencyNameTextView: TextView = view.findViewById(R.id.currency_converter_recycler_item_subtext),
    val currencyImage: ImageView = view.findViewById(R.id.currency_converter_recycler_item_image),
    val amountEditText: EditText = view.findViewById(R.id.currency_converter_recycler_item_amount_edit)
) : RecyclerView.ViewHolder(view) {

    fun setCurrencyCode(currencyCode: String) {
        currencyCodeTextView.text = currencyCode
    }

    fun setCurrencyName(currencyName: String) {
        currencyNameTextView.text = currencyName
    }

    fun setCurrencyAmount(currencyAmount: String) {
        amountEditText.text.apply {
            clear()
            insert(0, currencyAmount)
        }
    }

}
