package com.tabdal.android.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.tabdal.android.data.local.TabdalDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "tabdal_prefs")

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TabdalDatabase =
        Room.databaseBuilder(context, TabdalDatabase::class.java, "tabdal.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides fun provideUserDao(db: TabdalDatabase) = db.userDao()
    @Provides fun provideListingDao(db: TabdalDatabase) = db.listingDao()
    @Provides fun provideMessageDao(db: TabdalDatabase) = db.messageDao()

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStore
}
