package com.mitchelllustig.touchtris.ui.widgets

import android.view.HapticFeedbackConstants
import android.view.ViewGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mitchelllustig.touchtris.audio.AudioPlayer
import io.mockk.*
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HapticIconButtonTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun iconButton_triggersHapticFeedback_andAudio_andOnClick() {
        // Arrange
        val mockView = mockk<ViewGroup>(relaxed = true)
        every { mockView.context } returns ApplicationProvider.getApplicationContext()
        val hapticEnabled = mutableStateOf(true)
        val onClickMock = mockk<() -> Unit>(relaxed = true)

        mockkObject(AudioPlayer)
        every { AudioPlayer.button() } just Runs

        composeTestRule.setContent {
            // 👇 Inject our mock View instead of the real LocalView
            CompositionLocalProvider(LocalView provides mockView) {
                HapticIconButton(
                    hapticEnabled = hapticEnabled,
                    onClick = onClickMock
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "HapticIcon"
                    )
                }
            }
        }

        // Act
        composeTestRule.onNodeWithContentDescription("HapticIcon").performClick()
        composeTestRule.waitForIdle()

        // Assert
        verify { AudioPlayer.button() }
        verify { onClickMock.invoke() }
        verify {
            mockView.isHapticFeedbackEnabled = true
            mockView.performHapticFeedback(
                HapticFeedbackConstants.VIRTUAL_KEY,
                HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
            )
        }
    }

    @Test
    fun iconButton_noHaptic_whenDisabled() {
        val mockView = mockk<ViewGroup>(relaxed = true)
        every { mockView.context } returns ApplicationProvider.getApplicationContext()
        val hapticEnabled = mutableStateOf(false)
        val onClickMock = mockk<() -> Unit>(relaxed = true)

        mockkObject(AudioPlayer)
        every { AudioPlayer.button() } just Runs

        composeTestRule.setContent {
            CompositionLocalProvider(LocalView provides mockView) {
                HapticIconButton(
                    hapticEnabled = hapticEnabled,
                    onClick = onClickMock
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "HapticIcon"
                    )
                }
            }
        }

        composeTestRule.onNodeWithContentDescription("HapticIcon").performClick()
        composeTestRule.waitForIdle()

        // Still calls AudioPlayer and onClick
        verify { AudioPlayer.button() }
        verify { onClickMock.invoke() }

        // ❌ No haptic feedback called
        verify(exactly = 0) { mockView.performHapticFeedback(any(), any()) }
    }
}
