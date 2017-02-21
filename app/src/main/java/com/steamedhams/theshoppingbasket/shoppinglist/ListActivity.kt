package com.steamedhams.theshoppingbasket.shoppinglist

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Gravity
import com.steamedhams.theshoppingbasket.R
import com.steamedhams.theshoppingbasket.ShoppingBasket
import com.steamedhams.theshoppingbasket.api.ApiListService
import com.steamedhams.theshoppingbasket.api.helper.IoToMainThreadTransformer
import com.steamedhams.theshoppingbasket.data.model.ShoppingList
import com.steamedhams.theshoppingbasket.data.model.ShoppingListItem
import com.steamedhams.theshoppingbasket.data.model.ShoppingListItemsWrapper
import com.steamedhams.theshoppingbasket.data.realm.RealmDelegate
import com.steamedhams.theshoppingbasket.databinding.ActivityListBinding
import rx.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

/**
 * Activity which will host a Shopping list, handling displaying and interacting with that list
 * including adding, editing and removing items from the list
 * <p>
 * Created by richard on 13/02/17.
 */
class ListActivity : AppCompatActivity() {

    private lateinit var binding : ActivityListBinding
    private lateinit var adapter : ShoppingListAdapter

    @Inject lateinit var listService : ApiListService
    @Inject lateinit var realmDelegate : RealmDelegate

    private var currentList : String = ""
        set(value) {
            field = value
            adapter.viewId = value
            binding.listToolbar.title = realmDelegate.getList(value).title
        }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityListBinding>(this, R.layout.activity_list)
        binding.listToolbar.setTitle(R.string.list_activity_title)

        ShoppingBasket.netComponent.inject(this)

        setSupportActionBar(binding.listToolbar)
        binding.listToolbar.setNavigationOnClickListener { toggleNavDrawer() }

        NavDrawViewHolder(binding.navDrawer, this,
                { viewId ->
                        currentList = viewId
                        binding.navDrawerLayout.closeDrawers()
                })

        setUpShoppingList()
    }

    override fun onResume() {
        super.onResume()
        if (realmDelegate.getLists().isNotEmpty()) {
            currentList = realmDelegate.getLists()[0].UUID
        } else {
            NewItemDialog({viewTitle -> onListCreated(viewTitle) })
                    .show(fragmentManager, NewItemDialog.getTag())
        }
    }

    private fun onListCreated(viewTitle: String) {
        val list = ShoppingList(viewTitle)
        realmDelegate.addList(list)
        currentList = list.UUID
    }

    private fun toggleNavDrawer() {
        if (binding.navDrawerLayout.isDrawerOpen(Gravity.START)) {
            binding.navDrawerLayout.closeDrawers()
        } else {
            binding.navDrawerLayout.openDrawer(Gravity.START)
        }
    }

    private fun setUpShoppingList() {
        val shoppingList = binding.shoppingList

        shoppingList.layoutManager = LinearLayoutManager(this)
        shoppingList.itemAnimator = DefaultItemAnimator()
        adapter = ShoppingListAdapter()
        shoppingList.adapter = adapter

        binding.listActivityNewItemFab.setOnClickListener {
            NewItemDialog({string -> onShoppingListItemCreated(string)})
                    .show(fragmentManager, NewItemDialog.getTag())
        }
    }

    fun onShoppingListItemCreated(itemName: String) {
        val listItem = ShoppingListItem(currentList, itemName)
        adapter.addItem(listItem)
    }
}
