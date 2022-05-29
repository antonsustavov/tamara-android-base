package com.tamara.care.watch.di

import android.content.Context
import androidx.room.Room
import com.tamara.care.watch.data.room.TamaraDatabase
import com.tamara.care.watch.data.room.WatchInfoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by ArtemLampa on 25.10.2021.
 */

@InstallIn(SingletonComponent::class)
@Module
object RoomModule {

    @Singleton
    @Provides
    fun provideTamaraDb(@ApplicationContext context: Context): TamaraDatabase {
        return Room
            .databaseBuilder(
                context,
                TamaraDatabase::class.java,
                TamaraDatabase.DATABASE_NAME
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideWatchInfoDao(tamaraDatabase: TamaraDatabase): WatchInfoDao {
        return tamaraDatabase.watchDao()
    }
}