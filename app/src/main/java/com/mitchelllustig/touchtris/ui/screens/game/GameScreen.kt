package com.mitchelllustig.touchtris.ui.screens.game

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.mitchelllustig.touchtris.data.GameBoard
import com.mitchelllustig.touchtris.data.Orientations
import com.mitchelllustig.touchtris.data.Pieces
import com.mitchelllustig.touchtris.data.Point
import com.mitchelllustig.touchtris.data.Positions
import com.mitchelllustig.touchtris.data.SquareColorInfo
import com.mitchelllustig.touchtris.ui.theme.TouchtrisTheme
import com.mitchelllustig.touchtris.ui.widgets.Background
import com.mitchelllustig.touchtris.ui.widgets.FancyText
import com.mitchelllustig.touchtris.ui.widgets.VerticalProgress
import kotlinx.coroutines.isActive

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToHighScore: (highScoreID: Long) -> Unit,
) {

    LifecycleResumeEffect(Unit) {
        // Nothing
        onPauseOrDispose {
            if(viewModel.gameState.value != GameState.GameOver) {
                // Save game & return to main menu
                viewModel.saveGameState()
                onNavigateBack()
            }
        }
    }

    LaunchedEffect(Unit) {
        var lastTime = 0L
        var gameRunning = true
        while (isActive && gameRunning) {
            withFrameNanos { time ->
                if (lastTime > 0L) {
                    val deltaTimeSec = (time - lastTime) / 1_000_000_000f
                    val deltaTimeMS = deltaTimeSec * 1000
                    viewModel.gameLoop(deltaTimeMS)
                }
                lastTime = time
                if(viewModel.gameState.value == GameState.GameOver
                    && viewModel.stateProgress.value == 1f
                    && !viewModel.isSavingHighScore
                ){
                    if(viewModel.highScoreID != null) {
                        onNavigateBack()
                        onNavigateToHighScore(viewModel.highScoreID!!)
                    }else{
                        onNavigateBack()
                    }
                    gameRunning = false
                }
            }
        }
    }

    GameScreenContent(
        hapticEnabledState = viewModel.hapticEnabled.collectAsState(),
        zeroPositionState = viewModel.zeroPosition.collectAsState(),
        displayGridState = viewModel.displayGrid.collectAsState(),
        gameStateState = viewModel.gameState.collectAsState(),
        currentPieceState = viewModel.currentPiece.collectAsState(),
        nextPiecesState = viewModel.nextPieces.collectAsState(),
        heldPieceState = viewModel.heldPiece.collectAsState(),
        holdLockState = viewModel.holdLock.collectAsState(),
        progressState = viewModel.stateProgress.collectAsState(),
        gameBoardState = viewModel.gameBoard.collectAsState(),
        ghostPositionState = viewModel.ghostPosition.collectAsState(),
        currentRotationState = viewModel.rotation.collectAsState(),
        onOrientationChange = viewModel::onRotation,
        clearingLinesState = viewModel.clearingLines.collectAsState(),
        currentPositionState = viewModel.movement.collectAsState(),
        onPositionChange = viewModel::onMovement,
        onHoldClick = viewModel::onHold,
        onDropClick = viewModel::onDrop,
        scoreState = viewModel.score.collectAsState(),
        levelState = viewModel.currentLevel.collectAsState(),
        linesState = viewModel.lines.collectAsState(),
        quadsState = viewModel.quads.collectAsState(),
        droughtState = viewModel.drought.collectAsState(),
        highlightCenter = viewModel.highlightCenter.collectAsState()
    )
}

