package com.amrg.newsapp.data.repository

import com.amrg.newsapp.data.local.ArticleDatabase
import com.amrg.newsapp.data.mapper.toArticle
import com.amrg.newsapp.data.mapper.toArticleEntity
import com.amrg.newsapp.data.remote.ApiService
import com.amrg.newsapp.domain.models.Article
import com.amrg.newsapp.domain.repository.NewsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepositoryImp @Inject constructor(
    private val apiService: ApiService,
    private val database: ArticleDatabase
) : NewsRepository {

    override suspend fun getNews(page: Int): Result<Pair<List<Article>, Int>> {
        val response = apiService.getNews(page = page)
        return if (response.status == "ok" || response.status == "200") {
            val articles = response.articles.map { it.toArticle() }
            Result.success(Pair(articles, response.totalResults))
        } else {
            Result.failure(Exception("Network Error"))
        }
    }


    override suspend fun searchNews(query: String): Result<List<Article>> {
        if (query.isEmpty()) return Result.success(emptyList())

        val response = apiService.searchNews(query = query)
        return if (response.status == "ok" || response.status == "200") {
            val articles = response.articles.map { it.toArticle() }
            Result.success(articles)
        } else {
            Result.failure(Exception("Network Error"))
        }
    }

    override suspend fun getArticles(): List<Article> {
        return database.articleDao().getArticles().map { it.toArticle() }
    }


    override suspend fun saveArticle(article: Article) {
        database.articleDao().upsert(article.toArticleEntity())
    }

    override suspend fun deleteArticle(article: Article) {
        database.articleDao().delete(article.title!!, article.author!!)
    }
}