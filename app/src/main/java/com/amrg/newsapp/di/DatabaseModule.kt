package com.amrg.newsapp.di

import android.content.Context
import androidx.room.Room
import com.amrg.newsapp.data.local.ArticleDatabase
import com.amrg.newsapp.data.local.dao.ArticleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

const val DATABASE_NAME = "articles.db"

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideArticleDao(articleDatabase: ArticleDatabase): ArticleDao {
        return articleDatabase.articleDao()
    }

    @Singleton
    @Provides
    fun provideDatabaseService(
        @ApplicationContext application: Context,
    ): ArticleDatabase {

        return Room
            .databaseBuilder(application, ArticleDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}