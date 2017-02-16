package com.steamedhams.theshoppingbasket.dagger.component

import com.steamedhams.theshoppingbasket.dagger.module.AppModule
import com.steamedhams.theshoppingbasket.dagger.module.NetModule
import com.steamedhams.theshoppingbasket.shoppinglist.ShoppingListAdapter
import dagger.Component
import javax.inject.Singleton

/**
 * Created by richard on 16/02/17.
 */
@Singleton
@Component(modules= arrayOf(AppModule::class, NetModule::class))
interface NetComponent {

    fun inject(shoppingListAdapter: ShoppingListAdapter)

}
