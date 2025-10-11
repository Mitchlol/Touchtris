package com.mitchelllustig.touchtris.ui.screens.gamemenu

import com.mitchelllustig.touchtris.settings.AppSettings
import com.mitchelllustig.touchtris.settings.GameSettings
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GameMenuViewModelTest {

    private lateinit var appSettings: AppSettings
    private lateinit var gameSettings: GameSettings
    private lateinit var viewModel: GameMenuViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        appSettings = mockk(relaxed = true)
        gameSettings = mockk(relaxed = true) {
            every { startingLevel } returns 5
            every { memoryCount } returns 2
            every { nextPieceCount } returns 3
            every { ghostPiece } returns 1
            every { holdPiece } returns 0
            every { randomBag } returns 1
        }
        viewModel = GameMenuViewModel(appSettings, gameSettings)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initialization loads settings into StateFlows`() = runTest {
        assertEquals(5, viewModel.startingLevel.value)
        assertEquals(2, viewModel.memoryCount.value)
        assertEquals(3, viewModel.nextPieceCount.value)
        assertEquals(1, viewModel.ghostPiece.value)
        assertEquals(0, viewModel.holdPiece.value)
        assertEquals(1, viewModel.randomBag.value)
    }

    @Test
    fun `saveSettings writes flow values to GameSettings`() {
        // Arrange: Change the values in the flows
        viewModel.startingLevel.value = 10
        viewModel.memoryCount.value = 1
        viewModel.nextPieceCount.value = 4
        viewModel.ghostPiece.value = 0
        viewModel.holdPiece.value = 1
        viewModel.randomBag.value = 0

        // Act
        viewModel.saveSettings()

        // Assert: Verify the setters on the mock were called with the new values
        verify { gameSettings.startingLevel = 10 }
        verify { gameSettings.memoryCount = 1 }
        verify { gameSettings.nextPieceCount = 4 }
        verify { gameSettings.ghostPiece = 0 }
        verify { gameSettings.holdPiece = 1 }
        verify { gameSettings.randomBag = 0 }
        verify { gameSettings.saveSettings() }
    }

    @Test
    fun `startGame saves settings and emits event`() = runTest {
        var event: Boolean? = null
        val job = launch {
            event = viewModel.startGameEvents.first()
        }
        runCurrent()
        viewModel.startGame()
        advanceUntilIdle()

        verify { gameSettings.saveSettings() }
        assertEquals(true, event)
        job.cancel()
    }

    @Test
    fun `onCleared saves settings`() {
        // To test onCleared, we can call it directly since it's public in this class
        val onClearedMethod = viewModel.javaClass.getDeclaredMethod("onCleared")
        onClearedMethod.isAccessible = true
        onClearedMethod.invoke(viewModel)

        // Assert that the save method was called on our mock
        verify { gameSettings.saveSettings() }
    }
}
