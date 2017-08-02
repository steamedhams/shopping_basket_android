package com.steamedhams.theshoppingbasket.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.steamedhams.theshoppingbasket.data.model.ShoppingList
import com.steamedhams.theshoppingbasket.data.model.ShoppingListItem

/**
 * Created by richard on 08/06/17.
 */
@Database(entities = arrayOf(ShoppingList::class, ShoppingListItem::class), version = 2)
abstract class BasketDatabase : RoomDatabase() {

    abstract fun shoppingListDao(): ShoppingListDao

    abstract fun shoppingListItemDao(): ShoppingListItemDao

}