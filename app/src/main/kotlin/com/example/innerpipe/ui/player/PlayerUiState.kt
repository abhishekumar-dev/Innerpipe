package com.example.innerpipe.ui.player

import com.example.innertube.model.Comment
import com.example.innertube.model.Player
import com.example.innertube.model.PlayerMetadata
import com.example.innertube.model.RYD

data class PlayerUiState(
    val player: Player? = null,
    val metadata: PlayerMetadata? = null,
    val ryd: RYD? = null,
    val error: String? = null,
    val isLoading: Boolean = false,
    val comments: List<Comment> = emptyList(),
    val replies: List<Comment> = emptyList(),
    val token: String? = null
)