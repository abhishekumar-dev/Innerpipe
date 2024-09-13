package com.example.innertube.model.body

import kotlinx.serialization.Serializable

@Serializable
data class BrowseBody(
    val context: Context,
    val browseId: String? = null,
    val continuation: String? = null,
    val params: String? = null
)