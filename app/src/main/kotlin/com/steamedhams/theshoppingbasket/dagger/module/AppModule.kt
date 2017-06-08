package com.steamedhams.theshoppingbasket.dagger.module

import android.app.Application
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

}