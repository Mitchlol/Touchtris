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
class OrientationInputTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockPiece: Pieces = mockk {
        every { isValidPosition(any(), any()) } returns true
        every { ordinal } returns 7
    }

    @Test
    fun `buttons are displayed and clickable`() {
        // Arrange
        val onOrientationChangedMock = mockk<(Orientations) -> Unit>(relaxed = true)
        composeTestRule.setContent {
            TouchtrisTheme {
                OrientationInput(
                    gameState = GameState.Piece,
                    piece = mockPiece,
                    piecePosition = Positions.R2,
                    pieceOrientation = Orientations.zero,
                    hapticEnabled = false,
                    onOrientationChanged = onOrientationChangedMock
                )
            }
        }

        // Assert that all buttons are displayed
        composeTestRule.onNodeWithText("CounterCW").assertIsDisplayed()
        composeTestRule.onNodeWithText("None").assertIsDisplayed()
        composeTestRule.onNodeWithText("Flip").assertIsDisplayed()
        composeTestRule.onNodeWithText("Clockwise").assertIsDisplayed()

        // Act: Click a button
        composeTestRule.onNodeWithText("None").performClick()

        // Assert: Verify not callback
        verify(exactly = 0) { onOrientationChangedMock(Orientations.zero) }

        // Act: Click a button
        composeTestRule.onNodeWithText("Flip").performClick()

        // Assert: Verify callback
        verify(exactly = 1) { onOrientationChangedMock(Orientations.two) }


    }

    @Test
    fun `when gameState is not Piece, buttons are disabled`() {
        // Arrange
        composeTestRule.setContent {
            TouchtrisTheme {
                OrientationInput(
                    gameState = GameState.Prepiece, // Not Piece
                    piece = mockPiece,
                    piecePosition = Positions.R2,
                    pieceOrientation = Orientations.zero,
                    hapticEnabled = false,
                    onOrientationChanged = {}
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Clockwise").assertIsNotEnabled()
    }

    @Test
    fun `when orientation is invalid, button is disabled`() {
        // Arrange
        val invalidMockPiece: Pieces = mockk {
            every { isValidPosition(any(), Orientations.three) } returns false
            every { isValidPosition(any(), neq(Orientations.three)) } returns true
            every { ordinal } returns 7
        }
        composeTestRule.setContent {
            TouchtrisTheme {
                OrientationInput(
                    gameState = GameState.Piece,
                    piece = invalidMockPiece,
                    piecePosition = Positions.R2,
                    pieceOrientation = Orientations.zero,
                    hapticEnabled = false,
                    onOrientationChanged = {}
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Clockwise").assertIsNotEnabled()
        composeTestRule.onNodeWithText("None").assertIsEnabled()
    }
}
