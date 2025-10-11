package com.mitchelllustig.touchtris.data

object FallSpeeds {
    val map: Map<Int, Float> = mapOf(
        0  to 800.000f,
        1  to 716.667f,
        2  to 633.333f,
        3  to 550.000f,
        4  to 466.667f,
        5  to 383.333f,
        6  to 300.000f,
        7  to 216.667f,
        8  to 133.333f,
        9  to 100.000f,
        10 to 83.333f,
        11 to 83.333f,
        12 to 83.333f,
        13 to 66.667f,
        14 to 66.667f,
        15 to 66.667f,
        16 to 50.000f,
        17 to 50.000f,
        18 to 50.000f,
        19 to 33.333f,
        20 to 33.333f,
        21 to 33.333f,
        22 to 33.333f,
        23 to 33.333f,
        24 to 33.333f,
        25 to 33.333f,
        26 to 33.333f,
        27 to 33.333f,
        28 to 33.333f,
        29 to 16.667f
    )

    fun getSpeed(level: Int): Float {
        return (if (level > 29) map[29] else if (level < 0) map[0] else map[level])!!
    }
}