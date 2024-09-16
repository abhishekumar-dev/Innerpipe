package com.example.innertube.model

import com.example.innertube.model.common.Content
import kotlinx.serialization.Serializable

@Serializable
data class ReloadContinuationItemsCommand(
    val continuationItems: List<Content>
)