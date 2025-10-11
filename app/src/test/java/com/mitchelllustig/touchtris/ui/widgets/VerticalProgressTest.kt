package com.mitchelllustig.touchtris.ui.widgets

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.core.app.ApplicationProvider
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class VerticalProgressTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun filledHeight_matchesProgressRatio() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        val fakeView = android.view.View(context)

        composeTestRule.setContent {
            CompositionLocalProvider(LocalView provides fakeView) {
                MaterialTheme {
                    VerticalProgress(
                        progress = 0.25f,
                        modifier = Modifier
                            .testTag("progressBar"),
                        color = Color.Green,
                        trackColor = Color.Gray
                    )
                }
            }
        }

        // Wait for Compose to render
        composeTestRule.waitForIdle()

        val filledNode = composeTestRule.onNodeWithTag("progressFilled").fetchSemanticsNode()
        val emptyNode = composeTestRule.onNodeWithTag("progressEmpty").fetchSemanticsNode()

        // Measure bounding boxes
        val filledHeight = filledNode.layoutInfo.height
        val emptyHeight = emptyNode.layoutInfo.height
        val totalHeight = filledHeight + emptyHeight

        val ratio = filledHeight.toFloat() / totalHeight.toFloat()

        println("Filled height ratio = $ratio (expected ≈ 0.25)")

        assert(
            ratio in 0.2f..0.3f,
            {"Expected filled portion ≈ 0.25, but was $ratio"}
        )
    }
}
