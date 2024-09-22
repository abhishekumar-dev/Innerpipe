package com.example.innertube.model.renderers

import com.example.innertube.model.common.Runs
import kotlinx.serialization.Serializable

@Serializable
data class CommentsEntryPointHeaderRenderer(
    val commentCount: Runs?,
    private val contentRenderer: ContentRenderer?
) {
    @Serializable
    data class ContentRenderer(
        val commentsEntryPointTeaserRenderer: CommentsEntryPointTeaserRenderer?
    )

    val authorThumbnail = contentRenderer?.commentsEntryPointTeaserRenderer?.teaserAvatar?.url
    val comment = contentRenderer?.commentsEntryPointTeaserRenderer?.teaserContent?.text
}