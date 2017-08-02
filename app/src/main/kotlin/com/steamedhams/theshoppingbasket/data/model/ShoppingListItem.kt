package com.steamedhams.theshoppingbasket.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Data class representing an item in a shopping list
 * <p>
 * Created by richard on 13/02/17.
 */
@Entity
data class ShoppingListItem(@PrimaryKey(autoGenerate = true) var id: Long = -1L,
                            var title: String = "",
                            var list_id : Long = -1L,
                            var completed: Boolean = false) {
    constructor(title: String, listId: Long) : this(-1L, title, listId, false)
}
