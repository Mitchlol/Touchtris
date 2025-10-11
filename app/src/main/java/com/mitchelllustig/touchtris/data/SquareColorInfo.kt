package com.mitchelllustig.touchtris.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class SquareColorInfo (
    @Serializable(with = ColorSerializer::class)
    val primaryColor: Color,
    @Serializable(with = ColorSerializer::class)
    val secondaryColor: Color,
    val dark: Boolean,
    var renderDelay: Int = 0
)

object ColorSerializer : KSerializer<Color> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("ColorAsInt", PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: Color) {
        // Represent as an empty object
        encoder.encodeInt(value.toArgb())
    }

    override fun deserialize(decoder: Decoder): Color {
        return Color(decoder.decodeInt())
    }
}

fun getColorInfo(piece: Pieces, level: Int, memory: Int = 0): SquareColorInfo{
    return when (level % 10){
        0 -> when (piece.isPrimaryColor){
            true -> SquareColorInfo(Color(0xff2038ec), Color(0xffffffff), piece.isDark, memory)
            false -> SquareColorInfo(Color(0xff3cbcfc), Color(0xffffffff), piece.isDark, memory)
        }
        1 -> when (piece.isPrimaryColor){
            true -> SquareColorInfo(Color(0xff00a800), Color(0xffffffff), piece.isDark, memory)
            false -> SquareColorInfo(Color(0xff80d010), Color(0xffffffff), piece.isDark, memory)
        }
        2 -> when (piece.isPrimaryColor){
            true -> SquareColorInfo(Color(0xffbc00bc), Color(0xffffffff), piece.isDark, memory)
            false -> SquareColorInfo(Color(0xfff478fc), Color(0xffffffff), piece.isDark, memory)
        }
        3 -> when (piece.isPrimaryColor){
            true -> SquareColorInfo(Color(0xff2038ec), Color(0xffffffff), piece.isDark, memory)
            false -> SquareColorInfo(Color(0xff4cdc48), Color(0xffffffff), piece.isDark, memory)
        }
        4 -> when (piece.isPrimaryColor){
            true -> SquareColorInfo(Color(0xffe40058), Color(0xffffffff), piece.isDark, memory)
            false -> SquareColorInfo(Color(0xff58f898), Color(0xffffffff), piece.isDark, memory)
        }
        5 -> when (piece.isPrimaryColor){
            true -> SquareColorInfo(Color(0xff58f898), Color(0xffffffff), piece.isDark, memory)
            false -> SquareColorInfo(Color(0xff5c94fc), Color(0xffffffff), piece.isDark, memory)
        }
        6 -> when (piece.isPrimaryColor){
            true -> SquareColorInfo(Color(0xffd82800), Color(0xffffffff), piece.isDark, memory)
            false -> SquareColorInfo(Color(0xff747474), Color(0xffffffff), piece.isDark, memory)
        }
        7 -> when (piece.isPrimaryColor){
            true -> SquareColorInfo(Color(0xff8000f0), Color(0xffffffff), piece.isDark, memory)
            false -> SquareColorInfo(Color(0xffa80010), Color(0xffffffff), piece.isDark, memory)
        }
        8 -> when (piece.isPrimaryColor){
            true -> SquareColorInfo(Color(0xff2038ec), Color(0xffffffff), piece.isDark, memory)
            false -> SquareColorInfo(Color(0xffd82800), Color(0xffffffff), piece.isDark, memory)
        }
        9 -> when (piece.isPrimaryColor){
            true -> SquareColorInfo(Color(0xffd82800), Color(0xffffffff), piece.isDark, memory)
            false -> SquareColorInfo(Color(0xfffc9838), Color(0xffffffff), piece.isDark, memory)
        }
        else -> SquareColorInfo(Color.Gray, Color.DarkGray, true) // Can't happen :/
    }
}