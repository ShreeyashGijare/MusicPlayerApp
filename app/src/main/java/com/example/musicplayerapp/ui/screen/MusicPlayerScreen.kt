package com.example.musicplayerapp.ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.with
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicplayerapp.ui.components.LoadImageFromUrlLarge
import com.example.musicplayerapp.ui.components.PlayerIconItem
import com.example.musicplayerapp.ui.components.TextViewBold
import com.example.musicplayerapp.ui.components.TextViewNormal
import com.example.musicplayerapp.ui.music.MusicViewModel
import com.example.musicplayerapp.ui.music.UIEvents
import com.example.musicplayerapp.utils.AppConstants
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun MusicPlayerScreen(
    viewModel: MusicViewModel,
    exoPlayer: ExoPlayer
) {
    val configuration = LocalConfiguration.current
    val pagerState = rememberPagerState(initialPage = viewModel.currentPlayingIndex) {
        viewModel.audioList.count()
    }

    LaunchedEffect(pagerState.currentPage) {
        viewModel.currentPlayingIndex = pagerState.currentPage
        viewModel.onUIEvents(UIEvents.SelectedAudioChange(pagerState.currentPage))
    }

    LaunchedEffect(Unit) {
        viewModel.audioList.forEach {
            val mediaItem = MediaItem.fromUri(it.url)
            exoPlayer.addMediaItem(mediaItem)
        }
    }
    exoPlayer.prepare()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(android.graphics.Color.parseColor(viewModel.audioList[pagerState.currentPage].accent))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(

                modifier = Modifier.fillMaxWidth(),
                state = pagerState,
                pageSize = PageSize.Fixed((configuration.screenWidthDp / (1.7)).dp),
                contentPadding = PaddingValues(horizontal = 85.dp),
            ) { page ->
                Card(
                    modifier = Modifier
                        .size((configuration.screenWidthDp / (1.7)).dp)
                        .graphicsLayer {
                            val pageOffset = (
                                    (pagerState.currentPage - page
                                            ) + pagerState.currentPageOffsetFraction).absoluteValue
                            val alphaLerp = lerp(
                                start = 0.4f,
                                stop = 1f,
                                fraction = (1f - pageOffset.coerceIn(0f, 1f))
                            )
                            val scaleLerp = lerp(
                                start = 0.5f,
                                stop = 1f,
                                fraction = (1f - pageOffset.coerceIn(0f, .5f))
                            )
                            alpha = alphaLerp
                            scaleX = scaleLerp
                            scaleY = scaleLerp
                        }
                ) {
                    LoadImageFromUrlLarge(url = AppConstants.BASE_ASSETS_URL + viewModel.audioList[page].cover)
                }
            }
            Spacer(modifier = Modifier.height(50.dp))
            AnimatedContent(targetState = pagerState, transitionSpec = {
                (scaleIn() + fadeIn()) with (scaleOut() + fadeOut())
            }, label = "") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextViewBold(text = viewModel.audioList[it.currentPage].name, fontSize = 22.sp)
                    TextViewNormal(
                        text = viewModel.audioList[it.currentPage].artist,
                        fontSize = 16.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(50.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .padding(horizontal = 20.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF595453)),
                contentAlignment = Alignment.CenterStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = .5f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextViewNormal(text = "00.00")
                TextViewNormal(text = "00.00")
            }
            Spacer(modifier = Modifier.height(48.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PlayerIconItem(
                    modifier = Modifier.size(60.dp),
                    icon = Icons.Filled.FastRewind,
                    color = Color.Transparent,
                    iconTint = Color.White
                ) {
                    viewModel.onUIEvents(UIEvents.Backward)
                }
                PlayerIconItem(
                    modifier = Modifier.size(80.dp),
                    icon = if (viewModel.isPlaying) Icons.Default.Pause
                    else Icons.Default.PlayArrow
                ) {
                    viewModel.onUIEvents(UIEvents.PlayPause)
                }
                PlayerIconItem(
                    modifier = Modifier.size(60.dp),
                    icon = Icons.Filled.FastForward,
                    color = Color.Transparent,
                    iconTint = Color.White
                ) {
                    viewModel.onUIEvents(UIEvents.Forward)
                }
            }
        }
    }
}