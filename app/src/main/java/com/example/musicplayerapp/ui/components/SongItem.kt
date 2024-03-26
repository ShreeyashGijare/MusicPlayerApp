package com.example.musicplayerapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musicplayerapp.data.data_models.Data
import com.example.musicplayerapp.utils.AppConstants

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