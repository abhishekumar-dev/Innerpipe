package com.example.innerpipe.ui.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.innertube.Innertube
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val innertube: Innertube
): ViewModel() {
    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState = _uiState.asStateFlow()

    fun player(id: String) {
        _uiState.update { it.copy(player = null, isLoading = true) }
        viewModelScope.launch {
            try {
                val player = innertube.player(id)
                _uiState.update {
                    it.copy(player = player, isLoading = false)
                }
                metadata(id)
                returnYoutubeDislike(id)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = e.message, isLoading = false)
                }
            }
        }
    }

    private suspend fun metadata(id: String) {
        val metadata = innertube.playerMetadata(id)
        _uiState.update {
            it.copy(metadata = metadata)
        }
    }

    private suspend fun returnYoutubeDislike(id: String) {
        val ryd = innertube.returnYoutubeDislike(id)
        _uiState.update {
            it.copy(ryd = ryd)
        }
    }

    fun comments(token: String, isReply: Boolean = false) {
        viewModelScope.launch {
            val comments = innertube.comments(token)
            _uiState.update {
                it.copy(
                    comments = if (isReply) it.comments else it.comments + comments.comments,
                    replies = if (isReply) comments.comments + it.replies else it.replies,
                    token = comments.token
                )
            }
        }
    }
}