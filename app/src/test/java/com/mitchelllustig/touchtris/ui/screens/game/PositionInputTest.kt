package com.mitchelllustig.touchtris.ui.screens.game

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.mitchelllustig.touchtris.data.Orientations
import com.mitchelllustig.touchtris.data.Pieces
import com.mitchelllustig.touchtris.data.Positions
import com.mitchelllustig.touchtris.ui.theme.TouchtrisTheme
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PositionInputTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val piece: Pieces = Pieces.I

    @Test
    fun `buttons are displayed and clickable`() {
        // Arrange
        val onPositionChangedMock = mockk<(Positions) -> Unit>(relaxed = true)
        composeTestRule.setContent {
            TouchtrisTheme {
                PositionInput(
                    gameState = GameState.Piece,
                    piece = piece,
                    piecePosition = Positions.R2,
                    pieceOrientation = Orientations.zero,
                    hapticEnabled = false,
                    zeroPosition = false,
                    onPositionChanged = onPositionChangedMock
                )
            }
        }

        // Act & Assert
        composeTestRule.onNodeWithText("Left 5").assertIsDisplayed()
        composeTestRule.onNodeWithText("Right 1").assertIsDisplayed()

        composeTestRule.onNodeWithText("Left 3").performClick()
        verify { onPositionChangedMock(Positions.L3) }
    }

    @Test
    fun `when gameState is not Piece, buttons are disabled`() {
        // Arrange
        composeTestRule.setContent {
            TouchtrisTheme {
                PositionInput(
                    gameState = GameState.Prepiece, // Not Piece
                    piece = piece,
                    piecePosition = Positions.R2,
                    pieceOrientation = Orientations.zero,
                    hapticEnabled = false,
                    zeroPosition = false,
                    onPositionChanged = {}
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Left 2").assertIsNotEnabled()
    }

    @Test
    fun `when piece position is invalid, button is disabled`() {
        composeTestRule.setContent {
            TouchtrisTheme {
                PositionInput(
                    gameState = GameState.Piece,
                    piece = piece,
                    piecePosition = Positions.R2,
                    pieceOrientation = Orientations.zero,
                    hapticEnabled = false,
                    zeroPosition = false,
                    onPositionChanged = {}
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Left 4").assertIsNotEnabled()
        composeTestRule.onNodeWithText("Right 3").assertIsEnabled()
    }
}
