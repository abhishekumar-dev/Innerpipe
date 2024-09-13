package com.example.innertube.model.renderers

import kotlinx.serialization.Serializable

@Serializable
data class TwoColumnSearchResultsRenderer(
    val primaryContents: PrimaryContents
) {
    @Serializable
    data class PrimaryContents(
        val sectionListRenderer: SectionListRenderer
    )
}