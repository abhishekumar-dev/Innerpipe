@file:OptIn(UnstableApi::class)

package com.example.innerpipe.ui.components

import android.annotation.SuppressLint
import android.app.PictureInPictureParams
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import android.util.Rational
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toAndroidRectF
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.PictureInPictureModeChangedInfo
import androidx.core.graphics.toRect
import androidx.core.util.Consumer
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.TrackSelectionOverride
import androidx.media3.common.Tracks
import androidx.media3.common.VideoSize
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@Composable
fun VideoPlayer(
    url: String,
    modifier: Modifier = Modifier,
    videoPlayerViewModel: VideoPlayerViewModel = viewModel()
) {
    val context = LocalContext.current
    val player by videoPlayerViewModel.player.collectAsState()
    var expanded by rememberSaveable { mutableStateOf(false) }

    PlayerLifecycle(
        initialize = { videoPlayerViewModel.initializePlayer(url, context) },
        release = { videoPlayerViewModel.releasePlayer() }
    )
    if (isInPipMode()) {
        VideoPlayer(
            player = player,
            shouldEnterPipMode = videoPlayerViewModel.shouldEnterPipMode,
            modifier = Modifier.fillMaxSize()
        )
    } else {
        Box {
            VideoPlayer(
                player = player,
                shouldEnterPipMode = videoPlayerViewModel.shouldEnterPipMode,
                modifier = modifier
            )
            IconButton(
                onClick = { expanded = !expanded },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            if (expanded) {
                LazyColumn(
                    modifier = Modifier.height(170.dp)
                ) {
                    items(videoPlayerViewModel.formats.distinct()) { format ->
                        Text(
                            text = "${format.height} ${format.bitrate / 1000000.0} ${format.index}",
                            color = Color.White,
                            modifier = Modifier.clickable {
                                player?.switchTrack(format.index)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VideoPlayer(
    player: Player?,
    modifier: Modifier = Modifier,
    shouldEnterPipMode: Boolean
) {
    // When in preview, early return a Box with the received modifier preserving layout
    if (LocalInspectionMode.current) {
        Box(modifier = modifier)
        return
    }

    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    BackHandler(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        context.setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // create modifier that adds pip to video player
        val pipModifier = modifier.onGloballyPositioned { layoutCoordinates ->
            val builder = PictureInPictureParams.Builder()
            if (shouldEnterPipMode && player != null && player.videoSize != VideoSize.UNKNOWN) {
                // set source rect hint, aspect ratio
                val sourceRect = layoutCoordinates.boundsInWindow().toAndroidRectF().toRect()
                builder.setSourceRectHint(sourceRect)
                val aspectRatio = Rational(player.videoSize.width, player.videoSize.height)
                builder.setAspectRatio(aspectRatio)
            }

            // Add autoEnterEnabled for versions S and up
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                builder.setAutoEnterEnabled(shouldEnterPipMode)
            }
            context.findActivity().setPictureInPictureParams(builder.build())
        }

        val isInPipMode = isInPipMode()
        AndroidView(
            factory = { PlayerView(it) },
            update = { playerView ->
                playerView.player = player
                playerView.useController = !isInPipMode
                playerView.setShowBuffering(PlayerView.SHOW_BUFFERING_ALWAYS)
                playerView.setShowSubtitleButton(true)
                playerView.keepScreenOn = true
                playerView.setFullscreenButtonClickListener { isFullscreen ->
                    with(context) {
                        if (isFullscreen) setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
                        else setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                    }
                }
            },
            modifier = pipModifier.focusable()
        )
    } else {
        AndroidView(
            factory = { PlayerView(it) },
            update = { playerView ->
                playerView.player = player
            },
            modifier = Modifier.focusable()
        )
    }
}

/**
 * Handle the lifecycle of the player, making sure it's initialized and released at the
 * right moments in the Android lifecycle.
 */
@Composable
private fun PlayerLifecycle(
    initialize: () -> Unit,
    release: () -> Unit,
) {
    val currentOnInitializePlayer by rememberUpdatedState(initialize)
    val currentOnReleasePlayer by rememberUpdatedState(release)

    /**
     * Android API level 24 and higher supports multiple windows. As your app can be visible, but
     * not active in split window mode, you need to initialize the player in onStart
     */
    if (Build.VERSION.SDK_INT >= 24) {
        LifecycleStartEffect(true) {
            currentOnInitializePlayer()
            onStopOrDispose {
                currentOnReleasePlayer()
            }
        }
    }

    /**
     * Android API level 23 and lower requires you to wait as long as possible until you grab
     * resources, so you wait until onResume before initializing the player.
     */
    if (Build.VERSION.SDK_INT <= 23) {
        LifecycleResumeEffect(true) {
            currentOnInitializePlayer()
            onPauseOrDispose {
                currentOnReleasePlayer()
            }
        }
    }
}

/**
 * Uses Disposable Effect to add a pip observer to check when app enters pip mode so UI can be
 * updated
 */
@Composable
fun isInPipMode(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val activity = LocalContext.current.findActivity()
        var pipMode by remember { mutableStateOf(activity.isInPictureInPictureMode) }

        // Uses Disposable Effect to add a pip observer to check when app enters pip mode
        DisposableEffect(activity) {
            val observer = Consumer<PictureInPictureModeChangedInfo> { info ->
                pipMode = info.isInPictureInPictureMode
            }
            activity.addOnPictureInPictureModeChangedListener(
                observer,
            )
            onDispose { activity.removeOnPictureInPictureModeChangedListener(observer) }
        }

        return pipMode
    } else {
        return false
    }
}

private fun Context.setScreenOrientation(orientation: Int) {
    findActivity().requestedOrientation = orientation
    val window = findActivity().window
    WindowCompat.setDecorFitsSystemWindows(window, false)
    if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    } else {
        WindowInsetsControllerCompat(window, window.decorView)
            .show(WindowInsetsCompat.Type.systemBars())
        // reset orientation so that it doesn't lock to portrait
        findActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}

private fun Context.findActivity(): ComponentActivity {
    var context = this
    while (context is ContextWrapper) {
        if (context is ComponentActivity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Picture in picture should be called in the context of an Activity")
}

data class Format(
    val width: Int,
    val height: Int,
    val bitrate: Int,
    val index: Int,
    val codec: String?
)

@SuppressLint("UnsafeOptInUsageError")
private fun Tracks.formatList(): List<Format> {
    return groups.asSequence().map { group ->
        (0 until group.length).map {
            Format(
                group.getTrackFormat(it).width,
                group.getTrackFormat(it).height,
                group.getTrackFormat(it).bitrate,
                it,
                group.getTrackFormat(it).codecs
            )
        }
    }.flatten().filter { it.height > 0 }.sortedByDescending { it.height }.toList()
}

private fun Player.switchTrack(index: Int) {
    val mediaTrackGroup = currentTracks.groups.first().mediaTrackGroup
    trackSelectionParameters = trackSelectionParameters.buildUpon().setOverrideForType(
        TrackSelectionOverride(mediaTrackGroup, index)
    ).build()
}

// -------------------------------------------------------------- //

class VideoPlayerViewModel : ViewModel() {
    private val _player = MutableStateFlow<Player?>(null)
    val player = _player.asStateFlow()
    var shouldEnterPipMode by mutableStateOf(false)
    var formats = mutableListOf<Format>()

    @OptIn(UnstableApi::class)
    fun initializePlayer(uri: String, context: Context) {
        val player = ExoPlayer.Builder(context.applicationContext)
            .setSeekForwardIncrementMs(10_000)
            .setSeekBackIncrementMs(10_000)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(C.USAGE_MEDIA)
                    .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
                    .build(),
                true
            )
            .setHandleAudioBecomingNoisy(true)
            .build()
            .apply {
                setMediaItem(MediaItem.fromUri(uri))
                prepare()
                playWhenReady = true
            }


        player.addListener(
            object : Player.Listener {
                override fun onTracksChanged(tracks: Tracks) {
                    formats.addAll(tracks.formatList())
                }
            }
        )
        player.addListener(
            object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    shouldEnterPipMode = isPlaying
                }
            }
        )

        _player.value = player
        shouldEnterPipMode = true
    }

    fun releasePlayer() {
        _player.value?.release()
        _player.value = null
        shouldEnterPipMode = false
    }
}