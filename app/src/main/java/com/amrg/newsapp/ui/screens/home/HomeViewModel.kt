package com.amrg.newsapp.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrg.newsapp.domain.models.Article
import com.amrg.newsapp.domain.repository.NewsRepository
import com.amrg.newsapp.shared.NetworkObserver
import com.amrg.newsapp.shared.dispatcher.DispatcherProvider
import com.amrg.newsapp.ui.common.Paginator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    var allArticlesState by mutableStateOf(AllArticlesState())
    var topBarState by mutableStateOf(TopBarState())

    private var searchJob: Job? = null

    private val pagination = Paginator(initialPage = allArticlesState.page, onLoadUpdated = {
        allArticlesState = allArticlesState.copy(isLoading = it)
    }, onRequest = { nextPage ->
        try {
            newsRepository.getNews(nextPage)
        } catch (exc: Exception) {
            Result.failure(exc)
        }
    }, getNextPage = {
        allArticlesState.page + 1
    }, onError = {
        allArticlesState = allArticlesState.copy(error = it?.localizedMessage ?: "unknown-error")
    }, onSuccess = { articleSet, newPage ->

        @Suppress("SENSELESS_COMPARISON") val articles = if (articleSet.first != null) {
            articleSet.first.filter { !it.title.isNullOrEmpty() && !it.author.isNullOrEmpty() } as ArrayList<Article>
        } else {
            ArrayList()
        }

        allArticlesState = allArticlesState.copy(
            items = (allArticlesState.items + articles),
            page = newPage,
            endReached = articles.isEmpty()
        )
    })

    init {
        loadNextItems()
    }

    fun loadNextItems() {
        viewModelScope.launch {
            pagination.loadNextItems()
        }
    }

    fun reloadItems() {
        pagination.reset()
        allArticlesState = AllArticlesState()
        loadNextItems()
    }

    fun onAction(userAction: UserAction) {
        when (userAction) {
            UserAction.CloseIconClicked -> {
                topBarState = topBarState.copy(isSearchBarVisible = false)
            }

            UserAction.SearchIconClicked -> {
                topBarState = topBarState.copy(isSearchBarVisible = true)
            }

            is UserAction.FavIconAdd -> {
                saveArticle(userAction.article)
            }

            is UserAction.FavIconDelete -> {
                deleteArticle(userAction.article)
            }

            is UserAction.TextFieldInput -> {
                topBarState = topBarState.copy(searchText = userAction.text)
                if (userAction.networkStatus == NetworkObserver.Status.Available) {
                    searchJob?.cancel()
                    searchJob = viewModelScope.launch {
                        if (userAction.text.isNotBlank()) {
                            topBarState = topBarState.copy(isSearching = true)
                        }
                        delay(500L)
                        searchArticles(userAction.text)
                    }
                }
            }
        }
    }

    private suspend fun searchArticles(query: String) {
        val newSet = newsRepository.searchNews(query)
        val articles =
            newSet.getOrNull()!!.filter { !it.title.isNullOrEmpty() && !it.author.isNullOrEmpty() }
        topBarState = topBarState.copy(searchResults = articles, isSearching = false)
    }

    private fun saveArticle(article: Article) {
        viewModelScope.launch(dispatcherProvider.io) {
            newsRepository.saveArticle(article)
        }
    }

    private fun deleteArticle(article: Article) {
        viewModelScope.launch(dispatcherProvider.io) {
            newsRepository.deleteArticle(article)
        }
    }

    fun isArticleFav(article: Article): Boolean {
        var isFav = false
        runBlocking(dispatcherProvider.io) {
            isFav = newsRepository.getArticles()
                .any { it.title == article.title && it.author == article.author }
        }
        return isFav
    }

    /*    fun getFeed() {
            viewModelScope.launch {
                pager.flow.cachedIn(viewModelScope)
                    .map { it.filter { it.title.isNotEmpty() } }
                    .collect {

                    }
            }
        }*/
}