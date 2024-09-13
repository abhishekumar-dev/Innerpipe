package com.example.innertube.model

import kotlinx.serialization.Serializable

@Serializable
data class OnResponseReceivedCommands(
    val appendContinuationItemsAction: AppendContinuationItemsAction?
)