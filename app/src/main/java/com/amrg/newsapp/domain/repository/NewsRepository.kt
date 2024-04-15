package com.amrg.newsapp.domain.repository

import com.amrg.newsapp.domain.models.Article

interface NewsRepository {

    suspend fun getNews(page: Int): Result<Pair<List<Article>, Int>>

    suspend fun searchNews(query: String): Result<List<Article>>

    suspend fun getArticles(): List<Article>

    suspend fun saveArticle(article: Article)

    suspend fun deleteArticle(article: Article)
}