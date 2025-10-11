package com.mitchelllustig.touchtris.ui.screens.highscores

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.mitchelllustig.touchtris.database.HighScore
import com.mitchelllustig.touchtris.ui.theme.TouchtrisTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class HighScoresScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `screen displays titles, scores, and filters`() {
        // Arrange
        val mockScores = listOf(
            HighScore(id = 1, name = "Player1", score = 1000, level = 5, timestamp = 0, memoryCount = 0, nextPieceCount = 0, ghostPiece = 0, holdPiece = 0, randomBag = 0),
        )
        composeTestRule.setContent {
            TouchtrisTheme {
                HighScoresScreenContent(
                    memoryCountFlow = MutableStateFlow(1),
                    nextPieceCountFlow = MutableStateFlow(2),
                    ghostPieceFlow = MutableStateFlow(0),
                    holdPieceFlow = MutableStateFlow(1),
                    randomBagFlow = MutableStateFlow(0),
                    scoresQueryingFlow = MutableStateFlow(false),
                    scoresFlow = flowOf(mockScores)
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithContentDescription("FancyText:High Scores").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("FancyText:Name").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("FancyText:Level").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("FancyText:Score").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("FancyText:Player1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Memory Mode").assertIsDisplayed()
        composeTestRule.onNodeWithText("Next Piece").assertIsDisplayed()
        composeTestRule.onNodeWithTag("listLoadingIndicator").assertDoesNotExist()
    }

    @Test
    fun `clicking increase on memory mode updates flow`() {
        // Arrange
        val memoryCountFlow = MutableStateFlow(2)
        composeTestRule.setContent {
            TouchtrisTheme {
                HighScoresScreenContent(
                    memoryCountFlow = memoryCountFlow,
                    nextPieceCountFlow = MutableStateFlow(0),
                    ghostPieceFlow = MutableStateFlow(0),
                    holdPieceFlow = MutableStateFlow(0),
                    randomBagFlow = MutableStateFlow(0),
                    scoresQueryingFlow = MutableStateFlow(false),
                    scoresFlow = flowOf(emptyList())
                )
            }
        }

        // Act
        composeTestRule.onNodeWithContentDescription("Increase Memory Mode").performClick()

        // Assert
        assertEquals(3, memoryCountFlow.value)
    }

    @Test
    fun `when querying, progress indicator is shown`() {
        // Arrange
        composeTestRule.setContent {
            TouchtrisTheme {
                HighScoresScreenContent(
                    memoryCountFlow = MutableStateFlow(0),
                    nextPieceCountFlow = MutableStateFlow(0),
                    ghostPieceFlow = MutableStateFlow(0),
                    holdPieceFlow = MutableStateFlow(0),
                    randomBagFlow = MutableStateFlow(0),
                    scoresQueryingFlow = MutableStateFlow(true), // Querying is active
                    scoresFlow = flowOf(emptyList())
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithTag("listLoadingIndicator").assertIsDisplayed()
    }
}
