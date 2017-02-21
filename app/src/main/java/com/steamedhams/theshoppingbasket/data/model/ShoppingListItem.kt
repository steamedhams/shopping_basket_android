package com.steamedhams.theshoppingbasket.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

/**
 * Data class representing an item in a shopping list
 * <p>
 * Created by richard on 13/02/17.
 */
open class ShoppingListItem(@PrimaryKey @Required var UUID: String,
                            var listId : String,
                            var Title: String,
                            var Completed: Boolean)
                : RealmObject() {

    constructor() : this(java.util.UUID.randomUUID().toString(), "", "", false)

    constructor(listId: String, value : String)
            : this(java.util.UUID.randomUUID().toString(), listId, value, false)

}
