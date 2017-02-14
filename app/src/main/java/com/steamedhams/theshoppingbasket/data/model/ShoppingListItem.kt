package com.steamedhams.theshoppingbasket.data.model

import io.realm.RealmObject

/**
 * Data class representing an item in a shopping list
 * <p>
 * Created by richard on 13/02/17.
 */
open class ShoppingListItem(var value: String, var checked: Boolean) : RealmObject() {

    constructor() : this("", false)

    constructor(value : String) : this(value, false)

}
