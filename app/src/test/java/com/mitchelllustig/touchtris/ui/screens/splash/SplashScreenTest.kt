package com.mitchelllustig.touchtris.ui.screens.splash

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import com.mitchelllustig.touchtris.ui.theme.TouchtrisTheme
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class SplashScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `splashScreen displays logo and navigates after delay`() = runTest {
        // Arrange
        val navMock = mockk<() -> Unit>(relaxed = true)

        composeTestRule.setContent {
            TouchtrisTheme {
                SplashScreen(onNavigateToMainMenu = navMock)
            }
        }

        // Assert: Logo is immediately visible
        composeTestRule.onNodeWithContentDescription("Logo:Touchtris").assertIsDisplayed()

        // Assert: Navigation has not happened yet
        verify(exactly = 0) { navMock() }

        // Act: Advance the clock past the delay
        composeTestRule.mainClock.advanceTimeBy(1001)

        // Assert: Navigation has now occurred
        verify(exactly = 1) { navMock() }
    }
}
