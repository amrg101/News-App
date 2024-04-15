package com.amrg.newsapp.di

import android.content.Context
import com.amrg.newsapp.shared.NetworkObserver
import com.amrg.newsapp.shared.dispatcher.DefaultDispatcherProvider
import com.amrg.newsapp.shared.dispatcher.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideDispatcher(): DispatcherProvider = DefaultDispatcherProvider()

    @Provides
    @Singleton
    fun provideNetworkObserver(@ApplicationContext context: Context) = NetworkObserver(context)
}