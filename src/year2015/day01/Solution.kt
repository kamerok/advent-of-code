package year2015.day01

import readInput


fun main() {
    val input = readInput(2015, 1).first()

    println(calculateFloor(input))
    println(calculateBasementIndex(input))
}

private fun calculateFloor(input: String) = input.fold(0) { acc, c ->
    when (c) {
        '(' -> acc + 1
        else -> acc - 1
    }
}

private fun calculateBasementIndex(input: String): Int {
    input.foldIndexed(0) { index, acc, c ->
        val floor = when (c) {
            '(' -> acc + 1
            else -> acc - 1
        }
        if (floor < 0) return index + 1
        floor
    }
    return 0
}