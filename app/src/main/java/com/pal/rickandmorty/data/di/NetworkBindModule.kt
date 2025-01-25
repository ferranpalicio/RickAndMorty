package com.pal.rickandmorty.data.di

import com.pal.rickandmorty.data.network.NetworkDataSource
import com.pal.rickandmorty.data.network.NetworkDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkBindModule {

    @Binds
    @Singleton
    abstract fun bindNetworkDataSource(networkDataSource: NetworkDataSourceImpl): NetworkDataSource
}