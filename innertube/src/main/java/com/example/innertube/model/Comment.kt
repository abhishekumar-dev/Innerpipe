package com.example.innertube.model

import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val commentId: String,
    val author: String,
    val thumbnail: String,
    val comment: String,
    val publishedTime: String,
    val like: String,
    val reply: String,
    val isHearted: Boolean,
    val isPinned: Boolean,
    val isVerified: Boolean,
    val isCreator: Boolean,
    val isRepliedByCreator: Boolean,
    val channelId: String,
    val tokenForReply: String?
)