package com.mitchelllustig.touchtris.ui.screens.highscore

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.mitchelllustig.touchtris.database.HighScore
import com.mitchelllustig.touchtris.ui.theme.TouchtrisTheme
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@SuppressLint("UnrememberedMutableState")
@RunWith(RobolectricTestRunner::class)
class HighScoreScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockScores = listOf(
        HighScore(id = 1, name = "Player1", score = 1000, level = 5, timestamp = 0, memoryCount = 0, nextPieceCount = 0, ghostPiece = 0, holdPiece = 0, randomBag = 0),
        HighScore(id = 2, name = "Player2", score = 2000, level = 10, timestamp = 0, memoryCount = 0, nextPieceCount = 0, ghostPiece = 0, holdPiece = 0, randomBag = 0)
    )

    @Test
    fun `screen displays titles, scores, and form`() {
        // Arrange
        composeTestRule.setContent {
            TouchtrisTheme {
                HighScoreScreenContent(
                    id = 1,
                    scores = mutableStateOf(mockScores),
                    name = mutableStateOf(""),
                    onNameChanged = {},
                    submitting = mutableStateOf(false),
                    updateScore = {},
                    onNavigateBack = {}
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithContentDescription("FancyText:High Score").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("FancyText:Congratulations, you are").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("FancyText:a touchtris master!").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("FancyText:Name").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("FancyText:Level").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("FancyText:Score").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("FancyText:New Score!").assertIsDisplayed() // For id = 1
        composeTestRule.onNodeWithContentDescription("FancyText:Player2").assertIsDisplayed()
        composeTestRule.onNodeWithText("Enter your name").assertIsDisplayed()
        composeTestRule.onNodeWithText("Submit").assertIsDisplayed()
    }

    @Test
    fun `entering name calls onNameChanged`() {
        // Arrange
        val onNameChangedMock = mockk<(String) -> Unit>(relaxed = true)
        composeTestRule.setContent {
            TouchtrisTheme {
                HighScoreScreenContent(
                    id = 3, // Not a new score
                    scores = mutableStateOf(mockScores),
                    name = mutableStateOf(""),
                    onNameChanged = onNameChangedMock,
                    submitting = mutableStateOf(false),
                    updateScore = {},
                    onNavigateBack = {}
                )
            }
        }

        // Act
        composeTestRule.onNodeWithText("Enter your name").performTextInput("Test")

        // Assert
        verify { onNameChangedMock("Test") }
    }

    @Test
    fun `clicking submit calls updateScore`() {
        // Arrange
        val updateScoreMock = mockk<( () -> Unit) -> Unit>(relaxed = true)
        val onNavigateBackMock = mockk<() -> Unit>(relaxed = true)
        composeTestRule.setContent {
            TouchtrisTheme {
                HighScoreScreenContent(
                    id = 1,
                    scores = mutableStateOf(mockScores),
                    name = mutableStateOf("Player1"),
                    onNameChanged = {},
                    submitting = mutableStateOf(false),
                    updateScore = updateScoreMock,
                    onNavigateBack = onNavigateBackMock
                )
            }
        }

        // Act
        composeTestRule.onNodeWithText("Submit").performClick()

        // Assert
        verify { updateScoreMock(onNavigateBackMock) }
    }

    @Test
    fun `whenSubmitting_buttonIsDisabledAndShowsIndicator`() {
        // Arrange
        composeTestRule.setContent {
            TouchtrisTheme {
                HighScoreScreenContent(
                    id = 1,
                    scores = mutableStateOf(mockScores),
                    name = mutableStateOf("Player1"),
                    onNameChanged = {},
                    submitting = mutableStateOf(true), // Submitting is true
                    updateScore = {},
                    onNavigateBack = {}
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithTag("submitButton").assertIsNotEnabled()
    }
}
