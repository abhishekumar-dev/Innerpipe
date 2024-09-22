package com.example.innertube.model

import kotlinx.serialization.Serializable

@Serializable
data class RYD(
    val id: String,
    val dateCreated: String,
    val likes: Long,
    val dislikes: Long,
    val rating: Double,
    val viewCount: Long,
    val deleted: Boolean
)