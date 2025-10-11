package com.mitchelllustig.touchtris.settings

import android.content.SharedPreferences
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ContinueSettingsTest {

    private lateinit var prefs: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var continueSettings: ContinueSettings

    @Before
    fun setUp() {
        prefs = mockk<SharedPreferences>()
        editor = mockk<SharedPreferences.Editor>(relaxed = true)

        // Mock the editor chain
        every { prefs.edit() } returns editor
        every { editor.putBoolean(any(), any()) } returns editor
        every { editor.remove(any()) } returns editor
        every { editor.putString(any(), any()) } returns editor
    }

    @Test
    fun `loadSettings correctly initializes isContinueAvailable`() {
        // Arrange
        every { prefs.getBoolean("isContinueAvailable", false) } returns true

        // Act
        continueSettings = ContinueSettings(prefs)

        // Assert
        assertTrue(continueSettings.isContinueAvailable.value)
    }

    @Test
    fun `clearGameProgress updates SharedPreferences and state`() {
        // Arrange
        every { prefs.getBoolean("isContinueAvailable", false) } returns true // Start as if a game exists
        continueSettings = ContinueSettings(prefs)

        // Act
        continueSettings.clearGameProgress()

        // Assert
        verify { editor.putBoolean("isContinueAvailable", false) }
        verify { editor.remove("saveGame") }
        verify { editor.apply() }
        assertFalse(continueSettings.isContinueAvailable.value)
    }

    @Test
    fun `saveGameProgress writes to SharedPreferences and updates state`() {
        // Arrange
        every { prefs.getBoolean("isContinueAvailable", false) } returns false
        continueSettings = ContinueSettings(prefs)
        val saveGameJson = "{\"score\":100}"

        // Act
        continueSettings.saveGameProgress(saveGameJson)

        // Assert
        verify { editor.putString("saveGame", saveGameJson) }
        verify { editor.putBoolean("isContinueAvailable", true) }
        verify { editor.apply() }
        assertTrue(continueSettings.isContinueAvailable.value)
    }

    @Test
    fun `loadGameProgress retrieves data and clears progress`() {
        // Arrange
        val saveGameJson = "{\"score\":200}"
        every { prefs.getBoolean("isContinueAvailable", false) } returns true
        every { prefs.getString("saveGame", null) } returns saveGameJson
        continueSettings = ContinueSettings(prefs)

        // Act
        val loadedJson = continueSettings.loadGameProgress()

        // Assert
        assertEquals(saveGameJson, loadedJson)
        // Verify that loading the game also clears the progress
        verify { editor.putBoolean("isContinueAvailable", false) }
        verify { editor.remove("saveGame") }
        assertFalse(continueSettings.isContinueAvailable.value)
        assertEquals(saveGameJson, loadedJson)
    }
}
