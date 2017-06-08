package com.steamedhams.theshoppingbasket.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Data class representing a shopping list
 * <p>
 * Created by richard on 20/02/17.
 */
@Entity
data class ShoppingList(@PrimaryKey(autoGenerate = true) var id: Long = -1,
                        var title : String = "") {
    constructor(title: String) : this(title = title, id = -1)
}