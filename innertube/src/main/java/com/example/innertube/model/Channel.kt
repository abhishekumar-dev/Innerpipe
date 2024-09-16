package com.example.innertube.model

import com.example.innertube.model.common.Content
import kotlinx.serialization.Serializable

@Serializable
data class Channel(
    val header: Header?,
    val contents: Content?,
    val onResponseReceivedActions: List<OnResponseReceivedActions>?,
    val metadata: Metadata?
) {
    private val content = contents?.twoColumnBrowseResultsRenderer?.tabs?.get(1)?.tabRenderer?.content?.richGridRenderer?.contents
        ?: onResponseReceivedActions?.lastOrNull()?.reloadContinuationItemsCommand?.continuationItems
        ?: onResponseReceivedActions?.firstOrNull()?.appendContinuationItemsAction?.continuationItems
    val videos = content?.mapNotNull { it.richItemRenderer?.content?.videoRenderer }
    val token = content?.lastOrNull()?.continuationItemRenderer?.token

    private val filterContent = contents?.twoColumnBrowseResultsRenderer?.tabs?.get(1)?.tabRenderer?.content?.richGridRenderer?.header?.feedFilterChipBarRenderer?.contents
        ?: onResponseReceivedActions?.firstOrNull()?.reloadContinuationItemsCommand?.continuationItems?.firstOrNull()?.feedFilterChipBarRenderer?.contents
    val filters = filterContent?.map {
        Filter(
            title = it.chipCloudChipRenderer?.text?.text,
            token = it.chipCloudChipRenderer?.navigationEndpoint?.continuationCommand?.token
        )
    }

    private val pageHeaderViewModel = header?.pageHeaderRenderer?.content?.pageHeaderViewModel
    val banner = pageHeaderViewModel?.banner?.imageBannerViewModel?.image?.sources?.firstOrNull()?.url
    private val metadataRows = pageHeaderViewModel?.metadata?.contentMetadataViewModel?.metadataRows
    val handle = metadataRows?.firstOrNull()?.metadataParts?.joinToString { it.text.content }
    val subscribers = metadataRows?.lastOrNull()?.metadataParts?.firstOrNull()?.text?.content
    val videosCount = metadataRows?.lastOrNull()?.metadataParts?.lastOrNull()?.text?.content

    val channelMetadataRenderer = metadata?.channelMetadataRenderer
    val title = channelMetadataRenderer?.title
    val description = channelMetadataRenderer?.description
    val avatar = channelMetadataRenderer?.avatar?.url
}