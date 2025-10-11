package com.mitchelllustig.touchtris.ui.screens

import androidx.lifecycle.ViewModel
import com.mitchelllustig.touchtris.audio.AudioPlayer
import com.mitchelllustig.touchtris.settings.AppSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

open class BaseViewModel @Inject constructor(
    private val appSettings: AppSettings,
): ViewModel() {

    private val _hapticEnabled = MutableStateFlow(appSettings.hapticEnabled)
    private val _zeroPosition = MutableStateFlow(appSettings.zeroPosition)
    private val _displayGrid = MutableStateFlow(appSettings.displayGrid)
    private val _soundsEnabled = MutableStateFlow(appSettings.soundsEnabled)

    val hapticEnabled = _hapticEnabled.asStateFlow()
    val zeroPosition = _zeroPosition.asStateFlow()
    val displayGrid = _displayGrid.asStateFlow()
    val soundsEnabled = _soundsEnabled.asStateFlow()

    fun onHapticEnabledChanged (value: Boolean){ _hapticEnabled.value = value}
    fun onZeroPositionChanged (value: Boolean){ _zeroPosition.value = value}
    fun onDisplayGridChanged (value: Boolean){ _displayGrid.value = value}
    fun onSoundsEnabledChanged (value: Boolean){
        _soundsEnabled.value = value
        AudioPlayer.audioEnabled = _soundsEnabled.value
    }

    fun refreshAppSettings(){
        appSettings.loadSettings()
        _hapticEnabled.value = appSettings.hapticEnabled
        _zeroPosition.value = appSettings.zeroPosition
        _displayGrid.value = appSettings.displayGrid
        _soundsEnabled.value = appSettings.soundsEnabled

    }

    fun saveAppSettings() {
        appSettings.hapticEnabled = hapticEnabled.value
        appSettings.zeroPosition = zeroPosition.value
        appSettings.displayGrid = displayGrid.value
        appSettings.soundsEnabled = soundsEnabled.value
        appSettings.saveSettings()
    }

}