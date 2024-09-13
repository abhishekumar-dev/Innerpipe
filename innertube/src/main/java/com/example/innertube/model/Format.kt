package com.example.innertube.model

import kotlinx.serialization.Serializable

@Serializable
data class Format(
    val itag: Int,
    val url: String,
    val bitrate: Int,
    val width: Int?,
    val height: Int?,
    val lastModified: String,
    val contentLength: String,
    val quality: String,
    val fps: Int?,
    val qualityLabel: String?,
    val projectionType: String,
    val approxDurationMs: String
)