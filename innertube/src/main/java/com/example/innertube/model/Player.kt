package com.example.innertube.model

import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val playabilityStatus: PlayabilityStatus,
    val videoDetails: VideoDetails?,
    val streamingData: StreamingData?
)