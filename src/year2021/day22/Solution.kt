package year2021.day22

import readInput
import kotlin.math.max
import kotlin.math.min

fun main() {
    val testInput = readInput(2021, 22, "test").parse()
    val testInput2 = readInput(2021, 22, "test_2").parse()
    val input = readInput(2021, 22).parse()

    check(part1(testInput) == 590784)
    println(part1(input))

    check(part2(testInput2) == 2758514936282235)
    println(part2(input))
}

private fun part1(input: List<Command>): Int {
    val borders = Cube(-50..50, -50..50, -50..50)
    val resultCube = Array(101) { Array(101) { BooleanArray(101) } }
    input.forEach { command ->
        command.cube.iterate(borders, map = { it + 50 }) { x, y, z ->
            resultCube.getOrNull(x)?.getOrNull(y)?.getOrNull(z)
                ?.also { resultCube[x][y][z] = command.isOn }
        }
    }
    return resultCube.countTrue()
}

private fun part2(input: List<Command>): Long {

    return 0
}

private fun List<String>.parse(): List<Command> = map { line ->
    val (onText, data) = line.split(" ")
    val ranges = data.split(",").map { definition ->
        val (from, to) = definition.split("=")[1].split("..").map { it.toInt() }
        from..to
    }
    Command(
        isOn = onText == "on",
        cube = Cube(
            xRange = ranges[0],
            yRange = ranges[1],
            zRange = ranges[2]
        )
    )
}

private fun Array<Array<BooleanArray>>.countTrue() =
    sumOf { it.sumOf { it.count { it } } }

private inline fun Cube.iterate(
    borders: Cube,
    map: (Int) -> Int = { it },
    operation: (x: Int, y: Int, z: Int) -> Unit
) {
    val intersection = this and borders
    intersection.xRange.forEach { x ->
        intersection.yRange.forEach { y ->
            intersection.zRange.forEach { z ->
                operation(map(x), map(y), map(z))
            }
        }
    }
}

private infix fun Cube.and(cube: Cube): Cube =
    Cube(
        xRange and cube.xRange,
        yRange and cube.yRange,
        zRange and cube.zRange
    )

private infix fun IntRange.and(range: IntRange) = when {
    intersect(range) -> {
        max(first, range.first)..min(range.last, last)
    }
    else -> IntRange.EMPTY
}

private fun Cube.intersect(cube: Cube): Boolean = xRange.intersect(cube.xRange) ||
        yRange.intersect(cube.yRange) ||
        zRange.intersect(cube.zRange)

private fun IntRange.intersect(range: IntRange): Boolean =
    first <= range.last && last >= range.first

private data class Command(
    val isOn: Boolean,
    val cube: Cube
)

private data class Cube(
    val xRange: IntRange,
    val yRange: IntRange,
    val zRange: IntRange
) {
    val volume = (xRange.last - xRange.first).toLong() *
            (yRange.last - yRange.first) *
            (zRange.last - zRange.first)
}
