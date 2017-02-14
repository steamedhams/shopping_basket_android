package com.steamedhams.theshoppingbasket.data.realm

import com.steamedhams.theshoppingbasket.data.model.ShoppingListItem
import io.realm.Realm

/**
 * Delegate class to handle interacting with the Realm instance
 * <p>
 * Created by richard on 13/02/17.
 */
class RealmDelegate {

    fun getListItems() : List<ShoppingListItem> {
        return Realm.getDefaultInstance().where(ShoppingListItem::class.java).findAll()
    }

    fun addListItem(item : ShoppingListItem) {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.copyToRealm(item)
        realm.commitTransaction()
    }

    fun getItemCount() : Long {
        return Realm.getDefaultInstance().where(ShoppingListItem::class.java).count()
    }

    fun checkItem(checked: Boolean, position: Int) {

        val item = getListItems()[position]

        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        item.checked = checked
        realm.commitTransaction()
    }

    fun deleteItem(position: Int) {
        val item = getListItems()[position]


        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        item.deleteFromRealm()
        realm.commitTransaction()
    }

}