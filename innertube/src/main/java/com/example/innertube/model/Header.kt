package com.example.innertube.model

import kotlinx.serialization.Serializable

@Serializable
data class Header(
    val pageHeaderRenderer: PageHeaderRenderer
) {
    @Serializable
    data class PageHeaderRenderer(
        val content: Content
    )

    @Serializable
    data class Content(
        val pageHeaderViewModel: PageHeaderViewModel
    ) {
        @Serializable
        data class PageHeaderViewModel(
            val banner: Banner?,
            val metadata: Metadata
        ) {
            @Serializable
            data class Metadata(
                val contentMetadataViewModel: ContentMetadataViewModel,
            ) {
                @Serializable
                data class ContentMetadataViewModel(
                    val metadataRows: List<MetadataRow>
                ) {
                    @Serializable
                    data class MetadataRow(
                        val metadataParts: List<MetadataPart>
                    ) {
                        @Serializable
                        data class MetadataPart(
                            val text: Text
                        )
                    }
                }
            }

            @Serializable
            data class Banner(
                val imageBannerViewModel: ImageBannerViewModel
            ) {
                @Serializable
                data class ImageBannerViewModel(
                    val image: Image
                )
            }
        }
    }

    @Serializable
    data class Image(
        val sources: List<Source>
    ) {
        @Serializable
        data class Source(
            val url: String
        )
    }

    @Serializable
    data class Text(
        val content: String
    )
}