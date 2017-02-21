package com.steamedhams.theshoppingbasket.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

/**
 * Data class representing a shopping list
 * <p>
 * Created by richard on 20/02/17.
 */
open class ShoppingList(@PrimaryKey @Required var UUID : String,
                        var title : String)
                : RealmObject() {

    constructor() : this("")

    constructor(title : String) : this(java.util.UUID.randomUUID().toString(), title)
}