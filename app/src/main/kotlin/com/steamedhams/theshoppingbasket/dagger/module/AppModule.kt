package com.steamedhams.theshoppingbasket.dagger.module

import android.app.Application
import android.arch.persistence.room.Room
import com.steamedhams.theshoppingbasket.room.BasketDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Module class to provide dependencies globally scoped across the application
 * <p>
 * Created by richard on 16/02/17.
 */
@Module
class AppModule(val application: Application) {

    @Provides
    @Singleton
    fun providesApplication(): Application {
        return application
    }

    @Provides
    @Singleton
    fun providesBasketDatabase(): BasketDatabase {
        return Room.databaseBuilder(application, BasketDatabase::class.java, "basket_db")
                .build()
    }

}