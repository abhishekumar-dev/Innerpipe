package com.example.innertube.model

import com.example.innertube.model.renderers.TabRenderer
import kotlinx.serialization.Serializable

@Serializable
data class Tab(
    val tabRenderer: TabRenderer?
)