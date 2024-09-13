package com.example.innertube.model.renderers

import com.example.innertube.model.common.Badge
import com.example.innertube.model.common.Runs
import com.example.innertube.model.common.Thumbnail
import com.example.innertube.unixTimestampToDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoRenderer(
    val videoId: String,
    val thumbnail: Thumbnail,
    @SerialName("title")
    private val _title: Runs,
    private val longBylineText: Runs?,
    private val publishedTimeText: Runs?,
    private val lengthText: Runs?,
    private val badges: List<Badge>?,
    private val shortViewCountText: Runs?,
    private val channelThumbnailSupportedRenderers: ChannelThumbnailSupportedRenderers?,
    private val upcomingEventData: UpcomingEventData?
) {
    @Serializable
    data class UpcomingEventData(
        val startTime: String
    ) {
        val label = "Scheduled for ${startTime.toLong().unixTimestampToDateTime()}"
    }
    val title: String
        get() = _title.text
    val author = longBylineText?.text
    val publishedTime = publishedTimeText?.text
    val duration = when {
        lengthText != null -> lengthText.text
        upcomingEventData != null -> "UPCOMING"
        else -> "LIVE"
    }
    val views = shortViewCountText?.text
    val tags = badges?.map { it.metadataBadgeRenderer.label.orEmpty() } ?: emptyList()
    val authorThumbnail = channelThumbnailSupportedRenderers?.url
    val upcomingEvent = upcomingEventData?.label
}