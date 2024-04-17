package com.amrg.newsapp.shared

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

fun dateFormat(date: String): String {
    val newDate = date.replace('T', '•')
    return newDate.replace('Z', ' ')
}

fun navigateToCustomTab(context: Context, url: String) {
    val uri = Uri.parse(url)
    CustomTabsIntent.Builder().also { builder ->
        builder.setShowTitle(true)
        builder.build().also {
            it.launchUrl(context, uri)
        }
    }
}