package com.mitchelllustig.touchtris.ui.screens.game

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mitchelllustig.touchtris.audio.AudioPlayer
import com.mitchelllustig.touchtris.data.FallSpeeds
import com.mitchelllustig.touchtris.data.GameBoard
import com.mitchelllustig.touchtris.data.Orientations
import com.mitchelllustig.touchtris.data.Pieces
import com.mitchelllustig.touchtris.data.Point
import com.mitchelllustig.touchtris.data.Positions
import com.mitchelllustig.touchtris.data.SquareColorInfo
import com.mitchelllustig.touchtris.data.getColorInfo
import com.mitchelllustig.touchtris.data.getScoreForLineClear
import com.mitchelllustig.touchtris.database.HighScore
import com.mitchelllustig.touchtris.database.HighScoreDao
import com.mitchelllustig.touchtris.settings.AppSettings
import com.mitchelllustig.touchtris.settings.ContinueSettings
import com.mitchelllustig.touchtris.settings.GameSettings
import com.mitchelllustig.touchtris.ui.screens.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import kotlinx.serialization.serializer
import java.lang.Math.random
import javax.inject.Inject
import kotlin.math.min

enum class GameState {
    Init,
    Countdown,
    Prepiece,
    Piece,
    LineClear,
    GameOver
}

