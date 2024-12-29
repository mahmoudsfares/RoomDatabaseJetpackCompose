package com.example.roomdatabasejetpackcompose.di

import android.app.Application
import androidx.room.Room
import com.example.roomdatabasejetpackcompose.data_source.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // TODO 6: inject app database singleton
    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase =
        Room.databaseBuilder(app, AppDatabase::class.java, "my_db")
            .fallbackToDestructiveMigration()
            .build()
}