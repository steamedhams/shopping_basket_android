package com.bridgeri127.theshoppingbasket

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bridgeri127.theshoppingbasket.databinding.ActivityListBinding

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

        shoppingList.layoutManager
    }
}