@HiltViewModel
class GameViewModel @Inject constructor(
    val appSettings: AppSettings,
    gameSettings: GameSettings,
    private val continueSettings: ContinueSettings,
    private val highScoreDao: HighScoreDao,
    val savedStateHandle: SavedStateHandle
) : BaseViewModel(appSettings) {

    // Read game settings
    private var startingLevel = gameSettings.startingLevel
    private var memoryCount = gameSettings.memoryCount
    private var nextPieceCount = gameSettings.nextPieceCount
    private var ghostPiece = gameSettings.ghostPiece
    private var holdPiece = gameSettings.holdPiece
    private var randomBag = gameSettings.randomBag

    private val gb = GameBoard()
    private val _gameBoard = MutableStateFlow(gb)
    private val _currentLevel = MutableStateFlow(0)
    private val _score = MutableStateFlow(0L)
    private val _lines = MutableStateFlow(0)
    private val _quads = MutableStateFlow(0)
    private val _drought = MutableStateFlow(0)
    private val _gameState = MutableStateFlow(GameState.Init)
    private val _stateProgress = MutableStateFlow(0.3f)
    private val _currentPiece = MutableStateFlow(Pieces.I)
    private val _nextPieces = MutableStateFlow(mutableListOf(Pieces.I))
    private val _heldPiece = MutableStateFlow<Pieces?>(null)
    private val _holdLock = MutableStateFlow<Boolean?>(null)
    private val _clearingLines = MutableStateFlow(listOf<Int>())
    private val _rotation = MutableStateFlow(Orientations.zero)
    private val _movement = MutableStateFlow(Positions.STARTING_POSITION)
    private val _ghostPosition = MutableStateFlow<Point<Int>?>(null)

    val gameBoard = _gameBoard.asStateFlow()
    val currentLevel = _currentLevel.asStateFlow()
    val score = _score.asStateFlow()
    val lines = _lines.asStateFlow()
    val quads = _quads.asStateFlow()
    val drought = _drought.asStateFlow()
    val gameState = _gameState.asStateFlow()
    val stateProgress = _stateProgress.asStateFlow()
    val currentPiece = _currentPiece.asStateFlow()
    val nextPieces = _nextPieces.asStateFlow()
    val heldPiece = _heldPiece.asStateFlow()
    val holdLock = _holdLock.asStateFlow()
    val clearingLines = _clearingLines.asStateFlow()
    val rotation = _rotation.asStateFlow()
    val movement = _movement.asStateFlow()
    val ghostPosition = _ghostPosition.asStateFlow()

    private var currentPieceTime = 0f
    private var levelPieceTime = 16000.0f
    private val bag = mutableListOf<Pieces>()
    var highScoreID: Long? = null
        private set
    var isSavingHighScore = false
        private set

    fun gameLoop(deltaTimeMS: Float) {
        when (_gameState.value) {
            GameState.Init -> {
                if (savedStateHandle["continue"]?: false){
                    loadGameState()
                }else {
                    continueSettings.clearGameProgress()
                    _currentLevel.value = startingLevel
                    _currentPiece.value = Pieces.entries[(random() * Pieces.entries.size).toInt()]
                    if (_currentPiece.value != Pieces.I) {
                        _drought.value = 1
                    }
                    val generatedNextPieces = mutableListOf<Pieces>()
                    for (i in 0 until nextPieceCount) {
                        generatedNextPieces.add(getRandomPiece())
                    }
                    _nextPieces.value = generatedNextPieces
                    _heldPiece.value = null
                    if (holdPiece == 1) {
                        _holdLock.value = false
                    }
                    _gameState.value = GameState.Countdown
                }
            }

            GameState.Countdown -> {
                currentPieceTime += deltaTimeMS
                levelPieceTime = 2000f
                _stateProgress.value = currentPieceTime / levelPieceTime
                if (currentPieceTime >= levelPieceTime) {
                    currentPieceTime -= levelPieceTime
                    _gameState.value = GameState.Prepiece
                }
            }

            GameState.Prepiece -> {
                currentPieceTime += deltaTimeMS
                levelPieceTime = 166.666f
                _stateProgress.value = 0f
                if (currentPieceTime >= levelPieceTime) {
                    currentPieceTime -= levelPieceTime
                    currentPieceTime += (1 - gb.getAvailabilty()) * levelPieceTime
                    _gameState.value = GameState.Piece
                }
            }

            GameState.Piece -> {
                // Calculate time & progress
                currentPieceTime += deltaTimeMS
                levelPieceTime = FallSpeeds.getSpeed(_currentLevel.value) * 20
                _stateProgress.value = currentPieceTime / levelPieceTime

                // Update Ghost
                updateGhost()

                // If piece time is over, lock it into place and proceed
                if (currentPieceTime >= levelPieceTime) {
                    val colorInfo = getColorInfo(_currentPiece.value, _currentLevel.value, memoryCount)
                    // Add piece to board
                    val added = gb.addPiece(
                        _currentPiece.value,
                        rotation.value,
                        movement.value.ordinal,
                        colorInfo
                    )
                    // If piece fails to add it is game over
                    if (!added) {
                        currentPieceTime = 0f
                        _gameState.value = GameState.GameOver
                        return
                    }

                    // Check clear lines & enter line clear state if we did
                    _clearingLines.value = gb.checkClearLines()
                    if (_clearingLines.value.size > 0){
                        currentPieceTime -= levelPieceTime
                        _gameState.value = GameState.LineClear
                        _lines.value = _lines.value + _clearingLines.value.size
                        _score.value = _score.value + getScoreForLineClear(_clearingLines.value.size, _currentLevel.value)
                        if(_clearingLines.value.size == 4){
                            _quads.value = _quads.value + 1
                            AudioPlayer.quad()
                        }else{
                            AudioPlayer.line()
                        }
                    }else{
                        // Move to next Piece
                        proceedToNextPiece()
                        AudioPlayer.drop()
                    }
                }
            }

            GameState.LineClear -> {
                // Calculate time & progress
                currentPieceTime += deltaTimeMS
                levelPieceTime = 466.6667f
                _stateProgress.value = currentPieceTime / levelPieceTime

                // End line clearing state, increment level if needed
                if (currentPieceTime >= levelPieceTime) {
                    gb.clearLines()
                    if(_lines.value >= (_currentLevel.value + 1) * 10){
                        _currentLevel.value = _currentLevel.value + 1
                    }
                    proceedToNextPiece()
                }
            }
            GameState.GameOver -> {
                if(currentPieceTime == 0f){
                    isSavingHighScore = true
                    viewModelScope.launch {
                        val highScores = highScoreDao.findByGameMode(memoryCount, nextPieceCount, ghostPiece, holdPiece, randomBag)
                        if(highScores.size == 5 && highScores.last().score < _score.value){
                            highScoreDao.delete(highScores.last())
                        }
                        if (highScores.size < 5 || highScores.last().score < _score.value){
                            highScoreID = highScoreDao.insert(
                                HighScore(
                                    timestamp = System.currentTimeMillis(),
                                    name = "----------",
                                    score = _score.value,
                                    level = _currentLevel.value,
                                    memoryCount = memoryCount,
                                    nextPieceCount = nextPieceCount,
                                    ghostPiece = ghostPiece,
                                    holdPiece = holdPiece,
                                    randomBag = randomBag
                                )
                            )
                        }
                        isSavingHighScore = false
                    }
                    AudioPlayer.death()
                }
                currentPieceTime += deltaTimeMS
                _stateProgress.value = min(currentPieceTime / 1000, 1f)
            }
        }
    }

    fun onRotation(rotation: Orientations){
        _rotation.value = rotation
        AudioPlayer.rotate()
    }

    fun onMovement(movement: Positions){
        _movement.value = movement
        AudioPlayer.move()
    }

    fun onDrop() {
        _score.value = _score.value + ((1-(currentPieceTime/levelPieceTime)) * 20).toInt()
        currentPieceTime += levelPieceTime - currentPieceTime
    }

    fun onHold() {
        if (holdPiece == 1) {
            currentPieceTime = (1 - gb.getAvailabilty()) * levelPieceTime
            _holdLock.value = true
            _rotation.value = Orientations.zero
            _movement.value = Positions.STARTING_POSITION
            if (_heldPiece.value == null) {
                _heldPiece.value = _currentPiece.value
                _currentPiece.value = _nextPieces.value.removeAt(0)
                _nextPieces.value = _nextPieces.value.apply { add(getRandomPiece()) }
                if(_currentPiece.value != Pieces.I){
                    _drought.value = _drought.value + 1
                }else{
                    _drought.value = 0
                }
            } else {
                val temp = _heldPiece.value!!
                _heldPiece.value = _currentPiece.value
                _currentPiece.value = temp
            }
            updateGhost()
            AudioPlayer.hold()
        }
    }

    fun proceedToNextPiece() {
        currentPieceTime -= levelPieceTime
        if(_nextPieces.value.size == 0){
            _currentPiece.value = getRandomPiece()
        }else {
            _currentPiece.value = _nextPieces.value.removeAt(0)
            _nextPieces.value = _nextPieces.value.apply { add(getRandomPiece()) }
        }
        if (_holdLock.value != null) _holdLock.value = false
        _rotation.value = Orientations.zero
        _movement.value = Positions.STARTING_POSITION
        _gameState.value = GameState.Prepiece
        _clearingLines.value = listOf(0)
        if(_currentPiece.value != Pieces.I){
            _drought.value = _drought.value + 1
        }else{
            _drought.value = 0
        }
        updateGhost()
    }

    fun updateGhost(){
        if (ghostPiece == 1) {
            _ghostPosition.value = Point(
                movement.value.ordinal,
                gb.getGhostHeight(
                    _currentPiece.value,
                    rotation.value,
                    movement.value.ordinal
                )
            )
        }
    }

    private fun getRandomPiece(): Pieces{
        if(randomBag == 1){
            if(bag.size == 0){
                bag.addAll(Pieces.entries)
            }
            val piece = bag.random()
            bag.remove(piece)
            return piece
        }else{
            return Pieces.entries.random()
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun saveGameState(){
        val saveGame = buildJsonObject {
            // Game Settings
            put("startingLevel", JsonPrimitive(startingLevel))
            put("memoryCount", JsonPrimitive(memoryCount))
            put("nextPieceCount", JsonPrimitive(nextPieceCount))
            put("ghostPiece", JsonPrimitive(ghostPiece))
            put("holdPiece", JsonPrimitive(holdPiece))
            put("randomBag", JsonPrimitive(randomBag))
            // Game State
            put("currentLevel", JsonPrimitive(_currentLevel.value))
            put("score", JsonPrimitive(_score.value))
            put("lines", JsonPrimitive(_lines.value))
            put("quads", JsonPrimitive(_quads.value))
            put("drought", JsonPrimitive(_drought.value))
            put("currentPiece", JsonPrimitive(_currentPiece.value.ordinal))
            put("nextPieces", JsonArray(_nextPieces.value.map { JsonPrimitive(it.ordinal) }))
            put("heldPiece", _heldPiece.value?.ordinal?.let { JsonPrimitive(it) } ?: JsonNull)
            put("holdLock", _holdLock.value?.let { JsonPrimitive(it) } ?: JsonNull)
            put("bag", JsonArray(bag.map { JsonPrimitive(it.ordinal) }))
            put("squares", JsonArray(_gameBoard.value.squares.map { row ->
                JsonArray(row.map { square ->
                    Json.Default.encodeToJsonElement(serializer<SquareColorInfo?>(), square)
                })
            }))
        }
        continueSettings.saveGameProgress(saveGame.toString())
    }

    fun loadGameState(){
        val savedGameJson = continueSettings.loadGameProgress()


        val jsonObject = Json.parseToJsonElement(savedGameJson).jsonObject

        // Game Settings
        startingLevel = jsonObject["startingLevel"]!!.jsonPrimitive.int
        memoryCount = jsonObject["memoryCount"]!!.jsonPrimitive.int
        nextPieceCount = jsonObject["nextPieceCount"]!!.jsonPrimitive.int
        ghostPiece = jsonObject["ghostPiece"]!!.jsonPrimitive.int
        holdPiece = jsonObject["holdPiece"]!!.jsonPrimitive.int
        randomBag = jsonObject["randomBag"]!!.jsonPrimitive.int
        // Game State
        _currentLevel.value =  jsonObject["currentLevel"]!!.jsonPrimitive.int
        _score.value = jsonObject["score"]!!.jsonPrimitive.long
        _lines.value = jsonObject["lines"]!!.jsonPrimitive.int
        _quads.value = jsonObject["quads"]!!.jsonPrimitive.int
        _drought.value = jsonObject["drought"]!!.jsonPrimitive.int
        _currentPiece.value = Pieces.entries[jsonObject["currentPiece"]!!.jsonPrimitive.int]
        _nextPieces.value = jsonObject["nextPieces"]!!.jsonArray.map { Pieces.entries[it.jsonPrimitive.int] }.toMutableList()
        _heldPiece.value = if (jsonObject["heldPiece"]!! is JsonNull) null else Pieces.entries[jsonObject["heldPiece"]!!.jsonPrimitive.int]
        _holdLock.value = if (jsonObject["holdLock"]!! is JsonNull) null else jsonObject["holdLock"]!!.jsonPrimitive.boolean
        bag.addAll(jsonObject["bag"]!!.jsonArray.map { Pieces.entries[it.jsonPrimitive.int] })

        jsonObject["squares"]!!.jsonArray.forEachIndexed{ rowIndex, row ->
            row.jsonArray.forEachIndexed{ squareIndex, square ->
                gb.squares[rowIndex][squareIndex] = Json.Default.decodeFromJsonElement<SquareColorInfo?>(serializer<SquareColorInfo?>(), square)
            }
        }

        _gameState.value = GameState.Countdown
    }

}
