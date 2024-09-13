package com.example.innertube.model.common

import kotlinx.serialization.Serializable

@Serializable
data class Thumbnail(
    private val thumbnails: List<Thumbnails>
) {
    @Serializable
    data class Thumbnails(
        val url: String
    )

    val url = thumbnails.last().url.let { if (it.startsWith("//")) "https:$it" else it }
}