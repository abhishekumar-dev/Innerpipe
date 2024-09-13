package com.example.innertube.model.renderers

import kotlinx.serialization.Serializable

@Serializable
data class ContinuationItemRenderer(
    val continuationEndpoint: ContinuationEndpoint?
) {
    @Serializable
    data class ContinuationEndpoint(
        val continuationCommand: ContinuationCommand?
    ) {
        @Serializable
        data class ContinuationCommand(
            val token: String
        )
    }

    val token = continuationEndpoint?.continuationCommand?.token
}