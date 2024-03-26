package com.example.musicplayerapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun PlayerIconItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    color: Color = Color.White,
    iconTint: Color = Color.Black,
    onClick: () -> Unit
) {
    Surface(
        shape = CircleShape,
        modifier = Modifier
            .clip(CircleShape)
            .clickable {
                onClick()
            },
        color = color
    ) {
        Box(
            modifier = Modifier
                .padding(4.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                modifier = modifier,
                imageVector = icon,
                contentDescription = null,
                tint = iconTint
            )
        }
    }
}