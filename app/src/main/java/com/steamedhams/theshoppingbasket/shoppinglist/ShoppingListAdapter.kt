package com.steamedhams.theshoppingbasket.shoppinglist

import android.databinding.DataBindingUtil
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.CheckBox
import com.steamedhams.theshoppingbasket.R
import com.steamedhams.theshoppingbasket.ShoppingBasket
import com.steamedhams.theshoppingbasket.data.model.ShoppingListItem
import com.steamedhams.theshoppingbasket.databinding.ShoppingListItemCellBinding
import java.util.*

/**
 * Adapter to create and provide views representing items in a ShoppingList
 * <p>
 * Created by richard on 07/02/17.
 */
class ShoppingListAdapter : RecyclerView.Adapter<ShoppingListAdapter.ShoppingListItemHolder>() {

    val selectedPositions : MutableList<Int> = ArrayList()

    override fun getItemCount(): Int {
        return ShoppingBasket.realmDelegate.getItemCount().toInt()
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.shopping_list_item_cell
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ShoppingListItemHolder {
        val layoutInflater = LayoutInflater.from(parent!!.context)
        val itemBinding = DataBindingUtil.inflate<ShoppingListItemCellBinding>(layoutInflater, viewType, parent, false)
        return ShoppingListItemHolder(itemBinding, this)
    }

    override fun onBindViewHolder(holder: ShoppingListItemHolder?, position: Int) {
        val listItem = ShoppingBasket.realmDelegate.getListItems()[position]
        holder?.bind(listItem)
        holder?.setRowSelected(selectedPositions.contains(position))
    }

    fun addItem(item : ShoppingListItem) {
        ShoppingBasket.realmDelegate.addListItem(item)
    }

    private fun deleteItem(position: Int) {
        selectedPositions.remove(position)
        ShoppingBasket.realmDelegate.deleteItem(position)
        notifyItemRemoved(position)
    }

    private fun onViewLongPressed(position: Int) {
        if (!selectedPositions.contains(position)) {
            selectedPositions.add(position)
        } else {
            selectedPositions.remove(position)
        }
        notifyItemChanged(position)
    }

    class ShoppingListItemHolder(val binding: ShoppingListItemCellBinding, val adapter :ShoppingListAdapter)
            : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ShoppingListItem) {
            binding.item = item
            decorateText(item.checked)
            binding.root.setOnClickListener{ onCheckBoxClicked() }
            binding.root.setOnLongClickListener {
                adapter.onViewLongPressed(adapterPosition)
                true
            }
            binding.shoppingListItemCheckBox.isClickable = false // Allow the row onCLick listener to handle click events
            binding.shoppingListItemDeleteButton.setOnClickListener { adapter.deleteItem(adapterPosition) }
        }

        fun setRowSelected(selected : Boolean) {
            binding.shoppingListItemDeleteButton.visibility = if (selected) VISIBLE else INVISIBLE
            if (selected) {
                binding.root.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.primary_light))
            } else {
                binding.root.setBackgroundResource(R.drawable.white_ripple)
            }
        }

        private fun onCheckBoxClicked() {
            binding.shoppingListItemCheckBox.performClick()
            val checked = binding.shoppingListItemCheckBox.isChecked
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