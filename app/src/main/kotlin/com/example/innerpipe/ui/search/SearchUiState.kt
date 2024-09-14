package com.example.innerpipe.ui.search

data class SearchUiState(
    val itemsCount: Long = 0,
    val items: List<Any> = emptyList(),
    val suggestions: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)