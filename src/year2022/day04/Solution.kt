package year2022.day04

import readInput

fun main() {
    val testInput = readInput(2022, 4, "test").parse()
    val input = readInput(2022, 4).parse()

    check(part1(testInput) == 2)
    println(part1(input))

    check(part2(testInput) == 4)
    println(part2(input))
}

private fun part1(input: List<Pair<Range, Range>>): Int {
    return input.count { (left, right) -> left isFullOverlap right }
}

private fun part2(input: List<Pair<Range, Range>>): Int {
    return input.count { (left, right) -> left isOverlap right }
}

private fun List<String>.parse(): List<Pair<Range, Range>> = map { line ->
    val (a, b) = line.split(",").map { range ->
        val (start, end) = range.split("-").map { it.toInt() }
        Range(start, end)
    }
    a to b
}

private data class Range(
    val start: Int,
    val end: Int
) {
    infix fun isFullOverlap(range: Range): Boolean {
        return (range.start <= start && range.end >= end) ||
                (range.start >= start && range.end <= end)
    }

    infix fun isOverlap(range: Range): Boolean {
        return range.start in start..end || range.end in start..end ||
                start in range.start..range.end || end in range.start..range.end
    }
}
