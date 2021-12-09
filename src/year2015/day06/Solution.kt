package year2015.day06

import readInput

fun main() {
    val input = readInput(2015, 6)

    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Int {
    val grid = Array(1000) { BooleanArray(1000) }
    input.forEach { line ->
        val words = line.split(" ")
        val start = words[words.lastIndex - 2].split(",").let { (x, y) -> x.toInt() to y.toInt() }
        val end = words.last().split(",").let { (x, y) -> x.toInt() to y.toInt() }
        val command = when {
            words.first() == "toggle" -> Command.TOGGLE
            words.first() == "turn" && words[1] == "on" -> Command.ON
            else -> Command.OFF
        }
        (start.first..end.first).forEach { x ->
            (start.second..end.second).forEach { y ->
                grid[x][y] = when (command) {
                    Command.ON -> true
                    Command.OFF -> false
                    Command.TOGGLE -> !grid[x][y]
                }
            }
        }
    }
    return grid.sumOf { row -> row.count { it } }
}

private enum class Command {
    ON, OFF, TOGGLE
}

private fun part2(input: List<String>): Int {
    val grid = Array(1000) { IntArray(1000) }
    input.forEach { line ->
        val words = line.split(" ")
        val start = words[words.lastIndex - 2].split(",").let { (x, y) -> x.toInt() to y.toInt() }
        val end = words.last().split(",").let { (x, y) -> x.toInt() to y.toInt() }
        val command = when {
            words.first() == "toggle" -> Command.TOGGLE
            words.first() == "turn" && words[1] == "on" -> Command.ON
            else -> Command.OFF
        }
        (start.first..end.first).forEach { x ->
            (start.second..end.second).forEach { y ->
                when (command) {
                    Command.ON -> grid[x][y]++
                    Command.OFF -> grid[x][y] = (grid[x][y] - 1).coerceAtLeast(0)
                    Command.TOGGLE -> grid[x][y] += 2
                }
            }
        }
    }
    return grid.sumOf { row -> row.sum() }
}

