package com.example.innertube.model

import kotlinx.serialization.Serializable

@Serializable
data class Filter(
    val title: String?,
    val token: String?
)