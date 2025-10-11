package com.mitchelllustig.touchtris.ui.screens.highscore

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mitchelllustig.touchtris.database.HighScore
import com.mitchelllustig.touchtris.database.HighScoreDao
import com.mitchelllustig.touchtris.settings.AppSettings
import com.mitchelllustig.touchtris.ui.screens.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HighScoreViewModel @Inject constructor(
    appSettings: AppSettings,
    private val highScoreDao: HighScoreDao,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel(appSettings) {

    val id: Long = savedStateHandle["highScoreID"]!!
    val scores: Flow<List<HighScore>> = highScoreDao.findBySameGameModeAsId(id)
    val name = MutableStateFlow("")
    private val _submitting = MutableStateFlow(false)
    val submitting = _submitting.asStateFlow()

    fun updateScore(onCompleted: () -> Unit){
        if (name.value != "") {
            _submitting.value = true
            viewModelScope.launch {
                highScoreDao.updateNameById(id, name.value)
                _submitting.value = false
                onCompleted()
            }
        }else{
            onCompleted()
        }
    }
}