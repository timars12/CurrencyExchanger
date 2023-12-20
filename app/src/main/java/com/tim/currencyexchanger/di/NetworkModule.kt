package com.tim.currencyexchanger.di

import com.squareup.moshi.Moshi
import com.tim.currencyexchanger.domain.network.CurrencyExchangeApiService
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(timeout = TIMEOUT_30, TimeUnit.SECONDS)
            writeTimeout(timeout = TIMEOUT_30, TimeUnit.SECONDS)
            readTimeout(timeout = TIMEOUT_30, TimeUnit.SECONDS)
        }.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: Lazy<OkHttpClient>): Retrofit {
        val moshi = Moshi.Builder().build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
//            .callFactory { request -> okHttpClient.get().newCall(request) }
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Singleton
    @Provides
    fun provideNewAuthCalls(retrofit: Retrofit): CurrencyExchangeApiService = retrofit.create(
        CurrencyExchangeApiService::class.java)

    companion object {
        private const val BASE_URL: String = "https://developers.paysera.com/tasks/api/"
        private const val TIMEOUT_30 = 30L
    }
}