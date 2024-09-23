package com.example.innerpipe.ui.player

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.innerpipe.R
import com.example.innerpipe.Screen
import com.example.innerpipe.share
import com.example.innerpipe.toHumanReadableCount
import com.example.innerpipe.ui.components.LinkifyText
import com.example.innerpipe.ui.components.LoadingAnimation
import com.example.innerpipe.ui.components.VideoItemMini
import com.example.innerpipe.ui.components.VideoPlayer
import com.example.innertube.model.PlayerMetadata
import com.example.innertube.model.RYD
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    id: String,
    playerViewModel: PlayerViewModel,
    navController: NavController
) {
    val coroutineScope = rememberCoroutineScope()
    val uiState by playerViewModel.uiState.collectAsStateWithLifecycle()
    var type by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        playerViewModel.player(id)
    }
    BackHandler {
        navController.navigate(Screen.Search) {
            popUpTo(Screen.Search) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }
    when {
        uiState.isLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding(),
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
            val metadata = uiState.metadata
            val returnYoutubeDislike = uiState.ryd
            val url = player?.streamingData?.hlsManifestUrl

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
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

                WithBottomSheet(
                    sheetContent = {
                        when (type) {
                            0 -> {
                                Description(player?.videoDetails?.shortDescription.orEmpty())
                            }

                            1 -> {
                                Comments(
                                    uiState = uiState,
                                    playerViewModel = playerViewModel
                                )
                            }
                        }
                    }
                ) { scaffoldState ->
                    LazyColumn {
                        item {
                            if (metadata != null) {
                                Metadata(
                                    metadata = metadata,
                                    channelOnClick = {
                                        player?.videoDetails?.channelId?.let {
                                            navController.navigate(Screen.Channel(it)) {
                                                launchSingleTop = true
                                            }
                                        }
                                    }
                                ) {
                                    coroutineScope.launch {
                                        type = 0
                                        scaffoldState.bottomSheetState.expand()
                                    }
                                }
                            }
                            if (returnYoutubeDislike != null) {
                                Actions(returnYoutubeDislike)
                            }
                            if (metadata != null && metadata.commentCount > 0 && metadata.tokenForComments != null) {
                                Button(
                                    onClick = {
                                        coroutineScope.launch {
                                            playerViewModel.comments(metadata.tokenForComments!!)
                                            type = 1
                                            scaffoldState.bottomSheetState.expand()
                                        }
                                    }
                                ) {
                                    Text(text = "Comments ${metadata.commentCount}")
                                }
                            }
                        }
                        items(metadata?.recommendedVideos.orEmpty()) {
                            VideoItemMini(it) {
                                navController.navigate(Screen.Player(it.videoId)) {
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WithBottomSheet(
    modifier: Modifier = Modifier,
    sheetContent: @Composable ColumnScope.() -> Unit,
    content: @Composable (BottomSheetScaffoldState) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState(
        rememberStandardBottomSheetState(
            skipHiddenState = false,
            initialValue = SheetValue.Hidden
        )
    )
    BackHandler(scaffoldState.bottomSheetState.currentValue == SheetValue.Expanded) {
        coroutineScope.launch {
            scaffoldState.bottomSheetState.hide()
        }
    }
    BottomSheetScaffold(
        sheetContent = sheetContent,
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        modifier = modifier
    ) {
        content(scaffoldState)
    }
}

@Composable
private fun Metadata(
    metadata: PlayerMetadata,
    channelOnClick: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(13.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        Text(
            text = metadata.title.orEmpty(),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = buildAnnotatedString {
                if (metadata.shortViewCount != null) {
                    append(metadata.shortViewCount)
                } else {
                    append(metadata.viewCount)
                }
                if (metadata.relativeDate != null) {
                    append(" ")
                    append(metadata.relativeDate)
                } else {
                    append(" ")
                    append(metadata.datePublished)
                }
                withStyle(SpanStyle(fontWeight = FontWeight.Medium)) {
                    append(" ...more")
                }
            },
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.clickable(onClick = onClick)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    channelOnClick()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AsyncImage(
                model = metadata.authorThumbnail,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(text = metadata.author.orEmpty())
                    AnimatedVisibility(metadata.isVerified) {
                        Icon(
                            painter = painterResource(R.drawable.ic_verified),
                            contentDescription = null,
                            modifier = Modifier.size(13.dp),
                            tint = MaterialTheme.colorScheme.onBackground.copy(0.8f)
                        )
                    }
                }
                Text(
                    text = metadata.subscribers.orEmpty(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun Description(description: String) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Description",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 16.dp)
        )
        HorizontalDivider()
        LazyColumn {
            item {
                LinkifyText(
                    text = description,
                    modifier = Modifier.padding(10.dp)
                )
                Spacer(modifier = Modifier.navigationBarsPadding())
            }
        }
    }
}

@Composable
private fun Actions(returnYoutubeDislike: RYD) {
    val context = LocalContext.current
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(13.dp, 0.dp)
    ) {
        item {
            Chip(
                items = listOf(
                    Pair(
                        R.drawable.ic_thumbs_up,
                        returnYoutubeDislike.likes.toHumanReadableCount()
                    ),
                    Pair(
                        R.drawable.ic_thumbs_down,
                        returnYoutubeDislike.dislikes.toHumanReadableCount()
                    )
                )
            )
            Spacer(modifier = Modifier.size(10.dp))
            Chip(
                items = listOf(Pair(R.drawable.ic_share, "Share")),
                onClick = {
                    context.share(returnYoutubeDislike.id)
                }
            )
            Spacer(modifier = Modifier.size(10.dp))
            Chip(
                items = listOf(Pair(R.drawable.ic_download, "Download")),
                onClick = {
                    Toast.makeText(context, "Not supported yet", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

@Composable
private fun Chip(
    items: List<Pair<Int, String>>,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .height(40.dp)
            .background(
                color = MaterialTheme.colorScheme.onBackground.copy(0.05f),
                shape = CircleShape
            )
            .padding(13.dp, 0.dp)
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items.forEachIndexed { index, (icon, text) ->
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(22.dp)
            )
            Text(
                text = text,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
            if (index != items.lastIndex) {
                VerticalDivider()
            }
        }
    }
}