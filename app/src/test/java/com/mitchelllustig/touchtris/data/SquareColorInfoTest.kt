package com.mitchelllustig.touchtris.data

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class SquareColorInfoTest {

    @Test
    fun `getColorInfo returns correct primary and secondary colors`() {
        // Test Level 0, primary color piece (T)
        val tPieceLevel0 = getColorInfo(Pieces.T, 0)
        assertEquals(Color(0xff2038ec), tPieceLevel0.primaryColor)
        assertEquals(Color(0xffffffff), tPieceLevel0.secondaryColor)
        assertEquals(false, tPieceLevel0.dark)

        // Test Level 1, secondary color piece (Z)
        val zPieceLevel1 = getColorInfo(Pieces.Z, 1)
        assertEquals(Color(0xff80d010), zPieceLevel1.primaryColor)
        assertEquals(Color(0xffffffff), zPieceLevel1.secondaryColor)
        assertEquals(true, zPieceLevel1.dark)

        // Test a higher-level boundary (level 9)
        val lPieceLevel9 = getColorInfo(Pieces.L, 9)
        assertEquals(Color(0xfffc9838), lPieceLevel9.primaryColor)

        // Test that levels wrap correctly (level 10 should be same as level 0)
        val tPieceLevel10 = getColorInfo(Pieces.T, 10)
        assertEquals(tPieceLevel0.primaryColor, tPieceLevel10.primaryColor)
    }

    @Test
    fun `getColorInfo passes memory parameter correctly`() {
        val result = getColorInfo(Pieces.I, 0, memory = 5)
        assertEquals("Memory parameter should be passed to the object", 5, result.renderDelay)
    }

    @Test
    fun `SquareColorInfo correctly serializes and deserializes all properties`() {
        val originalColor = Color(0xFFE40058) // A unique color from the getColorInfo function
        val originalSecondaryColor = Color.White
        val originalDark = true
        val originalRenderDelay = 123

        // Create a dummy object to serialize
        val squareInfo = SquareColorInfo(originalColor, originalSecondaryColor, originalDark, originalRenderDelay)

        val jsonString = Json.encodeToString(SquareColorInfo.serializer(), squareInfo)

        // The important part: Does it deserialize back to an object with the same property values?
        val deserializedInfo = Json.decodeFromString(SquareColorInfo.serializer(), jsonString)

        assertEquals("Deserialized color should match original color", originalColor, deserializedInfo.primaryColor)
        assertEquals("Deserialized secondary color should match original", originalSecondaryColor, deserializedInfo.secondaryColor)
        assertEquals("Deserialized dark property should match original", originalDark, deserializedInfo.dark)
        assertEquals("Deserialized renderDelay property should match original", originalRenderDelay, deserializedInfo.renderDelay)
    }
}
