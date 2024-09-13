package com.example.innertube.model.body

import kotlinx.serialization.Serializable

@Serializable
data class NextBody(
    val context: Context,
    val videoId: String? = null,
    val continuation: String? = null
)