package com.mitchelllustig.touchtris.settings

import android.content.SharedPreferences
import com.mitchelllustig.touchtris.audio.AudioPlayer

class AppSettings(val prefs: SharedPreferences) {
    var hapticEnabled: Boolean = true
    var zeroPosition: Boolean = true
    var displayGrid: Boolean = true
    var soundsEnabled: Boolean = true
    var highlightCenter: Boolean = true

    init {
        loadSettings()
        AudioPlayer.audioEnabled = soundsEnabled
    }


    fun loadSettings() {
        hapticEnabled = prefs.getBoolean("hapticEnabled", hapticEnabled)
        zeroPosition = prefs.getBoolean("zeroPosition", zeroPosition)
        displayGrid = prefs.getBoolean("displayGrid", displayGrid)
        soundsEnabled = prefs.getBoolean("soundsEnabled", soundsEnabled)
        highlightCenter = prefs.getBoolean("highlightCenter", highlightCenter)
    }

    fun saveSettings() {
        prefs.edit().apply {
            putBoolean("hapticEnabled", hapticEnabled)
            putBoolean("zeroPosition", zeroPosition)
            putBoolean("displayGrid", displayGrid)
            putBoolean("soundsEnabled", soundsEnabled)
            putBoolean("highlightCenter", highlightCenter)
            apply()
        }
    }
}