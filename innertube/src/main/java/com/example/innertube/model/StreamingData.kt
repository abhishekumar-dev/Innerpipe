package com.example.innertube.model

import kotlinx.serialization.Serializable

@Serializable
data class StreamingData (
    val expiresInSeconds: String,
    val hlsManifestUrl: String?,
    val adaptiveFormats: List<Format>?
)