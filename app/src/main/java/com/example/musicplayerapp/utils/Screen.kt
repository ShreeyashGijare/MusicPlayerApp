package com.example.musicplayerapp.utils

sealed class Screen(val route: String) {

    object ForYouScreen: Screen("ForYou")
    object TopTracks: Screen("TopTracks")

    object MusicPlayerScreen: Screen("MusicPlayerScreen")
}