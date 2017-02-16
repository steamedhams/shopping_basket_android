package com.steamedhams.theshoppingbasket.dagger.module

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Module class to provide Network Related dependencies
 * <p>
 * Created by richard on 16/02/17.
 */
@Module
class NetModule {

    @Provides
    @Singleton
    internal fun providesSharedPreferences(application: Application): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(application)
    }

}