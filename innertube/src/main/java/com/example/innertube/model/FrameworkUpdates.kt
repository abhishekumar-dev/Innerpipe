package com.example.innertube.model

import kotlinx.serialization.Serializable

@Serializable
data class FrameworkUpdates(
    val entityBatchUpdate: EntityBatchUpdate
) {
    @Serializable
    data class EntityBatchUpdate(
        val mutations: List<Mutation>
    )
}