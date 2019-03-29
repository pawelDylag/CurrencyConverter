package com.paweldylag.currencyconverter.features.converter.view

import androidx.recyclerview.widget.DiffUtil
import com.paweldylag.currencyconverter.features.converter.model.CurrencyViewItemModel

/**
 * Created by Pawel Dylag
 */
class CurrencyItemDiffUtil(
    private val oldList: List<CurrencyViewItemModel>,
    private val newList: List<CurrencyViewItemModel>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].currencyModel == newList[newItemPosition].currencyModel
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.currencyModel == newItem.currencyModel && oldItem.currencyAmount == newItem.currencyAmount
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return CurrencyViewItemDifference(oldList[oldItemPosition], newList[newItemPosition])
    }


    data class CurrencyViewItemDifference(
        val old: CurrencyViewItemModel,
        val new: CurrencyViewItemModel
    )

}