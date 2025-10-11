package com.mitchelllustig.touchtris.audio

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

enum class PlayerState {
    Started, Stopped
}

object AudioPlayer: DefaultLifecycleObserver {
    @Volatile
    @VisibleForTesting
    var dispatcher = Dispatchers.Default

    private val coroutineScope get() = CoroutineScope(dispatcher + Job())
    var playerState = PlayerState.Stopped
    var audioEnabled = true

    var getNativeAudioBridge: () -> NativeAudioBridge = {
        NativeAudioBridgeImpl()
    }
    lateinit var nativeAudioBridge: NativeAudioBridge

    override fun onStart(owner: LifecycleOwner) {
        if (!::nativeAudioBridge.isInitialized){
            nativeAudioBridge = getNativeAudioBridge()
        }
        coroutineScope.launch {
            print("lolcat")
            nativeAudioBridge.startAudioStreamNative()
            playerState = PlayerState.Started
        }
        super.onStart(owner)
    }

    override fun onStop(owner: LifecycleOwner) {
        coroutineScope.launch {
            nativeAudioBridge.stopAudioStreamNative()
            playerState = PlayerState.Stopped
        }
        super.onStop(owner)
    }

    fun button(){
        if (playerState == PlayerState.Started && audioEnabled){
            nativeAudioBridge.buttonSoundNative()
        }
    }
    fun rotate(){
        if (playerState == PlayerState.Started && audioEnabled){
            nativeAudioBridge.rotateSoundNative()
        }
    }
    fun move(){
        if (playerState == PlayerState.Started && audioEnabled){
            nativeAudioBridge.moveSoundNative()
        }
    }
    fun drop(){
        if (playerState == PlayerState.Started && audioEnabled){
            nativeAudioBridge.dropSoundNative()
        }
    }
    fun line(){
        if (playerState == PlayerState.Started && audioEnabled){
            nativeAudioBridge.lineSoundNative()
        }
    }
    fun quad(){
        if (playerState == PlayerState.Started && audioEnabled){
            nativeAudioBridge.quadSoundNative()
        }
    }
    fun hold(){
        if (playerState == PlayerState.Started && audioEnabled){
            nativeAudioBridge.holdSoundNative()
        }
    }
    fun death(){
        if (playerState == PlayerState.Started && audioEnabled){
            nativeAudioBridge.deathSoundNative()
        }
    }
}
