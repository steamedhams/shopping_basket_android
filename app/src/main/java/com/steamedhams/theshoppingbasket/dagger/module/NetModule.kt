package com.steamedhams.theshoppingbasket.dagger.module

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.squareup.moshi.Moshi
import com.steamedhams.theshoppingbasket.api.ApiListService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
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

    @Provides
    @Singleton
    internal fun providesMoshi() : Moshi {
        return Moshi.Builder().build()
    }

    @Provides
    @Singleton
    internal fun providesOkHttpClient() : OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
    }

    @Provides
    @Singleton
    internal fun providesRetrofit(moshi: Moshi, okHttpClient: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("https://sk0qa5g6ze.execute-api.eu-west-1.amazonaws.com")
                .client(okHttpClient)
                .build()
    }

    @Provides
    @Singleton
    internal fun providesListService(retrofit: Retrofit) : ApiListService {
        return retrofit.create(ApiListService::class.java)
    }

}