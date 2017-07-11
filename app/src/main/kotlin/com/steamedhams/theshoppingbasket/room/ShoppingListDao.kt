package com.steamedhams.theshoppingbasket.room

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.steamedhams.theshoppingbasket.data.model.ShoppingList

/**
 * Created by richard on 08/06/17.
 */
@Dao
interface ShoppingListDao {

    @Query("SELECT * FROM shoppinglist")
    fun getAllShoppingLists(): LiveData<MutableList<ShoppingList>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(shoppingList: ShoppingList)

    @Update
    fun update(shoppingList: ShoppingList)

}