package com.example.innerpipe.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.innertube.Innertube
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val innertube: Innertube
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()
    private var token: String? = null

    fun search(query: String? = null, continuation: Boolean = false) {
        _uiState.update {
            it.copy(
                isLoading = !continuation,
                items = if (continuation) it.items else emptyList(),
                error = null
            )
        }
        if (!continuation) {
            token = null
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val search = innertube.search(query, token)
                _uiState.update {
                    it.copy(
                        items = it.items + search.items,
                        itemsCount = search.itemsCount,
                        isLoading = false
                    )
                }
                token = search.token
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = e.message, isLoading = false)
                }
            }
        }
    }

    fun nextPage() = search(continuation = true)

    fun suggestions(query: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val suggestions = innertube.suggestions(query)
            _uiState.update {
                it.copy(suggestions = suggestions)
            }
        } catch (_: Exception) {
            return@launch
        }
    }

    fun clear() = _uiState.update { it.copy(suggestions = emptyList()) }
}