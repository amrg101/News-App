package com.amrg.newsapp.ui.common

class Paginator<Page, ArticleSet>(
    private val initialPage: Page,
    private inline val onLoadUpdated: (Boolean) -> Unit,
    private inline val onRequest: suspend (nextPage: Page) -> Result<ArticleSet>,
    private inline val getNextPage: suspend (ArticleSet) -> Page,
    private inline val onError: suspend (Throwable?) -> Unit,
    private inline val onSuccess: suspend (item: ArticleSet, newPage: Page) -> Unit
) {

    private var currentPage = initialPage
    private var isMakingRequest = false

    suspend fun loadNextItems() {
        if (isMakingRequest) {
            return
        }
        isMakingRequest = true
        onLoadUpdated(true)
        val result = onRequest(currentPage)
        isMakingRequest = false
        val articleSet = result.getOrElse {
            onError(it)
            onLoadUpdated(false)
            return
        }
        currentPage = getNextPage(articleSet)
        onSuccess(articleSet, currentPage)
        onLoadUpdated(false)
    }

    fun reset() {
        currentPage = initialPage
    }
}