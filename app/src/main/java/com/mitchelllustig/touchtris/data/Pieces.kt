package com.mitchelllustig.touchtris.data

fun rotatePoint(point: Point<Int>): Point<Int> {
    return Point(point.y, -point.x)
}

fun rotatePositions(list: List<Point<Int>>):List<Point<Int>>{
    return list.map { rotatePoint(it) }
}

fun makeFourPositionsMap(list: List<Point<Int>>): Map<Orientations, List<Point<Int>>>{
    return mapOf(
        Orientations.zero to list,
        Orientations.one to rotatePositions(list),
        Orientations.two to rotatePositions(rotatePositions(list)),
        Orientations.three to rotatePositions(rotatePositions(rotatePositions(list)))
    )
}

fun makeTwoPositionsMap(list: List<Point<Int>>): Map<Orientations, List<Point<Int>>>{
    return mapOf(
        Orientations.zero to list,
        Orientations.one to rotatePositions(list),
        Orientations.two to list,
        Orientations.three to rotatePositions(list)
    )
}

fun makeOnePositionsMap(list: List<Point<Int>>): Map<Orientations, List<Point<Int>>>{
    return mapOf(
        Orientations.zero to list,
        Orientations.one to list,
        Orientations.two to list,
        Orientations.three to list
    )
}

enum class Pieces(val positions: Map<Orientations, List<Point<Int>>>, val isPrimaryColor: Boolean, val isDark: Boolean) {

    T(makeFourPositionsMap(
        listOf(
            Point(0, 0),
            Point(-1, 0),
            Point(1, 0),
            Point(0, 1)
        )),
        true,
        false,
    ),
    S(
        makeTwoPositionsMap(listOf(Point(0, 0), Point(-1, 1), Point(0, 1), Point(1, 0))),
        true,
        true,
    ),
    Z(
        makeTwoPositionsMap(listOf(Point(0, 0), Point(-1, 0), Point(0, 1), Point(1, 1))),
        false,
        true,
    ),
    J(
        makeFourPositionsMap(listOf(Point(0, 0), Point(-1, 0), Point(1, 0), Point(1, 1))),
        true,
        true,
    ),
    L(
        makeFourPositionsMap(listOf(Point(0, 0), Point(-1, 0), Point(1, 0), Point(-1, 1))),
        false,
        true,
    ),
    I(
        makeTwoPositionsMap(listOf(Point(0, 0), Point(-1, 0), Point(-2, 0), Point(1, 0))),
        true,
        false,
    ),
    O(
        makeOnePositionsMap(listOf(Point(0, 0), Point(-1, 0), Point(-1, 1), Point(0, 1))),
        true,
        false,
    );

    fun getSubPositions(orientation: Orientations): List<Point<Int>> {
        return positions[orientation]!!
    }

    fun isValidPosition(position: Positions, orientation: Orientations): Boolean {
        return if (position.ordinal + getSubPositions(orientation).minBy { it.x }.x < 0){
            false
        }else if (position.ordinal + getSubPositions(orientation).maxBy { it.x }.x > 9){
            false
        } else {
            true
        }
    }
}