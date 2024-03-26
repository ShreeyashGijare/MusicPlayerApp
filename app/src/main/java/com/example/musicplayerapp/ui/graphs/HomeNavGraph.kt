package com.example.musicplayerapp.ui.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.musicplayerapp.data.data_models.Data
import com.example.musicplayerapp.ui.music.MusicViewModel
import com.example.musicplayerapp.ui.screen.ForYouScreen
import com.example.musicplayerapp.ui.screen.MusicPlayerScreen
import com.example.musicplayerapp.ui.screen.TopTracksScreen
import com.example.musicplayerapp.utils.Graph
import com.example.musicplayerapp.utils.Screen

@Composable
fun HomeNavGraph(
    navController: NavHostController,
    viewModel: MusicViewModel,
    contentPaddingValues: PaddingValues,
    isAudioPlaying: Boolean,
    currentPlayingAudio: Data,
    isLoading: Boolean,
    onSongItemClick: (Int) -> Unit,
    exoPlayer: ExoPlayer,
    onStart: () -> Unit
) {
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = Screen.ForYouScreen.route
    ) {
        composable(Screen.ForYouScreen.route) {
            ForYouScreen(
                viewModel,
                contentPaddingValues = contentPaddingValues,
                isAudioPlaying = isAudioPlaying,
                currentPlayingAudio = currentPlayingAudio,
                isLoading = isLoading,
                onSongItemClick = {
                    onSongItemClick(it)
                    navController.navigate(Graph.MUSIC_PLAYER)
                },
                onStart = {
                    onStart()
                }
            )
        }
        composable(Screen.TopTracks.route) {
            TopTracksScreen(
                viewModel,
                contentPaddingValues = contentPaddingValues,
                isAudioPlaying = isAudioPlaying,
                currentPlayingAudio = currentPlayingAudio,
                isLoading = isLoading,
                onSongItemClick = {
                    onSongItemClick(it)
                    navController.navigate(Graph.MUSIC_PLAYER)
                },
                onStart = {
                    onStart()
                }
            )
        }
        musicPlayerScreenGraph(viewModel, exoPlayer)
    }
}

fun NavGraphBuilder.musicPlayerScreenGraph(
    viewModel: MusicViewModel,
    exoPlayer: ExoPlayer
) {
    navigation(
        route = Graph.MUSIC_PLAYER,
        startDestination = Screen.MusicPlayerScreen.route
    ) {
        composable(route = Screen.MusicPlayerScreen.route) {
            MusicPlayerScreen(viewModel = viewModel, exoPlayer = exoPlayer)
        }
    }
}