package com.example.innertube.model.body

import kotlinx.serialization.Serializable

@Serializable
data class SearchBody(
    val context: Context,
    val query: String? = null,
    val continuation: String? = null
)