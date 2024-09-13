package com.example.innertube.model.body

import kotlinx.serialization.Serializable

@Serializable
data class Context(
    val client: Client
)