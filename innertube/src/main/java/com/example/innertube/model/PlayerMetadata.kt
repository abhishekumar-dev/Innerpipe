package com.example.innertube.model

import com.example.innertube.model.common.Content
import kotlinx.serialization.Serializable

@Serializable
data class PlayerMetadata(
    val contents: Content
) {
    private val results = contents.twoColumnWatchNextResults?.results?.results?.contents
    private val videoPrimaryInfoRenderer = results?.firstOrNull()?.videoPrimaryInfoRenderer
    private val videoSecondaryInfoRenderer = results?.getOrNull(1)?.videoSecondaryInfoRenderer
    val viewCount = videoPrimaryInfoRenderer?.viewCount?.videoViewCountRenderer?.viewCount?.text
    val shortViewCount =
        videoPrimaryInfoRenderer?.viewCount?.videoViewCountRenderer?.shortViewCount?.text
    val datePublished = videoPrimaryInfoRenderer?.dateText?.text
    val relativeDate = videoPrimaryInfoRenderer?.relativeDateText?.text
    val title = videoPrimaryInfoRenderer?.title?.text
    val authorThumbnail = videoSecondaryInfoRenderer?.owner?.videoOwnerRenderer?.thumbnail?.url
    val author = videoSecondaryInfoRenderer?.owner?.videoOwnerRenderer?.title?.text
    val subscribers =
        videoSecondaryInfoRenderer?.owner?.videoOwnerRenderer?.subscriberCountText?.text
    val isVerified =
        videoSecondaryInfoRenderer?.owner?.videoOwnerRenderer?.badges?.any { it.metadataBadgeRenderer.tooltip == "Verified" } == true

    private val secondaryResults =
        contents.twoColumnWatchNextResults?.secondaryResults?.secondaryResults?.results
    val recommendedVideos = secondaryResults?.mapNotNull { it.compactVideoRenderer }
    val tokenForRecommendedVideos = secondaryResults?.lastOrNull()?.continuationItemRenderer?.token

    private val commentsEntryPoint =
        results?.getOrNull(results.lastIndex - 1)?.itemSectionRenderer?.contents?.first()?.commentsEntryPointHeaderRenderer
    val commentCount = commentsEntryPoint?.commentCount?.text?.filter { it.isDigit() }?.toInt() ?: 0
    val teaserAuthorThumbnail = commentsEntryPoint?.authorThumbnail
    val teaserComment = commentsEntryPoint?.comment
    val tokenForComments =
        results?.lastOrNull()?.itemSectionRenderer?.contents?.lastOrNull()?.continuationItemRenderer?.token
}