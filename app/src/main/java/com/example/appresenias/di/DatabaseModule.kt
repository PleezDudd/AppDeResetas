package com.example.appresenias.di

import android.content.Context
import androidx.room.Room
import com.example.appresenias.data.local.AppDatabase
import com.example.appresenias.data.local.ResenaDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "reseñas_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideResenaDao(appDatabase: AppDatabase): ResenaDao {
        return appDatabase.resenaDao()
    }
}