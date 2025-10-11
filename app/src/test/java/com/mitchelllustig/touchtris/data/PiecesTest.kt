package com.mitchelllustig.touchtris.data

import org.junit.Assert.*
import org.junit.Test

class PiecesTest {

    @Test
    fun `rotatePoint correctly rotates a point 90 degrees clockwise`() {
        assertEquals(Point(0, 0), rotatePoint(Point(0, 0)))
        assertEquals(Point(0, -1), rotatePoint(Point(1, 0)))
        assertEquals(Point(-1, 0), rotatePoint(Point(0, -1)))
        assertEquals(Point(2, -3), rotatePoint(Point(3, 2)))
    }

    @Test
    fun `T piece has correct shapes for all rotations`() {
        val piece = Pieces.T
        assertTrue(piece.getSubPositions(Orientations.zero).containsAll(listOf(Point(0, 0), Point(-1, 0), Point(1, 0), Point(0, 1))))
        assertTrue(piece.getSubPositions(Orientations.one).containsAll(listOf(Point(0, 0), Point(0, -1), Point(0, 1), Point(1, 0))))
        assertTrue(piece.getSubPositions(Orientations.two).containsAll(listOf(Point(0, 0), Point(1, 0), Point(-1, 0), Point(0, -1))))
        assertTrue(piece.getSubPositions(Orientations.three).containsAll(listOf(Point(0, 0), Point(0, 1), Point(0, -1), Point(-1, 0))))
    }

    @Test
    fun `I piece has correct shapes for its two orientations`() {
        val piece = Pieces.I
        val horizontal = piece.getSubPositions(Orientations.zero)
        val vertical = piece.getSubPositions(Orientations.one)

        assertTrue(horizontal.containsAll(listOf(Point(0, 0), Point(-1, 0), Point(-2, 0), Point(1, 0))))
        assertTrue(vertical.containsAll(listOf(Point(0, 0), Point(0, -1), Point(0, 2), Point(0, 1))))

        assertEquals("Rotation 'two' should be same as 'zero'", horizontal, piece.getSubPositions(Orientations.two))
        assertEquals("Rotation 'three' should be same as 'one'", vertical, piece.getSubPositions(Orientations.three))
    }

    @Test
    fun `O piece has one shape that never rotates`() {
        val piece = Pieces.O
        val shape = piece.getSubPositions(Orientations.zero)

        assertTrue(shape.containsAll(listOf(Point(0, 0), Point(-1, 0), Point(-1, 1), Point(0, 1))))

        assertEquals("O piece should not change on rotation one", shape, piece.getSubPositions(Orientations.one))
        assertEquals("O piece should not change on rotation two", shape, piece.getSubPositions(Orientations.two))
        assertEquals("O piece should not change on rotation three", shape, piece.getSubPositions(Orientations.three))
    }

    @Test
    fun `S piece has correct shapes for its two orientations`() {
        val piece = Pieces.S
        val horizontal = piece.getSubPositions(Orientations.zero)
        val vertical = piece.getSubPositions(Orientations.one)

        assertTrue(horizontal.containsAll(listOf(Point(0, 0), Point(-1, 1), Point(0, 1), Point(1, 0))))
        assertTrue(vertical.containsAll(listOf(Point(0, 0), Point(1, 1), Point(1, 0), Point(0, -1))))

        assertEquals("Rotation 'two' should be same as 'zero'", horizontal, piece.getSubPositions(Orientations.two))
        assertEquals("Rotation 'three' should be same as 'one'", vertical, piece.getSubPositions(Orientations.three))
    }

    @Test
    fun `Z piece has correct shapes for its two orientations`() {
        val piece = Pieces.Z
        val horizontal = piece.getSubPositions(Orientations.zero)
        val vertical = piece.getSubPositions(Orientations.one)

        assertTrue(horizontal.containsAll(listOf(Point(0, 0), Point(-1, 0), Point(0, 1), Point(1, 1))))
        assertTrue(vertical.containsAll(listOf(Point(0, 0), Point(0, 1), Point(1, 0), Point(1, -1))))

        assertEquals("Rotation 'two' should be same as 'zero'", horizontal, piece.getSubPositions(Orientations.two))
        assertEquals("Rotation 'three' should be same as 'one'", vertical, piece.getSubPositions(Orientations.three))
    }

    @Test
    fun `J piece has correct shapes for all rotations`() {
        val piece = Pieces.J
        assertTrue(piece.getSubPositions(Orientations.zero).containsAll(listOf(Point(0, 0), Point(-1, 0), Point(1, 0), Point(1, 1))))
        assertTrue(piece.getSubPositions(Orientations.one).containsAll(listOf(Point(0, 0), Point(0, 1), Point(0, -1), Point(1, -1))))
        assertTrue(piece.getSubPositions(Orientations.two).containsAll(listOf(Point(0, 0), Point(1, 0), Point(-1, 0), Point(-1, -1))))
        assertTrue(piece.getSubPositions(Orientations.three).containsAll(listOf(Point(0, 0), Point(0, -1), Point(0, 1), Point(-1, 1))))
    }

    @Test
    fun `L piece has correct shapes for all rotations`() {
        val piece = Pieces.L
        assertTrue(piece.getSubPositions(Orientations.zero).containsAll(listOf(Point(0, 0), Point(-1, 0), Point(1, 0), Point(-1, 1))))
        assertTrue(piece.getSubPositions(Orientations.one).containsAll(listOf(Point(0, 0), Point(0, 1), Point(0, -1), Point(1, 1))))
        assertTrue(piece.getSubPositions(Orientations.two).containsAll(listOf(Point(0, 0), Point(1, 0), Point(-1, 0), Point(1, -1))))
        assertTrue(piece.getSubPositions(Orientations.three).containsAll(listOf(Point(0, 0), Point(0, -1), Point(0, 1), Point(-1, -1))))
    }

    @Test
    fun `isValidPosition returns true for valid positions`() {
        // I piece (width 4) is valid from L3 (ordinal 2) to R4 (ordinal 8)
        assertTrue(Pieces.I.isValidPosition(Positions.L3, Orientations.zero))
        assertTrue(Pieces.I.isValidPosition(Positions.R4, Orientations.zero))

        // T piece (width 3) is valid from L4 (ordinal 1) to R4 (ordinal 8)
        assertTrue(Pieces.T.isValidPosition(Positions.L4, Orientations.zero))
        assertTrue(Pieces.T.isValidPosition(Positions.R4, Orientations.zero))
    }

    @Test
    fun `isValidPosition returns false for positions off the left edge`() {
        // I piece (minX: -2) should be invalid at L4 (ordinal 1) because 1 + (-2) < 0
        assertFalse(Pieces.I.isValidPosition(Positions.L4, Orientations.zero))
        assertFalse(Pieces.I.isValidPosition(Positions.L5, Orientations.zero))

        // T piece (minX: -1) should be invalid at L5 (ordinal 0) because 0 + (-1) < 0
        assertFalse(Pieces.T.isValidPosition(Positions.L5, Orientations.zero))
    }

    @Test
    fun `isValidPosition returns false for positions off the right edge`() {
        // I piece (maxX: 1) should be invalid at R5 (ordinal 9) because 9 + 1 > 9
        assertFalse(Pieces.I.isValidPosition(Positions.R5, Orientations.zero))

        // T piece (maxX: 1) should be invalid at R5 (ordinal 9) because 9 + 1 > 9
        assertFalse(Pieces.T.isValidPosition(Positions.R5, Orientations.zero))
    }
}
