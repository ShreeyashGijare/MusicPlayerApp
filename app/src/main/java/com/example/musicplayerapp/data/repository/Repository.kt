package com.example.musicplayerapp.data.repository

import com.example.musicplayerapp.data.data_models.SongsModelResponse

interface Repository {

    suspend fun getSongs(): SongsModelResponse

}