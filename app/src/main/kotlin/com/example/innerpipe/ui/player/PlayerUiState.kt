package com.example.innerpipe.ui.player

import com.example.innertube.model.Player

data class PlayerUiState(
    val player: Player? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)