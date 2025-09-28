package com.rksrtx76.truebalance.di

import android.app.Application
import androidx.room.Room
import com.rksrtx76.truebalance.data.local.TrueBalanceDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideTrueBalanceDatabase(application: Application) : TrueBalanceDatabase {
        return Room.databaseBuilder(
            application,
            TrueBalanceDatabase::class.java,
            "true_balance_database"
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }
}