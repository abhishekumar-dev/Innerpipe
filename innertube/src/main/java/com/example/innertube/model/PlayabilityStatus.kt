package com.example.innertube.model

import kotlinx.serialization.Serializable

@Serializable
data class PlayabilityStatus(
    val status: String,
    val reason: String?
)