package com.mitchelllustig.touchtris.ui.screens.appmenu

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToString
import com.mitchelllustig.touchtris.ui.theme.TouchtrisTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@SuppressLint("UnrememberedMutableState")
@Composable
private fun AppMenuScreenMock() {
    AppMenuScreenContent(
        hapticEnabled = mutableStateOf(true),
        onHapticEnabledChanged = {},
        zeroPosition = mutableStateOf(false),
        onZeroPositionChanged = {},
        displayGrid = mutableStateOf(true),
        onDisplayGridChanged = {},
        soundsEnabled = mutableStateOf(true),
        onSoundsEnabledChanged = {}
    )
}

@SuppressLint("UnrememberedMutableState")
@RunWith(RobolectricTestRunner::class)
class AppMenuScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `screen displays all settings`() {
        // Arrange
        composeTestRule.setContent {
            TouchtrisTheme {
                AppMenuScreenMock()
            }
        }

        // Assert
        composeTestRule.onNodeWithContentDescription("Logo:Options").assertIsDisplayed()
        composeTestRule.onNodeWithText("Haptic Feedback").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sound").assertIsDisplayed()
        composeTestRule.onNodeWithText("Right Zero Label").assertIsDisplayed()
        composeTestRule.onNodeWithText("Grid").assertIsDisplayed()
    }

    @Test
    fun `whenHapticIsOff_clickingIncrease_callsCallbackWithTrue`() {
        // Arrange
        var callbackValue: Boolean? = null
        composeTestRule.setContent {
            TouchtrisTheme {
                AppMenuScreenContent(
                    hapticEnabled = mutableStateOf(false),
                    onHapticEnabledChanged = { callbackValue = it },
                    // Dummy values for other states
                    zeroPosition = mutableStateOf(false), onZeroPositionChanged = {},
                    displayGrid = mutableStateOf(false), onDisplayGridChanged = {},
                    soundsEnabled = mutableStateOf(false), onSoundsEnabledChanged = {}
                )
            }
        }

        // Act
        composeTestRule.onNodeWithContentDescription("Increase Haptic Feedback").performClick()

        // Assert
        assertEquals(true, callbackValue)
    }

    @Test
    fun `whenSoundIsOn_clickingDecrease_callsCallbackWithFalse`() {
        // Arrange
        var callbackValue: Boolean? = null
        composeTestRule.setContent {
            TouchtrisTheme {
                AppMenuScreenContent(
                    soundsEnabled = mutableStateOf(true),
                    onSoundsEnabledChanged = { callbackValue = it },
                    // Dummy values for other states
                    hapticEnabled = mutableStateOf(false), onHapticEnabledChanged = {},
                    zeroPosition = mutableStateOf(false), onZeroPositionChanged = {},
                    displayGrid = mutableStateOf(false), onDisplayGridChanged = {}
                )
            }
        }

        // Act
        composeTestRule.onNodeWithContentDescription("Decrease Sound").performClick()

        // Assert
        assertEquals(false, callbackValue)
    }

    @Test
    fun `whenGridIsOn_increaseButtonIsDisabled`() {
        // Arrange
        composeTestRule.setContent {
            TouchtrisTheme {
                AppMenuScreenContent(
                    displayGrid = mutableStateOf(true),
                    onDisplayGridChanged = {},
                     // Dummy values for other states
                    hapticEnabled = mutableStateOf(false), onHapticEnabledChanged = {},
                    zeroPosition = mutableStateOf(false), onZeroPositionChanged = {},
                    soundsEnabled = mutableStateOf(false), onSoundsEnabledChanged = {}
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithContentDescription("Increase Grid").assertIsNotEnabled()
    }

    @Test
    fun `whenZeroPositionIsOff_decreaseButtonIsDisabled`() {
        // Arrange
        composeTestRule.setContent {
            TouchtrisTheme {
                AppMenuScreenContent(
                    zeroPosition = mutableStateOf(false),
                    onZeroPositionChanged = {},
                     // Dummy values for other states
                    hapticEnabled = mutableStateOf(false), onHapticEnabledChanged = {},
                    displayGrid = mutableStateOf(false), onDisplayGridChanged = {},
                    soundsEnabled = mutableStateOf(false), onSoundsEnabledChanged = {}
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithContentDescription("Decrease Right Zero Label").assertIsNotEnabled()
    }
}
