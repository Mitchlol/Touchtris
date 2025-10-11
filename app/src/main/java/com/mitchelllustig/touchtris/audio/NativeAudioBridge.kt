package com.mitchelllustig.touchtris.audio

interface NativeAudioBridge {
    fun startAudioStreamNative(): Int
    fun stopAudioStreamNative(): Int
    fun buttonSoundNative()
    fun rotateSoundNative()
    fun moveSoundNative()
    fun dropSoundNative()
    fun lineSoundNative()
    fun quadSoundNative()
    fun holdSoundNative()
    fun deathSoundNative()
}

class NativeAudioBridgeImpl: NativeAudioBridge {
    init {
        System.loadLibrary("native-lib")
    }

    override external fun startAudioStreamNative(): Int
    override external fun stopAudioStreamNative(): Int
    override external fun buttonSoundNative()
    override external fun rotateSoundNative()
    override external fun moveSoundNative()
    override external fun dropSoundNative()
    override external fun lineSoundNative()
    override external fun quadSoundNative()
    override external fun holdSoundNative()
    override external fun deathSoundNative()
}
