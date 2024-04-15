package com.amrg.newsapp.ui.screens.home

import com.amrg.newsapp.data.remote.Constants
import com.amrg.newsapp.domain.models.Article

data class AllArticlesState(
    val isLoading: Boolean = false,
    val items: List<Article> = emptyList(),
    val error: String? = null,
    val endReached: Boolean = false,
    val page: Int = Constants.DEFAULT_PAGE_NUMBER
)