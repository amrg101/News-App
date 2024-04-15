package com.amrg.newsapp.data.remote.models.responses

import com.google.gson.annotations.SerializedName

data class ArticleApi(
    @SerializedName("source")
    val sourceApi: SourceApi,

    @SerializedName("author")
    val author: String?,

    @SerializedName("title")
    val title: String?,

    @SerializedName("description")
    val description: String?,

    @SerializedName("url")
    val url: String?,

    @SerializedName("urlToImage")
    val urlToImage: String?,

    @SerializedName("publishedAt")
    val publishedAt: String?,

    @SerializedName("content")
    val content: String?
)
