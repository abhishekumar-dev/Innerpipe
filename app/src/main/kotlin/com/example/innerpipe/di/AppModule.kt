package com.example.innerpipe.di

import com.example.innerpipe.ui.channel.ChannelViewModel
import com.example.innerpipe.ui.player.PlayerViewModel
import com.example.innerpipe.ui.search.SearchViewModel
import com.example.innertube.Innertube
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single {
        Innertube()
    }
    viewModelOf(::SearchViewModel)
    viewModelOf(::PlayerViewModel)
    viewModelOf(::ChannelViewModel)
}