package com.mitchelllustig.touchtris.ui.screens.gamemenu

import androidx.lifecycle.viewModelScope
import com.mitchelllustig.touchtris.settings.AppSettings
import com.mitchelllustig.touchtris.settings.GameSettings
import com.mitchelllustig.touchtris.ui.screens.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameMenuViewModel @Inject constructor(
    private val appSettings: AppSettings,
    private val gameSettings: GameSettings,
) : BaseViewModel(appSettings) {
    val startingLevel = MutableStateFlow(gameSettings.startingLevel)
    val memoryCount = MutableStateFlow(gameSettings.memoryCount)
    val nextPieceCount = MutableStateFlow(gameSettings.nextPieceCount)
    val ghostPiece = MutableStateFlow(gameSettings.ghostPiece)
    val holdPiece = MutableStateFlow(gameSettings.holdPiece)
    val randomBag = MutableStateFlow(gameSettings.randomBag)

    private val _startGameEvents = MutableSharedFlow<Boolean>()
    val startGameEvents = _startGameEvents.asSharedFlow()


    fun saveSettings() {
        gameSettings.startingLevel = startingLevel.value
        gameSettings.memoryCount = memoryCount.value
        gameSettings.nextPieceCount = nextPieceCount.value
        gameSettings.ghostPiece = ghostPiece.value
        gameSettings.holdPiece = holdPiece.value
        gameSettings.randomBag = randomBag.value
        gameSettings.saveSettings()
    }

    fun startGame() {
        saveSettings()
        viewModelScope.launch {
            _startGameEvents.emit(true)
        }
    }

    override fun onCleared() {
        super.onCleared()
        saveSettings()
    }
}