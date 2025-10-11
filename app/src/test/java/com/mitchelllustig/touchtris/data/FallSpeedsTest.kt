package com.mitchelllustig.touchtris.data

import org.junit.Assert.assertEquals
import org.junit.Test

class FallSpeedsTest {

    @Test
    fun `getSpeed function returns correct speeds for various levels`() {
        // Test a level at the beginning of the map
        assertEquals(800.000f, FallSpeeds.getSpeed(0))

        // Test a level in the middle of the map
        assertEquals(100.000f, FallSpeeds.getSpeed(9))

        // Test the last explicitly defined level
        assertEquals(16.667f, FallSpeeds.getSpeed(29))

        // Test a level that is higher than any defined level
        assertEquals(FallSpeeds.map[29], FallSpeeds.getSpeed(100))

        // Test a level that is lower than any defined level (out of bounds)
        assertEquals(FallSpeeds.map[0], FallSpeeds.getSpeed(-5))
    }
}
