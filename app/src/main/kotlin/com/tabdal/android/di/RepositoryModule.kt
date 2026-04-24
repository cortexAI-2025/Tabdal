package com.tabdal.android.di

import com.tabdal.android.data.repository.AuthRepositoryImpl
import com.tabdal.android.data.repository.ListingRepositoryImpl
import com.tabdal.android.data.repository.MessageRepositoryImpl
import com.tabdal.android.domain.repository.AuthRepository
import com.tabdal.android.domain.repository.ListingRepository
import com.tabdal.android.domain.repository.MessageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds @Singleton
    abstract fun bindListingRepository(impl: ListingRepositoryImpl): ListingRepository

    @Binds @Singleton
    abstract fun bindMessageRepository(impl: MessageRepositoryImpl): MessageRepository
}
