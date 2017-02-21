package com.steamedhams.theshoppingbasket

import android.app.Application
import com.steamedhams.theshoppingbasket.dagger.component.DaggerNetComponent
import com.steamedhams.theshoppingbasket.dagger.component.NetComponent
import com.steamedhams.theshoppingbasket.dagger.module.AppModule
import io.realm.Realm
import io.realm.RealmConfiguration


/**
 * Created by richard on 13/02/17.
 */
class ShoppingBasket : Application() {

    lateinit var appModule : AppModule


    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val config = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(config)

        setUpDagger()
    }

    private fun setUpDagger() {
        appModule = AppModule(this)

        netComponent = DaggerNetComponent.builder().build()
    }

    companion object {
        lateinit var netComponent : NetComponent
    }

}