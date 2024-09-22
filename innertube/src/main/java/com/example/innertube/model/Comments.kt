package com.example.innertube.model

import kotlinx.serialization.Serializable

@Serializable
data class Comments(
    val frameworkUpdates: FrameworkUpdates,
    val onResponseReceivedEndpoints: List<OnResponseReceivedEndpoints>
) {
    private val mutations = frameworkUpdates.entityBatchUpdate.mutations
    private val commentsList = mutations.mapNotNull { it.payload?.commentEntityPayload }
    private val engagementList = mutations.mapNotNull { it.payload?.engagementToolbarStateEntityPayload }.associateBy { it.key }
    private val tokenListForReplies =
        onResponseReceivedEndpoints.lastOrNull()?.reloadContinuationItemsCommand?.continuationItems?.map { it.commentThreadRenderer }?.associateBy { it?.commentId }
    val token =
        onResponseReceivedEndpoints.getOrNull(1)?.reloadContinuationItemsCommand?.continuationItems?.lastOrNull()?.continuationItemRenderer?.token
            ?: onResponseReceivedEndpoints.firstOrNull()?.appendContinuationItemsAction?.continuationItems?.lastOrNull()?.continuationItemRenderer?.token

    val comments = commentsList.associateBy { it.properties.commentId }.map {
        Comment(
            commentId = it.key,
            author = it.value.author.displayName,
            thumbnail = it.value.author.avatarThumbnailUrl,
            comment = it.value.properties.content.content ?: "error while getting this comment",
            publishedTime = it.value.properties.publishedTime,
            like = it.value.toolbar.likeCountA11y,
            reply = it.value.toolbar.replyCountA11y,
            isHearted = engagementList[it.value.properties.toolbarStateKey]?.heartState == "TOOLBAR_HEART_STATE_HEARTED",
            isPinned = tokenListForReplies?.get(it.key)?.isPinned == true,
            isVerified = it.value.author.isVerified,
            isCreator = it.value.author.isCreator,
            isRepliedByCreator = tokenListForReplies?.get(it.key)?.isRepliedByCreator == true,
            channelId = it.value.author.channelId,
            tokenForReply = tokenListForReplies?.get(it.key)?.replyToken,
        )
    }
}