package com.example.innertube.model.renderers

import com.example.innertube.model.common.Badge
import com.example.innertube.model.common.Runs
import com.example.innertube.model.common.Thumbnail
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChannelRenderer(
    val channelId: String,
    @SerialName("title")
    private val _title: Runs,
    val thumbnail: Thumbnail,
    private val videoCountText: Runs?,
    private val subscriberCountText: Runs?,
    private val ownerBadges: List<Badge>?
) {
    val title: String
        get() = _title.text
    val subscribers = videoCountText?.text.orEmpty()
    val handle = subscriberCountText?.text ?: "handle missing"
    val isVerified = ownerBadges?.any { it.metadataBadgeRenderer.tooltip == "Verified" } ?: false
}