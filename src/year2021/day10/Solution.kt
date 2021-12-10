package year2021.day10

import readInput

fun main() {
    val testInput = readInput(2021, 10, "test")
    val input = readInput(2021, 10)

    check(part1(testInput) == 26397L)
    println(part1(input))

    check(part2(testInput) == 288957L)
    println(part2(input))
}

private fun part1(input: List<String>): Long =
    input
        .mapNotNull { line ->
            val result = line.process()
            if (result is Result.Error) {
                result
            } else {
                null
            }
        }
        .fold(0) { acc, error ->
            acc + error.points
        }

private fun part2(input: List<String>): Long =
    input
        .mapNotNull { line ->
            val result = line.process()
            if (result is Result.Incomplete) {
                result
            } else {
                null
            }
        }
        .map { it.points }
        .sorted()
        .let { it[it.size / 2] }

private fun String.process(): Result {
    val openBrackets = setOf('(', '[', '{', '<')
    val completeBrackets = setOf("()", "[]", "{}", "<>")
    val stack = ArrayDeque<Char>()
    for (c in this) {
        if (openBrackets.contains(c)) {
            stack.add(c)
        } else {
            val lastChar = stack.removeLastOrNull()
            if (!completeBrackets.contains("$lastChar$c")) {
                return Result.Error(c)
            }
        }
    }
    return if (stack.isEmpty()) {
        Result.Complete
    } else {
        Result.Incomplete(buildString {
            while (stack.isNotEmpty()) {
                val c = stack.removeLast()
                append(
                    when (c) {
                        '(' -> ')'
                        '[' -> ']'
                        '{' -> '}'
                        else -> '>'
                    }
                )
            }
        })
    }
}

sealed class Result {
    data class Error(val cause: Char) : Result() {
        val points: Long = when (cause) {
            ')' -> 3
            ']' -> 57
            '}' -> 1197
            else -> 25137
        }
    }

    object Complete : Result()
    data class Incomplete(val missingPart: String) : Result() {
        val points: Long = missingPart.fold(0) { acc, c ->
            acc * 5 + when (c) {
                ')' -> 1
                ']' -> 2
                '}' -> 3
                else -> 4
            }
        }
    }
}