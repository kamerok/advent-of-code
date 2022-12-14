package year2022.day14

import readInput
import kotlin.math.max
import kotlin.math.min

fun main() {
    val testInput = readInput(2022, 14, "test")
    val input = readInput(2022, 14)


    check(part1(testInput.parse()) == 24)
    println(part1(input.parse()))

    check(part2(testInput) == 93)
    println(part2(input))
}

private fun part1(input: Field): Int {
    var answer = 0
    while (input.settleSand() != null) {
        answer++
    }
    return answer
}

private fun part2(input: List<String>): Int {
    val originalField = input.parse()
    val y = originalField.yRange.last + 2
    val field = input
        .plus("${originalField.xRange.first - 1000},${y} -> ${originalField.xRange.last + 1000},${y}")
        .parse()
    var answer = 0
    while (field.settleSand() != null) {
        answer++
    }
    return answer
}

private fun List<String>.parse(): Field {
    var minX = Int.MAX_VALUE
    var minY = 0
    var maxX = Int.MIN_VALUE
    var maxY = Int.MIN_VALUE
    val paths = this.map { line ->
        line
            .split(" -> ")
            .map { rawPoint ->
                rawPoint
                    .split(",")
                    .let { (rawX, rawY) ->
                        val x = rawX.toInt()
                        val y = rawY.toInt()
                        minX = min(minX, x)
                        maxX = max(maxX, x)
                        minY = min(minY, y)
                        maxY = max(maxY, y)
                        Point(x, y)
                    }
            }
    }
    val grid = List(maxY - minY + 1) {
        MutableList(maxX - minX + 1) { Cell.Empty }
    }
    paths.forEach { path ->
        path.windowed(2) { (start, end) ->
            for (x in start.x..end.x) {
                for (y in start.y..end.y) {
                    grid[y - minY][x - minX] = Cell.Rock
                }
            }
            for (x in start.x downTo end.x) {
                for (y in start.y downTo end.y) {
                    grid[y - minY][x - minX] = Cell.Rock
                }
            }
        }
    }
    return Field(grid, minX..maxX, minY..maxY)
}

private fun Field.settleSand(): Point? {
    var point = spawn
    while (contains(point) && fall(point) != null) {
        point = fall(point)!!
    }
    return if (contains(point) && this[point] == Cell.Empty) {
        grid[point.y - yRange.first][point.x - xRange.first] = Cell.Sand
        point
    } else {
        null
    }
}

private fun Field.fall(point: Point): Point? {
    val candidates = listOf(
        point.copy(y = point.y + 1),
        point.copy(y = point.y + 1, x = point.x - 1),
        point.copy(y = point.y + 1, x = point.x + 1)
    )
    return candidates.firstOrNull { candidate ->
        getOrNull(candidate) == Cell.Empty || getOrNull(candidate) == null
    }
}

private operator fun Field.get(point: Point): Cell = grid[point.y - yRange.first][point.x - xRange.first]

private fun Field.getOrNull(point: Point): Cell? =
    grid.getOrNull(point.y - yRange.first)?.getOrNull(point.x - xRange.first)

private fun Field.contains(point: Point): Boolean {
    return point.x in xRange && point.y in yRange
}

private data class Field(
    val grid: List<MutableList<Cell>>,
    val xRange: IntRange,
    val yRange: IntRange,
    val spawn: Point = Point(500, 0)
) {
    override fun toString(): String {
        return buildString {
            grid.forEach {
                println(it.joinToString(""))
            }
        }
    }
}

private enum class Cell {
    Empty, Rock, Sand;

    override fun toString(): String {
        return when (this) {
            Empty -> "."
            Rock -> "#"
            Sand -> "o"
        }
    }
}

private data class Point(
    val x: Int,
    val y: Int
)
