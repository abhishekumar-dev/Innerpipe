package com.example.innerpipe

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.innerpipe.ui.channel.ChannelScreen
import com.example.innerpipe.ui.channel.ChannelViewModel
import com.example.innerpipe.ui.player.PlayerScreen
import com.example.innerpipe.ui.player.PlayerViewModel
import com.example.innerpipe.ui.search.SearchScreen
import com.example.innerpipe.ui.search.SearchViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

object Screen {
    @Serializable
    object Search

    @Serializable
    data class Player(val id: String)

    @Serializable
    data class Channel(val id: String)
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Search) {
        composable<Screen.Search> {
            val searchViewModel = koinViewModel<SearchViewModel>()
            SearchScreen(searchViewModel, navController)
        }
        composable<Screen.Player> { backStackEntry ->
            val player = backStackEntry.toRoute<Screen.Player>()
            val playerViewModel = koinViewModel<PlayerViewModel>()
            PlayerScreen(player.id, playerViewModel, navController)
        }
        composable<Screen.Channel> { backStackEntry ->
            val channel = backStackEntry.toRoute<Screen.Channel>()
            val channelViewModel = koinViewModel<ChannelViewModel>()
            ChannelScreen(channel.id, navController, channelViewModel)
        }
    }
}