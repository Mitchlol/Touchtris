package com.mitchelllustig.touchtris.ui.screens.highscores

import androidx.annotation.VisibleForTesting
import com.mitchelllustig.touchtris.database.HighScore
import com.mitchelllustig.touchtris.database.HighScoreDao
import com.mitchelllustig.touchtris.settings.AppSettings
import com.mitchelllustig.touchtris.settings.GameSettings
import com.mitchelllustig.touchtris.ui.screens.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import kotlin.text.get

@HiltViewModel
class HighScoresViewModel @Inject constructor(
    appSettings: AppSettings,
    gameSettings: GameSettings,
    private val highScoreDao: HighScoreDao
) : BaseViewModel(appSettings) {

    val memoryCount: MutableStateFlow<Int> = MutableStateFlow(0)
    val nextPieceCount: MutableStateFlow<Int> = MutableStateFlow(0)
    val ghostPiece: MutableStateFlow<Int> = MutableStateFlow(0)
    val holdPiece: MutableStateFlow<Int> = MutableStateFlow(0)
    val randomBag: MutableStateFlow<Int> = MutableStateFlow(0)

    val scoresQuerying: MutableStateFlow<Boolean> = MutableStateFlow(false)

    @VisibleForTesting
    var dispatcher: CoroutineDispatcher = Dispatchers.IO

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val scores: Flow<List<HighScore>> = combine(
        memoryCount, nextPieceCount, ghostPiece, holdPiece, randomBag
    ) { memory, next, ghost, hold, bag ->
        mapOf(
            "memoryCount" to memory,
            "nextPieceCount" to next,
            "ghostPiece" to ghost,
            "holdPiece" to hold,
            "randomBag" to bag
        )
    }.debounce(5).distinctUntilChanged().flatMapLatest { map ->
        highScoreDao.findByGameModeFlow(
            memoryCount = map["memoryCount"] ?: 0,
            nextPieceCount = map["nextPieceCount"] ?: 0,
            ghostPiece = map["ghostPiece"] ?: 0,
            holdPiece = map["holdPiece"] ?: 0,
            randomBag = map["randomBag"] ?: 0,
        ).onStart {
            scoresQuerying.value = true
        }.onEach {
            scoresQuerying.value = false
        }.onCompletion {
            scoresQuerying.value = false
        }.flowOn(dispatcher)
    }


    init {
        gameSettings.loadSettings()
        memoryCount.value = gameSettings.memoryCount
        nextPieceCount.value = gameSettings.nextPieceCount
        ghostPiece.value = gameSettings.ghostPiece
        holdPiece.value = gameSettings.holdPiece
        randomBag.value = gameSettings.randomBag
    }

}