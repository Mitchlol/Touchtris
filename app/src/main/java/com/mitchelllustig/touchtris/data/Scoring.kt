package com.mitchelllustig.touchtris.data

val scoreTable = arrayOf(40,100,300,1200)

fun getScoreForLineClear(lines: Int, level: Int): Int{
    return scoreTable[lines -1] * (level+1)
}