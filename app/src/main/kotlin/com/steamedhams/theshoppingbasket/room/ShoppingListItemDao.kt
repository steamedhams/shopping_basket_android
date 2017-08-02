package com.steamedhams.theshoppingbasket.room

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.steamedhams.theshoppingbasket.data.model.ShoppingListItem
import io.reactivex.Flowable

/**
 * Created by richard on 08/06/17.
 */
@Dao
interface ShoppingListItemDao {

    @Query("SELECT * FROM shoppinglistitem")
    fun getAllShoppingListItems(): LiveData<MutableList<ShoppingListItem>>

    @Query("SELECT * FROM shoppinglistitem WHERE list_id = :arg0")
    fun getAllShoppingListItemsForList(listId : Long): LiveData<MutableList<ShoppingListItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(shoppingListItem: ShoppingListItem)

}