package com.amrg.newsapp.shared

fun dateFormat(date: String): String {
    val newDate = date.replace('T', '•')
    return newDate.replace('Z', ' ')
}