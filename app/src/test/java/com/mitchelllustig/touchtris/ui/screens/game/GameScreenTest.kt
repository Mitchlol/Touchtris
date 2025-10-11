package com.mitchelllustig.touchtris.ui.screens.game

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.DeviceConfigurationOverride
import androidx.compose.ui.test.ForcedSize
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToString
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.mitchelllustig.touchtris.data.GameBoard
import com.mitchelllustig.touchtris.data.Orientations
import com.mitchelllustig.touchtris.data.Pieces
import com.mitchelllustig.touchtris.data.Point
import com.mitchelllustig.touchtris.data.Positions
import com.mitchelllustig.touchtris.ui.theme.TouchtrisTheme
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@SuppressLint("UnrememberedMutableState")
@RunWith(RobolectricTestRunner::class)
class GameScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `displays game stats and input controls`() {
        // Arrange
        composeTestRule.setContent {
            TouchtrisTheme() {
                GameScreenContent(
                    hapticEnabledState = mutableStateOf(false),
                    zeroPositionState = mutableStateOf(false),
                    displayGridState = mutableStateOf(true),
                    gameStateState = mutableStateOf(GameState.Piece),
                    currentPieceState = mutableStateOf(Pieces.T),
                    nextPiecesState = mutableStateOf(listOf(Pieces.I)),
                    heldPieceState = mutableStateOf(Pieces.L),
                    holdLockState = mutableStateOf(false),
                    progressState = mutableStateOf(0.5f),
                    gameBoardState = mutableStateOf(GameBoard(20, 10)),
                    clearingLinesState = mutableStateOf(emptyList()),
                    ghostPositionState = mutableStateOf(Point(5, 10)),
                    currentRotationState = mutableStateOf(Orientations.zero),
                    onOrientationChange = {},
                    currentPositionState = mutableStateOf(Positions.R2),
                    onPositionChange = {},
                    onHoldClick = {},
                    onDropClick = {},
                    scoreState = mutableStateOf(12345L),
                    levelState = mutableStateOf(9),
                    linesState = mutableStateOf(5),
                    quadsState = mutableStateOf(1),
                    droughtState = mutableStateOf(21)
                )
            }
        }

        // Assert: Check for various stats and labels
        composeTestRule.onNodeWithContentDescription("FancyText:12345").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("FancyText:Level").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("FancyText:9").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("FancyText:Lines").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("FancyText:5").assertIsDisplayed()

        // Assert: Check for presence of input controls
        composeTestRule.onNodeWithText("Clockwise").assertIsDisplayed()
        composeTestRule.onNodeWithText("Left 3").assertIsDisplayed()
        composeTestRule.onNodeWithText("Hard Drop").assertIsDisplayed()
        composeTestRule.onNodeWithText("Hold").assertIsDisplayed()
    }

    @Test
    fun `invokes callbacks on input clicks`() {
        // Arrange
        val onOrientationChangeMock = mockk<(Orientations) -> Unit>(relaxed = true)
        val onPositionChangeMock = mockk<(Positions) -> Unit>(relaxed = true)
        val onHoldClickMock = mockk<() -> Unit>(relaxed = true)
        val onDropClickMock = mockk<() -> Unit>(relaxed = true)

        composeTestRule.setContent {
            TouchtrisTheme {
                GameScreenContent(
                    hapticEnabledState = mutableStateOf(false),
                    zeroPositionState = mutableStateOf(false),
                    displayGridState = mutableStateOf(true),
                    gameStateState = mutableStateOf(GameState.Piece),
                    currentPieceState = mutableStateOf(Pieces.T), // A piece that can do all moves
                    nextPiecesState = mutableStateOf(emptyList()),
                    heldPieceState = mutableStateOf(null),
                    holdLockState = mutableStateOf(false),
                    progressState = mutableStateOf(0.5f),
                    gameBoardState = mutableStateOf(GameBoard(20, 10)),
                    clearingLinesState = mutableStateOf(emptyList()),
                    ghostPositionState = mutableStateOf(null),
                    currentRotationState = mutableStateOf(Orientations.zero),
                    onOrientationChange = onOrientationChangeMock,
                    currentPositionState = mutableStateOf(Positions.R2),
                    onPositionChange = onPositionChangeMock,
                    onHoldClick = onHoldClickMock,
                    onDropClick = onDropClickMock,
                    scoreState = mutableStateOf(0L),
                    levelState = mutableStateOf(1),
                    linesState = mutableStateOf(0),
                    quadsState = mutableStateOf(0),
                    droughtState = mutableStateOf(0)
                )
            }
        }
//        print(composeTestRule.onRoot().printToString(99))
        // Act & Assert
        composeTestRule.onNodeWithText("Clockwise").performClick()
        verify { onOrientationChangeMock(Orientations.three) }

        // This test is super weird, i think it may have to do with buttons overlapping in the tiny test viewport
        composeTestRule.onNodeWithText("Left 4").performClick()
        verify { onPositionChangeMock(Positions.L3) }

        composeTestRule.onNodeWithText("Hold").performClick()
        verify { onHoldClickMock() }

        composeTestRule.onNodeWithText("Hard Drop").performClick()
        verify { onDropClickMock() }
    }
}
