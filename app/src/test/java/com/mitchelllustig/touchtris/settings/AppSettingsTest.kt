package com.mitchelllustig.touchtris.settings

import android.content.SharedPreferences
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AppSettingsTest {

    private lateinit var prefs: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var appSettings: AppSettings

    @Before
    fun setUp() {
        prefs = mockk<SharedPreferences>()
        editor = mockk<SharedPreferences.Editor>(relaxed = true) // relaxed = true to allow all calls

        // Mock the editor chain
        every { prefs.edit() } returns editor
        every { editor.putBoolean(any(), any()) } returns editor
    }

    @Test
    fun `loadSettings reads values from SharedPreferences`() {
        // Arrange: Mock the return values for loading
        every { prefs.getBoolean("hapticEnabled", true) } returns false
        every { prefs.getBoolean("zeroPosition", false) } returns true
        every { prefs.getBoolean("displayGrid", true) } returns false
        every { prefs.getBoolean("soundsEnabled", true) } returns false

        // Act: Create instance, which triggers loadSettings in init
        appSettings = AppSettings(prefs)

        // Assert: Check that properties match the mocked values
        assertEquals(false, appSettings.hapticEnabled)
        assertEquals(true, appSettings.zeroPosition)
        assertEquals(false, appSettings.displayGrid)
        assertEquals(false, appSettings.soundsEnabled)
    }

    @Test
    fun `saveSettings writes values to SharedPreferences`() {
        // Arrange: Set up a default state for loading
        every { prefs.getBoolean(any(), any()) } returns true
        appSettings = AppSettings(prefs)

        // Act: Change the settings properties
        appSettings.hapticEnabled = false
        appSettings.zeroPosition = true
        appSettings.displayGrid = false
        appSettings.soundsEnabled = false
        appSettings.saveSettings()

        // Assert: Verify that putBoolean was called for each setting with the correct value
        verify { editor.putBoolean("hapticEnabled", false) }
        verify { editor.putBoolean("zeroPosition", true) }
        verify { editor.putBoolean("displayGrid", false) }
        verify { editor.putBoolean("soundsEnabled", false) }
        verify { editor.apply() }
    }
}
