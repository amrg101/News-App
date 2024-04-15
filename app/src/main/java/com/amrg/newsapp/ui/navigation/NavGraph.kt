package com.amrg.newsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.amrg.newsapp.shared.NetworkObserver
import com.amrg.newsapp.ui.screens.fav.FavScreen
import com.amrg.newsapp.ui.screens.home.HomeScreen

@Composable
fun NavGraph(
    startDestination: String,
    navController: NavHostController,
    networkStatus: NetworkObserver.Status
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(
            route = BottomBarScreen.Home.route,
            exitTransition = { exitTransition() },
            popEnterTransition = { popEnterTransition() },
        ) {
            HomeScreen(navController = navController, networkStatus)
        }

        composable(
            route = BottomBarScreen.Settings.route,
            exitTransition = { exitTransition() },
            popEnterTransition = { popEnterTransition() },
        ) {
            FavScreen(navController = navController)
        }
    }
}