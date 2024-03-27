package com.example.musicplayerapp

import HomeScreen
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
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


        /*installSplashScreen().apply {

            setKeepOnScreenCondition {
                !viewModel.isReady.value
            }
            setOnExitAnimationListener { screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.4f,
                    0.0f
                )
                zoomX.interpolator = OvershootInterpolator()
                zoomX.duration = 500L
                zoomX.doOnEnd { screen.remove() }

                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.4f,
                    0.0f
                )
                zoomY.interpolator = OvershootInterpolator()
                zoomY.duration = 500L
                zoomY.doOnEnd { screen.remove() }

                zoomX.start()
                zoomY.start()
            }
        }*/

        lifecycleScope.launchWhenStarted {
            viewModel.isConnected.collect {
                if (it) {
                    viewModel.loadAudioData()
                }
            }
        }

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
                    },
                    onMediaPlayerClick = {



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

