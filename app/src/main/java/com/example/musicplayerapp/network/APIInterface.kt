package com.example.musicplayerapp.network

import com.example.musicplayerapp.data.data_models.SongsModelResponse
import retrofit2.http.GET

interface APIInterface {

    @GET("items/songs")
    suspend fun getSongsData(): SongsModelResponse

}