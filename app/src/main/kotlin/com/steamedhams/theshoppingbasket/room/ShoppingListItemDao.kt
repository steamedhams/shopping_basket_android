package com.steamedhams.theshoppingbasket.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.steamedhams.theshoppingbasket.data.model.ShoppingListItem
import io.reactivex.Flowable

/**
 * Created by richard on 08/06/17.
 */
@Dao
interface ShoppingListItemDao {

    @Query("SELECT * FROM shoppinglistitem")
    fun getAllShoppingListItems(): Flowable<List<ShoppingListItem>>

    @Insert
    fun insert(shoppingListItem: ShoppingListItem)
}