package com.mitchelllustig.touchtris.ui.screens.mainmenu

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.mitchelllustig.touchtris.ui.theme.TouchtrisTheme
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@SuppressLint("UnrememberedMutableState")
@RunWith(RobolectricTestRunner::class)
class MainMenuScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `screen displays logo and all buttons`() {
        // Arrange
        composeTestRule.setContent {
            TouchtrisTheme {
                MainMenuScreenContent(
                    hapticEnabledState = mutableStateOf(false),
                    continueEnabledState = mutableStateOf(true),
                    onNavigateToGameMenu = {},
                    onNavigateToContinue = {},
                    onNavigateToAppMenu = {},
                    onNavigateToHighScores = {}
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithContentDescription("Logo:Touchtris").assertIsDisplayed()
        composeTestRule.onNodeWithText("New Game").assertIsDisplayed()
        composeTestRule.onNodeWithText("Continue").assertIsDisplayed()
        composeTestRule.onNodeWithText("Options").assertIsDisplayed()
        composeTestRule.onNodeWithText("High Scores").assertIsDisplayed()
    }

    @Test
    fun `clicking New Game calls correct navigation function`() {
        // Arrange
        val navMock = mockk<() -> Unit>(relaxed = true)
        composeTestRule.setContent {
            TouchtrisTheme {
                MainMenuScreenContent(
                    hapticEnabledState = mutableStateOf(false),
                    continueEnabledState = mutableStateOf(true),
                    onNavigateToGameMenu = navMock,
                    onNavigateToContinue = {},
                    onNavigateToAppMenu = {},
                    onNavigateToHighScores = {}
                )
            }
        }

        // Act
        composeTestRule.onNodeWithText("New Game").performClick()

        // Assert
        verify { navMock() }
    }

    @Test
    fun `when continue is available, Continue button is enabled`() {
        // Arrange
        composeTestRule.setContent {
            TouchtrisTheme {
                MainMenuScreenContent(
                    hapticEnabledState = mutableStateOf(false),
                    continueEnabledState = mutableStateOf(true),
                    onNavigateToGameMenu = {},
                    onNavigateToContinue = {},
                    onNavigateToAppMenu = {},
                    onNavigateToHighScores = {}
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Continue").assertIsEnabled()
    }

    @Test
    fun `when continue is unavailable, Continue button is disabled`() {
        // Arrange
        composeTestRule.setContent {
            TouchtrisTheme {
                MainMenuScreenContent(
                    hapticEnabledState = mutableStateOf(false),
                    continueEnabledState = mutableStateOf(false),
                    onNavigateToGameMenu = {},
                    onNavigateToContinue = {},
                    onNavigateToAppMenu = {},
                    onNavigateToHighScores = {}
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Continue").assertIsNotEnabled()
    }
}
