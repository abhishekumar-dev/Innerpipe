package com.example.innerpipe.ui.channel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.innerpipe.ui.components.LoadingAnimation
import com.example.innerpipe.ui.components.VideoItemMini

@Composable
fun ChannelScreen(
    id: String,
    channelViewModel: ChannelViewModel
) {
    val uiState by channelViewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        channelViewModel.channel(id)
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(Modifier.statusBarsPadding())

            val header = uiState.header
            if (header != null) {
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
                Text(
                    text = header.description,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
                val metadata = listOfNotNull(
                    header.handle,
                    header.subscribers,
                    header.videosCount
                )
                Text(
                    text = metadata.joinToString(separator = " • "),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (uiState.isLoading) {
                LoadingAnimation(modifier = Modifier.size(90.dp))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                uiState.filters.forEach {
                    FilterChip(
                        selected = false,
                        onClick = { it.token?.let { token -> channelViewModel.filter(token) } },
                        label = { Text(it.title!!) }
                    )
                }
            }
            HorizontalDivider()
        }
        items(uiState.videos) {
            VideoItemMini(it)
        }
        item {
            channelViewModel.nextPage()
            Spacer(Modifier.navigationBarsPadding())
        }
    }
}