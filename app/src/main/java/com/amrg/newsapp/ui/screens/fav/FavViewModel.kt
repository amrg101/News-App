package com.amrg.newsapp.ui.screens.fav

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrg.newsapp.domain.models.Article
import com.amrg.newsapp.domain.repository.NewsRepository
import com.amrg.newsapp.shared.dispatcher.DispatcherProvider
import com.amrg.newsapp.ui.screens.home.UserAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _savedArticles = mutableStateListOf<Article>()
    val savedArticles: List<Article> = _savedArticles

    fun onAction(userAction: UserAction) {
        if (userAction is UserAction.FavIconDelete) {
            deleteArticle(userAction.article)
            _savedArticles.remove(userAction.article)
        }
    }

    fun loadSavedArticles() {
        viewModelScope.launch(dispatcherProvider.io) {
            _savedArticles.clear()
            _savedArticles.addAll(newsRepository.getArticles())
        }
    }

    private fun deleteArticle(article: Article) {
        viewModelScope.launch(dispatcherProvider.io) {
            newsRepository.deleteArticle(article)
        }
    }
}