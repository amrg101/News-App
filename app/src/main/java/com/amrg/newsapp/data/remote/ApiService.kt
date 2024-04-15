package com.amrg.newsapp.data.remote

import com.amrg.newsapp.data.remote.Constants.DEFAULT_COUNTRY
import com.amrg.newsapp.data.remote.Constants.DEFAULT_PAGE_NUMBER
import com.amrg.newsapp.data.remote.Constants.DEFAULT_PAGE_SIZE
import com.amrg.newsapp.data.remote.Constants.DEFAULT_SEARCH_PAGE_SIZE
import com.amrg.newsapp.data.remote.Constants.GET_NEWS_URL
import com.amrg.newsapp.data.remote.Constants.SEARCH_NEWS_URL
import com.amrg.newsapp.data.remote.models.responses.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(GET_NEWS_URL)
    suspend fun getNews(
        @Query("country") country: String = DEFAULT_COUNTRY,
        @Query("page") page: Int = DEFAULT_PAGE_NUMBER,
        @Query("pageSize") pageSize: Int = DEFAULT_PAGE_SIZE,
    ): NewsResponse

    @GET(SEARCH_NEWS_URL)
    suspend fun searchNews(
        @Query("q") query: String,
        @Query("page") page: Int = DEFAULT_PAGE_NUMBER,
        @Query("pageSize") pageSize: Int = DEFAULT_SEARCH_PAGE_SIZE,
    ): NewsResponse
}