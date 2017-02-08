package com.steamedhams.theshoppingbasket.shoppinglist

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.steamedhams.theshoppingbasket.R
import com.steamedhams.theshoppingbasket.databinding.ActivityListBinding

class ListActivity : AppCompatActivity() {

    private lateinit var binding : ActivityListBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityListBinding>(this, R.layout.activity_list)
        binding.listToolbar.setTitle(R.string.list_activity_title)

        setSupportActionBar(binding.listToolbar)

        setUpShoppingList()
    }

    private fun setUpShoppingList() {
        val shoppingList = binding.shoppingList

        shoppingList.layoutManager = LinearLayoutManager(this)
        shoppingList.adapter = ShoppingListAdapter()
    }
}
