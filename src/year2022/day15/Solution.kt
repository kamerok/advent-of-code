package year2022.day15

import readInput
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    val testInput = readInput(2022, 15, "test").parse()
    val input = readInput(2022, 15).parse()

    check(part1(testInput, 10) == 26)
    println(part1(input, 2_000_000))

    check(part2(testInput, 0..20) == 56_000_011L)
    println(part2(input, 0..4_000_000))
}

private fun part1(input: List<Sensor>, position: Int): Int {
    val ranges = input
        .map { it.getRangeForY(position) }
        .filter { !it.isEmpty() }
    var min = ranges.first().first
    var max = ranges.first().last
    ranges.forEach { range ->
        min = min(range.first, min)
        max = max(range.last, max)
    }
    val totalRange = min..max

    val array = BooleanArray(totalRange.count())
    ranges.forEach { range ->
        range.forEach { array[it - min] = true }
    }
    input
        .flatMap { listOf(it.beacon, it.position) }
        .forEach { point ->
            if (point.y == position && point.x in totalRange) {
                array[point.x - min] = false
            }
        }
    return array.count { it }
}

private fun part2(input: List<Sensor>, limit: IntRange): Long {
    limit.forEach { y ->
        val ranges = input
            .map { it.getRangeForY(y) }
            .filter { !it.isEmpty() }
        val rest = limit.remove(ranges)
        if (rest.isNotEmpty()) {
            return Point(rest.first().first, y).let { it.x * 4000000L + it.y }
        }
    }
    return 0
}

private fun List<String>.parse(): List<Sensor> = map { line ->
    val words = line.split(" ")
    Sensor(
        position = Point(
            x = words[2].substring(2, words[2].length - 1).toInt(),
            y = words[3].substring(2, words[3].length - 1).toInt()
        ),
        beacon = Point(
            x = words[words.lastIndex - 1].substring(2, words[words.lastIndex - 1].length - 1).toInt(),
            y = words.last().substring(2).toInt()
        )
    )
}

private fun List<MutableList<Char>>.draw() {
    forEach {
        println(it.joinToString(""))
    }
}

private fun buildGrid(input: List<Sensor>, limit: IntRange): List<MutableList<Char>> {
    val grid = List(limit.count()) { MutableList(limit.count()) { '.' } }

    fun List<MutableList<Char>>.getOrNull(point: Point) =
        getOrNull(point.y - limit.first)?.getOrNull(point.x - limit.first)

    operator fun List<MutableList<Char>>.set(point: Point, char: Char) {
        if (getOrNull(point) != null) {
            this[point.y - limit.first][point.x - limit.first] = char
        }
    }
    input.forEach { sensor ->
        grid[sensor.position] = 'S'
        grid[sensor.beacon] = 'B'
        (sensor.position.y - sensor.radius..sensor.position.y + sensor.radius).forEach { y ->
            sensor.getRangeForY(y).forEach { x ->
                val point = Point(x, y)
                if (grid.getOrNull(point) == '.') grid[point] = '#'
            }
        }
    }
    return grid
}

private fun IntRange.remove(ranges: Iterable<IntRange>): List<IntRange> {
    var result = listOf(this)
    ranges.forEach { toRemove ->
        result = result.flatMap { range ->
            when {
                (toRemove.first < range.first && toRemove.last < range.first)
                        || (toRemove.first > range.last && toRemove.last > range.last) -> listOf(range)

                toRemove.first <= range.first && toRemove.last >= range.last -> listOf()
                toRemove.first > range.first && toRemove.last < range.last -> listOf(
                    range.first until toRemove.first,
                    toRemove.last + 1..range.last
                )

                toRemove.first <= range.first -> listOf(toRemove.last + 1..range.last)
                toRemove.last >= range.last -> listOf(range.first until toRemove.first)
                else -> throw Exception("Unknown case $range - $toRemove")
            }
        }
    }
    return result
}

private data class Point(val x: Int, val y: Int)

private data class Sensor(
    val position: Point,
    val beacon: Point,
    val radius: Int = abs(position.x - beacon.x) + abs(position.y - beacon.y)
) {
    fun getRangeForY(y: Int): IntRange {
        val yDistance = abs(y - position.y)
        val remaining = radius - yDistance
        return position.x - remaining..position.x + remaining
    }
}
