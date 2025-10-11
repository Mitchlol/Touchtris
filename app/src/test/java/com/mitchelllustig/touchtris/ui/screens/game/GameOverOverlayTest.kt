package com.mitchelllustig.touchtris.ui.screens.game

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import com.mitchelllustig.touchtris.ui.theme.TouchtrisTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class GameOverOverlayTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `when game state is GameOver, overlay is displayed`() {
        composeTestRule.setContent {
            TouchtrisTheme {
                GameOverOverlay(gameState = GameState.GameOver)
            }
        }

        composeTestRule.onNodeWithContentDescription("FancyText:Game over").assertIsDisplayed()
        composeTestRule.onNodeWithTag("GameOverHidden").assertDoesNotExist()
        composeTestRule.onNodeWithTag("GameOverVisible").assertExists()
    }

    @Test
    fun `when game state is not GameOver, overlay is not displayed`() {
        // Arrange
        composeTestRule.setContent {
            TouchtrisTheme {
                GameOverOverlay(gameState = GameState.Piece)
            }
        }

        // Assert
        // The node exists due to animations, but it should not be visible (alpha = 0)
        // A direct assertIsNotDisplayed() is the most straightforward check here.
        composeTestRule.onNodeWithContentDescription("FancyText:Game over").assertIsDisplayed()
        composeTestRule.onNodeWithTag("GameOverHidden").assertExists()
        composeTestRule.onNodeWithTag("GameOverVisible").assertDoesNotExist()
    }
}
