package com.amrg.newsapp.di

import com.amrg.newsapp.data.local.ArticleDatabase
import com.amrg.newsapp.data.remote.ApiService
import com.amrg.newsapp.data.repository.NewsRepositoryImp
import com.amrg.newsapp.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideNewsRepository(api: ApiService, database: ArticleDatabase): NewsRepository =
        NewsRepositoryImp(api, database)
}
