package year2022.day10

import readInput

fun main() {
    val testInput = readInput(2022, 10, "test").parse()
    val input = readInput(2022, 10).parse()

    check(part1(testInput) == 13140)
    println(part1(input))

    println(part2(testInput))
    println(part2(input))
}

private fun part1(input: List<Command>): Int {
    val program = executeProgram(input)
    return listOf(20, 60, 100, 140, 180, 220).sumOf { cycle ->
        program[cycle - 1].second * cycle
    }
}

private fun part2(input: List<Command>): String {
    val program = executeProgram(input).mapIndexed { index, (_, x) ->
        if (index % 40 in (x - 1 .. x + 1)) {
            "#"
        } else {
            "."
        }
    }
    return buildString {
        program.chunked(40)
            .forEach {
                if (it.isNotEmpty()) {
                    append("\n")
                }
                append(it.joinToString(""))
            }
    }
}

private fun List<String>.parse(): List<Command> =
    map {
        if (it.startsWith("addx")) {
            Add(it.drop(5).toInt())
        } else {
            Noop
        }
    }

private fun executeProgram(input: List<Command>): List<Pair<Command, Int>> {
    var value = 1
    return input
        .flatMap {
            if (it is Noop) {
                listOf(it)
            } else {
                listOf(Noop, it)
            }
        }
        .map { command ->
            (command to value).also {
                if (command is Add) value += command.value
            }
        }
}

private sealed class Command

private object Noop : Command()

private data class Add(val value: Int) : Command()
