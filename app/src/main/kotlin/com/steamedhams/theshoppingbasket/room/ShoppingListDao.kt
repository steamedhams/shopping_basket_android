package com.steamedhams.theshoppingbasket.room

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.steamedhams.theshoppingbasket.data.model.ShoppingList

/**
 * Created by richard on 08/06/17.
 */
@Dao
interface ShoppingListDao {

    @Query("SELECT * FROM shoppinglist")
    fun getAllShoppingLists(): LiveData<MutableList<ShoppingList>>

    @Insert
    fun insert(shoppingList: ShoppingList)

    @Update
    fun update(shoppingList: ShoppingList)

}