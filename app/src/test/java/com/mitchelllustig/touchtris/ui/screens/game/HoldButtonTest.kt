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
class HoldButtonTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `when state is Piece and not locked, button is enabled`() {
        // Arrange
        val onDropMock = mockk<() -> Unit>(relaxed = true)
        composeTestRule.setContent {
            TouchtrisTheme {
                HoldButton(gameState = GameState.Piece, holdLock = false, onDrop = onDropMock)
            }
        }

        // Act
        val button = composeTestRule.onNodeWithText("Hold")
        button.assertIsEnabled()
        button.performClick()

        // Assert
        verify { onDropMock() }
    }

    @Test
    fun `when state is not Piece, button is disabled`() {
        // Arrange
        composeTestRule.setContent {
            TouchtrisTheme {
                HoldButton(gameState = GameState.Prepiece, holdLock = false, onDrop = {})
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Hold").assertIsNotEnabled()
    }

    @Test
    fun `when hold is locked, button is disabled`() {
        // Arrange
        composeTestRule.setContent {
            TouchtrisTheme {
                HoldButton(gameState = GameState.Piece, holdLock = true, onDrop = {})
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Hold").assertIsNotEnabled()
    }
}
