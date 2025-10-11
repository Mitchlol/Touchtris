package com.mitchelllustig.touchtris.data

import kotlin.math.max

class GameBoard(val height: Int = 20, val width: Int = 10) {
    val squares = MutableList(height) { MutableList<SquareColorInfo?>(width) { null } }

    fun addPiece(piece: Pieces, orientation: Orientations, x: Int, colorInfo: SquareColorInfo) : Boolean {
        // Decrement render delays
        for(row in squares){
            for (square in row){
                square?.renderDelay = max(0, square.renderDelay - 1)
            }
        }
        // Add new piece
        val positions = piece.getSubPositions(orientation)
        for (i in 19 downTo 0){
            if (checkPieceFits(positions, x, i)){
                for (point in positions){
                    squares[i + point.y][x + point.x] = colorInfo.copy()
                }
                return true
            }
        }
        return false
    }

    fun getAvailabilty(): Float{
        val availableSquares = squares.flatten().fold(0) { acc, squareColorInfo ->
            if (squareColorInfo == null) acc + 1 else acc
        }
        return availableSquares.toFloat() / (height * width)
    }

    fun getGhostHeight(piece: Pieces, orientation: Orientations, x: Int): Int{
        val positions = piece.getSubPositions(orientation)
        for (i in 19 downTo 0){
            if (checkPieceFits(positions, x, i)){
                return i
            }
        }
        return -1
    }

    fun checkClearLines(): List<Int>{
        val fullLines = mutableListOf<Int>()
        for (y in 0 until 20){
            if (squares[y].all { it != null }){
                fullLines.add(y)
            }
        }
        return fullLines
    }

    fun clearLines(){
        for (y in 19 downTo 0){
            if (squares[y].all { it != null }){
                squares.removeAt(y)
            }
        }
        while (squares.size < 20){
            squares.add(0, MutableList(10) { null })
        }
    }

    fun checkPieceFits(piece: List<Point<Int>>, x: Int, y: Int): Boolean{
        for (point in piece){
            val currentX = x + point.x
            val currentY = y + point.y
            if (currentX < 0 || currentX >= 10 || currentY < 0 || currentY >= 20){
                return false
            }
            if (squares[currentY][currentX] != null){
                return false

            }
        }
        return true
    }
}