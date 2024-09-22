package com.example.innertube.model.renderers

import com.example.innertube.model.common.Runs
import com.example.innertube.model.common.Thumbnail
import kotlinx.serialization.Serializable

@Serializable
data class CommentsEntryPointTeaserRenderer(
    val teaserAvatar: Thumbnail,
    val teaserContent: Runs
)