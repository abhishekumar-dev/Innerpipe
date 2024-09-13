package com.example.innertube.model.common

import com.example.innertube.model.renderers.ChannelRenderer
import com.example.innertube.model.renderers.ContinuationItemRenderer
import com.example.innertube.model.renderers.HorizontalListRenderer
import com.example.innertube.model.renderers.ItemSectionRenderer
import com.example.innertube.model.renderers.PlaylistRenderer
import com.example.innertube.model.renderers.ShelfRenderer
import com.example.innertube.model.renderers.TwoColumnSearchResultsRenderer
import com.example.innertube.model.renderers.VerticalListRenderer
import com.example.innertube.model.renderers.VideoRenderer
import kotlinx.serialization.Serializable

@Serializable
data class Content(
    val twoColumnSearchResultsRenderer: TwoColumnSearchResultsRenderer?,
    val itemSectionRenderer: ItemSectionRenderer?,
    val videoRenderer: VideoRenderer?,
    val channelRenderer: ChannelRenderer?,
    val shelfRenderer: ShelfRenderer?,
    val verticalListRenderer: VerticalListRenderer?,
    val horizontalListRenderer: HorizontalListRenderer?,
    val gridVideoRenderer: VideoRenderer?,
    val continuationItemRenderer: ContinuationItemRenderer?,
    val playlistRenderer: PlaylistRenderer?
)