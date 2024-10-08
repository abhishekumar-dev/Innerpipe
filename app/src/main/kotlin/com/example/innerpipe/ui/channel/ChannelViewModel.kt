package com.example.innerpipe.ui.channel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.innertube.Innertube
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChannelViewModel(
    private val innertube: Innertube
) : ViewModel() {
    private val _uiState = MutableStateFlow(ChannelUiState())
    val uiState = _uiState.asStateFlow()
    private var continuationToken: String? = null

    fun channel(id: String) {
        continuationToken = null
        _uiState.update {
            it.copy(
                isLoading = true,
                videos = emptyList(),
                error = null
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val channel = innertube.channel(id)
                _uiState.update {
                    it.copy(
                        videos = channel.videos,
                        filters = channel.filters,
                        header = Header(
                            title = channel.title.orEmpty(),
                            description = channel.description,
                            avatar = channel.avatar.orEmpty(),
                            banner = channel.banner.orEmpty(),
                            handle = channel.handle,
                            subscribers = channel.subscribers,
                            videosCount = channel.videosCount
                        ),
                        isLoading = false
                    )
                }
                continuationToken = channel.token
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = e.message, isLoading = false)
                }
            }
        }
    }

    private fun continuation(token: String?, isFilter: Boolean) {
        _uiState.update { it.copy(videos = if (isFilter) emptyList() else it.videos) }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val channel = innertube.channel(token = token)
                _uiState.update {
                    it.copy(
                        videos = it.videos + channel.videos,
                        isLoading = false
                    )
                }
                continuationToken = channel.token
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = e.message, isLoading = false)
                }
            }
        }
    }

    fun filter(token: String) = continuation(token, true)
    fun nextPage() = continuation(continuationToken, false)
}