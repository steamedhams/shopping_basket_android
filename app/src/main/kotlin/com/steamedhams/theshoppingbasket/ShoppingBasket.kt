package com.steamedhams.theshoppingbasket

import android.app.Application
import android.arch.persistence.room.Room
import com.steamedhams.theshoppingbasket.dagger.component.DaggerNetComponent
import com.steamedhams.theshoppingbasket.dagger.component.NetComponent
import com.steamedhams.theshoppingbasket.dagger.module.AppModule
import com.steamedhams.theshoppingbasket.room.BasketDatabase

/**
 * Created by richard on 13/02/17.
 */
class ShoppingBasket : Application() {

    lateinit var appModule : AppModule

    companion object {
        lateinit var netComponent : NetComponent
    }

    override fun onCreate() {
        super.onCreate()
        setUpDagger()
    }

    private fun setUpDagger() {
        appModule = AppModule(this)

        netComponent = DaggerNetComponent.builder().build()

    }

}