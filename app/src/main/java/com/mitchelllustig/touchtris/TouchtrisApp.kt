package com.mitchelllustig.touchtris

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.mitchelllustig.touchtris.audio.AudioPlayer
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TouchtrisApp: Application() {
    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(AudioPlayer)
    }
}