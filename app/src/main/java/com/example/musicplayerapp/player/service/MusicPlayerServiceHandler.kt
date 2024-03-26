package com.example.musicplayerapp.player.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class MusicPlayerServiceHandler @Inject constructor(
    private val exoPlayer: ExoPlayer
) : Player.Listener {

    private val _audioState: MutableStateFlow<MusicPlayerState> =
        MutableStateFlow(MusicPlayerState.Initial)
    val audioState: StateFlow<MusicPlayerState> = _audioState.asStateFlow()

    private var job: Job? = null

    init {
        exoPlayer.addListener(this)
    }

    fun setMediaItemList(mediaItem: List<MediaItem>) {
        exoPlayer.setMediaItems(mediaItem)
        exoPlayer.prepare()
    }

    suspend fun onPlayerEvents(
        playerEvent: PlayerEvent,
        selectedAudio: Int = -1,
        seekToPosition: Long = 0
    ) {
        when (playerEvent) {
            PlayerEvent.BackWard -> exoPlayer.seekBack()
            PlayerEvent.Forward -> exoPlayer.seekForward()
            PlayerEvent.PlayPause -> playOrPause()
            PlayerEvent.SeekTo -> exoPlayer.seekTo(seekToPosition)
            PlayerEvent.SeekToNext -> exoPlayer.seekToNext()
            PlayerEvent.SelectedAudioChange -> {
                when (selectedAudio) {
                    exoPlayer.currentMediaItemIndex -> {
                        playOrPause()
                    }

                    else -> {
                        exoPlayer.seekToDefaultPosition(selectedAudio)
                        _audioState.value = MusicPlayerState.Playing(isPlaying = true)
                        exoPlayer.playWhenReady = true
                        startProgressUpdate()
                    }
                }
            }

            PlayerEvent.Stop -> stopProgressUpdate()
            is PlayerEvent.UpdateProgress -> {
                exoPlayer.seekTo(
                    (exoPlayer.duration * playerEvent.newProgress).toLong()
                )
            }
        }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            ExoPlayer.STATE_BUFFERING -> _audioState.value =
                MusicPlayerState.Buffering(exoPlayer.currentPosition)

            ExoPlayer.STATE_READY -> _audioState.value =
                MusicPlayerState.Ready(exoPlayer.duration)
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        _audioState.value = MusicPlayerState.Playing(isPlaying = isPlaying)
        _audioState.value = MusicPlayerState.CurrentMusic(exoPlayer.currentMediaItemIndex)
        if (isPlaying) {
            GlobalScope.launch(Dispatchers.Main) {
                startProgressUpdate()
            }
        } else {
            stopProgressUpdate()
        }
    }


    private suspend fun playOrPause() {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
            _audioState.value = MusicPlayerState.Playing(isPlaying = false)
            stopProgressUpdate()
        } else {
            exoPlayer.play()
            _audioState.value = MusicPlayerState.Playing(isPlaying = true)
            startProgressUpdate()
        }
    }

    private suspend fun startProgressUpdate() = job?.run {
        while (true) {
            delay(500L)
            _audioState.value = MusicPlayerState.Progress(exoPlayer.currentPosition)
        }
    }

    private fun stopProgressUpdate() {
        job?.cancel()
        _audioState.value = MusicPlayerState.Playing(isPlaying = false)
    }
}


sealed class PlayerEvent {
    object PlayPause : PlayerEvent()
    object SelectedAudioChange : PlayerEvent()
    object BackWard : PlayerEvent()
    object SeekToNext : PlayerEvent()
    object Forward : PlayerEvent()
    object SeekTo : PlayerEvent()
    object Stop : PlayerEvent()
    data class UpdateProgress(val newProgress: Float) : PlayerEvent()
}

sealed class MusicPlayerState {
    object Initial : MusicPlayerState()
    data class Ready(val duration: Long) : MusicPlayerState()
    data class Progress(val progress: Long) : MusicPlayerState()
    data class Buffering(val progress: Long) : MusicPlayerState()
    data class Playing(val isPlaying: Boolean) : MusicPlayerState()
    data class CurrentMusic(val mediaItemIndex: Int) : MusicPlayerState()
}