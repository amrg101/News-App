package com.amrg.newsapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.amrg.newsapp.shared.NetworkObserver
import com.amrg.newsapp.ui.navigation.BottomBar
import com.amrg.newsapp.ui.navigation.BottomBarScreen
import com.amrg.newsapp.ui.navigation.NavGraph
import com.amrg.newsapp.ui.theme.NewsAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var networkObserver: NetworkObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NewsAppTheme {
                val navController = rememberNavController()
                val status by networkObserver.observe().collectAsState(
                    initial = NetworkObserver.Status.Unavailable
                )
                Scaffold(
                    bottomBar = {
                        BottomBar(navController = navController)
                    }, containerColor = MaterialTheme.colorScheme.background
                ) {
                    Modifier.padding(it)
                    NavGraph(
                        startDestination = BottomBarScreen.Home.route,
                        navController = navController,
                        status
                    )
                }
            }
        }
    }
}

