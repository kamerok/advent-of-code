package year2022.day03

import readInput
import java.lang.IllegalArgumentException

fun main() {
    val testInput = readInput(2022, 3, "test")
    val input = readInput(2022, 3)

    check(part1(testInput) == 157)
    println(part1(input))

    check(part2(testInput) == 70)
    println(part2(input))
}

private fun part1(input: List<String>): Int {
    return input.sumOf { line ->
        val first = line.substring(0, line.length / 2)
        val second = line.substring(line.length / 2, line.length)
        first.toSet().intersect(second.toSet()).sumOf { it.score }
    }
}

private fun part2(input: List<String>): Int {
    return input.chunked(3).sumOf { group ->
        val letter = group[0].toSet()
            .intersect(group[1].toSet())
            .intersect(group[2].toSet())
            .first()
        letter.score
    }
}

private val Char.score: Int
    get() = when (this) {
        in ('a'..'z') -> this - 'a' + 1
        in ('A'..'Z') -> this - 'A' + 27
        else -> throw IllegalArgumentException("wrong char")
    }
