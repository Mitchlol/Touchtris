package com.mitchelllustig.touchtris.ui.widgets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ShadowIconTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testIcon = Icons.Default.Star
    private val contentDescription = "Test Shadow Icon"

    @Test
    fun `whenShadowIsNull_rendersOneIcon`() {
        // Arrange
        composeTestRule.setContent {
            ShadowIcon(
                imageVector = testIcon,
                contentDescription = contentDescription,
                shadow = null
            )
        }

        // Assert
        composeTestRule.onAllNodesWithContentDescription(contentDescription)
            .assertCountEquals(1)
    }

    @Test
    fun `whenShadowIsProvided_rendersTwoIcons`() {
        // Arrange
        composeTestRule.setContent {
            ShadowIcon(
                imageVector = testIcon,
                contentDescription = contentDescription,
                shadow = Shadow(
                    color = Color.Black,
                    offset = Offset(4f, 4f),
                    blurRadius = 8f
                )
            )
        }

        // Assert: One for the shadow, one for the foreground
        composeTestRule.onAllNodesWithContentDescription(contentDescription)
            .assertCountEquals(2)
    }
}
