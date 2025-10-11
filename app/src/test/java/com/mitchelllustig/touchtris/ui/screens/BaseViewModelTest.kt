package com.mitchelllustig.touchtris.ui.screens

import com.mitchelllustig.touchtris.audio.AudioPlayer
import com.mitchelllustig.touchtris.settings.AppSettings
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BaseViewModelTest {

    private lateinit var appSettings: AppSettings
    private lateinit var viewModel: BaseViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        appSettings = mockk(relaxed = true)
        mockkObject(AudioPlayer)
        every { AudioPlayer.audioEnabled = any() } just Runs
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    private fun createViewModel(
        haptic: Boolean = true,
        zeroPos: Boolean = false,
        grid: Boolean = true,
        sounds: Boolean = true
    ) {
        every { appSettings.hapticEnabled } returns haptic
        every { appSettings.zeroPosition } returns zeroPos
        every { appSettings.displayGrid } returns grid
        every { appSettings.soundsEnabled } returns sounds
        viewModel = BaseViewModel(appSettings)
    }

    @Test
    fun `initialization loads settings into StateFlows`() = runTest {
        createViewModel(haptic = false, zeroPos = true, grid = false, sounds = false)

        assertEquals(false, viewModel.hapticEnabled.value)
        assertEquals(true, viewModel.zeroPosition.value)
        assertEquals(false, viewModel.displayGrid.value)
        assertEquals(false, viewModel.soundsEnabled.value)
    }

    @Test
    fun `onSoundsEnabledChanged updates flow and AudioPlayer`() {
        createViewModel(sounds = true)

        viewModel.onSoundsEnabledChanged(false)
        assertEquals(false, viewModel.soundsEnabled.value)
        verify { AudioPlayer.audioEnabled = false }

        viewModel.onSoundsEnabledChanged(true)
        assertEquals(true, viewModel.soundsEnabled.value)
        verify { AudioPlayer.audioEnabled = true }
    }

    @Test
    fun `refreshAppSettings reloads settings and updates flows`() {
        createViewModel(haptic = true, grid = true)
        assertEquals(true, viewModel.hapticEnabled.value)
        assertEquals(true, viewModel.displayGrid.value)

        // Arrange new values in the source
        every { appSettings.hapticEnabled } returns false
        every { appSettings.displayGrid } returns false
        every { appSettings.loadSettings() } just Runs

        // Act
        viewModel.refreshAppSettings()

        // Assert
        verify { appSettings.loadSettings() }
        assertEquals(false, viewModel.hapticEnabled.value)
        assertEquals(false, viewModel.displayGrid.value)
    }

    @Test
    fun `saveAppSettings writes flow values to AppSettings`() {
        createViewModel()

        // Act: change the values
        viewModel.onHapticEnabledChanged(false)
        viewModel.onZeroPositionChanged(true)
        viewModel.onDisplayGridChanged(false)
        viewModel.onSoundsEnabledChanged(false)

        // Act: save
        viewModel.saveAppSettings()

        // Assert: Verify that the setters on the mock were called with the new values
        verify { appSettings.hapticEnabled = false }
        verify { appSettings.zeroPosition = true }
        verify { appSettings.displayGrid = false }
        verify { appSettings.soundsEnabled = false }
        verify { appSettings.saveSettings() }
    }
}
