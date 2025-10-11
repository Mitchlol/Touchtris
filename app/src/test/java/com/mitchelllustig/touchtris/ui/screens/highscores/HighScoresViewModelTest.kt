package com.mitchelllustig.touchtris.ui.screens.highscores

import com.mitchelllustig.touchtris.database.HighScore
import com.mitchelllustig.touchtris.database.HighScoreDao
import com.mitchelllustig.touchtris.settings.AppSettings
import com.mitchelllustig.touchtris.settings.GameSettings
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HighScoresViewModelTest {

    private lateinit var appSettings: AppSettings
    private lateinit var gameSettings: GameSettings
    private lateinit var highScoreDao: HighScoreDao
    private lateinit var viewModel: HighScoresViewModel

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private lateinit var job: Job

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        appSettings = mockk(relaxed = true)
        gameSettings = mockk(relaxed = true) {
            every { memoryCount } returns 2
            every { nextPieceCount } returns 3
            every { ghostPiece } returns 0
            every { holdPiece } returns 1
            every { randomBag } returns 0
        }
        highScoreDao = mockk(relaxed = true) {
            coEvery { findByGameModeFlow(any(), any(), any(), any(), any()) } returns flowOf(emptyList())
        }

        viewModel = HighScoresViewModel(appSettings, gameSettings, highScoreDao)
        viewModel.dispatcher = testDispatcher

        job = testScope.launch(testDispatcher) {
            viewModel.scores.collect {
            }
        }
    }

    @After
    fun tearDown() {
        job.cancel()
        Dispatchers.resetMain()
    }

    @Test
    fun `initialization loads settings and triggers initial query`() = runTest {
        // Assert: Initial values are set from gameSettings
        verify { gameSettings.loadSettings() }
        assertEquals(2, viewModel.memoryCount.value)
        assertEquals(3, viewModel.nextPieceCount.value)
        assertEquals(0, viewModel.ghostPiece.value)
        assertEquals(1, viewModel.holdPiece.value)
        assertEquals(0, viewModel.randomBag.value)

        // Act: Let the debounce time pass
        testScheduler.advanceUntilIdle()

        // Assert: DAO is called with the initial values
        verify { highScoreDao.findByGameModeFlow(2, 3, 0, 1, 0) }
    }

    @Test
    fun `updating a filter triggers a new query after debounce`() = runTest {
        // Arrange: Let initial query finish
        testScheduler.advanceUntilIdle()

        // Act: Update a filter and advance past the debounce time
        viewModel.ghostPiece.value = 1
        testScheduler.advanceUntilIdle()

        // Assert: DAO is called with the new, updated values
        verify { highScoreDao.findByGameModeFlow(2, 3, 1, 1, 0) }
    }

    @Test
    fun `rapid updates trigger only one query`() = runTest {
        // Act: Rapidly update multiple filters
        viewModel.memoryCount.value = 5
        testScheduler.advanceTimeBy(1)
        viewModel.nextPieceCount.value = 5
        testScheduler.advanceTimeBy(1)
        viewModel.holdPiece.value = 0

        // Assert: Verify that the DAO was not called yet (due to debounce)
        verify(exactly = 0) { highScoreDao.findByGameModeFlow(any(), any(), any(), any(), any()) }

        // Act: Advance time past the debounce period
        testScheduler.advanceUntilIdle()

        // Assert: DAO was called only once with the final values
        verify(exactly = 1) { highScoreDao.findByGameModeFlow(5, 5, 0, 0, 0) }
    }
}
