package com.mitchelllustig.touchtris.ui.widgets

import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mitchelllustig.touchtris.audio.AudioPlayer
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import io.mockk.*

@RunWith(AndroidJUnit4::class)
class HapticButtonTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun button_performsClick_andTriggersHapticAndAudio() {
        // Arrange
        val hapticEnabled = mutableStateOf(true)
        val onClickMock = mockk<() -> Unit>(relaxed = true)
        mockkObject(AudioPlayer)
        every { AudioPlayer.button() } just Runs

        composeTestRule.setContent {
            HapticButton(
                hapticEnabled = hapticEnabled,
                onClick = onClickMock
            ) {
                Text("Press")
            }
        }

        // Act
        composeTestRule.onNodeWithTag("hapticButton").performClick()
        composeTestRule.waitForIdle()

        // Assert
        verify { AudioPlayer.button() }
        verify { onClickMock.invoke() }

        // Cleanup
        unmockkObject(AudioPlayer)
    }
}
