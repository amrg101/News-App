package com.amrg.newsapp.ui.screens.home

import com.amrg.newsapp.domain.models.Article

data class TopBarState(
    val searchText: String = "",
    val isSearchBarVisible: Boolean = false,
    val isSortMenuVisible: Boolean = false,
    val isSearching: Boolean = false,
    val searchResults: List<Article> = emptyList()
)