package com.steamedhams.theshoppingbasket.shoppinglist

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.steamedhams.theshoppingbasket.R
import com.steamedhams.theshoppingbasket.data.model.ShoppingListItem
import com.steamedhams.theshoppingbasket.databinding.ActivityListBinding

/**
 * Activity which will host a Shopping list, handling displaying and interacting with that list
 * including adding, editing and removing items from the list
 * <p>
 * Created by richard on 13/02/17.
 */
class ListActivity : AppCompatActivity(), NewListItemDialog.Host {

    private lateinit var binding : ActivityListBinding
    private lateinit var adapter : ShoppingListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityListBinding>(this, R.layout.activity_list)
        binding.listToolbar.setTitle(R.string.list_activity_title)

        R.id.list_toolbar

        setSupportActionBar(binding.listToolbar)

        setUpShoppingList()
    }

    private fun setUpShoppingList() {
        val shoppingList = binding.shoppingList

        shoppingList.layoutManager = LinearLayoutManager(this)
        shoppingList.itemAnimator = DefaultItemAnimator()
        adapter = ShoppingListAdapter()
        shoppingList.adapter = adapter

        binding.listActivityNewItemFab.setOnClickListener {
            NewListItemDialog(this).show(fragmentManager, NewListItemDialog.getTag())
        }
    }

    override fun onItemCreated(item: String) {
        adapter.addItem(ShoppingListItem(item))
    }
}
