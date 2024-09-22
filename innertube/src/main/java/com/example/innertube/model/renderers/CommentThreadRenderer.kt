package com.example.innertube.model.renderers

import com.example.innertube.model.common.Content
import com.example.innertube.model.common.Thumbnail
import kotlinx.serialization.Serializable

@Serializable
data class CommentThreadRenderer(
    private val replies: Replies?,
    private val commentViewModel: CommentViewModel?
) {
    @Serializable
    data class Replies(
        val commentRepliesRenderer: CommentRepliesRenderer
    ) {
        @Serializable
        data class CommentRepliesRenderer(
            val contents: List<Content>,
            val targetId: String,
            val viewRepliesCreatorThumbnail: Thumbnail?
        )
    }

    @Serializable
    data class CommentViewModel(
        val commentViewModel: CommentViewModelX
    ) {
        @Serializable
        data class CommentViewModelX(
            val commentKey: String,
            val commentId: String,
            val pinnedText: String?
        )
    }

    val commentId = replies?.commentRepliesRenderer?.targetId?.removePrefix("comment-replies-item-")
    val isPinned = commentViewModel?.commentViewModel?.pinnedText != null
    val isRepliedByCreator = replies?.commentRepliesRenderer?.viewRepliesCreatorThumbnail != null
    val replyToken = replies?.commentRepliesRenderer?.contents?.firstOrNull()?.continuationItemRenderer?.token
}