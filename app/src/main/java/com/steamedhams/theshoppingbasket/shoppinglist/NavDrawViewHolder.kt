package com.steamedhams.theshoppingbasket.shoppinglist

import android.app.Activity
import android.databinding.DataBindingUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.steamedhams.theshoppingbasket.R
import com.steamedhams.theshoppingbasket.ShoppingBasket
import com.steamedhams.theshoppingbasket.data.model.ShoppingList
import com.steamedhams.theshoppingbasket.data.realm.RealmDelegate
import com.steamedhams.theshoppingbasket.databinding.NavDrawerLayoutBinding
import com.steamedhams.theshoppingbasket.databinding.ShoppingListCellBinding
import com.steamedhams.theshoppingbasket.shoppinglist.NavDrawViewHolder.ShoppingListListAdapter.ShoppingListHolder
import javax.inject.Inject

/**
 * Class responsible for inflating and managing the content of the Nav Drawer
 * <p>
 * Created by richard on 20/02/17.
 */
class NavDrawViewHolder(binding : NavDrawerLayoutBinding,
                        activity : Activity,
                        val onListSelected : (String) -> Unit) {

    val adapter : ShoppingListListAdapter

    init {
        Log.d(NavDrawViewHolder::class.java.simpleName, "New list button clicked")
        adapter = ShoppingListListAdapter()
        binding.shoppingListList.adapter = adapter
        binding.shoppingListList.layoutManager = LinearLayoutManager(binding.root.context)

        binding.newListButton.setOnClickListener {
            NewItemDialog({itemName -> onItemCreated(itemName)})
                    .show(activity.fragmentManager, NewItemDialog.getTag())
        }
    }

    fun onItemCreated(itemName : String) {
        val list = ShoppingList(itemName)
        adapter.addList(list)
        adapter.notifyDataSetChanged()
    }

    inner class ShoppingListListAdapter : RecyclerView.Adapter<ShoppingListHolder>() {

        init {
            ShoppingBasket.netComponent.inject(this)
        }

        @Inject lateinit var  realmDelegate : RealmDelegate


        override fun getItemCount(): Int {
            return realmDelegate.getListCount().toInt()
        }

        override fun getItemViewType(position: Int): Int {
            return R.layout.shopping_list_cell
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ShoppingListHolder {

            val layoutInflater = LayoutInflater.from(parent!!.context)
            val itemBinding = DataBindingUtil.inflate<ShoppingListCellBinding>(layoutInflater, viewType, parent, false)
            return ShoppingListHolder(itemBinding)
        }

        override fun onBindViewHolder(holder: ShoppingListHolder?, position: Int) {
            val shoppingList = realmDelegate.getLists()[position]
            holder?.bind(shoppingList)
        }

        fun addList(list: ShoppingList) {
            realmDelegate.addList(list)
        }

        inner class ShoppingListHolder(val binding : ShoppingListCellBinding )
            : RecyclerView.ViewHolder(binding.root) {

            fun bind(list : ShoppingList) {
                binding.shoppinglist = list
                binding.root.setOnClickListener { onListSelected(list.UUID) }
            }
        }

    }

}