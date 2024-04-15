package com.amrg.newsapp.ui.navigation

sealed class Screen(val route: String) {
    data object HomeScreen : Screen("/home_screen")
    data object FavScreen : Screen("/fav_screen")
}