package com.example.innertube.model

import com.example.innertube.model.common.Thumbnail
import kotlinx.serialization.Serializable

@Serializable
data class VideoDetails(
    val videoId: String,
    val title: String,
    val author: String,
    val channelId: String,
    val viewCount: String,
    val isCrawlable: Boolean,
    val isLiveContent: Boolean,
    val isPrivate: Boolean,
    val lengthSeconds: String,
    val shortDescription: String,
    val thumbnail: Thumbnail
)