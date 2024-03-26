package com.example.musicplayerapp.data.data_models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Data(
    val accent: String,
    val artist: String,
    val cover: String,
    val date_created: String,
    val date_updated: String,
    val id: Int,
    val name: String,
    val sort: Int,
    val status: String,
    val top_track: Boolean,
    val url: String,
    val user_created: String,
    val user_updated: String
) : Parcelable