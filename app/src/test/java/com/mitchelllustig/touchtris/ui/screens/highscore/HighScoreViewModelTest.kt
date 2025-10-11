package com.mitchelllustig.touchtris.ui.screens.highscore

import androidx.lifecycle.SavedStateHandle
import com.mitchelllustig.touchtris.database.HighScore
import com.mitchelllustig.touchtris.database.HighScoreDao
import com.mitchelllustig.touchtris.settings.AppSettings
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HighScoreViewModelTest {

    private lateinit var appSettings: AppSettings
    private lateinit var highScoreDao: HighScoreDao
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: HighScoreViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        appSettings = mockk(relaxed = true)
        highScoreDao = mockk(relaxed = true) {
            every { findBySameGameModeAsId(any()) } returns flowOf(listOf())
        }
        savedStateHandle = SavedStateHandle().apply {
            set("highScoreID", 123L)
        }
        viewModel = HighScoreViewModel(appSettings, highScoreDao, savedStateHandle)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initialization retrieves ID and fetches scores`() = runTest {
        // Assert
        assertEquals(123L, viewModel.id)
        verify { highScoreDao.findBySameGameModeAsId(123L) }
    }

    @Test
    fun `updateScore with valid name updates DAO and calls onCompleted`() = runTest {
        // Arrange
        val onCompletedMock = mockk<() -> Unit>(relaxed = true)
        viewModel.name.value = "Player1"

        // Act
        viewModel.updateScore(onCompletedMock)

        // Assert
        coVerify { highScoreDao.updateNameById(123L, "Player1") }
        verify { onCompletedMock() }
        assertEquals(false, viewModel.submitting.value) // Should be false after completion
    }

    @Test
    fun `updateScore with empty name calls onCompleted immediately`() = runTest {
        // Arrange
        val onCompletedMock = mockk<() -> Unit>(relaxed = true)
        viewModel.name.value = ""

        // Act
        viewModel.updateScore(onCompletedMock)

        // Assert: DAO should not be called
        coVerify(exactly = 0) { highScoreDao.updateNameById(any(), any()) }
        verify { onCompletedMock() }
        assertEquals(false, viewModel.submitting.value) // Should remain false
    }
}
