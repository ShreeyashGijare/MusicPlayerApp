package com.example.musicplayerapp.di

import android.annotation.SuppressLint
import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.session.MediaSession
import com.example.musicplayerapp.connectivity.ConnectivityObserver
import com.example.musicplayerapp.connectivity.NetworkConnectivityObserver
import com.example.musicplayerapp.data.repository.Repository
import com.example.musicplayerapp.data.repository.RepositoryImpl
import com.example.musicplayerapp.network.APIClient
import com.example.musicplayerapp.network.APIInterface
import com.example.musicplayerapp.player.notification.MusicPlayerNotificationManager
import com.example.musicplayerapp.player.service.MusicPlayerServiceHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesAPIClient(): APIClient = APIClient()

    @Provides
    @Singleton
    fun providesAPIInterface(apiClient: APIClient): APIInterface =
        apiClient.getClient()!!.create(APIInterface::class.java)

    @Provides
    @Singleton
    fun providesRepository(apiInterface: APIInterface): Repository = RepositoryImpl(apiInterface)


    @SuppressLint("WrongConstant")
    @Provides
    @Singleton
    fun providesAudioAttributes(): AudioAttributes = AudioAttributes.Builder()
        .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
        .setUsage(C.USAGE_MEDIA)
        .build()


    @UnstableApi
    @Provides
    @Singleton
    fun providesExoPlayer(
        @ApplicationContext context: Context,
        audioAttributes: AudioAttributes
    ): ExoPlayer = ExoPlayer.Builder(context)
        .setAudioAttributes(audioAttributes, true)
        .setHandleAudioBecomingNoisy(true)
        .setTrackSelector(DefaultTrackSelector(context))
        .build()

    @Provides
    @Singleton
    fun providesMediaSession(
        @ApplicationContext context: Context,
        player: ExoPlayer
    ): MediaSession = MediaSession.Builder(context, player).build()

    @Provides
    @Singleton
    fun providesNotificationManager(
        @ApplicationContext context: Context,
        player: ExoPlayer
    ): MusicPlayerNotificationManager = MusicPlayerNotificationManager(context, player)

    @Provides
    @Singleton
    fun providesServiceHandler(
        player: ExoPlayer
    ): MusicPlayerServiceHandler = MusicPlayerServiceHandler(player)

    @Provides
    @Singleton
    fun providesConnectivityObserver(
        @ApplicationContext context: Context
    ): ConnectivityObserver = NetworkConnectivityObserver(context)

}