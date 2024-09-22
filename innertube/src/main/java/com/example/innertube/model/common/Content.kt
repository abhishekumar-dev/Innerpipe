package com.example.innertube.model.common

import com.example.innertube.model.renderers.*
import com.example.innertube.model.TwoColumnWatchNextResults
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
    val playlistRenderer: PlaylistRenderer?,
    val twoColumnBrowseResultsRenderer: TwoColumnBrowseResultsRenderer?,
    val richItemRenderer: RichItemRenderer?,
    val richGridRenderer: RichGridRenderer?,
    val chipCloudChipRenderer: ChipCloudChipRenderer?,
    val feedFilterChipBarRenderer: FeedFilterChipBarRenderer?,
    val twoColumnWatchNextResults: TwoColumnWatchNextResults?,
    val videoPrimaryInfoRenderer: VideoPrimaryInfoRenderer?,
    val videoSecondaryInfoRenderer: VideoSecondaryInfoRenderer?,
    val commentsEntryPointHeaderRenderer: CommentsEntryPointHeaderRenderer?,
    val content: String?,
    val commentThreadRenderer: CommentThreadRenderer?
)