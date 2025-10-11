package com.mitchelllustig.touchtris.ui.screens.game

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.mitchelllustig.touchtris.ui.theme.TouchtrisTheme
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DropButtonTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `when game state is Piece, button is enabled and clickable`() {
        // Arrange
        val onDropMock = mockk<() -> Unit>(relaxed = true)
        composeTestRule.setContent {
            TouchtrisTheme {
                DropButton(gameState = GameState.Piece, onDrop = onDropMock)
            }
        }

        // Act
        val button = composeTestRule.onNodeWithText("Hard Drop")
        button.assertIsEnabled()
        button.performClick()

        // Assert
        verify { onDropMock() }
    }

    @Test
    fun `when game state is not Piece, button is disabled`() {
        // Arrange
        val onDropMock = mockk<() -> Unit>(relaxed = true)
        composeTestRule.setContent {
            TouchtrisTheme {
                DropButton(gameState = GameState.Init, onDrop = onDropMock)
            }
        }

        // Act
        val button = composeTestRule.onNodeWithText("Hard Drop")
        button.assertIsNotEnabled()

        // Assert that clicking does nothing
        verify(exactly = 0) { onDropMock() }
    }

    @Test
    fun `when game state is Over, button is disabled`() {
        // Arrange
        val onDropMock = mockk<() -> Unit>(relaxed = true)
        composeTestRule.setContent {
            TouchtrisTheme {
                DropButton(gameState = GameState.GameOver, onDrop = onDropMock)
            }
        }

        // Act
        val button = composeTestRule.onNodeWithText("Hard Drop")
        button.assertIsNotEnabled()

        // Assert that clicking does nothing
        verify(exactly = 0) { onDropMock() }
    }
}
