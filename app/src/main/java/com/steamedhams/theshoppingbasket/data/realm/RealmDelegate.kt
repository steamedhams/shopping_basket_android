package com.steamedhams.theshoppingbasket.data.realm

import com.steamedhams.theshoppingbasket.data.model.ShoppingList
import com.steamedhams.theshoppingbasket.data.model.ShoppingListItem
import io.realm.Realm
import javax.inject.Inject

/**
 * Delegate class to handle interacting with the Realm instance
 * <p>
 * Created by richard on 13/02/17.
 */
class RealmDelegate @Inject constructor()  {

    fun getListItems() : List<ShoppingListItem> {
        val realm = Realm.getDefaultInstance()
        val listItems = realm.copyFromRealm(realm.where(ShoppingListItem::class.java).findAll())
        realm.close()
        return listItems
    }

    fun addListItem(item : ShoppingListItem) {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        var realmItem = realm.where(ShoppingListItem::class.java)
                .equalTo("UUID", item.UUID)
                .findFirst()

        if (realmItem == null) {
            realmItem = realm.createObject(ShoppingListItem::class.java, item.UUID)
            realmItem.listId = item.listId
            realmItem.Title = item.Title
            realmItem.Completed = item.Completed
        } else {
            realmItem.UUID = item.UUID
            realmItem.listId = item.listId
            realmItem.Title = item.Title
            realmItem.Completed = item.Completed
        }
        realm.commitTransaction()
        realm.close()
    }

    fun getItemCount() : Long {
        val realm = Realm.getDefaultInstance()
        val count = realm.where(ShoppingListItem::class.java).count()
        realm.close()
        return count
    }

    fun checkItem(checked: Boolean, position: Int) {

        val item = getListItems()[position]

        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        item.Completed = checked
        realm.commitTransaction()
        realm.close()
    }

    fun deleteItem(position: Int) {
        val item = getListItems()[position]
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        item.deleteFromRealm()
        realm.commitTransaction()
        realm.close()
    }

    fun addListItems(items : List<ShoppingListItem>) {
        for (item : ShoppingListItem in items) addListItem(item)
    }

    fun getListCount() : Long {
        val realm = Realm.getDefaultInstance()
        val count = realm.where(ShoppingList::class.java).count()
        realm.close()
        return count
    }

    fun getLists(): List<ShoppingList> {
        val realm = Realm.getDefaultInstance()
        val listItems = realm.copyFromRealm(
                realm.where(ShoppingList::class.java)
                        .findAll())
        realm.close()
        return listItems
    }

    fun addList(list : ShoppingList) {

        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.copyToRealmOrUpdate(list)
        realm.commitTransaction()
        realm.close()

    }

    fun getCountForList(viewId : String) : Int {

        val realm = Realm.getDefaultInstance()
        val count = realm.where(ShoppingListItem::class.java)
                .equalTo("UUID", viewId)
                .count()
                .toInt()
        realm.close()
        return count
    }

    fun getList(listId : String) : ShoppingList {
        val realm= Realm.getDefaultInstance()
        val list = realm.copyFromRealm(realm.where(ShoppingList::class.java)
                .equalTo("UUID", listId)
                .findFirst())

        realm.close()
        return list
    }


}