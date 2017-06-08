package com.steamedhams.theshoppingbasket.shoppinglist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.steamedhams.theshoppingbasket.R
import com.steamedhams.theshoppingbasket.ShoppingBasket
import com.steamedhams.theshoppingbasket.data.model.ShoppingList
import com.steamedhams.theshoppingbasket.databinding.NavDrawerLayoutBinding
import com.steamedhams.theshoppingbasket.databinding.ShoppingListCellBinding
import com.steamedhams.theshoppingbasket.shoppinglist.NavDrawViewHolder.ShoppingListListAdapter.ShoppingListHolder
import rx.Single
import rx.schedulers.Schedulers

/**
 * Class responsible for inflating and managing the content of the Nav Drawer
 * <p>
 * Created by richard on 20/02/17.
 */
class NavDrawViewHolder(binding : NavDrawerLayoutBinding?,
                        activity : ListActivity,
                        val onListSelected : (String) -> Unit)  {

    val adapter : ShoppingListListAdapter
    val listsLiveData : LiveData<MutableList<ShoppingList>>?

    var lists : MutableList<ShoppingList> = ArrayList()
        set (value) {
            lists.clear()
            lists.addAll(value)
        }

    init {
        Log.d(NavDrawViewHolder::class.java.simpleName, "New list button clicked")
        adapter = ShoppingListListAdapter()
        binding?.shoppingListList?.adapter = adapter
        binding?.shoppingListList?.layoutManager = LinearLayoutManager(binding?.root?.context)

        binding?.newListButton?.setOnClickListener {
            NewItemDialog({itemName -> onItemCreated(itemName)})
                    .show(activity.fragmentManager, NewItemDialog.getTag())
        }
        listsLiveData = ShoppingBasket.database?.shoppingListDao()?.getAllShoppingLists()
        listsLiveData?.observe(activity, Observer { lists ->
            run {
                this.lists.clear()
                lists?.forEach { list -> addList(list) }
            }
        })
    }

    fun onItemCreated(itemName : String) {
        val list = ShoppingList(itemName)
        Single.fromCallable {
                        run {
                            ShoppingBasket.database?.shoppingListDao()?.insert(list)
                        }
                    }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    fun addList(list: ShoppingList) {
        lists.add(list)
        adapter.notifyDataSetChanged()
        Log.d("LISTS", list.toString())
    }

    inner class ShoppingListListAdapter : RecyclerView.Adapter<ShoppingListHolder>() {

        init {
            ShoppingBasket.netComponent.inject(this)
        }

        override fun getItemCount(): Int {
            return lists.size
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
            val shoppingList = lists[position]
            holder?.bind(shoppingList)
        }

        inner class ShoppingListHolder(val binding : ShoppingListCellBinding )
            : RecyclerView.ViewHolder(binding.root) {

            fun bind(list : ShoppingList) {
                binding.shoppinglist = list
                binding.root.setOnClickListener { onListSelected(list.title) }
            }
        }

    }

}