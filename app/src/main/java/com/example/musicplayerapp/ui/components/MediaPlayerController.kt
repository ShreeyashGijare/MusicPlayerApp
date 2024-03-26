package com.example.musicplayerapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musicplayerapp.data.data_models.Data
import com.example.musicplayerapp.ui.theme.Brown
import com.example.musicplayerapp.utils.AppConstants

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