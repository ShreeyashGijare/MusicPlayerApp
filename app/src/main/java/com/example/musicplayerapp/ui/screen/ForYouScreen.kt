package com.example.musicplayerapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musicplayerapp.data.data_models.Data
import com.example.musicplayerapp.player.service.MusicPlayerState
import com.example.musicplayerapp.ui.components.LoadImageFromUrl
import com.example.musicplayerapp.ui.components.PlayerIconItem
import com.example.musicplayerapp.ui.components.ShimmerListItem
import com.example.musicplayerapp.ui.components.TextViewBold
import com.example.musicplayerapp.ui.components.TextViewSemiBold
import com.example.musicplayerapp.ui.music.MusicViewModel
import com.example.musicplayerapp.ui.music.UIState
import com.example.musicplayerapp.ui.theme.Brown
import com.example.musicplayerapp.utils.AppConstants

@Composable
fun ForYouScreen(
    viewModel: MusicViewModel,
    contentPaddingValues: PaddingValues,
    isAudioPlaying: Boolean,
    currentPlayingAudio: Data,
    isLoading: Boolean,
    onSongItemClick: (Int) -> Unit,
    onStart: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.padding(contentPaddingValues)) {
        LazyColumn(Modifier.weight(1f)) {
            if (isLoading) {
                items(15) {
                    ShimmerListItem()
                }
            } else {
                itemsIndexed(viewModel.audioList) { index, song ->
                    SongItem(song = song) {
                        onSongItemClick(index)
                    }
                }
            }
        }

        when(uiState) {
            UIState.Initial -> {

            }
            UIState.Ready -> {
                MediaPlayerController(
                    currentPlayingAudio = currentPlayingAudio,
                    isAudioPlaying = isAudioPlaying
                ) {
                    onStart()
                }
            }
        }
    }
}

@Composable
fun SongItem(
    song: Data,
    onSongItemClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 20.dp)
            .clickable {
                onSongItemClick()
            }
    ) {
        LoadImageFromUrl(url = AppConstants.BASE_ASSETS_URL + song.cover)
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            TextViewBold(
                text = song.name
            )
            TextViewSemiBold(
                text = song.artist
            )
        }

    }
}


@Composable
fun MediaPlayerController(
    currentPlayingAudio: Data,
    isAudioPlaying: Boolean,
    onStart: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(Brown),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(18.dp))
        LoadImageFromUrl(url = AppConstants.BASE_ASSETS_URL + currentPlayingAudio.cover)
        Spacer(modifier = Modifier.width(16.dp))
        TextViewBold(text = currentPlayingAudio.name)
        Spacer(modifier = Modifier.weight(1f))
        PlayerIconItem(
            icon = if (isAudioPlaying) Icons.Default.Pause
            else Icons.Default.PlayArrow
        ) {
            onStart()
        }
        Spacer(modifier = Modifier.width(20.dp))
    }
}


