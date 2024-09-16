package com.example.innertube.model

import kotlinx.serialization.Serializable

@Serializable
data class OnResponseReceivedActions(
    val appendContinuationItemsAction: AppendContinuationItemsAction?,
    val reloadContinuationItemsCommand: ReloadContinuationItemsCommand?
)