@Composable
fun GameScreenContent(
    hapticEnabledState: State<Boolean>,
    zeroPositionState: State<Boolean>,
    displayGridState: State<Boolean>,
    gameStateState: State<GameState>,
    currentPieceState: State<Pieces>,
    nextPiecesState: State<List<Pieces>>,
    heldPieceState: State<Pieces?>,
    holdLockState: State<Boolean?>,
    progressState: State<Float>,
    gameBoardState: State<GameBoard>,
    clearingLinesState: State<List<Int>>,
    ghostPositionState: State<Point<Int>?>,
    currentRotationState: State<Orientations>,
    onOrientationChange: (Orientations) -> Unit,
    currentPositionState: State<Positions>,
    onPositionChange: (Positions) -> Unit,
    onHoldClick: () -> Unit,
    onDropClick: () -> Unit,
    scoreState: State<Long>,
    levelState: State<Int>,
    linesState: State<Int>,
    quadsState: State<Int>,
    droughtState: State<Int>,
    highlightCenter: State<Boolean>,
) {
    val gameState by gameStateState
    val configuration = LocalConfiguration.current
    Background()
    if(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
        Row{
            GameOutput(
                displayGridState = displayGridState,
                gameStateState = gameStateState,
                currentPieceState = currentPieceState,
                nextPiecesState = nextPiecesState,
                heldPieceState = heldPieceState,
                holdLockState = holdLockState,
                progressState = progressState,
                gameBoardState = gameBoardState,
                clearingLinesState = clearingLinesState,
                ghostPositionState = ghostPositionState,
                currentRotationState = currentRotationState,
                scoreState = scoreState,
                levelState = levelState,
                linesState = linesState,
                quadsState = quadsState,
                droughtState = droughtState,
                highlightCenter = highlightCenter,
                modifier = Modifier.weight(0.5f)
            )
            GameInput(
                hapticEnabledState = hapticEnabledState,
                zeroPositionState = zeroPositionState,
                gameStateState = gameStateState,
                currentPieceState = currentPieceState,
                holdLockState = holdLockState,
                currentRotationState = currentRotationState,
                onOrientationChange = onOrientationChange,
                currentPositionState = currentPositionState,
                onPositionChange = onPositionChange,
                onHoldClick = onHoldClick,
                onDropClick = onDropClick,
                modifier = Modifier.weight(0.5f)
            )
        }
    }else{
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            GameOutput(
                displayGridState = displayGridState,
                gameStateState = gameStateState,
                currentPieceState = currentPieceState,
                nextPiecesState = nextPiecesState,
                heldPieceState = heldPieceState,
                holdLockState = holdLockState,
                progressState = progressState,
                gameBoardState = gameBoardState,
                clearingLinesState = clearingLinesState,
                ghostPositionState = ghostPositionState,
                currentRotationState = currentRotationState,
                scoreState = scoreState,
                levelState = levelState,
                linesState = linesState,
                quadsState = quadsState,
                droughtState = droughtState,
                highlightCenter = highlightCenter,
                modifier = Modifier.weight(0.5f)
            )
            GameInput(
                hapticEnabledState = hapticEnabledState,
                zeroPositionState = zeroPositionState,
                gameStateState = gameStateState,
                currentPieceState = currentPieceState,
                holdLockState = holdLockState,
                currentRotationState = currentRotationState,
                onOrientationChange = onOrientationChange,
                currentPositionState = currentPositionState,
                onPositionChange = onPositionChange,
                onHoldClick = onHoldClick,
                onDropClick = onDropClick,
                modifier = Modifier.weight(0.5f)
            )
        }
    }

    GameOverOverlay(gameState)
}

