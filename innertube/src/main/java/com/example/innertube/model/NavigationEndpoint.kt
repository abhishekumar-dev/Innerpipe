package com.example.innertube.model

import kotlinx.serialization.Serializable

@Serializable
data class NavigationEndpoint(
    val browseEndpoint: BrowseEndpoint
) {
    @Serializable
    data class BrowseEndpoint(
        val browseId: String
    )
}