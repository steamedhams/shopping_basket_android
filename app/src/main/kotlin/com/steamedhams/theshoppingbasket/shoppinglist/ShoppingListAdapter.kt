package com.steamedhams.theshoppingbasket.shoppinglist

import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.steamedhams.theshoppingbasket.R
import com.steamedhams.theshoppingbasket.ShoppingBasket
import com.steamedhams.theshoppingbasket.data.model.ShoppingListItem

/**
 * Adapter to create and provide views representing items in a ShoppingList
 * <p>
 * Created by richard on 07/02/17.
 */
class ShoppingListAdapter : RecyclerView.Adapter<ShoppingListAdapter.ShoppingListItemHolder>() {

    init {
        ShoppingBasket.netComponent.inject(this)
    }

    val items : MutableList<ShoppingListItem> = ArrayList()

    var viewId : String = ""
        set(value) {
            field = value
        }

    val selectedPositions : MutableList<Int> = ArrayList()

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.shopping_list_item_cell
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ShoppingListItemHolder {
        val layoutInflater = LayoutInflater.from(parent!!.context)
        val view = layoutInflater.inflate(viewType, parent, false)
        return ShoppingListItemHolder(view, this)
    }

    override fun onBindViewHolder(holder: ShoppingListItemHolder?, position: Int) {
        val listItem = items[position]
        holder?.bind(listItem)
        holder?.setRowSelected(selectedPositions.contains(position))
    }

    fun clearItems() {
        items.clear()
    }

    fun addItem(item : ShoppingListItem) {
        items.add(item)
    }

    private fun deleteItem(position: Int) {
        selectedPositions.remove(position)
        deleteItem(position)
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

    inner class ShoppingListItemHolder(val view : View, val adapter : ShoppingListAdapter)
            : RecyclerView.ViewHolder(view) {

        @BindView(R.id.shopping_list_item_check_box)
        lateinit var shoppingListItemCheckBox : CheckBox
        @BindView(R.id.shopping_list_item_delete_button)
        lateinit var shoppingListItemDeleteButton : ImageView
        @BindView(R.id.shopping_list_item_title)
        lateinit var shoppingListItemTitle : TextView

        init {
            ButterKnife.bind(this, view)
        }

        fun bind(item: ShoppingListItem) {
            shoppingListItemTitle.text = item.title
            decorateText(item.completed)
            view.setOnClickListener{ onCheckBoxClicked() }
            view.setOnLongClickListener {
                adapter.onViewLongPressed(adapterPosition)
                true
            }
            shoppingListItemCheckBox.isClickable = false // Allow the row onCLick listener to handle click events
            shoppingListItemDeleteButton.setOnClickListener { adapter.deleteItem(adapterPosition) }
        }

        fun setRowSelected(selected : Boolean) {
            shoppingListItemDeleteButton.visibility = if (selected) VISIBLE else INVISIBLE
            if (selected) {
                view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.primary_light))
            } else {
                view.setBackgroundResource(R.drawable.white_ripple)
            }
        }

        private fun onCheckBoxClicked() {
            shoppingListItemCheckBox.performClick()
            val checked = shoppingListItemCheckBox.isChecked
            checkItem(checked, adapterPosition)
            decorateText(checked)
        }

        private fun decorateText(checked: Boolean) {
            if (checked) {
                shoppingListItemTitle.paintFlags = shoppingListItemTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                shoppingListItemTitle.paintFlags = 0
            }
        }
    }

    private fun checkItem(checked: Boolean, adapterPosition: Int) {
        if (checked) {
            selectedPositions.add(adapterPosition)
        } else {
            selectedPositions.remove(adapterPosition)
        }
    }

}
