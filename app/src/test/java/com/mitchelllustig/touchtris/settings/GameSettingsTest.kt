package com.mitchelllustig.touchtris.settings

import android.content.SharedPreferences
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GameSettingsTest {

    private lateinit var prefs: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var gameSettings: GameSettings

    @Before
    fun setUp() {
        prefs = mockk<SharedPreferences>()
        editor = mockk<SharedPreferences.Editor>(relaxed = true) // relaxed = true to allow all calls

        // Mock the editor chain
        every { prefs.edit() } returns editor
        every { editor.putInt(any(), any()) } returns editor
    }

    @Test
    fun `loadSettings reads values from SharedPreferences`() {
        // Arrange: Mock the return values for loading
        every { prefs.getInt("startingLevel", -1) } returns 5
        every { prefs.getInt("memoryCount", -1) } returns 2
        every { prefs.getInt("nextPieceCount", 1) } returns 1
        every { prefs.getInt("ghostPiece", 1) } returns 0
        every { prefs.getInt("holdPiece", 0) } returns 0
        every { prefs.getInt("randomBag", 0) } returns 1

        // Act: Create instance, which triggers loadSettings in init
        gameSettings = GameSettings(prefs)

        // Assert: Check that properties match the mocked values
        assertEquals(5, gameSettings.startingLevel)
        assertEquals(2, gameSettings.memoryCount)
        assertEquals(1, gameSettings.nextPieceCount)
        assertEquals(0, gameSettings.ghostPiece)
        assertEquals(0, gameSettings.holdPiece)
        assertEquals(1, gameSettings.randomBag)
    }

    @Test
    fun `saveSettings writes values to SharedPreferences`() {
        // Arrange: Set up a default state for loading
        every { prefs.getInt(any(), any()) } returns -1 // Return a default value for init
        gameSettings = GameSettings(prefs)

        // Act: Change the settings properties
        gameSettings.startingLevel = 9
        gameSettings.memoryCount = 1
        gameSettings.nextPieceCount = 2
        gameSettings.ghostPiece = 0
        gameSettings.holdPiece = 1
        gameSettings.randomBag = 0
        gameSettings.saveSettings()

        // Assert: Verify that putInt was called for each setting with the correct value
        verify { editor.putInt("startingLevel", 9) }
        verify { editor.putInt("memoryCount", 1) }
        verify { editor.putInt("nextPieceCount", 2) }
        verify { editor.putInt("ghostPiece", 0) }
        verify { editor.putInt("holdPiece", 1) }
        verify { editor.putInt("randomBag", 0) }
        verify { editor.apply() }
    }
}
