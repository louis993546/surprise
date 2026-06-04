package de.berlindroid.mario.pages

import android.view.LayoutInflater
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import de.berlindroid.mario.LocalPageIndex
import de.berlindroid.mario.LocalPagerState
import de.berlindroid.mario.R
import de.berlindroid.mario.di.AppScope
import de.berlindroid.mario.model.Page
import dev.zacsweers.metro.ContributesIntoSet

/**
 * A beautiful, dynamic page that shows a thank you message on the left
 * and plays a local vertical video on the right side.
 * 
 * Updated to use modern Jetpack Media3 ExoPlayer for flawless vertical video
 * scaling, hardware acceleration, and memory-safe lifecycle handling.
 *
 * =========================================================================
 * INSTRUCTIONS FOR ADDING YOUR VIDEO:
 * =========================================================================
 * 1. FILE FORMAT:
 *    - Format: MP4 (H.264 video codec, AAC audio codec). Highly compatible.
 *    - Naming convention: Lowercase, alphanumeric, and underscores only (e.g., "mario_tribute.mp4").
 *      Do NOT use spaces, dashes, or special characters in the filename.
 *
 * 2. SUITABLE RESOLUTION:
 *    - Standard vertical video sizes like 1080x1920 (Full HD) or 720x1280 (HD) work flawlessly.
 *    - The player uses fit-scaling (RESIZE_MODE_FIT), meaning it maintains the full framing 
 *      without cutting off any edges.
 *
 * 3. FILE LOCATION:
 *    - Place your video file inside the Android raw resources folder:
 *      `app/src/main/res/raw/`
 *    - If the `raw` directory does not exist, create it inside `app/src/main/res/`.
 *    - Update the `videoResourceName` variable below to match your filename (without the .mp4 extension).
 * =========================================================================
 */
@ContributesIntoSet(AppScope::class)
object VideoPage : Page {

    override val title: String = "A Video Message"
    override val author: String = "Your Name"

    @Composable
    override fun LeftContent() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFEFEBE9)) // Soft warm grey-cream background
                .padding(64.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "A Message in Motion",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3E2723), // Deep brown/bronze
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(48.dp))

                Text(
                    text = "Words on paper are beautiful, but we wanted to send you a more personal greeting.\n\nTap the video on the right to play, pause, or loop it!",
                    fontSize = 24.sp,
                    lineHeight = 36.sp,
                    color = Color(0xFF5D4037),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(64.dp))

                Text(
                    text = "— Signed with gratitude",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF8D6E63)
                )
            }
        }
    }

    @OptIn(UnstableApi::class)
    @Composable
    override fun RightContent() {
        val context = LocalContext.current
        val pagerState = LocalPagerState.current
        val pageIndex = LocalPageIndex.current
        val isVisible = pagerState.currentPage == pageIndex

        var isPlaying by remember { mutableStateOf(false) }
        var isVideoPrepared by remember { mutableStateOf(false) }

        // Track controller states
        var isMuted by remember { mutableStateOf(false) }
        var isCcEnabled by remember { mutableStateOf(true) }

        // Resolve raw resource ID
        val resourceId = R.raw.video

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF151515)), // Cinematic dark background
            contentAlignment = Alignment.Center
        ) {
            if (resourceId != 0) {
                // Initialize ExoPlayer
                val exoPlayer = remember {
                    val mediaUri = "android.resource://${context.packageName}/$resourceId".toUri()
                    ExoPlayer.Builder(context).build().apply {
                        setMediaItem(MediaItem.fromUri(mediaUri))
                        repeatMode = Player.REPEAT_MODE_ALL
                        prepare()
                        playWhenReady = false // Don't auto-play yet
                    }
                }

                // Play or pause based on visibility
                LaunchedEffect(isVisible) {
                    if (isVisible) {
                        exoPlayer.play()
                    } else {
                        exoPlayer.pause()
                    }
                }

                // Keep track of playback states
                DisposableEffect(exoPlayer) {
                    val listener = object : Player.Listener {
                        override fun onIsPlayingChanged(playing: Boolean) {
                            isPlaying = playing
                        }

                        override fun onPlaybackStateChanged(playbackState: Int) {
                            if (playbackState == Player.STATE_READY) {
                                isVideoPrepared = true
                            }
                        }
                    }
                    exoPlayer.addListener(listener)
                    onDispose {
                        exoPlayer.removeListener(listener)
                        exoPlayer.release()
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            if (exoPlayer.isPlaying) {
                                exoPlayer.pause()
                            } else {
                                exoPlayer.play()
                            }
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Black)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        AndroidView(
                            factory = { ctx ->
                                LayoutInflater.from(ctx)
                                    .inflate(R.layout.texture_video_player, null) as PlayerView
                            },
                            update = { playerView ->
                                playerView.player = exoPlayer
                            },
                            modifier = Modifier.fillMaxSize()
                        )

                        // Custom Play/Pause Overlay indicator
                        if (isVideoPrepared && !isPlaying) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.4f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Paused",
                                    tint = Color.White,
                                    modifier = Modifier.fillMaxSize(0.2f)
                                )
                            }
                        }

                        // Quick Controls Overlay (Top Right)
                        if (isVideoPrepared) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                contentAlignment = Alignment.TopEnd
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // Mute / Unmute Button
                                    IconButton(
                                        onClick = {
                                            isMuted = !isMuted
                                            exoPlayer.volume = if (isMuted) 0f else 1f
                                        },
                                        colors = IconButtonDefaults.iconButtonColors(
                                            containerColor = Color.Black.copy(alpha = 0.6f),
                                            contentColor = Color.White
                                        )
                                    ) {
                                        Icon(
                                            imageVector = if (isMuted) Icons.AutoMirrored.Filled.VolumeOff else Icons.AutoMirrored.Filled.VolumeUp,
                                            contentDescription = if (isMuted) "Unmute" else "Mute"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // Fallback instructions screen displayed if the video file has not been copied to res/raw yet
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "📽️ Add Your Video File!",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "To play a video here using Media3:\n\n" +
                                    "1. Create the folder 'raw' inside 'app/src/main/res/'\n" +
                                    "2. Drop your MP4 file into it\n" +
                                    "3. Rebuild and run the app!",
                            fontSize = 18.sp,
                            lineHeight = 28.sp,
                            color = Color.LightGray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
