package com.example.innerpipe

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.innerpipe.ui.search.SearchScreen
import com.example.innerpipe.ui.search.SearchViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

object Screen {
    @Serializable
    object Search
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Search) {
        composable<Screen.Search> {
            val searchViewModel = koinViewModel<SearchViewModel>()
            SearchScreen(searchViewModel)
        }
    }
}