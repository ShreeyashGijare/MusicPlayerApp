package com.example.musicplayerapp.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.musicplayerapp.R

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun LoadImageFromUrl(url: String) {
    GlideImage(
        model = url,
        contentDescription = "Song Cover",
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape),
        contentScale = ContentScale.FillBounds
    ) {
        it.error(R.drawable.ic_launcher_foreground)
            .placeholder(R.drawable.ic_launcher_foreground)
            .load(url)
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun LoadImageFromUrlLarge(url: String) {
    GlideImage(
        model = url,
        contentDescription = "Song Cover",
        modifier = Modifier
            .size(300.dp)
            .clip(RoundedCornerShape(4.dp)),
        contentScale = ContentScale.FillBounds
    ) {
        it.error(R.drawable.ic_launcher_foreground)
            .placeholder(R.drawable.ic_launcher_foreground)
            .load(url)
    }
}