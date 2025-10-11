package com.mitchelllustig.touchtris.ui.screens.game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import com.mitchelllustig.touchtris.ui.theme.TouchtrisTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CountdownOverlayTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Composable
    private fun CountdownTestHarness(progress: MutableState<Float>) {
        TouchtrisTheme {
            CountdownOverlay(gameState = GameState.Countdown, progress = progress.value)
        }
    }

    @Test
    fun `displays correct numbers during countdown`() {
        // Arrange
        val progress = mutableStateOf(0.1f)

        composeTestRule.setContent { CountdownTestHarness(progress) }

        // Assert: Should show "3" at the beginning
        composeTestRule.onNodeWithContentDescription("FancyText:3").assertIsDisplayed()

        // Act: Update progress to the next number
        progress.value = 0.4f

        // Assert: Should now show "2"
        composeTestRule.onNodeWithContentDescription("FancyText:2").assertIsDisplayed()

        // Act: Update progress to the final number
        progress.value = 0.8f

        // Assert: Should now show "1"
        composeTestRule.onNodeWithContentDescription("FancyText:1").assertIsDisplayed()
    }

    @Test
    fun `when gameState is not Countdown, text is not displayed`() {
        // Arrange
        composeTestRule.setContent {
            TouchtrisTheme {
                // State is Piece, not Countdown
                CountdownOverlay(gameState = GameState.Piece, progress = 0.1f)
            }
        }

        // Assert: The text is empty, so the node shouldn't be found by its content description
        composeTestRule.onNodeWithContentDescription("FancyText:3").assertDoesNotExist()
    }
}