@Composable
private fun GameOutput(
    displayGridState: State<Boolean>,
    gameStateState: State<GameState>,
    currentPieceState: State<Pieces>,
    nextPiecesState: State<List<Pieces>>,
    heldPieceState: State<Pieces?>,
    holdLockState: State<Boolean?>,
    progressState: State<Float>,
    gameBoardState: State<GameBoard>,
    clearingLinesState: State<List<Int>>,
    ghostPositionState: State<Point<Int>?>,
    currentRotationState: State<Orientations>,
    scoreState: State<Long>,
    levelState: State<Int>,
    linesState: State<Int>,
    quadsState: State<Int>,
    droughtState: State<Int>,
    highlightCenter: State<Boolean>,
    modifier: Modifier = Modifier
){
    val displayGrid by displayGridState
    val gameState by gameStateState
    val currentPiece by currentPieceState
    val nextPieces by nextPiecesState
    val heldPiece by heldPieceState
    val holdLock by holdLockState
    val progress by progressState
    val gameBoard by gameBoardState
    val clearingLines by clearingLinesState
    val ghostPosition by ghostPositionState
    val pieceOrientation by currentRotationState
    val score by scoreState
    val level by levelState
    val lines by linesState
    val quads by quadsState
    val drought by droughtState
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FancyText(
            text = score.toString()
        )
        Row(
            Modifier.aspectRatio(1f)
        ) {
            Box(modifier = Modifier.weight(0.25f), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.height(20.dp)) {}
                    FancyText(
                        text = "Level",
                        textSize = 20.sp
                    )
                    FancyText(
                        text = level.toString(),
                        textSize = 20.sp,
                    )
                    Box(modifier = Modifier.height(20.dp)) {}
                    FancyText(
                        text = "Lines",
                        textSize = 20.sp
                    )
                    FancyText(
                        text = lines.toString(),
                        textSize = 20.sp,
                    )
                    Box(modifier = Modifier.height(20.dp)) {}
                    FancyText(
                        text = "Rate",
                        textSize = 20.sp
                    )
                    FancyText(
                        text = "${if (lines == 0 ) "100.0" else "%.1f".format(((quads * 4f)/lines)*100f)} %",
                        textSize = 20.sp,
                    )
                    Box(modifier = Modifier.height(20.dp)) {}
                    FancyText(
                        text = "I-Wait",
                        textSize = 20.sp
                    )
                    FancyText(
                        text = drought.toString(),
                        textSize = 20.sp,
                    )
                }
            }
            Box(
                modifier = Modifier.weight(0.5f)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier.padding(vertical = 5.dp)
                ) {
                    GameBoardCanvas(
                        modifier = Modifier
                            .aspectRatio(0.49685535f)
                            .padding(0.dp),
                        gameBoard = gameBoard,
                        ghostPosition = ghostPosition,
                        currentPiece = currentPiece,
                        pieceOrientation = pieceOrientation,
                        level = level,
                        gameState = gameState,
                        highlightCenter = highlightCenter.value
                    )
                    if (displayGrid) {
                        GridCanvas(
                            modifier = Modifier
                                .aspectRatio(0.49685535f)
                                .padding(0.dp),
                            gameBoard
                        )
                    }
                    LineClearCanvas(
                        modifier = Modifier
                            .aspectRatio(0.49685535f)
                            .padding(0.dp),
                        gameState = gameState,
                        clearingLines = clearingLines,
                        progress = progress
                    )
                }
                CountdownOverlay(
                    gameState,
                    progress
                )
            }
            Box(
                modifier = Modifier
                    .weight(0.25f)
                    .fillMaxHeight()
            ) {
                Row {
                    VerticalProgress(
                        progress = if (gameState == GameState.Piece) progress else 1f,
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(5.dp),
                    )
                    NextPieces(nextPieces, level, heldPiece, holdLock)
                }
            }
        }

    }
}

@Composable
private fun GameInput(
    hapticEnabledState: State<Boolean>,
    zeroPositionState: State<Boolean>,
    gameStateState: State<GameState>,
    currentPieceState: State<Pieces>,
    holdLockState: State<Boolean?>,
    currentRotationState: State<Orientations>,
    onOrientationChange: (Orientations) -> Unit,
    currentPositionState: State<Positions>,
    onPositionChange: (Positions) -> Unit,
    onHoldClick: () -> Unit,
    onDropClick: () -> Unit,
    modifier: Modifier = Modifier
){
    val hapticEnabled by hapticEnabledState
    val zeroPosition by zeroPositionState
    val gameState by gameStateState
    val currentPiece by currentPieceState
    val holdLock by holdLockState
    val pieceOrientation by currentRotationState
    val piecePosition by currentPositionState

    Column(modifier = modifier) {
        OrientationInput(
            gameState,
            currentPiece,
            piecePosition,
            pieceOrientation,
            hapticEnabled,
            onOrientationChange
        )

        Box {
            PositionInput(
                gameState,
                currentPiece,
                piecePosition,
                pieceOrientation,
                hapticEnabled,
                zeroPosition,
                onPositionChange,
            )

            Column(
                modifier = Modifier.align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if(holdLock != null){
                    HoldButton(
                        modifier = Modifier
                            .width(100.dp),
                        gameState = gameState,
                        holdLock = holdLock == true,
                        onDrop = onHoldClick
                    )
                }
                DropButton(
                    modifier = Modifier
                        .width(150.dp),
                    gameState = gameState,
                    onDrop = onDropClick
                )
            }
        }
    }
}

