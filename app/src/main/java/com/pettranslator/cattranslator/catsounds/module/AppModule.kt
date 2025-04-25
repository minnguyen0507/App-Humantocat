package com.pettranslator.cattranslator.catsounds.module

import android.content.Context
import com.pettranslator.cattranslator.catsounds.utils.MediaSoundPlayer
import com.pettranslator.cattranslator.catsounds.utils.SharedPref
import com.pettranslator.cattranslator.catsounds.utils.ad.AdManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAdManager(@ApplicationContext context: Context): AdManager {
        return AdManager(context)
    }

    @Provides
    @Singleton
    fun provideSharePref(@ApplicationContext context: Context): SharedPref {
        return SharedPref(context)
    }

    @Provides
    @Singleton
    fun provideMediaSoundPlayer(@ApplicationContext context: Context): MediaSoundPlayer {
        return MediaSoundPlayer(context)
    }
//    @Provides
//    @Singleton
//    fun provideNotificationWork(@ApplicationContext context: Context): MediaSoundPlayer {
//        return MediaSoundPlayer(context)
//    }

}
