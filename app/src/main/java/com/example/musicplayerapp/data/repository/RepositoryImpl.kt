package com.example.musicplayerapp.data.repository

import com.example.musicplayerapp.data.data_models.SongsModelResponse
import com.example.musicplayerapp.network.APIInterface

class RepositoryImpl(
    private val apiInterface: APIInterface
) : Repository {
    override suspend fun getSongs(): SongsModelResponse {
        return apiInterface.getSongsData()
    }
}