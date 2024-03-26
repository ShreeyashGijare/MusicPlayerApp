package com.example.musicplayerapp.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun TextViewBold(text: String, modifier: Modifier = Modifier, fontSize: TextUnit = 17.sp) {
    Text(
        modifier = modifier,
        text = text,
        color = Color.White,
        fontSize = fontSize,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
}

@Composable
fun TextViewSemiBold(text: String, modifier: Modifier = Modifier, fontSize: TextUnit = 15.sp) {
    Text(
        modifier = modifier,
        text = text,
        color = Color.White,
        fontSize = fontSize,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center
    )
}

@Composable
fun TextViewNormal(text: String, modifier: Modifier = Modifier, fontSize: TextUnit = 13.sp) {
    Text(
        modifier = modifier,
        text = text,
        color = Color.White,
        fontSize = fontSize,
        fontWeight = FontWeight.Normal,
        textAlign = TextAlign.Center
    )
}