package com.mitchelllustig.touchtris.data

import androidx.compose.ui.graphics.Color
import org.junit.Assert.*
import org.junit.Test

class GameBoardTest {

    private val dummySquare = SquareColorInfo(primaryColor = Color.Gray, secondaryColor = Color.DarkGray, dark = true)

    @Test
    fun `checkPieceFits handles boundaries and collisions`() {
        val board = GameBoard()
        val tPiece = Pieces.T.getSubPositions(Orientations.zero)

        assertTrue("Piece should fit in an empty board", board.checkPieceFits(tPiece, 4, 10))

        assertFalse("Piece should not fit when placed off the left edge", board.checkPieceFits(tPiece, 0, 10))
        assertFalse("Piece should not fit when placed off the right edge", board.checkPieceFits(tPiece, 9, 10))
        assertFalse("Piece should not fit when placed off the bottom edge", board.checkPieceFits(tPiece, 4, 19))

        board.squares[11][5] = dummySquare
        assertFalse("Piece should not fit when colliding with another square", board.checkPieceFits(tPiece, 5, 10))
    }

    @Test
    fun `addPiece adds piece correctly and decrements render delays`() {
        val board = GameBoard()
        board.squares[5][5] = dummySquare.copy(renderDelay = 3)

        val success = board.addPiece(Pieces.T, Orientations.zero, 4, dummySquare)

        assertTrue("addPiece should return true on success", success)
        assertEquals("Render delay of other pieces should be decremented", 2, board.squares[5][5]?.renderDelay)

        assertNotNull("New piece square [19][4] should be filled", board.squares[19][4])
        assertNotNull("New piece square [18][3] should be filled", board.squares[18][3])
        assertNotNull("New piece square [18][4] should be filled", board.squares[18][4])
        assertNotNull("New piece square [18][5] should be filled", board.squares[18][5])
    }

    @Test
    fun `addPiece returns false when piece does not fit`() {
        val board = GameBoard()
        for (y in 0 until board.height) {
            for (x in 0 until board.width) {
                board.squares[y][x] = dummySquare
            }
        }
        val success = board.addPiece(Pieces.I, Orientations.zero, 10, dummySquare)
        assertFalse("addPiece should return false if no space is available", success)
    }

    @Test
    fun `getGhostHeight returns correct drop position`() {
        val board = GameBoard()

        assertEquals("Should return lowest row on an empty board", 18, board.getGhostHeight(Pieces.T, Orientations.zero, 4))

        board.squares[19][4] = dummySquare
        assertEquals("Should return position on top of an existing square", 17, board.getGhostHeight(Pieces.T, Orientations.zero, 4))

        for (y in 0 until board.height) board.squares[y][0] = dummySquare
        assertEquals("Should return -1 if the piece cannot fit at all", -1, board.getGhostHeight(Pieces.I, Orientations.zero, 0))
    }

    @Test
    fun `getAvailabilty calculates percentage of empty squares`() {
        val board = GameBoard()
        val totalSquares = (board.width * board.height).toFloat()

        assertEquals("Availability should be 1.0 for an empty board", 1.0f, board.getAvailabilty(), 0.001f)

        board.addPiece(Pieces.T, Orientations.zero, 4, dummySquare) // Adds 4 squares
        val expected = (totalSquares - 4) / totalSquares
        assertEquals("Availability should decrease after adding a piece", expected, board.getAvailabilty(), 0.001f)
    }

    @Test
    fun `checkClearLines identifies all full lines`() {
        val board = GameBoard()

        assertTrue("Should return an empty list for an empty board", board.checkClearLines().isEmpty())

        for (x in 0 until board.width) { board.squares[19][x] = dummySquare } // Fill line 19
        for (x in 0 until board.width) { board.squares[10][x] = dummySquare } // Fill line 10

        val lines = board.checkClearLines()
        assertEquals("Should identify 2 full lines", 2, lines.size)
        assertTrue("List should contain the correct indices of cleared lines", lines.containsAll(listOf(10, 19)))
    }

    @Test
    fun `clearLines removes full lines and shifts other lines down`() {
        val board = GameBoard()
        val marker = dummySquare.copy(renderDelay = 50) // A unique square to track

        board.squares[18][5] = marker // Place a marker that should shift down

        for (x in 0 until board.width) { // Fill the line below the marker
            board.squares[19][x] = dummySquare
        }

        board.clearLines()

        assertEquals("Board height should remain constant after clearing lines", 20, board.squares.size)
        assertNull("The marker's original position should now be empty", board.squares[18][5])
        assertEquals("The marker should have shifted down one row", marker, board.squares[19][5])
        assertTrue("The top row of the board should be new and empty", board.squares[0].all { it == null })
        assertTrue("The row with marker should be empty", board.squares[18].all { it == null })
    }
}
