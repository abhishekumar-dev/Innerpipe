package com.example.innertube.model.renderers

import com.example.innertube.model.common.Badge
import com.example.innertube.model.common.Runs
import com.example.innertube.model.common.Thumbnail
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaylistRenderer(
    val playlistId: String,
    @SerialName("title")
    private val _title: Runs,
    val thumbnails: List<Thumbnail>,
    val videoCount: String,
    private val ownerBadges: List<Badge>?,
    private val publishedTimeText: Runs?,
    private val longBylineText: Runs,
    private val viewPlaylistText: Runs
) {
    val title: String
        get() = _title.text
    val isVerified = ownerBadges?.any { it.metadataBadgeRenderer.tooltip == "Verified" } == true
    val publishedTime = publishedTimeText?.text
    val author = longBylineText.first
    val browseId = viewPlaylistText.browseId
}