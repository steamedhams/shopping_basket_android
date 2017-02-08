package com.steamedhams.theshoppingbasket.shoppinglist

import android.databinding.DataBindingUtil
import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import com.steamedhams.theshoppingbasket.R
import com.steamedhams.theshoppingbasket.databinding.ListItemCreateBoxBinding
import com.steamedhams.theshoppingbasket.databinding.ShoppingListItemBinding
import java.util.*
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG



/**
 * Created by richard on 07/02/17.
 */
class ShoppingListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val items = ArrayList<CharSequence>()

    override fun getItemCount(): Int {
        return items.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        if (position == itemCount - 1)
            return R.layout.list_item_create_box

        return R.layout.shopping_list_item
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent!!.context)

        if (viewType == R.layout.list_item_create_box) {
            val itemBinding = DataBindingUtil.inflate<ListItemCreateBoxBinding>(layoutInflater, viewType, parent, false)
            return CreateListItemViewHolder(object : CreateListItemViewHolder.ItemCreationListener {
                override fun onItemCreated(item: CharSequence) {
                    items.add(item)
                    notifyItemInserted(items.size - 1)
                }
            }, itemBinding)
        }

        val itemBinding = DataBindingUtil.inflate<ShoppingListItemBinding>(layoutInflater, viewType, parent, false)
        return ShoppingListItemHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is ShoppingListItemHolder) {
            val listItem = items[position]
            val shoppingItemHolder: ShoppingListItemHolder = holder
            shoppingItemHolder.bind(listItem)
        }
    }

    class ShoppingListItemHolder(val binding: ShoppingListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CharSequence) {
            binding.item = item
            (binding.shoppingListItemCheckBox as CheckBox).setOnClickListener { v -> onCheckBoxClicked() }
        }

        private fun onCheckBoxClicked() {
            val checked = (binding.shoppingListItemCheckBox as CheckBox).isChecked
            binding.shoppingListItemValue.paintFlags = binding.shoppingListItemValue.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    class CreateListItemViewHolder(val listener: ItemCreationListener, val binding: ListItemCreateBoxBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.newListItemEditText.setOnEditorActionListener({ textView, i, keyEvent -> addItemToList(textView.text)})
        }

        private fun addItemToList(text: CharSequence): Boolean {
            listener.onItemCreated(text)
            binding.newListItemEditText.setText("")
            return true
        }

        interface ItemCreationListener {
            fun onItemCreated(item: CharSequence)
        }
    }

}