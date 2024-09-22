package com.example.innertube.model

import kotlinx.serialization.Serializable

@Serializable
data class OnResponseReceivedEndpoints(
    val reloadContinuationItemsCommand: ReloadContinuationItemsCommand?,
    val appendContinuationItemsAction: AppendContinuationItemsAction?
)