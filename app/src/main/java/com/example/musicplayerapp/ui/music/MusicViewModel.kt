package com.example.musicplayerapp.ui.music

import android.net.Uri
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.musicplayerapp.connectivity.ConnectivityObserver
import com.example.musicplayerapp.data.data_models.Data
import com.example.musicplayerapp.data.repository.Repository
import com.example.musicplayerapp.player.service.MusicPlayerServiceHandler
import com.example.musicplayerapp.player.service.MusicPlayerState
import com.example.musicplayerapp.player.service.PlayerEvent
import com.example.musicplayerapp.utils.AppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

val dummyAudio = Data(
    accent = "",
    artist = "",
    cover = "",
    date_created = "",
    date_updated = "",
    id = -1,
    name = "",
    sort = -1,
    status = "",
    top_track = false,
    url = "",
    user_created = "",
    user_updated = ""
)

@HiltViewModel
class MusicViewModel @Inject constructor(
    private val musicPlayerServiceHandler: MusicPlayerServiceHandler,
    private val repository: Repository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var duration by savedStateHandle.saveable { mutableLongStateOf(0L) }
    var progress by savedStateHandle.saveable { mutableFloatStateOf(0F) }
    var progressString by savedStateHandle.saveable { mutableStateOf("00:00") }
    var isPlaying by savedStateHandle.saveable { mutableStateOf(false) }
    var currentSelectedAudio by savedStateHandle.saveable { mutableStateOf(dummyAudio) }
    var audioList by savedStateHandle.saveable { mutableStateOf(listOf<Data>()) }
    var isLoading by savedStateHandle.saveable { mutableStateOf(true) }
    var currentPlayingIndex by savedStateHandle.saveable { mutableIntStateOf(3) }

    private val _uiState: MutableStateFlow<UIState> = MutableStateFlow(UIState.Initial)
    val uiState: StateFlow<UIState> = _uiState

    init {
        loadAudioData()
    }

    init {
        viewModelScope.launch {
            musicPlayerServiceHandler.audioState.collectLatest { mediaState ->
                when (mediaState) {
                    is MusicPlayerState.Buffering -> calculateProgress(mediaState.progress)
                    is MusicPlayerState.CurrentMusic -> {
                        currentSelectedAudio = audioList[mediaState.mediaItemIndex]
                    }
                    MusicPlayerState.Initial -> _uiState.value = UIState.Initial
                    is MusicPlayerState.Playing -> isPlaying = mediaState.isPlaying
                    is MusicPlayerState.Progress -> calculateProgress(mediaState.progress)
                    is MusicPlayerState.Ready -> {
                        duration = mediaState.duration
                        _uiState.value = UIState.Ready
                    }
                }
            }
        }
    }

    private fun loadAudioData() {
        viewModelScope.launch {
            val audio = repository.getSongs()
            audioList = audio.data
            setMediaItems()
        }
    }

    private fun setMediaItems() {
        isLoading = false
        audioList.map { audio ->
            MediaItem.Builder()
                .setUri(audio.url)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setAlbumArtist(audio.artist)
                        .setDisplayTitle(audio.name)
                        .setSubtitle(audio.name)
                        .setArtist(audio.artist)
                        .setAlbumTitle(audio.name)
                        .setArtworkUri(Uri.parse(AppConstants.BASE_ASSETS_URL + audio.cover))
                        .build()
                )
                .build()
        }.also {
            musicPlayerServiceHandler.setMediaItemList(it)
        }
    }

    private fun calculateProgress(currentProgress: Long) {
        progress =
            if (currentProgress > 0) ((currentProgress.toFloat() / duration.toFloat()) * 100f)
            else 0f
        progressString = formatDuration(currentProgress)
    }

    fun formatDuration(duration: Long): String {
        val minute = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
        val seconds = (minute) - minute * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES)
        return String.format("%02d:%02d", minute, seconds)
    }

    fun onUIEvents(uiEvents: UIEvents) = viewModelScope.launch {
        when (uiEvents) {
            UIEvents.Backward -> musicPlayerServiceHandler.onPlayerEvents(PlayerEvent.BackWard)
            UIEvents.Forward -> musicPlayerServiceHandler.onPlayerEvents(PlayerEvent.Forward)
            UIEvents.PlayPause -> {
                musicPlayerServiceHandler.onPlayerEvents(PlayerEvent.PlayPause)
            }

            is UIEvents.SeekTo -> {
                musicPlayerServiceHandler.onPlayerEvents(
                    PlayerEvent.SeekTo,
                    seekToPosition = ((duration * uiEvents.position) / 100f).toLong()
                )
            }

            UIEvents.SeekToNext -> musicPlayerServiceHandler.onPlayerEvents(PlayerEvent.SeekToNext)
            is UIEvents.SelectedAudioChange -> {
                musicPlayerServiceHandler.onPlayerEvents(
                    PlayerEvent.SelectedAudioChange,
                    selectedAudio = uiEvents.index
                )
                currentPlayingIndex = uiEvents.index
            }

            is UIEvents.UpdateProgress -> {
                musicPlayerServiceHandler.onPlayerEvents(
                    PlayerEvent.UpdateProgress(
                        uiEvents.newProgress
                    )
                )
                progress = uiEvents.newProgress
            }
        }
    }


    override fun onCleared() {
        viewModelScope.launch {
            musicPlayerServiceHandler.onPlayerEvents(PlayerEvent.Stop)
        }
        super.onCleared()
    }

}

sealed class UIEvents {
    object PlayPause : UIEvents()
    data class SelectedAudioChange(val index: Int) : UIEvents()
    data class SeekTo(val position: Float) : UIEvents()
    object SeekToNext : UIEvents()
    object Backward : UIEvents()
    object Forward : UIEvents()
    data class UpdateProgress(val newProgress: Float) : UIEvents()
}

sealed class UIState {
    object Initial : UIState()
    object Ready : UIState()
}