@Composable
private fun GameScreenMock() {
    val dummyGameState = remember { mutableStateOf(GameState.Piece) }
    val dummyCurrentPiece = remember { mutableStateOf(Pieces.T) }
    val dummyHapticEnabledState = remember { mutableStateOf(true) }
    val dummyZeroPositionState = remember { mutableStateOf(false) }
    val dummyDisplayGridState = remember { mutableStateOf(true) }
    val dummyNextPieces = remember { mutableStateOf(listOf(Pieces.L, Pieces.S, Pieces.I, Pieces.T)) }
    val dummyHeldPieceState = remember { mutableStateOf<Pieces?>(Pieces.I) }
    val dummyHoldLockState = remember { mutableStateOf(false) }
    val dummyProgress = remember { mutableStateOf(0.6f) }
    val dummyGameBoardState = remember { mutableStateOf(GameBoard(20, 10)) }
    val dummyClearingLines = remember { mutableStateOf(listOf<Int>()) }
    val dummyGhostPosition = remember { mutableStateOf(Point(3, 4)) }
    val dummyRotation = remember { mutableStateOf(Orientations.zero) }
    val dummyMovement = remember { mutableStateOf(Positions.STARTING_POSITION) }
    val dummyScore = remember { mutableStateOf(12345L) }
    val dummyLevel = remember { mutableStateOf(9) }
    val dummyLines = remember { mutableStateOf(5) }
    val dummyQuads = remember { mutableStateOf(1) }
    val dummyDrought = remember { mutableStateOf(21) }
    val dummyHighlight = remember { mutableStateOf(true) }

    val colorInfo = SquareColorInfo(Color.Blue, Color.White, false)
    for (i in 0..4) {
        for (j in 0..4 - i) {
            dummyGameBoardState.value.squares[19 - i][4 - j] = colorInfo
            dummyGameBoardState.value.squares[19 - i][5 + j] = colorInfo
        }
    }

    TouchtrisTheme {
        GameScreenContent(
            hapticEnabledState = dummyHapticEnabledState,
            zeroPositionState = dummyZeroPositionState,
            displayGridState = dummyDisplayGridState,
            gameStateState = dummyGameState,
            currentPieceState = dummyCurrentPiece,
            nextPiecesState = dummyNextPieces,
            heldPieceState = dummyHeldPieceState,
            holdLockState = dummyHoldLockState,
            progressState = dummyProgress,
            gameBoardState = dummyGameBoardState,
            clearingLinesState = dummyClearingLines,
            ghostPositionState = dummyGhostPosition,
            currentRotationState = dummyRotation,
            onOrientationChange = { },
            currentPositionState = dummyMovement,
            onPositionChange = { },
            onHoldClick = { },
            onDropClick = { },
            scoreState = dummyScore,
            levelState = dummyLevel,
            linesState = dummyLines,
            quadsState = dummyQuads,
            droughtState = dummyDrought,
            highlightCenter = dummyHighlight
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, device = "spec:width=411dp,height=891dp,orientation=portrait")
@Composable
private fun GameScreenPreviewPortrait() {
    GameScreenMock()
}

@Preview(showBackground = true, showSystemUi = true, device = "spec:width=411dp,height=891dp,orientation=landscape")
@Composable
private fun GameScreenPreviewLandscape() {
    GameScreenMock()
}

@Preview(showBackground = true, showSystemUi = true, device = "spec:width=313dp,height=469dp,orientation=portrait")
@Composable
private fun GameScreenPreviewPortrait2() {
    GameScreenMock()
}