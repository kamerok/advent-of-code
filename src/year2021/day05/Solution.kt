package year2021.day05

import readInput

fun main() {
    fun List<String>.parseLines() = this
        .map { it.split(" -> ") }
        .map { (start, end) ->
            Line(
                start.split(",").let { (x, y) -> Point(x.toInt(), y.toInt()) },
                end.split(",").let { (x, y) -> Point(x.toInt(), y.toInt()) }
            )
        }

    fun List<Line>.countIntersections(): Int {
        val filledPoints = mutableMapOf<Point, Int>()
        forEach { line ->
            line.getAllPoints().forEach { point ->
                filledPoints[point] = filledPoints.getOrDefault(point, 0) + 1
            }
        }
        return filledPoints.count { it.value > 1 }
    }

    fun part1(input: List<String>): Int = input
        .parseLines()
        .filter { it.isOrthogonal }
        .countIntersections()

    fun part2(input: List<String>): Int = input
        .parseLines()
        .countIntersections()

    val testInput = readInput(2021, 5, "test")
    val input = readInput(2021, 5)

    check(part1(testInput) == 5)
    println(part1(input))

    check(part2(testInput) == 12)
    println(part2(input))
}

data class Point(val x: Int, val y: Int)

data class Line(val start: Point, val end: Point) {
    val isOrthogonal = start.x == end.x || start.y == end.y

    fun getAllPoints(): List<Point> {
        val points = mutableListOf(start)
        var current = start
        while (current != end) {
            val nextPoint: Point = when {
                current.x < end.x && current.y < end.y -> current.copy(
                    x = current.x + 1,
                    y = current.y + 1
                )
                current.x < end.x && current.y > end.y -> current.copy(
                    x = current.x + 1,
                    y = current.y - 1
                )
                current.x > end.x && current.y < end.y -> current.copy(
                    x = current.x - 1,
                    y = current.y + 1
                )
                current.x > end.x && current.y > end.y -> current.copy(
                    x = current.x - 1,
                    y = current.y - 1
                )
                current.x < end.x -> current.copy(x = current.x + 1)
                current.x > end.x -> current.copy(x = current.x - 1)
                current.y < end.y -> current.copy(y = current.y + 1)
                else -> current.copy(y = current.y - 1)
            }
            current = nextPoint
            points.add(nextPoint)
        }
        return points
    }

}
