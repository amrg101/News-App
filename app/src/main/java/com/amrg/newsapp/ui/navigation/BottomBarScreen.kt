package com.amrg.newsapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val label: String,
    val icon: ImageVector
) {

    data object Home : BottomBarScreen(
        route = "home",
        label = "Home",
        icon = Icons.Rounded.Home
    )

    data object Settings : BottomBarScreen(
        route = "favorite",
        label = "Favorite",
        icon = Icons.Rounded.Favorite
    )
}