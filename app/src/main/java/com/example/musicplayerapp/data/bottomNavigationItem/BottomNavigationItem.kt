package com.example.musicplayerapp.data.bottomNavigationItem

import com.example.musicplayerapp.utils.Screen

data class BottomNavigationItem(
    val title: String,
    val route: String
) {


    companion object {
        fun getItemList() = listOf(
            BottomNavigationItem(
                title = "For You",
                route = Screen.ForYouScreen.route
            ),
            BottomNavigationItem(
                title = "Top Tracks",
                route = Screen.TopTracks.route
            )

        )
    }

}