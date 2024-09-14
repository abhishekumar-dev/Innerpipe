package com.example.innerpipe.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import com.example.innerpipe.ui.components.ChannelItem
import com.example.innerpipe.ui.components.LoadingAnimation
import com.example.innerpipe.ui.components.VideoItem
import com.example.innertube.model.Shelf
import com.example.innertube.model.renderers.ChannelRenderer
import com.example.innertube.model.renderers.VideoRenderer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel
) {
    val uiState by searchViewModel.uiState.collectAsStateWithLifecycle()
    var query by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    val searchBarPadding by animateDpAsState(
        targetValue = if (expanded) 0.dp else 10.dp,
        label = "search bar padding"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .semantics { isTraversalGroup = true },
        contentAlignment = Alignment.Center
    ) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = 0f }
                .padding(horizontal = searchBarPadding),
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = {
                        query = it
                        searchViewModel.suggestions(it)
                    },
                    onSearch = {
                        expanded = false
                        if (query.isNotBlank()) {
                            searchViewModel.search(query)
                            searchViewModel.clear()
                        }
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text(text = "Search Youtube") },
                    leadingIcon = {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(
                                imageVector = if (expanded) Icons.AutoMirrored.Filled.ArrowBack else Icons.Default.Search,
                                contentDescription = null
                            )
                        }
                    },
                    trailingIcon = {
                        AnimatedVisibility(
                            visible = query.isNotBlank(),
                            enter = slideInVertically() + fadeIn(),
                            exit = slideOutVertically() + fadeOut()
                        ) {
                            IconButton(onClick = { query = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = null)
                            }
                        }
                    }
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            LazyColumn {
                items(uiState.suggestions) { suggestion ->
                    ListItem(
                        headlineContent = { Text(suggestion) },
                        leadingContent = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                            )
                        },
                        modifier = Modifier.clickable {
                            query = suggestion
                            expanded = false
                            searchViewModel.search(query)
                            searchViewModel.clear()
                        },
                        colors = ListItemDefaults.colors(
                            containerColor = Color.Transparent
                        )
                    )
                }
            }
        }

        when {
            uiState.isLoading -> {
                LoadingAnimation(modifier = Modifier.size(190.dp))
            }

            uiState.error != null && uiState.items.isEmpty() -> {
                Text(text = uiState.error ?: "Something went wrong!")
            }

            uiState.items.isNotEmpty() -> {
                ItemList(uiState, searchViewModel::nextPage)
            }
        }
    }
}

@Composable
fun ItemList(
    uiState: SearchUiState,
    nextPage: () -> Unit
) {
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val lazyListState = rememberLazyListState()

    LaunchedEffect(uiState.items.size) {
        if (uiState.items.isEmpty()) {
            lazyListState.scrollToItem(0)
        }
    }

    LazyColumn(
        state = lazyListState,
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(top = 70.dp + statusBarPadding)
    ) {
        itemsIndexed(
            items = uiState.items,
            key = { index, item ->
                "$index - $item"
            }
        ) { _, item ->
            when (item) {
                is ChannelRenderer -> ChannelItem(item)
                is VideoRenderer -> VideoItem(item)
                is Shelf -> if (item.title?.contains("Latest posts from") == false) {
                    Column {
                        item.title?.let { title ->
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        item.videos.forEach { item ->
                            VideoItem(item)
                        }
                        HorizontalDivider(modifier = Modifier.padding(bottom = 10.dp))
                    }
                }
            }
        }
        item {
            if (uiState.items.size <= uiState.itemsCount) {
                LoadingAnimation(modifier = Modifier.size(90.dp))
                nextPage.invoke()
            }
        }
        item {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            )
        }
    }
}