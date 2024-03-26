package com.example.musicplayerapp.data.data_models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class SongsModelResponse(
    val data: List<Data>
) : Parcelable