package com.example.innertube.model.renderers

import kotlinx.serialization.Serializable

@Serializable
data class VideoSecondaryInfoRenderer(
    val owner: Owner
) {
    @Serializable
    data class Owner(
        val videoOwnerRenderer: VideoOwnerRenderer
    )
}