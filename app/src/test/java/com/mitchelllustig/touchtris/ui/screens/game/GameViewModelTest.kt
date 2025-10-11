package com.mitchelllustig.touchtris.ui.screens.game

import androidx.lifecycle.SavedStateHandle
import com.mitchelllustig.touchtris.database.HighScore
import com.mitchelllustig.touchtris.database.HighScoreDao
import com.mitchelllustig.touchtris.settings.AppSettings
import com.mitchelllustig.touchtris.settings.ContinueSettings
import com.mitchelllustig.touchtris.settings.GameSettings
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GameViewModelTest {

    private lateinit var appSettings: AppSettings
    private lateinit var gameSettings: GameSettings
    private lateinit var continueSettings: ContinueSettings
    private lateinit var highScoreDao: HighScoreDao
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: GameViewModel

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        appSettings = mockk(relaxed = true)
        gameSettings = mockk(relaxed = true) {
            every { startingLevel } returns 5
            every { nextPieceCount } returns 3
            every { holdPiece } returns 1
        }
        continueSettings = mockk(relaxed = true)
        highScoreDao = mockk(relaxed = true)
        savedStateHandle = SavedStateHandle()

        viewModel = GameViewModel(appSettings, gameSettings, continueSettings, highScoreDao, savedStateHandle)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init starts countdown and sets up initial pieces`() = testScope.runTest {
        // Initial state should be Init
        assertEquals(GameState.Init, viewModel.gameState.value)

        // Act: Run one loop to trigger initialization
        viewModel.gameLoop(1f)
        advanceUntilIdle()

        // Assert
        assertEquals(GameState.Countdown, viewModel.gameState.value)
        assertEquals(5, viewModel.currentLevel.value)
        assertEquals(3, viewModel.nextPieces.value.size)
        assertEquals(false, viewModel.holdLock.value)
    }

    @Test
    fun `gameLoop transitions from Countdown to Prepiece`() = testScope.runTest {
        // Arrange: Start the game
        viewModel.gameLoop(1f)

        // Act: Advance time past the countdown duration
        viewModel.gameLoop(2001f)
        advanceUntilIdle()

        // Assert
        assertEquals(GameState.Prepiece, viewModel.gameState.value)
    }


    @Test
    fun onDrop_advancesPieceTimeAndScore() {
        // Arrange: Start game and get to a point where dropping is possible
        viewModel.gameLoop(1f) // Init
        viewModel.gameLoop(2001f) // Countdown
        viewModel.gameLoop(167f) // Prepiece
        assertEquals(GameState.Piece, viewModel.gameState.value)
        viewModel.gameLoop(500f) // Spend some time in the piece state
        val initialScore = viewModel.score.value

        // Act
        viewModel.onDrop()
        viewModel.gameLoop(1f) // Next loop tick

        // Assert
        assertEquals(1f, viewModel.stateProgress.value, 0.01f)
        assert(viewModel.score.value > initialScore)
        assertEquals(GameState.Prepiece, viewModel.gameState.value)
    }

    @Test
    fun onHold_swapsPieceAndEngagesLock() {
        // Arrange
        viewModel.gameLoop(1f) // Init
        viewModel.gameLoop(2001f) // Countdown
        val initialPiece = viewModel.currentPiece.value
        val nextPiece = viewModel.nextPieces.value.first()

        // Act
        viewModel.onHold()

        // Assert
        assertEquals(true, viewModel.holdLock.value)
        assertEquals(initialPiece, viewModel.heldPiece.value) // old piece is now held
        assertEquals(nextPiece, viewModel.currentPiece.value) // new piece is from the queue
    }

    @Test
    fun `gameOver saves high score if eligible`() = testScope.runTest {
        // Arrange
        val lowScore = HighScore(id = 5, name = "loser", score = 10, level = 1, timestamp = 0, memoryCount = 0, nextPieceCount = 0, ghostPiece = 0, holdPiece = 0, randomBag = 0)
        coEvery { highScoreDao.findByGameMode(any(), any(), any(), any(), any()) } returns listOf(lowScore)

        // Set score and state directly using reflection to access private backing fields
        val scoreField = viewModel::class.java.getDeclaredField("_score")
        scoreField.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        (scoreField.get(viewModel) as MutableStateFlow<Long>).value = 9999L

        val gameStateField = viewModel::class.java.getDeclaredField("_gameState")
        gameStateField.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        (gameStateField.get(viewModel) as MutableStateFlow<GameState>).value = GameState.GameOver

        // Act: Run the game loop once to trigger the logic inside the GameOver state
        viewModel.gameLoop(1f)
        advanceUntilIdle()

        // Assert
        coVerify { highScoreDao.insert(any()) }
        assertNotNull(viewModel.highScoreID)
    }

    @Test
    fun `saveGameState calls continueSettings with correct data`() {
        // Arrange
        viewModel.gameLoop(1f) // init

        // Act
        viewModel.saveGameState()

        // Assert
        verify { continueSettings.saveGameProgress(any()) }
    }
}
