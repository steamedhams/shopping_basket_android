package com.steamedhams.theshoppingbasket.shoppinglist

import android.databinding.DataBindingUtil
import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import com.steamedhams.theshoppingbasket.R
import com.steamedhams.theshoppingbasket.ShoppingBasket
import com.steamedhams.theshoppingbasket.data.model.ShoppingListItem
import com.steamedhams.theshoppingbasket.data.realm.RealmDelegate
import com.steamedhams.theshoppingbasket.databinding.ShoppingListItemBinding

/**
 * Created by richard on 07/02/17.
 */
class ShoppingListAdapter : RecyclerView.Adapter<ShoppingListAdapter.ShoppingListItemHolder>() {

    override fun getItemCount(): Int {
        return ShoppingBasket.realmDelegate.getItemCount().toInt()
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.shopping_list_item
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ShoppingListItemHolder {
        val layoutInflater = LayoutInflater.from(parent!!.context)
        val itemBinding = DataBindingUtil.inflate<ShoppingListItemBinding>(layoutInflater, viewType, parent, false)
        return ShoppingListItemHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ShoppingListItemHolder?, position: Int) {
        val listItem = ShoppingBasket.realmDelegate.getListItems()[position]
        holder?.bind(listItem)
    }

    fun addItem(item : ShoppingListItem) {
        ShoppingBasket.realmDelegate.addListItem(item)
    }

    class ShoppingListItemHolder(val binding: ShoppingListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ShoppingListItem) {
            binding.item = item
            decorateText(item.checked)
            (binding.shoppingListItemCheckBox as CheckBox).setOnClickListener { v -> onCheckBoxClicked() }
        }

        private fun onCheckBoxClicked() {
            val checked = (binding.shoppingListItemCheckBox as CheckBox).isChecked
            ShoppingBasket.realmDelegate.checkItem(checked, adapterPosition)
            decorateText(checked)
        }

        private fun decorateText(checked: Boolean) {
            if (checked) {
                binding.shoppingListItemValue.paintFlags = binding.shoppingListItemValue.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                binding.shoppingListItemValue.paintFlags = 0
            }
        }
    }

}