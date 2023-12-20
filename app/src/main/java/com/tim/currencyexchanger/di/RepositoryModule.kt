package com.tim.currencyexchanger.di

import com.tim.currencyexchanger.domain.repository.CurrencyExchangeRepository
import com.tim.currencyexchanger.domain.repository.CurrencyExchangeRepositoryImpl
import com.tim.currencyexchanger.domain.repository.MyBalanceRepository
import com.tim.currencyexchanger.domain.repository.MyBalanceRepositoryImpl
import com.tim.currencyexchanger.domain.repository.TransactionRepository
import com.tim.currencyexchanger.domain.repository.TransactionRepositoryImpl
import com.tim.currencyexchanger.domain.rules.ExchangeTransactionRule
import com.tim.currencyexchanger.domain.rules.TransactionRule
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.multibindings.IntoSet

@InstallIn(ViewModelComponent::class)
@Module
interface RepositoryModule {
    @Binds
    @ViewModelScoped
    fun bindRepository(repository: CurrencyExchangeRepositoryImpl): CurrencyExchangeRepository

    @Binds
    @ViewModelScoped
    fun bindBalanceRepository(repository: MyBalanceRepositoryImpl): MyBalanceRepository

    @Binds
    @ViewModelScoped
    fun bindTransactionRepository(repository: TransactionRepositoryImpl): TransactionRepository

    @Binds
    @IntoSet
    fun bindExchangeTransactionRule(transactionRule: ExchangeTransactionRule): TransactionRule
}