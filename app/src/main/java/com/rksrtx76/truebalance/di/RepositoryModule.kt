package com.rksrtx76.truebalance.di

import com.rksrtx76.truebalance.data.repository.TransactionRepositoryImpl
import com.rksrtx76.truebalance.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindTransactionRepository(
        transactionRepositoryImpl: TransactionRepositoryImpl
    ) : TransactionRepository
}