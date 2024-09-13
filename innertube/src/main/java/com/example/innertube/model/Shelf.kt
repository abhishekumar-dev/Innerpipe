package com.example.innertube.model

import com.example.innertube.model.renderers.VideoRenderer

data class Shelf(
    val title: String?,
    val videos: List<VideoRenderer>
)