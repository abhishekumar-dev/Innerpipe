package com.example.innertube.model

import com.example.innertube.model.common.Content
import kotlinx.serialization.Serializable

@Serializable
data class Mutation(
    val payload: Payload?
) {
    @Serializable
    data class Payload(
        val commentEntityPayload: CommentEntityPayload?,
        val engagementToolbarStateEntityPayload: EngagementToolbarStateEntityPayload?
    ) {
        @Serializable
        data class CommentEntityPayload(
            val properties: Properties,
            val author: Author,
            val toolbar: Toolbar
        ) {
            @Serializable
            data class Properties(
                val commentId: String,
                val content: Content,
                val publishedTime: String,
                val toolbarStateKey: String
            )

            @Serializable
            data class Author(
                val channelId: String,
                val displayName: String,
                val avatarThumbnailUrl: String,
                val isVerified: Boolean,
                val isCreator: Boolean
            )

            @Serializable
            data class Toolbar(
                val likeCountA11y: String,
                val replyCountA11y: String
            )
        }

        @Serializable
        data class EngagementToolbarStateEntityPayload(
            val key: String,
            val heartState: String
        )
    }
}