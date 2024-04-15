package com.amrg.newsapp.data.mapper

import com.amrg.newsapp.data.remote.models.responses.ArticleApi
import com.amrg.newsapp.data.remote.models.responses.SourceApi
import com.amrg.newsapp.domain.models.Article
import com.amrg.newsapp.domain.models.Source

fun ArticleApi.toArticle() = Article(
    id = 0,
    source = sourceApi.toSource(),
    author = author,
    title = title,
    description = description,
    url = url,
    urlToImage = urlToImage,
    publishedAt = publishedAt,
    content = publishedAt
)

fun SourceApi.toSource() = Source(
    id = id,
    name = name
)