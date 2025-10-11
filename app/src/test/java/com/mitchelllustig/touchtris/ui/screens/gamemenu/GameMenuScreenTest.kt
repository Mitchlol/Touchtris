package com.mitchelllustig.touchtris.ui.screens.gamemenu

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.printToString
import com.mitchelllustig.touchtris.ui.theme.TouchtrisTheme
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@SuppressLint("UnrememberedMutableState")
@RunWith(RobolectricTestRunner::class)
class GameMenuScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `screen displays all elements correctly`() {
        // Arrange
        composeTestRule.setContent {
            TouchtrisTheme {
                GameMenuScreenContent(
                    hapticEnabled = mutableStateOf(false),
                    startingLevel = MutableStateFlow(1),
                    memoryCount = MutableStateFlow(2),
                    nextPieceCount = MutableStateFlow(3),
                    ghostPiece = MutableStateFlow(1), // On
                    holdPiece = MutableStateFlow(0), // Off
                    randomBag = MutableStateFlow(1), // On
                    onStartGameClick = {}
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithContentDescription("Logo:New Game").assertIsDisplayed()
        composeTestRule.onNodeWithText("Start").assertIsDisplayed()
        composeTestRule.onNodeWithText("Starting Speed").assertIsDisplayed()
        composeTestRule.onNodeWithText("Memory Mode").assertIsDisplayed()
        composeTestRule.onNodeWithText("Next Piece").assertIsDisplayed()
        composeTestRule.onNodeWithText("Ghost").assertIsDisplayed()
        composeTestRule.onNodeWithText("Hold Piece").assertIsDisplayed()
        composeTestRule.onNode(hasScrollAction()).performScrollToIndex(5)
        composeTestRule.onNodeWithText("Bag Random").assertIsDisplayed()
    }

    @Test
    fun `clickingStartButton_callsOnStartGameClick`() {
        // Arrange
        val onStartClickMock = mockk<() -> Unit>(relaxed = true)
        composeTestRule.setContent {
            TouchtrisTheme {
                GameMenuScreenContent(
                    hapticEnabled = mutableStateOf(false),
                    startingLevel = MutableStateFlow(1),
                    memoryCount = MutableStateFlow(2),
                    nextPieceCount = MutableStateFlow(3),
                    ghostPiece = MutableStateFlow(1),
                    holdPiece = MutableStateFlow(0),
                    randomBag = MutableStateFlow(1),
                    onStartGameClick = onStartClickMock
                )
            }
        }

        // Act
        composeTestRule.onNodeWithText("Start").performClick()

        // Assert
        verify { onStartClickMock() }
    }

    @Test
    fun `clickingIncreaseOnStartingSpeed_updatesStateFlow`() {
        // Arrange
        val startingLevelFlow = MutableStateFlow(5)
        composeTestRule.setContent {
            TouchtrisTheme {
                GameMenuScreenContent(
                    hapticEnabled = mutableStateOf(false),
                    startingLevel = startingLevelFlow,
                    memoryCount = MutableStateFlow(0),
                    nextPieceCount = MutableStateFlow(0),
                    ghostPiece = MutableStateFlow(0),
                    holdPiece = MutableStateFlow(0),
                    randomBag = MutableStateFlow(0),
                    onStartGameClick = {}
                )
            }
        }

        // Act
        composeTestRule.onNodeWithContentDescription("Increase Starting Speed").performClick()

        // Assert
        assertEquals(6, startingLevelFlow.value)
    }

    @Test
    fun `whenGhostIsOn_increaseButtonIsDisabled`() {
        // Ghost is a boolean-style setting (0=Off, 1=On)
        // Arrange
        composeTestRule.setContent {
            TouchtrisTheme {
                GameMenuScreenContent(
                    hapticEnabled = mutableStateOf(false),
                    startingLevel = MutableStateFlow(0),
                    memoryCount = MutableStateFlow(0),
                    nextPieceCount = MutableStateFlow(0),
                    ghostPiece = MutableStateFlow(1), // On
                    holdPiece = MutableStateFlow(0),
                    randomBag = MutableStateFlow(0),
                    onStartGameClick = {}
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithContentDescription("Increase Ghost").assertIsNotEnabled()
    }

    @Test
    fun `whenHoldPieceIsOff_decreaseButtonIsDisabled`() {
        // Arrange
        composeTestRule.setContent {
            TouchtrisTheme {
                GameMenuScreenContent(
                    hapticEnabled = mutableStateOf(false),
                    startingLevel = MutableStateFlow(0),
                    memoryCount = MutableStateFlow(0),
                    nextPieceCount = MutableStateFlow(0),
                    ghostPiece = MutableStateFlow(0),
                    holdPiece = MutableStateFlow(0), // Off
                    randomBag = MutableStateFlow(0),
                    onStartGameClick = {}
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithContentDescription("Decrease Hold Piece").assertIsNotEnabled()
    }
}
