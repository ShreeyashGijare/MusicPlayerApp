package com.example.musicplayerapp.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.musicplayerapp.data.data_models.Data
import com.example.musicplayerapp.ui.components.MediaPlayerController
import com.example.musicplayerapp.ui.components.ShimmerListItem
import com.example.musicplayerapp.ui.components.SongItem
import com.example.musicplayerapp.ui.music.MusicViewModel
import com.example.musicplayerapp.ui.music.UIState

@Composable
fun TopTracksScreen(
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
                itemsIndexed(viewModel.audioList.filter { it.top_track }) { index, song ->
                    SongItem(song = song) {
                        onSongItemClick(index)
                    }
                }
            }
        }

        when (uiState) {
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