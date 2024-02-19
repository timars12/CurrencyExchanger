package com.tim.currencyexchanger.di

import android.content.Context
import com.tim.currencyexchanger.data.db.CurrencyExchangeDatabase
import com.tim.currencyexchanger.utils.NavigationDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): CurrencyExchangeDatabase =
        CurrencyExchangeDatabase.getInstance(context)

    @Singleton
    @Provides
    fun provideNavDispatcher(): NavigationDispatcher = NavigationDispatcher()
}