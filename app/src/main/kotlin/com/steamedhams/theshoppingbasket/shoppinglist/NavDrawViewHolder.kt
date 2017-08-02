package com.steamedhams.theshoppingbasket.shoppinglist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.steamedhams.theshoppingbasket.R
import com.steamedhams.theshoppingbasket.ShoppingBasket
import com.steamedhams.theshoppingbasket.api.ApiListService
import com.steamedhams.theshoppingbasket.data.model.ShoppingList
import com.steamedhams.theshoppingbasket.room.BasketDatabase
import com.steamedhams.theshoppingbasket.shoppinglist.NavDrawViewHolder.ShoppingListListAdapter.ShoppingListHolder
import rx.schedulers.Schedulers
import java.util.logging.Level
import java.util.logging.Logger
import javax.inject.Inject

/**
 * Class responsible for inflating and managing the content of the Nav Drawer
 * <p>
 * Created by richard on 20/02/17.
 */
class NavDrawViewHolder(navDrawerView: View, activity: ListActivity, val onListSelected: (Long) -> Unit) {

    val LOG = Logger.getLogger(this.javaClass.name)

    @Inject lateinit var database: BasketDatabase
    @Inject lateinit var apiListService: ApiListService

    val adapter: ShoppingListListAdapter
    val listsLiveData: LiveData<MutableList<ShoppingList>>?

    var lists: MutableList<ShoppingList> = ArrayList()
        set (value) {
            lists.clear()
            lists.addAll(value)
        }

    @BindView(R.id.shopping_list_list)
    lateinit var shoppingListList: RecyclerView

    @BindView(R.id.newListButton)
    lateinit var newListButton: Button

    init {
        ShoppingBasket.netComponent.inject(this)
        ButterKnife.bind(this, navDrawerView)

        adapter = ShoppingListListAdapter()
        shoppingListList.adapter = adapter
        shoppingListList.layoutManager = LinearLayoutManager(navDrawerView.context)

        newListButton.setOnClickListener {
            NewItemDialog({ itemName -> onItemCreated(itemName) })
                    .show(activity.fragmentManager, NewItemDialog.getTag())
        }
        listsLiveData = database.shoppingListDao().getAllShoppingLists()
        listsLiveData.observe(activity, Observer {
            lists ->
                this.lists.clear()
                lists?.forEach { list ->
                    addList(list)
                    LOG.info("Adding list to adapter: " + list.toString())
                 }
        })
    }

    fun onItemCreated(itemName: String) {
        val list = ShoppingList(itemName)

        apiListService.createList(list)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe( { shoppingList ->
                        database.shoppingListDao().insert(shoppingList)
                        LOG.info(list.toString())
                }, {
                    throwable ->
                    LOG.log(Level.WARNING, "Fail to add list", throwable)
                })
    }

    fun addList(list: ShoppingList) {
        lists.add(list)
        adapter.notifyDataSetChanged()
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

            val view = LayoutInflater.from(parent?.context).inflate(R.layout.shopping_list_cell, parent, false)
            return ShoppingListHolder(view)
        }

        override fun onBindViewHolder(holder: ShoppingListHolder?, position: Int) {
            val shoppingList = lists[position]
            holder?.bind(shoppingList)
        }

        inner class ShoppingListHolder(val view: View)
            : RecyclerView.ViewHolder(view) {

            @BindView(R.id.shopping_list_name)
            lateinit var shoppingListName: TextView

            init {
                ButterKnife.bind(this, view)
            }

            fun bind(list: ShoppingList) {
                shoppingListName.text = list.title
                view.setOnClickListener { onListSelected(list.id) }
            }
        }

    }

}