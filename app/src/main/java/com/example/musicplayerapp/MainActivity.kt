package com.example.musicplayerapp

import HomeScreen
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicplayerapp.connectivity.ConnectivityObserver
import com.example.musicplayerapp.connectivity.NetworkConnectivityObserver
import com.example.musicplayerapp.player.service.MusicPlayerService
import com.example.musicplayerapp.ui.music.MusicViewModel
import com.example.musicplayerapp.ui.music.UIEvents
import com.example.musicplayerapp.ui.theme.MusicPlayerAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MusicViewModel by viewModels()
    private var isServiceRunning = false

    @Inject
    lateinit var exoPlayer: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicPlayerAppTheme {
                HomeScreen(
                    viewModel = viewModel,
                    isAudioPlaying = viewModel.isPlaying,
                    currentPlayingAudio = viewModel.currentSelectedAudio,
                    isLoading = viewModel.isLoading,
                    exoPlayer = exoPlayer,
                    onSongItemClick = {
                        viewModel.onUIEvents(UIEvents.SelectedAudioChange(it))
                        startMusicService()
                    },
                    onStart = {
                        viewModel.onUIEvents(UIEvents.PlayPause)
                    }
                )


            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val intent = Intent(this, MusicPlayerService::class.java)
        stopService(intent)
        isServiceRunning = false
    }

    private fun startMusicService() {
        if (!isServiceRunning) {
            val intent = Intent(this, MusicPlayerService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
            isServiceRunning = true
        }
    }
}
