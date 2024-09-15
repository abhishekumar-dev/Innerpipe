package com.example.innerpipe.ui.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.innerpipe.ui.components.LoadingAnimation
import com.example.innerpipe.ui.components.VideoPlayer

@Composable
fun PlayerScreen(
    id: String,
    playerViewModel: PlayerViewModel
) {
    val uiState by playerViewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        playerViewModel.player(id)
    }
    Column {
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize().statusBarsPadding(),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingAnimation(modifier = Modifier.size(140.dp))
                }
            }

            uiState.error != null -> {
                Text(text = uiState.error ?: "Something went wrong")
            }

            uiState.player != null -> {
                val player = uiState.player
                val url = player?.streamingData?.hlsManifestUrl

                Spacer(modifier = Modifier.statusBarsPadding())
                if (url != null) {
                    VideoPlayer(
                        url = url,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black)
                    )
                } else {
                    Text(text = player?.playabilityStatus?.reason.orEmpty())
                }

                Column(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(
                        text = player?.videoDetails?.title.orEmpty(),
                        fontSize = 17.sp
                    )
                    Text(player?.videoDetails?.author.orEmpty())
                    Text(player?.videoDetails?.viewCount.orEmpty())
                    Text(player?.videoDetails?.isCrawlable.toString())
                }
            }
        }
    }
}