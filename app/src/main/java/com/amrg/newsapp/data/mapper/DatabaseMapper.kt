package com.amrg.newsapp.data.mapper

import com.amrg.newsapp.data.local.entity.ArticleEntity
import com.amrg.newsapp.data.local.entity.SourceEntity
import com.amrg.newsapp.domain.models.Article
import com.amrg.newsapp.domain.models.Source


fun ArticleEntity.toArticle() = Article(
    id = id,
    source = sourceEntity.toSource(),
    author = author,
    title = title,
    description = description,
    url = url,
    urlToImage = urlToImage,
    publishedAt = publishedAt,
    content = publishedAt
)

fun Article.toArticleEntity() = ArticleEntity(
    id = id,
    sourceEntity = source.toSourceEntity(),
    author = author,
    title = title,
    description = description,
    url = url,
    urlToImage = urlToImage,
    publishedAt = publishedAt,
    content = publishedAt
)

fun SourceEntity.toSource() = Source(
    id = id,
    name = name
)

fun Source.toSourceEntity() = SourceEntity(
    id = id,
    name = name
)
