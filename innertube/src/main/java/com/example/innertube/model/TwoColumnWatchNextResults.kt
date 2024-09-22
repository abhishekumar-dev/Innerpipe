package com.example.innertube.model

import com.example.innertube.model.common.Content
import com.example.innertube.model.renderers.CompactVideoRenderer
import kotlinx.serialization.Serializable

@Serializable
data class TwoColumnWatchNextResults(
    val results: Results,
    val secondaryResults: SecondaryResults
) {
    @Serializable
    data class Results(
        val results: ResultsX
    ) {
        @Serializable
        data class ResultsX(
            val contents: List<Content>
        )
    }

    @Serializable
    data class SecondaryResults(
        val secondaryResults: SecondaryResultsX
    ) {
        @Serializable
        data class SecondaryResultsX(
            val results: List<CompactVideoRenderer>
        )
    }
}