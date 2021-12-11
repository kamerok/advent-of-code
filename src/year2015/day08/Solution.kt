package year2015.day08

import readInput

fun main() {
    val testInput = readInput(2015, 8, "test")
    val input = readInput(2015, 8)

    check(part1(testInput) == 12)
    println(part1(input))

    check(part2(testInput) == 19)
    println(part2(input))
}

private fun part1(input: List<String>): Int =
    input
        .sumOf { line ->
            val memoryLine =
                line
                    .let { it.substring(1, it.lastIndex) }
                    .replace(Regex("\\\\x([0-9A-Fa-f]{2})"), "a")
                    .replace("\\\\", "\\")
                    .replace("\\\"", "\"")
            line.length - memoryLine.length
        }

private fun part2(input: List<String>): Int =
    input
        .sumOf { line ->
            val encodedLine =
                line
                    .replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .let { "\"$it\"" }
            encodedLine.length - line.length
        }
