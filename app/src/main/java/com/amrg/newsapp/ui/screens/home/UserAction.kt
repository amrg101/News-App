package com.amrg.newsapp.ui.screens.home

import com.amrg.newsapp.domain.models.Article
import com.amrg.newsapp.shared.NetworkObserver

sealed class UserAction {
    data object SearchIconClicked : UserAction()
    data object CloseIconClicked : UserAction()
    data class FavIconAdd(val article: Article) : UserAction()
    data class FavIconDelete(val article: Article) : UserAction()
    data class TextFieldInput(
        val text: String,
        val networkStatus: NetworkObserver.Status
    ) : UserAction()
}