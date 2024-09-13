package com.example.innertube.model

import com.example.innertube.model.common.Content
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class Search(
    private val estimatedResults: String,
    val contents: Content?,
    val onResponseReceivedCommands: List<OnResponseReceivedCommands>?
) {
    val itemsCount = estimatedResults.filter { it.isDigit() }.toLong()
    private val sectionListRenderer =
        contents?.twoColumnSearchResultsRenderer?.primaryContents?.sectionListRenderer?.contents
            ?: onResponseReceivedCommands?.firstOrNull()?.appendContinuationItemsAction?.continuationItems
    val items: List<@Contextual Any> =
        sectionListRenderer?.filter { it.itemSectionRenderer?.contents?.isNotEmpty() == true }
            ?.mapNotNull {
                it.itemSectionRenderer?.contents?.mapNotNull { item ->
                    when {
                        item.shelfRenderer != null -> {
                            Shelf(
                                title = item.shelfRenderer.title.text,
                                videos = item.shelfRenderer.content.verticalListRenderer?.items?.mapNotNull { it.videoRenderer }
                                    ?: item.shelfRenderer.content.horizontalListRenderer?.items?.mapNotNull { it.gridVideoRenderer }
                                    ?: emptyList()
                            )
                        }

                        else -> item.channelRenderer ?: item.videoRenderer ?: item.playlistRenderer
                    }
                }
            }?.flatten().orEmpty()
    val token = sectionListRenderer?.lastOrNull()?.continuationItemRenderer?.token
}