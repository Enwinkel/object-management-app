package com.stasst.objectmanager.di

import android.content.Context
import androidx.room.Room
import com.stasst.objectmanager.data.AppDatabase
import com.stasst.objectmanager.data.ObjectItemDao
import com.stasst.objectmanager.data.RelationshipDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule{
    @Provides
    @Singleton
    fun provideDatabase(appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideObjectDao(database: AppDatabase): ObjectItemDao = database.objectItemDao()

    @Provides
    fun provideRelationshipDao(database: AppDatabase): RelationshipDao = database.relationshipDao()

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }
}