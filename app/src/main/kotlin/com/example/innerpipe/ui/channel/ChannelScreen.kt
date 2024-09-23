package com.example.innerpipe.ui.channel

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.innerpipe.Screen
import com.example.innerpipe.ui.components.LoadingAnimation
import com.example.innerpipe.ui.components.VideoItemMini
import com.example.innertube.model.Filter

@Composable
fun ChannelScreen(
    id: String,
    navController: NavController,
    channelViewModel: ChannelViewModel
) {
    val uiState by channelViewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        channelViewModel.channel(id)
    }
    BackHandler {
        navController.navigate(Screen.Search) {
            popUpTo(Screen.Search) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (uiState.isLoading) {
            LoadingAnimation(modifier = Modifier.size(140.dp))
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                val header = uiState.header
                if (header != null) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        AsyncImage(
                            model = header.banner.split("-fcrop").first(),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp),
                            contentScale = ContentScale.FillWidth
                        )
                        AsyncImage(
                            model = header.avatar,
                            contentDescription = null,
                            modifier = Modifier
                                .size(70.dp)
                                .clip(CircleShape)
                        )
                        Text(text = header.title, style = MaterialTheme.typography.titleLarge)
                        var maxLine by rememberSaveable { mutableIntStateOf(2) }
                        header.description?.let {
                            Text(
                                text = header.description,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                maxLines = maxLine,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) {
                                        maxLine = if (maxLine == 2) Int.MAX_VALUE else 2
                                    }
                                    .animateContentSize()
                                    .padding(horizontal = 10.dp)
                            )
                        }
                        val metadata = listOfNotNull(
                            header.handle,
                            header.subscribers,
                            header.videosCount
                        )
                        Text(
                            text = metadata.joinToString(separator = " • "),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
                        )
                    }
                }
                SegmentedFilters(uiState.filters) { token ->
                    channelViewModel.filter(token)
                }
            }
            items(uiState.videos) {
                VideoItemMini(it) {
                    navController.navigate(Screen.Player(it.videoId)) {
                        launchSingleTop = true
                    }
                }
            }
            item {
                channelViewModel.nextPage()
                Spacer(Modifier.navigationBarsPadding())
            }
        }
    }
}

@Composable
private fun SegmentedFilters(filters: List<Filter>, onClick: (String) -> Unit) {
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    SingleChoiceSegmentedButtonRow {
        filters.forEachIndexed { index, filter ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = filters.size),
                onClick = {
                    selectedIndex = index
                    filter.token?.let { onClick(it) }
                },
                selected = index == selectedIndex
            ) {
                Text(filter.title.orEmpty())
            }
        }
    }
}