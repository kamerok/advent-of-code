package year2021.day07

import readInput

fun main() {
    fun part1(input: List<String>): Int {
        val positions = input.first().split(",").map { it.toInt() }
        val indexesCount = positions.maxOf { it } + 1
        val distribution = IntArray(indexesCount)
        positions.forEach { distribution[it]++ }
        val fuel = IntArray(indexesCount)
        var fuelWasted = 0
        val operation: (Int, Int, Int) -> Int = { index, moving, current ->
            fuel[index] += fuelWasted
            val nowMoving = moving + current
            fuelWasted += nowMoving
            nowMoving
        }
        distribution.foldIndexed(0, operation)
        fuelWasted = 0
        distribution.foldRightIndexed(0, operation)
        return fuel.minOf { it }
    }

    fun part2(input: List<String>): Int {
        val positions = input.first().split(",").map { it.toInt() }
        val indexesCount = positions.maxOf { it } + 1
        val distribution = IntArray(indexesCount)
        positions.forEach { distribution[it]++ }
        val fuel = IntArray(indexesCount)
        var fuelConsumption = 0
        var fuelWasted = 0
        val operation: (Int, Int, Int) -> Int = { index, moving, current ->
            fuel[index] += fuelWasted
            val nowMoving = moving + current
            fuelConsumption += nowMoving
            fuelWasted += fuelConsumption
            nowMoving
        }
        distribution.foldIndexed(0, operation)
        fuelConsumption = 0
        fuelWasted = 0
        distribution.foldRightIndexed(0, operation)
        return fuel.minOf { it }
    }

    val testInput = readInput(2021, 7, "test")
    val input = readInput(2021, 7)

    check(part1(testInput) == 37)
    println(part1(input))

    check(part2(testInput) == 168)
    println(part2(input))
}
