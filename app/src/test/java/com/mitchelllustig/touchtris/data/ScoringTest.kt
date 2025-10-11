package com.mitchelllustig.touchtris.data

import org.junit.Assert.assertEquals
import org.junit.Test

class ScoringTest {

    @Test
    fun `getScoreForLineClear calculates score correctly for various levels and line counts`() {
        // Test scoring on Level 0
        assertEquals("Score for 1 line on level 0 should be 40", 40, getScoreForLineClear(1, 0))
        assertEquals("Score for 2 lines on level 0 should be 100", 100, getScoreForLineClear(2, 0))
        assertEquals("Score for 3 lines on level 0 should be 300", 300, getScoreForLineClear(3, 0))
        assertEquals("Score for 4 lines on level 0 should be 1200", 1200, getScoreForLineClear(4, 0))

        // Test scoring on a higher level (Level 9)
        assertEquals("Score for 1 line on level 9 should be 400", 400, getScoreForLineClear(1, 9))
        assertEquals("Score for 2 lines on level 9 should be 1000", 1000, getScoreForLineClear(2, 9))
        assertEquals("Score for 3 lines on level 9 should be 3000", 3000, getScoreForLineClear(3, 9))
        assertEquals("Score for 4 lines on level 9 should be 12000", 12000, getScoreForLineClear(4, 9))

        // Test a middle level to be thorough (Level 4)
        assertEquals("Score for 1 line on level 4 should be 200", 200, getScoreForLineClear(1, 4))
        assertEquals("Score for 4 lines on level 4 should be 6000", 6000, getScoreForLineClear(4, 4))
    }
}
