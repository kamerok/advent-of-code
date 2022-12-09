package year2022.day09

import readInput
import kotlin.math.abs
import kotlin.math.sign

fun main() {
    val testInput = readInput(2022, 9, "test").parse()
    val testInputLarge = readInput(2022, 9, "test_large").parse()
    val input = readInput(2022, 9).parse()

    check(Point(0, 0).follow(Point(1, 0)) == Point(0, 0))
    check(Point(0, 0).follow(Point(2, 0)) == Point(1, 0))
    check(Point(0, 0).follow(Point(0, -2)) == Point(0, -1))
    check(Point(0, 0).follow(Point(1, 2)) == Point(1, 1))
    check(Point(0, 0).follow(Point(2, 1)) == Point(1, 1))
    check(part1(testInput) == 13) { "${part1(testInput)}" }
    println(part1(input))

    check(part2(testInput) == 1)
    check(part2(testInputLarge) == 36)
    println(part2(input))
}

private fun part1(input: List<Command>): Int {
    val visited = mutableSetOf(Point(0, 0))
    var head = Point(0, 0)
    var tail = Point(0, 0)
    input.forEach { command ->
        repeat(command.steps) {
            head = head.move(command.direction)
            tail = tail.follow(head)
            visited.add(tail)
        }
    }
    return visited.size
}

private fun part2(input: List<Command>): Int {
    val visited = mutableSetOf(Point(0, 0))
    var head = Point(0, 0)
    val tails = Array(9) { Point(0, 0) }
    input.forEach { command ->
        repeat(command.steps) {
            head = head.move(command.direction)

            tails[0] = tails[0].follow(head)
            for (i in 1 until tails.size) {
                tails[i] = tails[i].follow(tails[i - 1])
            }

            visited.add(tails.last())
        }
    }
    return visited.size
}

private fun List<String>.parse(): List<Command> {
    return map { line ->
        val words = line.split(" ")
        Command(
            direction = when (words.first()) {
                "U" -> Direction.Up
                "L" -> Direction.Left
                "D" -> Direction.Down
                "R" -> Direction.Right
                else -> throw IllegalArgumentException("Unsupported direction")
            },
            steps = words.last().toInt()
        )
    }
}

private fun Point.follow(point: Point): Point {
    val dx = point.x - x
    val dy = point.y - y
    if (abs(dx) < 2 && abs(dy) < 2) return this
    return Point(
        x = x + 1 * dx.sign,
        y = y + 1 * dy.sign
    )
}

private fun Point.move(direction: Direction): Point {
    return Point(
        x = when (direction) {
            Direction.Right -> x + 1
            Direction.Left -> x - 1
            else -> x
        },
        y = when (direction) {
            Direction.Up -> y + 1
            Direction.Down -> y - 1
            else -> y
        }
    )
}

private data class Command(
    val direction: Direction,
    val steps: Int
)

private enum class Direction {
    Left, Right, Up, Down
}

private data class Point(
    val x: Int,
    val y: Int
)
