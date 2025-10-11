package com.mitchelllustig.touchtris.settings

import android.content.SharedPreferences

class GameSettings(val prefs: SharedPreferences) {
    var startingLevel: Int = -1
    var memoryCount: Int = -1
    var nextPieceCount: Int = 1
    var ghostPiece: Int = 1
    var holdPiece: Int = 0
    var randomBag: Int = 0

    init {
        loadSettings()
    }


    fun loadSettings() {
        startingLevel = prefs.getInt("startingLevel", startingLevel)
        memoryCount = prefs.getInt("memoryCount", memoryCount)
        nextPieceCount = prefs.getInt("nextPieceCount", nextPieceCount)
        ghostPiece = prefs.getInt("ghostPiece", ghostPiece)
        holdPiece = prefs.getInt("holdPiece", holdPiece)
        randomBag = prefs.getInt("randomBag", randomBag)
    }

    fun saveSettings() {
        prefs.edit().apply {
            putInt("startingLevel", startingLevel)
            putInt("memoryCount", memoryCount)
            putInt("nextPieceCount", nextPieceCount)
            putInt("ghostPiece", ghostPiece)
            putInt("holdPiece", holdPiece)
            putInt("randomBag", randomBag)
            apply()
        }
    }
}