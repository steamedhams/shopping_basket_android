package com.steamedhams.theshoppingbasket

import android.app.Application
import com.steamedhams.theshoppingbasket.data.realm.RealmDelegate
import io.realm.Realm
import io.realm.RealmConfiguration



/**
 * Created by richard on 13/02/17.
 */
class ShoppingBasket : Application() {

    companion object {
        lateinit var realmDelegate: RealmDelegate
    }

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build()
        realmDelegate = RealmDelegate()
    }

}