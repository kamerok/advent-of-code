package year2021.day01

import readInput

fun main() {
    fun part1(input: List<String>): Int {
        val depth = input.map { it.toInt() }
        return depth.zipWithNext().count { it.second > it.first }
    }

    fun part2(input: List<String>): Int {
        val depth = input.map { it.toInt() }
        return depth.windowed(3).zipWithNext().count { it.second.sum() > it.first.sum() }
    }

    val testInput = readInput(2021, 1, "test")
    val input = readInput(2021, 1)

    check(part1(testInput) == 7)
    println(part1(input))

    check(part2(testInput) == 5)
    println(part2(input))
}
