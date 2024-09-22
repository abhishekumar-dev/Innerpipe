package com.example.innerpipe.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.innerpipe.R
import com.example.innertube.model.renderers.ChannelRenderer
import com.example.innertube.model.renderers.VideoRenderer

@Composable
fun ChannelItem(channel: ChannelRenderer, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(0.5F)
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = channel.thumbnail.url,
                contentDescription = null,
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onBackground.copy(0.1f))
            )
        }
        Column(
            modifier = Modifier.weight(0.5F)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(text = channel.title)
                if (channel.isVerified) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_verified),
                        contentDescription = null,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
            Text(
                text = buildString {
                    append(channel.handle)
                    append("\n")
                    append(channel.subscribers)
                },
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


@Composable
fun VideoItem(video: VideoRenderer, onClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box {
            AsyncImage(
                model = video.thumbnail.url,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16F / 9F)
                    .background(MaterialTheme.colorScheme.onBackground.copy(0.1F)),
                contentScale = ContentScale.FillWidth
            )
            val color = if (video.duration == "LIVE") Color.Red else Color.Black
            Text(
                text = video.duration,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(13.dp)
                    .background(color = color.copy(0.8f), shape = RoundedCornerShape(4.dp))
                    .padding(4.dp, 1.dp),
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
            )
        }
        Row(
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            AsyncImage(
                model = video.authorThumbnail,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onBackground.copy(0.1f))
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(text = video.title)
                val metadata = listOfNotNull(
                    video.author,
                    video.views,
                    video.publishedTime,
                    video.upcomingEvent
                )
                Text(
                    text = metadata.separateByDots(),
                    color = MaterialTheme.colorScheme.onBackground.copy(0.9f),
                    style = MaterialTheme.typography.bodyMedium
                )
                Row {
                    video.tags.forEach { tag ->
                        Text(
                            text = tag,
                            color = MaterialTheme.colorScheme.onBackground.copy(0.9F),
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(end = 4.dp, top = 4.dp)
                                .background(Color.Gray.copy(0.22F), RoundedCornerShape(4.dp))
                                .padding(4.dp, 1.dp)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun VideoItemMini(video: VideoRenderer, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box {
            AsyncImage(
                model = video.thumbnail.url,
                contentDescription = null,
                modifier = Modifier
                    .width(140.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        color = MaterialTheme.colorScheme.onBackground.copy(0.1F),
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentScale = ContentScale.FillWidth
            )
            val color = if (video.duration == "LIVE") Color.Red else Color.Black
            Text(
                text = video.duration,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(5.dp)
                    .background(color = color.copy(0.8f), shape = RoundedCornerShape(4.dp))
                    .padding(4.dp, 1.dp),
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(text = video.title, maxLines = 2, overflow = TextOverflow.Ellipsis)
            val metadata = listOfNotNull(
                video.views,
                video.publishedTime,
                video.upcomingEvent
            )
            Text(
                text = metadata.separateByDots(),
                color = MaterialTheme.colorScheme.onBackground.copy(0.9f),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

private fun List<String?>.separateByDots() = filterNotNull().joinToString(separator = " • ")