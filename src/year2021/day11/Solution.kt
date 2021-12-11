package year2021.day11

import readInput
import java.util.ArrayDeque
import java.util.Queue

fun main() {
    val testInput = readInput(2021, 11, "test")
    val input = readInput(2021, 11)

    check(part1(testInput.parseGrid()) == 1656)
    println(part1(input.parseGrid()))

    check(part2(testInput.parseGrid()) == 195)
    println(part2(input.parseGrid()))
}

private fun part1(input: List<IntArray>): Int {
    var totalFlashed = 0
    repeat(100) {
        totalFlashed += flash(input)
    }
    return totalFlashed
}

private fun part2(input: List<IntArray>): Int {
    val totalSize = input.sumOf { it.size }
    var count = 1
    while (flash(input) != totalSize) count++
    return count
}

private fun flash(input: List<IntArray>): Int {
    input.forEachIndexed { i, row ->
        row.indices.forEach { j ->
            input[i][j]++
        }
    }
    val flashed = mutableSetOf<Point>()
    val flashingPoints: Queue<Point> = ArrayDeque()
    input.forEachIndexed { i, row ->
        row.forEachIndexed { j, value ->
            if (value > 9) {
                flashingPoints.add(Point(i, j))
            }
        }
    }
    while (flashingPoints.isNotEmpty()) {
        val flashingPoint = flashingPoints.poll()
        flashed.add(flashingPoint)
        input[flashingPoint.x][flashingPoint.y] = 0
        flashingPoint.adjacentPoints().forEach { adjacentPoint ->
            val value = input.getOrNull(adjacentPoint.x)?.getOrNull(adjacentPoint.y)
                ?.let { value -> value + 1 }
            val isExist = value != null
            val isFlashing = flashingPoints.contains(adjacentPoint)
            val isFlashed = flashed.contains(adjacentPoint)
            if (isExist && !isFlashing && !isFlashed) {
                input[adjacentPoint.x][adjacentPoint.y] = requireNotNull(value)
                if (value > 9) {
                    flashingPoints.add(adjacentPoint)
                }
            }
        }
    }
    return flashed.size
}

private fun List<String>.parseGrid(): List<IntArray> = map { line ->
    IntArray(line.length) { i -> line[i].digitToInt() }
}

private data class Point(val x: Int, val y: Int) {
    fun adjacentPoints() = setOf(
        Point(x - 1, y - 1),
        Point(x, y - 1),
        Point(x + 1, y - 1),
        Point(x - 1, y),
        Point(x + 1, y),
        Point(x - 1, y + 1),
        Point(x, y + 1),
        Point(x + 1, y + 1)
    )
}