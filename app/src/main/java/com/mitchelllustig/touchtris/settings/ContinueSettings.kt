package com.mitchelllustig.touchtris.settings

import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf

class ContinueSettings(val prefs: SharedPreferences) {
    var isContinueAvailable = mutableStateOf(false)

    init {
        loadSettings()
    }

    fun loadSettings() {
        isContinueAvailable.value = prefs.getBoolean("isContinueAvailable", false)
    }

    fun clearGameProgress() {
        prefs.edit().apply {
            putBoolean("isContinueAvailable", false)
            remove("saveGame")
            apply()
        }
        isContinueAvailable.value = false
    }

    fun saveGameProgress(saveGame: String) {
        prefs.edit().apply {
            putString("saveGame", saveGame)
            putBoolean("isContinueAvailable", true)
            apply()
        }
        isContinueAvailable.value = true
    }

    fun loadGameProgress(): String {
        val saveGameJson = prefs.getString("saveGame", null)!!
        prefs.edit().apply {
            putBoolean("isContinueAvailable", false)
            remove("saveGame")
            apply()
        }
        this.isContinueAvailable.value = false
        return saveGameJson
    }
}
