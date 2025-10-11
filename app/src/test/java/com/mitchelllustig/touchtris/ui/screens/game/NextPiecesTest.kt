package com.mitchelllustig.touchtris.ui.screens.game

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import com.mitchelllustig.touchtris.data.Pieces
import com.mitchelllustig.touchtris.ui.theme.TouchtrisTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NextPiecesTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `when holdLock is not null, Hold and Next labels are displayed`() {
        // Arrange
        composeTestRule.setContent {
            TouchtrisTheme {
                NextPieces(
                    nextPieces = listOf(Pieces.I),
                    level = 1,
                    heldPiece = Pieces.T,
                    holdLock = false
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithContentDescription("FancyText:Hold").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("FancyText:Next").assertIsDisplayed()
    }

    @Test
    fun `when holdLock is null, only Next label is displayed`() {
        // Arrange
        composeTestRule.setContent {
            TouchtrisTheme {
                NextPieces(
                    nextPieces = listOf(Pieces.I),
                    level = 1,
                    heldPiece = Pieces.T,
                    holdLock = null // Is null
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithContentDescription("FancyText:Hold").assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription("FancyText:Next").assertIsDisplayed()
    }
}
