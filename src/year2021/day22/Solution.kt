package year2021.day22

import readInput
import kotlin.math.max
import kotlin.math.min

fun main() {
    val testInput = readInput(2021, 22, "test").parse()
    val testInput2 = readInput(2021, 22, "test_2").parse()
    val input = readInput(2021, 22).parse()

    check(part1(testInput).also { println(it) } == 590784L)
    println(part1(input))

    check(part2(testInput2).also { println(it) } == 2758514936282235)
    println(part2(input))
}

private fun part1(input: List<Command>): Long {
    val borders = Cube(-50..50, -50..50, -50..50)
    val cubes = findEnabledCuboids(input).also { println(it) }
    return cubes.sumOf { (it and borders).volume }
}

private fun part2(input: List<Command>): Long {
    val cubes = findEnabledCuboids(input)
    return cubes.sumOf { it.volume }
}

private fun findEnabledCuboids(input: List<Command>): MutableSet<Cube> {
    val cubes = mutableSetOf<Cube>()
    input.forEach { command ->
        val intersectCubes = cubes.filter { it.intersect(command.cube) }
        val resultCubes = mutableListOf<Cube>()
        intersectCubes.forEach { cube ->
            resultCubes.addAll(
                cube.splitBy(command.cube).also { println("$cube -> $it") }
                    .filter { !it.intersect(command.cube) }
            )
        }
        if (command.isOn) resultCubes.add(command.cube)
        cubes.removeAll(intersectCubes.toSet())
        cubes.addAll(resultCubes)
    }
    return cubes
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

private fun Cube.intersect(cube: Cube): Boolean = xRange.intersect(cube.xRange) &&
        yRange.intersect(cube.yRange) &&
        zRange.intersect(cube.zRange)

private fun IntRange.intersect(range: IntRange): Boolean =
    first <= range.last && last >= range.first

private fun Cube.splitBy(cube: Cube): List<Cube> =
    buildList {
        val xSplits = xRange.splitBy(cube.xRange)
        val ySplits = yRange.splitBy(cube.yRange)
        val zSplits = zRange.splitBy(cube.zRange)
        xSplits.forEach { x ->
            ySplits.forEach { y ->
                zSplits.forEach { z ->
                    add(Cube(x, y, z))
                }
            }
        }
    }

private fun IntRange.splitBy(range: IntRange): List<IntRange> = when {
    intersect(range) -> when {
        //---****---
        range.first > first && range.last < last -> listOf(
            (first until range.first),
            (range),
            ((range.last + 1)..last),
        )
        //***----
        range.first <= first && range.last < last -> listOf(
            (first .. range.last),
            ((range.last + 1)..last),
        )
        //----**
        range.first > first && range.last >= last -> listOf(
            (first until range.first),
            (range.first..last),
        )
        //******
        else -> listOf(this)
    }
    //-------
    else -> listOf(this)
}.also { println("split $this by $range -> $it") }

private data class Command(
    val isOn: Boolean,
    val cube: Cube
)

private data class Cube(
    val xRange: IntRange,
    val yRange: IntRange,
    val zRange: IntRange
) {
    val volume = (xRange.last + 1 - xRange.first).toLong() *
            (yRange.last + 1 - yRange.first) *
            (zRange.last + 1 - zRange.first)
}
